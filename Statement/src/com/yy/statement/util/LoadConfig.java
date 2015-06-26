package com.yy.statement.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class LoadConfig {
	private final String[] confFiles = new String[] { "conf.xml",
			"monitor.xml", };
	private static Logger log = Logger.getLogger(LoadConfig.class);

	public void init() {
		String rootDir = System.getProperty("user.dir");
		File configDir = new File(rootDir + "\\config");
		System.out.println("configDir:" + configDir);
		if (configDir.exists() && configDir.isDirectory()) {
			log.info("文件夹config存在");
		} else {
			log.info("文件夹config不存在，创建文件夹");
			configDir.mkdir();
		}

		for (int i = 0; i < confFiles.length; i++) {
			File destFile = new File(configDir + "\\" + confFiles[i]);
			if (destFile.exists() && destFile.isFile()) {
				log.info("文件" + confFiles[i] + "已存在");
			} else {
				log.info("文件" + confFiles[i] + "不存在，复制文件至config下");
				this.copyFile(confFiles[i], destFile);
			}
		}
	}

	public void copyFile(String srcFileName, File destFile) {
		// 复制文件
		int byteread = 0;
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			destFile.createNewFile();
			InputStream is = this.getClass().getResourceAsStream(
					"/" + srcFileName);
			BufferedInputStream bis = new BufferedInputStream(is);

			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = bis.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			System.out.println(srcFileName + "复制完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws Exception {
		LoadConfig l = new LoadConfig();
		l.init();
	}
}
