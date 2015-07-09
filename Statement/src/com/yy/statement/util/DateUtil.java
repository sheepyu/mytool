package com.yy.statement.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	@Deprecated
	/**
	 * 
	 * @return
	 */
	public static String getDateFormat() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getTime() {
		return getTime("yyyy-MM-dd HH-mm-ss");
	}

	public static String getTime(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String timeString = formatter.format(cal.getTime());
		return timeString;
	}

	public static boolean isRestTime() {
		boolean result = false;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour <= 8 || hour >= 20) {
			result = true;
		}
		return result;
	}

	public static String getYesterday() {
		return getYesterday("yyyy-MM-dd HH-mm-ss");
	}

	public static String getYesterday(String format) {
		return getDayBefor(1,format);
	}
	
	public static String getDayBefor(int day,String format){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -day);
		String befor = new SimpleDateFormat(format).format(cal.getTime());
		return befor;
	}
	
	public static String getDayBefor(int day,String format,Calendar srcDay){
		Date date = srcDay.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -day);
		String befor = new SimpleDateFormat(format).format(cal.getTime());
		return befor;
	}
}
