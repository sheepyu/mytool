package com.yy.statement.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yy.statement.bean.SaleBean;
import com.yy.statement.domain.Sale;
import com.yy.statement.domain.Syts;
import com.yy.statement.util.DateUtil;

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
		excelService.copyXlsFile(srcName, destName);
		log.info(destName + "复制完成");
	}

	public void writeExcel() throws Exception {
		FileInputStream inputStream = new FileInputStream(destName);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

		excelService.statisticsSheet(workbook,sytsMap);
		excelService.writeSyts(workbook,sytsMap);
		excelService.writeSale(workbook,saleBeanList);

		FileOutputStream out = new FileOutputStream(destName);
		workbook.write(out);
		inputStream.close();
		if (out != null) {
			out.close();
		}
		log.info("增值业务部发送量统计报表 已完成");
	}


	private void initSytsMap() {
		sytsMap.put("1006", 0);// 广东移动
		sytsMap.put("1009", 0);// 安徽移动
		sytsMap.put("1010", 0);// 北京移动
		sytsMap.put("2004", 0);// 联通10690
		sytsMap.put("3002", 0);// 电信10690
	}
}
