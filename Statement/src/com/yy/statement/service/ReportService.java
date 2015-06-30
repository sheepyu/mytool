package com.yy.statement.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yy.statement.domain.Remain;
import com.yy.statement.domain.Syts;
import com.yy.statement.util.DateUtil;
import com.yy.statement.util.ExcelUtil;
import com.yy.statement.util.PoiUtil;

/**
 * 报表
 * 
 * @author Administrator
 * 
 */
public class ReportService {
	private static Logger log = Logger.getLogger(RemainService.class);
	private ExcelUtil excelUtil = new ExcelUtil();
	SqlSession session = null;
	List<Syts> sytsList = new ArrayList<Syts>();
	String destName = null;

	public ReportService(SqlSession session) {
		super();
		this.session = session;
	}

	public void begin() {
		this.createNewXls();
		this.searchSyts(DateUtil.getYesterday("yyyyMMdd"));
		try {
			this.writeExcel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void searchSyts(String dayTime) {
		dayTime = "20150618";
		sytsList = session.selectList(
				"com.yy.statement.mapper.sytsMapper.getSyts", dayTime);
		log.info(sytsList);
	}

	public void createNewXls() {
		String yesterday = DateUtil.getYesterday("MMdd");
		String befor = DateUtil.getDayBefor(2, "MMdd");
		String srcName = "excel\\增值业务部统计报表" + befor + ".xls";
		destName = "excel\\增值业务部统计报表" + yesterday + ".xls";
		excelUtil.copyXls(srcName, destName);
		log.info(destName + "复制完成");
	}

	public void writeExcel() throws Exception {
		// 打开excel
		FileInputStream inputStream = new FileInputStream(destName);
		// 读取excel内容
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet("增值业务部发送量统计报表"
				+ DateUtil.getTime("MM") + "月");
		/**
		 * 写入数据
		 */
		HSSFRow srcRow = sheet.getRow(2);
		HSSFRow destRow = PoiUtil.insertRow(sheet, sheet.getLastRowNum());// 插入一行
		PoiUtil.copyRowStyle(srcRow, destRow);// 格式

		sheet.setForceFormulaRecalculation(true);// 刷新公式
		FileOutputStream out = new FileOutputStream(destName);
		workbook.write(out);
		inputStream.close();
		if (out != null) {
			out.close();
		}
		log.info("增值业务部发送量统计报表 已完成");
	}
}
