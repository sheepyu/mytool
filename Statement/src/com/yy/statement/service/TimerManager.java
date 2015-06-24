package com.yy.statement.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerManager implements Runnable {

	public static void main (String[] args) throws Exception{
		Calendar cal = Calendar.getInstance();

		// 每天定点执行
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 14);
		cal.set(Calendar.SECOND, 0);
		Timer timer = new Timer();
		System.out.println("启动");
		timer.schedule(new TimerTask(){
			public void run(){
				System.out.println(new Date()+"测试");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("111");
			}
		},cal.getTime(),3*1000);
		timer.schedule(new TimerTask(){
			public void run(){
				System.out.println(new Date()+"测试2");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("222");
			}
		},cal.getTime(),3*1000);
	}

	@Override
	public void run() {

	}
}
