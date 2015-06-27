package com.yy.statement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yy.statement.domain.Remain;
import com.yy.statement.util.DateUtil;
import com.yy.statement.util.PoiUtil;

public class RemainService {

	private static Logger log = Logger.getLogger(RemainService.class);

	SqlSession session = null;

	public RemainService(SqlSession session) {
		super();
		this.session = session;
	}

	public void search() {
		List<Remain> remains = session
				.selectList("com.yy.statement.mapper.remainMapper.getRemain2");
		try {
			writeExcel(remains);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeExcel(List<Remain> remains) throws Exception {
		String FileName = "余额提醒-" + DateUtil.getMMdd() + ".xls";
		this.createXls(FileName);// 复制文件
		// 打开excel
		FileInputStream inputStream = new FileInputStream("excel/" + FileName);
		// 读取excel内容
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		// 获得sheet
		HSSFSheet sheet = workbook.getSheet("Sheet1");

		/**
		 * 写入数据
		 */
		HSSFRow srcRow = sheet.getRow(2);
		for (int i = 0; i < remains.size(); i++) {
			if (i != 0) {
				HSSFRow destRow = sheet.createRow(i + 2);
				PoiUtil.copyRowStyle(srcRow, destRow);
			}
			HSSFRow row = sheet.getRow(i + 2);
			HSSFCell cell = row.getCell(0);
			cell.setCellValue(remains.get(i).getDlm());
			cell = row.getCell(1);
			cell.setCellValue(remains.get(i).getDlmc());
			cell = row.getCell(2);
			cell.setCellValue(remains.get(i).getZjye());
			cell = row.getCell(3);
			cell.setCellValue(remains.get(i).getExpendDay());
			cell = row.getCell(4);
			cell.setCellValue(remains.get(i).getExpendWeek());
			cell = row.getCell(5);
			cell.setCellValue(remains.get(i).getCanUse());
		}
		sheet.setForceFormulaRecalculation(true);// 刷新公式
		FileOutputStream out = new FileOutputStream("excel/" + FileName);
		workbook.write(out);
		inputStream.close();
		if (out != null) {
			out.close();
		}
		log.info(FileName + "已完成");
	}

	private void createXls(String fileName) {
		int byteread = 0;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(new File("excel/余额提醒模板.xls"));
			out = new FileOutputStream(new File("excel/" + fileName));
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			log.info(fileName + "复制完成");
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
}
