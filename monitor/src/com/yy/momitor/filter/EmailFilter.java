package com.yy.momitor.filter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yy.momitor.domain.DL;
import com.yy.momitor.domain.TD;

public class EmailFilter {
	private static Logger log = Logger.getLogger(EmailFilter.class);
	private static Map<String, Calendar> sendMap = new HashMap<String, Calendar>();

	public static boolean canSend(List<DL> dls) {
		boolean result = false;
		Calendar previous = null;
		Calendar current = Calendar.getInstance();

		if (isRestTime()) {
			return false;
		}
		for (int i = 0; i < dls.size(); i++) {

			if ((previous = sendMap.get(dls.get(i).getDlm())) == null) {
				log.info(dls.get(i).getDlm() + "为第一次余额不足");
				result = true;
			} else {
				// 比较上次发送时间，间隔超过24小时则发送
				Long difference = current.getTimeInMillis()
						- previous.getTimeInMillis();
				log.info("difference:" + difference);
				if (difference >= 1000 * 3600 * 24) {
					result = true;
				}
			}
		}
		/**
		 * 如果发送，更新发送时间
		 */
		if (result) {
			for (DL dl : dls) {
				sendMap.put(dl.getDlm(), current);
			}
		}
		return result;
	}

	public static boolean isRestTime() {
		boolean result = false;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour <= 8 || hour >= 23) {
			result = true;
		}
		return result;
	}
}
