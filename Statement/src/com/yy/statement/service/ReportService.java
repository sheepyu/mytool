package com.yy.statement.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yy.statement.bean.SaleBean;
import com.yy.statement.domain.Sale;
import com.yy.statement.domain.Syts;
import com.yy.statement.util.DateUtil;
import com.yy.statement.util.PoiUtil;

/**
 * 报表
 * 
 * @author Administrator
 * 
 */
public class ReportService {
	private static Logger log = Logger.getLogger(RemainService.class);
	private ExcelService excelService = new ExcelService();
	SqlSession session = null;
	List<Syts> sytsList = new ArrayList<Syts>();
	List<Sale> saleList = new ArrayList<Sale>();
	List<SaleBean> saleBeanList = new ArrayList<SaleBean>();
	String destName = null;

	private static Map<String, Integer> sytsMap = new HashMap<String, Integer>();

	public ReportService(SqlSession session) {
		super();
		this.session = session;
		initSytsMap();
	}

	public void begin() {
		this.createNewXls();
		this.searchSyts(DateUtil.getYesterday("yyyyMMdd"));
		this.searchSale(DateUtil.getYesterday("yyyyMMdd"));
		this.saleTosaleBean();

		try {
			this.writeExcel();
		} catch (Exception e) {
			log.error(e.getStackTrace());
			e.printStackTrace();
		}
	}

	private void searchSale(String dayTime) {
		// TODO 测试使用
		dayTime = "20150618";
		saleList = session.selectList("com.yy.statement.mapper.saleMapper.getSale", dayTime);
		log.info(saleList);

	}

	public void saleTosaleBean() {
		for (int i = 0; i < saleList.size(); i++) {
			int j = 0;
			boolean flag = false;
			for (; j < saleBeanList.size(); j++) {
				if (saleList.get(i).getDlm().equals(saleBeanList.get(j).getDlm())) {
					flag = true;
					break;
				}
			}

			if (flag) {
				saleBeanList.get(j).getTdMap().put(saleList.get(i).getTdbh(), saleList.get(i).getTs());
				saleBeanList.get(j).addSaleroomn(saleList.get(i).getSaleroomn());
			} else {
				SaleBean saleBean = new SaleBean(saleList.get(i).getDlm(), saleList.get(i).getDlmc(), saleList.get(i)
						.getSaleroomn());
				saleBean.getTdMap().put(saleList.get(i).getTdbh(), saleList.get(i).getTs());
				saleBeanList.add(saleBean);
			}
		}
	}

	private void searchSyts(String dayTime) {
		// TODO 测试使用，测试完毕撤下
		dayTime = "20150618";
		sytsList = session.selectList("com.yy.statement.mapper.sytsMapper.getSyts", dayTime);

		for (int i = 0; i < sytsList.size(); i++) {
			sytsMap.put(sytsList.get(i).getTdbh(), sytsList.get(i).getSumSyts());
		}
		log.info(sytsList);
	}

	public void createNewXls() {
		String yesterday = DateUtil.getYesterday("MMdd");
		String befor = DateUtil.getDayBefor(2, "MMdd");
		String srcName = "excel\\增值业务部统计报表" + befor + ".xls";
		destName = "excel\\增值业务部统计报表" + yesterday + ".xls";
		excelService.copyXls(srcName, destName);
		log.info(destName + "复制完成");
	}

	public void writeExcel() throws Exception {
		// 打开excel
		FileInputStream inputStream = new FileInputStream(destName);
		// 读取excel内容
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

		this.statisticsSheet(workbook);
		this.writeSyts(workbook);
		this.writeSale(workbook);

		FileOutputStream out = new FileOutputStream(destName);
		workbook.write(out);
		inputStream.close();
		if (out != null) {
			out.close();
		}
		log.info("增值业务部发送量统计报表 已完成");
	}

	/**
	 * 对运营商数据统计
	 * 
	 * @param workbook
	 */
	public void writeSyts(HSSFWorkbook workbook) {
		// 获得sheet
		System.out.println(DateUtil.getDayBefor(2, "M月dd日") + "系统数据统计");
		HSSFSheet sheet = workbook.getSheet(DateUtil.getDayBefor(2, "M月dd日") + "系统数据统计");
		HSSFRow row = sheet.getRow(3);
		HSSFCell cell = row.getCell(1);
		cell.setCellValue(sytsMap.get("1006"));
		cell = row.getCell(2);
		cell.setCellValue(sytsMap.get("1009"));
		cell = row.getCell(3);
		cell.setCellValue(sytsMap.get("1010"));
		cell = row.getCell(4);
		cell.setCellValue(sytsMap.get("2004"));
		cell = row.getCell(5);
		cell.setCellValue(sytsMap.get("3002"));
		sheet.setForceFormulaRecalculation(true);// 刷新公式
	}

	/**
	 * 对系统发送数据统计
	 * 
	 * @param workbook
	 */
	public void statisticsSheet(HSSFWorkbook workbook) {
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet("增值业务部发送量统计报表" + DateUtil.getTime("MM") + "月");

		HSSFRow srcRow = sheet.getRow(2);
		HSSFRow destRow = PoiUtil.insertRow(sheet, sheet.getLastRowNum());// 插入一行
		PoiUtil.copyRowStyle(srcRow, destRow);// 格式
		/**
		 * 对sheet2写入数据
		 */
		HSSFCell cell = destRow.getCell(0);
		cell.setCellValue(DateUtil.getYesterday("M月dd日"));
		cell = destRow.getCell(1);
		cell.setCellValue(sytsMap.get("1006"));
		cell = destRow.getCell(2);
		cell.setCellValue(sytsMap.get("1009"));
		cell = destRow.getCell(3);
		cell.setCellValue(sytsMap.get("1010"));
		cell = destRow.getCell(4);
		cell.setCellValue(sytsMap.get("2004"));
		cell = destRow.getCell(5);
		cell.setCellValue(sytsMap.get("3002"));
		cell = destRow.getCell(6);// 总量cell
		PoiUtil.rowSum(2, 6, cell);
		// 总计公式
		HSSFRow sumRow = sheet.getRow(sheet.getLastRowNum());
		int rowNum = destRow.getRowNum() + 1;
		for (int i = 1; i <= 6; i++) {
			PoiUtil.cellSum(3, rowNum, sumRow.getCell(i));
		}

		sheet.setForceFormulaRecalculation(true);// 刷新公式
		log.info("数据统计完成");
	}

	/**
	 * 销售情况
	 */
	private void writeSale(HSSFWorkbook workbook) {
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet(DateUtil.getDayBefor(2, "M月dd日") + "系统数据统计");

		int saleNumBefor = sheet.getLastRowNum() - 17;// 目前有多少条销售数据
		int saleNumNow = saleBeanList.size();
		if (saleNumBefor < saleNumNow) {
			PoiUtil.insertRows(sheet, 16, saleNumNow - saleNumBefor);
		} else {
			PoiUtil.delteRows(sheet, 16, saleNumBefor - saleNumNow);
		}
		// 复制格式
		for (int i = 1; i <= saleNumNow - saleNumBefor; i++) {
			PoiUtil.copyRowStyle(sheet.getRow(15), sheet.getRow(15 + i));// 格式
		}
		// 写入数据
		for (int i = 0; i < saleBeanList.size(); i++) {
			SaleBean saleBean = saleBeanList.get(i);
			HSSFRow row = sheet.getRow(15 + i);
			HSSFCell cell = row.getCell(0);
			cell.setCellValue(saleBean.getDlm());
			cell = row.getCell(1);
			cell.setCellValue(saleBean.getDlmc());

			int[] tdts = { 1006, 1009, 1010, 2004, 3002 };
			for (int j = 0; j < tdts.length; j++) {
				cell = row.getCell(j + 2);
				if (saleBean.getTdMap().get(tdts[j]) != null) {
					cell.setCellValue(saleBean.getTdMap().get(tdts[j]));
				} else {
					cell.setCellValue("");
				}
			}
			cell = row.getCell(7);
			PoiUtil.rowSum(3, 7, cell);
			cell = row.getCell(8);
			cell.setCellValue(saleBean.getSaleroomn());

		}
		int sheetIndex = workbook.getSheetIndex(sheet);
		workbook.setSheetName(sheetIndex, DateUtil.getDayBefor(1, "M月dd日") + "系统数据统计");
	}

	private void initSytsMap() {
		sytsMap.put("1006", 0);// 广东移动
		sytsMap.put("1009", 0);// 安徽移动
		sytsMap.put("1010", 0);// 北京移动
		sytsMap.put("2004", 0);// 联通10690
		sytsMap.put("3002", 0);// 电信10690
	}
}
