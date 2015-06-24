package com.yy.statement.main;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Start {

	public static void main(String[] args) {

		InputStream is = Start.class.getResourceAsStream("/conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
				.build(is);
		SqlSession session = sessionFactory.openSession(true);

		// 创建线程
		ExecutorService exec = Executors.newCachedThreadPool();
		//exec.execute(new Monitor(session));
	}

}
