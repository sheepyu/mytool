package com.yy.momitor.service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yy.momitor.domain.DL;
import com.yy.momitor.domain.TD;
import com.yy.momitor.util.DateUtil;

public class Monitor implements Runnable {

	private static Logger log = Logger.getLogger(Monitor.class);

	private int sleepSeconds = 300;
	private SqlSession session = null;
	private int tdts;// 通道条数警戒线
	private String tdTitle = null;
	private String tdContent = null;
	private String zjTitle = null;
	private String zjContent = null;
	private static int tdFlag = 1;
	private static int zjFlag = 1;
	private List<DL> dlMonitor = new ArrayList<DL>();

	public Monitor(SqlSession session) {
		this.session = session;

		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(this.getClass().getResource(
					"/monitor.xml"));
			Element root = document.getRootElement();

			Element confElement = root.element("config");
			if (confElement.elementText("sleep") != null) {
				sleepSeconds = Integer
						.valueOf(confElement.elementText("sleep"));
			}

			Element tdElement = root.element("td");
			tdTitle = tdElement.elementText("title");
			tdContent = tdElement.elementText("content");
			tdts = Integer.valueOf(tdElement.elementText("ts"));

			Element zjElement = root.element("zj");
			zjTitle = zjElement.elementText("title");
			zjContent = zjElement.elementText("content");
			List<Element> list = zjElement.element("xx_dl").elements("dlm");

			for (Element el : list) {
				int dlid = Integer.valueOf(el.attributeValue("id"));
				float zjye = Float.valueOf(el.attributeValue("zjye"));
				String dlm = el.getStringValue();
				dlMonitor.add(new DL(dlid, zjye, dlm));
			}
			zjElement.element("xx_dl").elementText("dlm");
		} catch (DocumentException e) {
			log.info("配置文件monitor.xml载入错误");
			e.printStackTrace();
		}

	}

	// 启动
	public void start() {
		Calendar cal = Calendar.getInstance();
		// 每天定时执行
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				monitorTD();
				monitorZJ();
			}
		}, cal.getTime(), sleepSeconds * 1000);
	}

	@Override
	public void run() {
		while (true) {
			this.monitorTD();
			this.monitorZJ();
			try {
				TimeUnit.SECONDS.sleep(sleepSeconds);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void monitorTD() {
		session.clearCache();
		List<TD> tds = session
				.selectList("com.yy.momitor.mapper.tdMapper.getTs");
		log.info(tds);
		for (TD td : tds) {
			if (td.getTdbh() == null && td.getTs() > tdts) {
				String content = tdContent.replaceAll("%ts", "" + td.getTs());
				content = DateUtil.getDateFormat() + "  " + content;

				try {
					tdFlag = new SendMail().send(tdTitle, content, tdFlag);
				} catch (Exception e) {
					log.info("发送失败");
					e.printStackTrace();
				}

			} else if (td.getTdbh() == null && td.getTs() <= tdts) {
				log.info(DateUtil.getDateFormat() + " 通道正常");
			}
		}
	}

	public void monitorZJ() {
		// 编辑参数
		List<Integer> ids = new ArrayList<Integer>();
		for (DL dl : dlMonitor) {
			ids.add(dl.getDlid());
		}
		session.clearCache();
		List<DL> dls = session.selectList(
				"com.yy.momitor.mapper.dlMapper.getZj", ids);
		log.info(dls);
		String content = "";
		String title = "";
		for (DL dl : dlMonitor) {
			for (int i = 0; i < dls.size(); i++) {
				if (dl.getDlid() == dls.get(i).getDlid()
						&& dl.getZjye() > dls.get(i).getZjye()) {
					String temp = zjContent.replaceAll("%dlm", dl.getDlm());
					temp = temp.replaceAll("%zjye", "" + dls.get(i).getZjye());
					content += DateUtil.getDateFormat() + " " + temp + "<br/>";
					title += dl.getDlm() + ",";
				}
			}
		}
		title = zjTitle.replaceAll("%dlm", title);
		try {
			if (!content.equals("")) {
				zjFlag = new SendMail().send(title, content, zjFlag);
			} else {
				log.info(DateUtil.getDateFormat() + " 余额正常");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
