package com.vteam.common.mail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.ssl.internal.ssl.Provider;
import com.vteam.common.CommonUtil;

public class MailSender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
	
	private String host;
	private String from;
	private String to;
	private String filename;
	private String subject;
	private String content;
	private String fromname;
	private String charset;
	private String htmlfile;
	private String htmlcharset;
	private boolean ishtmlfile;
	private String cc;
	Properties props = System.getProperties();

	public MailSender(String host, String from, String to, String filename) {
		this.host = host;
		this.from = from;
		this.to = to;
		this.filename = filename;
		this.props.put("mail.smtp.host", host);
	}

	public void setSubject(String str) {
		this.subject = str;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void mail(String username, String password) throws Exception {
		if ((this.charset == null)
				|| ((this.charset != null) && (this.charset.equals("")))) {
			this.charset = "Big5";
		}

		Session session = null;
		if ((username == null) && (password == null)) {
			session = Session.getInstance(this.props, null);
		} else {
			final String u = username;
			final String p = password;
			session = Session.getDefaultInstance(this.props,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(u, p);
						}
					});
		}

		MimeMessage message = new MimeMessage(session);
		if ((this.fromname != null) && (!this.fromname.trim().equals(""))) {
			message.setFrom(new InternetAddress(this.from, this.fromname,
					this.charset));
		} else {
			message.setFrom(new InternetAddress(this.from));
		}
		Address[] tos = InternetAddress.parse(this.to);
		message.addRecipients(Message.RecipientType.TO, tos);

		if ((this.cc != null) && (this.cc.length() > 0)) {
			Address[] ccs = InternetAddress.parse(this.cc);
			message.addRecipients(Message.RecipientType.CC, ccs);
		}

		if (this.subject == null) {
			this.subject = "(no subject)";
		}

		if (this.content == null) {
			this.content = "this is blank content..";
		}
		message.setSubject(this.subject, this.charset);

		message.setSentDate(CommonUtil.getNowDateTime());

		if ((this.filename != null) && (!this.filename.equals(""))) {
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(this.filename);
			messageBodyPart.setText(this.content, this.charset);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(this.filename);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

		} else if ((this.htmlfile == null)
				|| ((this.htmlfile != null) && (this.htmlfile.equals("")))) {
			message.setText(this.content, this.charset);

		} else if (this.ishtmlfile) {
			StringBuffer sb = getHtmlContent(this.htmlfile, this.htmlcharset);
			message.setContent(sb.toString(), "text/html;charset="
					+ this.htmlcharset);
		} else {
			message.setContent(this.htmlfile, "text/html;charset="
					+ this.htmlcharset);
		}

		Transport.send(message);
	}

	public StringBuffer getHtmlContent(String filename, String charset)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), charset));
		String readLine;
		while ((readLine = in.readLine()) != null) {
			sb.append(readLine + "\r\n");
		}

		in.close();
		return sb;
	}

	public void setSecurity() {
		Security.addProvider(new Provider());
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth", "true");
	}

	public void setTlsSecurity() {
		Security.addProvider(new Provider());
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
	}

	public void setAuthority() {
		props.put("mail.smtp.auth", "true");
	}

	public void setPort(String port) {
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.smtp.socketFactory.port", port);
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
		this.props.put("mail.smtp.host", host);
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return this.cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getFromname() {
		return this.fromname;
	}

	public void setFromname(String fromname) {
		this.fromname = fromname;
	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setHtmlfile(String htmlfile, String charset, boolean ishtmlfile) {
		this.htmlfile = htmlfile;
		this.htmlcharset = charset;
		this.ishtmlfile = ishtmlfile;
	}

	public String getHtmlfile() {
		return this.htmlfile;
	}

	public static void main(String[] args) {
		MailSender mail = new MailSender("ms.mailcloud.com.tw", "cosmohsueh@flysheet.com.tw",
				"cosmohsueh@flysheet.com.tw", null);
		mail.setCharset("utf-8");
		mail.setAuthority();
		mail.setFromname("cosmo");
		mail.setSubject("這次是Utf8寄來的!");
		mail.setContent("你好啊!!!");
		try {
			mail.mail("", "");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}