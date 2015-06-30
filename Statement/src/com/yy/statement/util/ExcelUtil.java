package com.yy.statement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

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

}
