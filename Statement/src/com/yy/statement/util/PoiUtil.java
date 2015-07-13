package com.yy.statement.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class PoiUtil {

	private static Logger log = Logger.getLogger(PoiUtil.class);

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
	public static void deleteRows(HSSFSheet sheet, int startRow, int n) {
		log.debug("befor shiftRows sheet.getLastRowNum():" + sheet.getLastRowNum());
		sheet.shiftRows(startRow + n, sheet.getLastRowNum(), -n, true, false);
		log.debug("after shiftRows sheet.getLastRowNum():" + sheet.getLastRowNum());
		// begin 2015年7月10日00:09:23 bug修复：往上移动行会在原地留下空行，删除留下的空行
		// PS: 2015年7月13日23:42:33  bug依然存在，不过此代码作用变为清理多余的有值行，空值行依然无法完全去除
		for (int i = 0; i < n - 1; i++) {
			HSSFRow row = sheet.getRow(sheet.getLastRowNum());
			sheet.removeRow(row);
		}
		log.debug("deleteRows finish sheet.getLastRowNum():" + sheet.getLastRowNum() + "  and n:" + n);
		// end 2015年7月13日00:10:00 bug修复：往上移动行会在原地留下空行，删除留下的空行
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
		char startChar = (char) (startNum % 26 + 65);
		char endChar = (char) (endNum % 26 + 65);
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