package com.yy.statement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yy.statement.bean.SaleBean;
import com.yy.statement.util.DateUtil;
import com.yy.statement.util.PoiUtil;

/**
 * 专门用来对付excel操作的业务
 * 
 * @author Administrator
 * 
 */
public class ExcelService {
	private static Logger log = Logger.getLogger(ExcelService.class);

	/**
	 * 复制ExcelUtil
	 */
	public void copyXlsFile(String srcName, String destName) {
		int byteread = 0;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(new File(srcName));
			out = new FileOutputStream(new File(destName));
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
		} catch (FileNotFoundException e) {
			log.info(e.getStackTrace());
			e.printStackTrace();
		} catch (IOException e) {
			log.info(e.getStackTrace());
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.info(e.getStackTrace());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对运营商数据统计
	 * 
	 * @param workbook
	 */
	public void writeSyts(HSSFWorkbook workbook, Map<String, Integer> sytsMap, String sdate) {
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet(DateUtil.StringToString("M月dd日", sdate) + "系统数据统计");
		HSSFRow row = sheet.getRow(3);
		HSSFCell cell = row.getCell(1);
		cell.setCellValue(sytsMap.get("1006"));
		cell = row.getCell(2);
		cell.setCellValue(sytsMap.get("1009"));
		cell = row.getCell(3);
		cell.setCellValue(sytsMap.get("1010"));
		cell = row.getCell(4);
		cell.setCellValue(sytsMap.get("1011"));
		cell = row.getCell(5);
		cell.setCellValue(sytsMap.get("2004"));
		cell = row.getCell(6);
		cell.setCellValue(sytsMap.get("3002"));
		sheet.setForceFormulaRecalculation(true);// 刷新公式
		log.info("数据统计完成");
	}

	/**
	 * 对系统发送数据统计
	 * 
	 * @param workbook
	 */
	public void statisticsSheet(HSSFWorkbook workbook, Map<String, Integer> sytsMap, String sdate) {
		String month = sdate.substring(4, 6);
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet("增值业务部发送量统计报表" + month + "月");

		HSSFRow srcRow = sheet.getRow(2);
		HSSFRow destRow = PoiUtil.insertRow(sheet, sheet.getLastRowNum());// 插入一行
		PoiUtil.copyRowStyle(srcRow, destRow);// 格式
		/**
		 * 对sheet2写入数据
		 */
		HSSFCell cell = destRow.getCell(0);
		cell.setCellValue(DateUtil.StringToString("M月dd日", sdate));
		cell = destRow.getCell(1);
		cell.setCellValue(sytsMap.get("1006"));
		cell = destRow.getCell(2);
		cell.setCellValue(sytsMap.get("1009"));
		cell = destRow.getCell(3);
		cell.setCellValue(sytsMap.get("1010"));
		cell = destRow.getCell(4);
		cell.setCellValue(sytsMap.get("1011"));
		cell = destRow.getCell(5);
		cell.setCellValue(sytsMap.get("2004"));
		cell = destRow.getCell(6);
		cell.setCellValue(sytsMap.get("3002"));
		cell = destRow.getCell(7);// 总量cell
		PoiUtil.rowSum(1, 6, cell);
		// 总计公式
		HSSFRow sumRow = sheet.getRow(sheet.getLastRowNum());
		int rowNum = destRow.getRowNum() + 1;
		for (int i = 1; i <= 7; i++) {
			PoiUtil.cellSum(3, rowNum, sumRow.getCell(i));
		}
		sheet.setForceFormulaRecalculation(true);// 刷新公式
		log.info("系统发送统计完成");
	}

	/**
	 * 销售情况
	 */
	public void writeSale(HSSFWorkbook workbook, List<SaleBean> saleBeanList,String sdate) {
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet(DateUtil.StringToString("M月dd日",sdate) + "系统数据统计");
		int saleNumBefor = sheet.getLastRowNum() - 17;// 目前有多少条销售数据
		int saleNumNow = saleBeanList.size();
		if (saleNumBefor < saleNumNow) {
			PoiUtil.insertRows(sheet, 16, saleNumNow - saleNumBefor);
		} else {
			PoiUtil.deleteRows(sheet, 16, saleNumBefor - saleNumNow);
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

			int[] tdts = { 1006, 1009, 1010, 1011, 2004, 3002 };
			for (int j = 0; j < tdts.length; j++) {
				cell = row.getCell(j + 2);
				if (saleBean.getTdMap().get(tdts[j]) != null) {
					cell.setCellValue(saleBean.getTdMap().get(tdts[j]));
				} else {
					cell.setCellValue("");
				}
			}
			cell = row.getCell(8);
			PoiUtil.rowSum(2, 7, cell);
			cell = row.getCell(9);
			cell.setCellValue(saleBean.getSaleroomn());
		}

	}

	/**
	 * 对sheet进行增删和改名
	 * @param workbook
	 * @param days
	 */
	public void sheetWork(HSSFWorkbook workbook, String[] days) {
		
		int sheetNum = workbook.getNumberOfSheets();
		
		if(sheetNum-1<days.length){
			for(int i=sheetNum-1;i<=days.length;i++){
				HSSFSheet sheet = workbook.cloneSheet(0);//克隆sheet
				workbook.setSheetOrder(sheet.getSheetName(), 0);//把克隆的sheet移动到最前面
			}
		}
		//批量改名
		for(int i=0;i<days.length;i++){
			String sheetName = DateUtil.StringToString("M月dd日", days[i]) + "系统数据统计";
			workbook.setSheetName(i, sheetName);
		}
		
		
	}

}
