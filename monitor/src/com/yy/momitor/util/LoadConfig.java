package com.yy.momitor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoadConfig {

	public void read() {

		String rootUrl = this.getClass().getResource("/").getPath();
		File dir = new File(rootUrl + "\\config");

		if (dir.exists() && dir.isDirectory()) {
			System.out.println("文件夹config存在");
		} else {
			System.out.println("文件夹config不存在，创建文件夹");
			dir.mkdir();
		}

		File file = new File(dir + "\\monitor.xml");
		System.out.println(file);
		if (file.exists() && file.isFile()) {
			System.out.println("文件monitor.xml存在");
		} else {
			System.out.println("文件monitor.xml不存在，复制文件");
			File srcFile = new File(rootUrl + "\\monitor.xml");
			this.copyFile(srcFile, file);
		}
	}

	public void copyFile(File srcFile, File destFile) {
		// 复制文件
		int byteread = 0;
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			destFile.createNewFile();
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			System.out.println("复制完成");
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
		l.read();
	}
}
