package com.yy.momitor.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.yy.momitor.service.Monitor;

public class Demo {

	static SqlSession session = null;

	public static void main(String[] args) throws IOException{
		// 读入配置文件
		InputStream is = Demo.class.getResourceAsStream("/conf.xml");
		//InputStream is = new FileInputStream(new File("D:\\conf.xml"));
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
				.build(is);
		session = sessionFactory.openSession(true);
		
		//创建线程
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Monitor(session));

	}

}

