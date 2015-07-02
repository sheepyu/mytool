package com.yy.statement.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class PoiUtil {
	/**
	 * 插入一行
	 * 
	 * @param sheet
	 * @param rowNum
	 * @return
	 */
	public static HSSFRow insertRow(HSSFSheet sheet, int rowNum) {
		sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1, true, false);// 将插入位置以下的行都往下移动一行，以空出位置来创建row
		HSSFRow row = sheet.createRow(rowNum);// 然后创建插入行
		return row;
	}

	/**
	 * 插入多行
	 * 
	 * @param sheet
	 * @param rowNum
	 * @return
	 */
	public static void insertRows(HSSFSheet sheet, int startRow, int n) {
		sheet.shiftRows(startRow, sheet.getLastRowNum(), n, true, false);
		for (int i = 0; i < n; i++) {
			sheet.createRow(startRow + i);
		}
	}

	/**
	 * 删除多行
	 * 
	 * @param sheet
	 * @param rowNum
	 * @return
	 */
	public static void delteRows(HSSFSheet sheet, int startRow, int n) {
		sheet.shiftRows(startRow + n, sheet.getLastRowNum(), -n, true, false);
	}

	/**
	 * 复制行的格式
	 * 
	 * @param srcRow
	 * @param destRow
	 */
	public static void copyRowStyle(HSSFRow srcRow, HSSFRow destRow) {
		for (int i = 0; i < srcRow.getLastCellNum(); i++) {
			HSSFCell srcCell = srcRow.getCell(i);
			HSSFCell destCell = destRow.getCell(i);
			// cell为空时给建一个cell
			if (destCell == null) {
				destCell = destRow.createCell(i);
			}
			copyCellStyle(srcCell, destCell);
		}
	}

	/**
	 * 复制列的格式
	 * 
	 * @param srcCell
	 * @param destCell
	 */
	public static void copyCellStyle(HSSFCell srcCell, HSSFCell destCell) {
		// 获得格式
		destCell.setCellStyle(srcCell.getCellStyle());
	}

	/**
	 * 对行进行求和
	 * 
	 * @param startNum
	 * @param endNum
	 * @param cell
	 */
	public static void rowSum(int startNum, int endNum, HSSFCell cell) {
		char startChar = (char) ((startNum - 2) % 26 + 66);
		char endChar = (char) ((endNum - 2) % 26 + 66);
		int rowNum = cell.getRowIndex() + 1;
		String formula = "SUM(" + startChar + rowNum + ":" + endChar + rowNum + ")";
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
	}

	/**
	 * 对列进行求和
	 * 
	 * @param startCellNum
	 * @param endCellNum
	 * @param cell
	 */
	public static void cellSum(int startCellNum, int endCellNum, HSSFCell cell) {
		char cellChar = (char) ((cell.getColumnIndex() - 1) % 26 + 66);
		String formula = "SUM(" + cellChar + startCellNum + ":" + cellChar + endCellNum + ")";
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
	}
}