package com.yy.momitor.service;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yy.momitor.util.DateUtil;

public class SendMail {
	private static Logger log = Logger.getLogger(SendMail.class);
	private String username; //
	private String password;
	private List<String> addressee = null;// 投递地址

	public SendMail() throws Exception {
		// 1.读取XML文件,获得document对象
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File("config/monitor.xml"));
		// 获得根节点
		Element root = document.getRootElement();
		Element childNode = root.element("mail");
		this.username = childNode.element("username").getText();
		this.password = childNode.element("password").getText();
		List<Element> addList = childNode.elements("addressee");
		this.addressee = new ArrayList<String>();
		for (Element ele : addList) {
			addressee.add(ele.getText());
		}
	}

	public int send(String title, String content, int flag) {

		if (flag == 1) {
			this.send(title, content);
			flag = 12;
		} else {
			log.info("邮件防骚扰中！！！flag:" + flag);
			if (!DateUtil.isRestTime()) {
				flag--;
				log.info("休息时间！！！flag:" + flag);
			}
		}

		return flag;
	}

	public boolean send(String title, String content) {

		Session session = this.getSession();
		session.setDebug(true);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));// 发件人
			// 收件人
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			addressee.size();
			for (int i = 0; i < addressee.size(); i++) {
				addressList.add(new InternetAddress(addressee.get(i)));
			}
			InternetAddress[] address = (InternetAddress[]) addressList
					.toArray(new InternetAddress[addressList.size()]);
			msg.setRecipients(RecipientType.TO, address);
			msg.setSubject(title);// 主题
			msg.setContent("<span style='color:red'>" + content + "</span>",
					"text/html;charset=utf-8");
			Transport.send(msg);// 发送
			log.info("邮件发送成功");
		} catch (AddressException e) {
			log.error("邮件地址错误");
			log.error(this.getTrace(e));
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			log.error("邮件服务器无法连接");
			log.error(this.getTrace(e));
			e.printStackTrace();

			for (int i = 2; i <= 10; i++) {
				try {
					TimeUnit.SECONDS.sleep(i * 10);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				log.info("尝试第" + i + "连接");
				if (send(title, content)) {
					log.info("第" + i + "连接成功，发送邮件");
					return true;
				}
			}
		}
		return true;
	}

	private Session getSession() {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		String smtpService = getSmtpService(username);
		props.setProperty("mail.host", smtpService);
		if(getSmtpService(username).equals("qq")){//如果是QQ邮箱，设置端口号为587
			props.setProperty("mail.smtp.port", "587");
		}
		
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		return session;
	}

	private String getSmtpService(String username) {
		String name = username.split("@")[1].split("\\.")[0];
		String smtp = "smtp." + name + ".com";
		return smtp;
	}

	public static String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}

}
