package com.yy.statement.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static String getDateFormat(){
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	public static String getMMdd(){
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	public static boolean isRestTime(){
		boolean result = false;
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(new Date());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour<=8||hour>=20){
			result = true;
		}
		
		return result;
	}
}
