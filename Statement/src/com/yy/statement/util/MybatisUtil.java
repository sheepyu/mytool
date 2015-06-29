package com.yy.statement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class MybatisUtil {
	
	private static Logger log = Logger.getLogger(MybatisUtil.class);
	
	private static SqlSessionFactory sessionFactory = null;
	private static SqlSession session = null;

	public MybatisUtil() {
		try {
			InputStream is = new FileInputStream(new File("config/conf.xml"));
			sessionFactory = new SqlSessionFactoryBuilder().build(is);
			//session = sessionFactory.openSession(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.fillInStackTrace());
		}
	}
	

	public static SqlSession getSession() {
		return sessionFactory.openSession(true);
	}

	public static void close() {
		session.close();
	}

	public static void main(String[] args) {
		new MybatisUtil();
	}
}
