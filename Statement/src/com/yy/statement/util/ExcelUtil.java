package com.yy.statement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class ExcelUtil {
	private static Logger log = Logger.getLogger(ExcelUtil.class);

	/**
	 * 复制ExcelUtil
	 */
	public void copyXls(String srcName, String destName) {
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
			log.info(srcName + "复制完成");
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

	public void rowSum(int startNum, int endNum, HSSFCell cell) {
		char startChar = (char) ((startNum - 2) % 26 + 66);
		char endChar = (char) ((endNum - 2) % 26 + 66);
		int rowNum = cell.getRowIndex()+1;
		String formula = "SUM(" + startChar + rowNum + ":" + endChar + rowNum
				+ ")";
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
	}

	public void cellSum(int startCellNum, int endCellNum, HSSFCell cell) {
		char cellChar = (char) ((cell.getColumnIndex() - 1) % 26 + 66);
		String formula = "SUM(" + cellChar + startCellNum + ":" + cellChar
				+ endCellNum + ")";
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
	}

}
