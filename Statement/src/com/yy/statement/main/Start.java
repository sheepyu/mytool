package com.yy.statement.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.yy.statement.domain.Remain;
import com.yy.statement.domain.TD;
import com.yy.statement.service.RemainService;
import com.yy.statement.util.DateUtil;
import com.yy.statement.util.LoadConfig;
import com.yy.statement.util.MybatisUtil;

public class Start {

	public static final long ONE_DAY = 24 * 60 * 60 * 1000;// 一天内的毫秒数

	public static Logger log = Logger.getLogger(Start.class);
	SqlSessionFactory sessionFactory = null;
	SqlSession session = null;

	public Start() throws IOException {
		new LoadConfig().init();
		new MybatisUtil();
	}

	public static void main(String[] args) throws IOException {

		new Start().start();

	}

	public void start() {
		Calendar current = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		Long curMillis = current.getTimeInMillis();// 现在时间的偏移量
		// 设置每天早上8点
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Long timeMillis = cal.getTimeInMillis();// 定时时间的偏移量
		Long nextTime = (curMillis - timeMillis) > 0 ? timeMillis - curMillis
				+ ONE_DAY : timeMillis - curMillis;// 离下次定时的时间还有多少毫秒
		log.info("现在时间是：" + DateUtil.getDateFormat() + " 距离下次定时还有 :" + nextTime
				+ "毫秒");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				log.info(DateUtil.getDateFormat() + " 定时开启 ");
				getSession();
				session.clearCache();
				List<TD> tds = session.selectList("com.yy.statement.mapper.remainMapper.getTs");
				new RemainService(session).search();
				log.info(tds);
				log.info("查询完毕，等待下次查询");
			}
		}, 0, ONE_DAY);
	}
	
	public void getSession(){
		session = MybatisUtil.getSession();
	}
}
