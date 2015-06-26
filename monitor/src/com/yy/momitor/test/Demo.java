package com.yy.momitor.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.yy.momitor.service.Monitor;
import com.yy.momitor.util.LoadConfig;

public class Demo {

	static SqlSession session = null;

	public static void main(String[] args) throws IOException {
		// 读入配置文件
		// InputStream is = Demo.class.getResourceAsStream("/conf.xml");
		new LoadConfig().init();// 初始化配置文件
		InputStream is = new FileInputStream(new File("config/conf.xml"));

		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
				.build(is);
		session = sessionFactory.openSession(true);

		new Monitor(session).start();
	}

}
