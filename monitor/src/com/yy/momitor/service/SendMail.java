package com.yy.momitor.service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		Document document = reader.read(this.getClass().getResource("/monitor.xml"));
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

	public static void main(String[] args) throws Exception {
		SendMail s = new SendMail();
		s.send("测试标题", "测试内容");
	}
	
	public int send(String title,String content,int flag){
		
		if(flag == 1){
			this.send(title,content);
			log.info("邮件发送成功");
			flag = 12;
		}
		else{
			log.info("邮件防骚扰中！！！flag:"+flag);
			if(!DateUtil.isRestTime()){
				flag--;
				log.info("休息时间！！！flag:"+flag);
			}
		}
		
		return flag;
	}
	

	public void send(String title, String content) {
		
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
			InternetAddress[] address = (InternetAddress[]) addressList.toArray(new InternetAddress[addressList.size()]);
			msg.setRecipients(RecipientType.TO, address);
			msg.setSubject(title);// 主题
			msg.setContent("<span style='color:red'>" + content + "</span>", "text/html;charset=utf-8");
		
			Transport.send(msg);//发送
		} catch (AddressException e) {
			log.error("邮件地址错误");
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private Session getSession(){
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		String smtpService = getSmtpService(username);
		props.setProperty("mail.host", smtpService);

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

}
