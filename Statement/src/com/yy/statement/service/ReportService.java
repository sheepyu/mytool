package com.yy.statement.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

		/**
		 * 判断今天星期几
		 */
		Calendar today = Calendar.getInstance();
		// 周一到周五间
		String srcDay = "";
		String destDay = "";
		String[] days = null;// 需要查询的日期
		int weeks = today.get(Calendar.DAY_OF_WEEK);
		switch (weeks) {
		// 周一需要做周5,6,7三天的报表
		case Calendar.MONDAY:
			srcDay = DateUtil.getDayBefor(4, "MMdd", today);
			destDay = DateUtil.getDayBefor(3, "MMdd", today) + "-" + DateUtil.getDayBefor(1, "MMdd", today);
			days = new String[] { DateUtil.getDayBefor(3, "yyyyMMdd", today), DateUtil.getDayBefor(2, "yyyyMMdd", today),
					DateUtil.getDayBefor(1, "yyyyMMdd", today) };
			break;
		case Calendar.TUESDAY:
			srcDay = DateUtil.getDayBefor(4, "MMdd", today) + "-" + DateUtil.getDayBefor(2, "MMdd", today);
			destDay = DateUtil.getDayBefor(1, "MMdd", today);
			days = new String[] { DateUtil.getDayBefor(1, "yyyyMMdd") };
			break;
		case Calendar.WEDNESDAY:
		case Calendar.THURSDAY:
		case Calendar.FRIDAY:
			srcDay = DateUtil.getDayBefor(2, "MMdd", today);
			destDay = DateUtil.getDayBefor(1, "MMdd", today);
			days = new String[] { DateUtil.getDayBefor(1, "yyyyMMdd", today) };
			break;
		// 周六和周日则不做报表
		case Calendar.SATURDAY:
		case Calendar.SUNDAY:
			return;
		}

		this.createNewXls(srcDay, destDay);
		try {
			this.writeExcel(days);
		} catch (Exception e1) {
			e1.printStackTrace();
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
				SaleBean saleBean = new SaleBean(saleList.get(i).getDlm(), saleList.get(i).getDlmc(), saleList.get(i).getSaleroomn());
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

	/**
	 * 
	 * @param srcDays
	 * @param destDays
	 */
	public void createNewXls(String srcDays, String destDays) {
		String srcName = "excel\\增值业务部统计报表" + srcDays + ".xls";
		destName = "excel\\增值业务部统计报表" + destDays + ".xls";
		excelService.copyXlsFile(srcName, destName);
		log.info(destName + "复制完成");
	}

	public void writeExcel(String[] days) throws Exception {
		FileInputStream inputStream = new FileInputStream(destName);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

		excelService.sheetWork(workbook, days);

		for (int i = 0; i < days.length; i++) {
			this.searchSyts(days[i]);
			excelService.statisticsSheet(workbook, sytsMap, days[i]);
			excelService.writeSyts(workbook, sytsMap, days[i]);
			this.searchSale(days[i]);
			this.saleTosaleBean();
			excelService.writeSale(workbook, saleBeanList, days[i]);
		}

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
		sytsMap.put("1011", 0);// 杭州移动
		sytsMap.put("2004", 0);// 联通10690
		sytsMap.put("3002", 0);// 电信10690
	}
}
