package com.vteam.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.ssl.internal.ssl.Provider;
import com.vteam.common.CommonUtil;

public class MailSenders {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSenders.class);
	
	private String from;
	private String to;
	private String[] filename;
	private String subject;
	private String content;
	private String fromname;
	private String charset;
	private String htmlfile;
	private String htmlcharset;
	private boolean ishtmlfile;
	private String cc;
	private Properties props;
	private Integer reply;
	private boolean ishigh;

	public MailSenders(String from, String to) throws Exception {
		this.from = from;
		this.to = to;
		getSession();
	}

	public MailSenders(String from, String to, String charset) throws Exception {
		this.from = from;
		this.to = to;
		this.charset = charset;
		getSession();
	}

	public void setSubject(String str) {
		this.subject = str;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Session getSession() throws Exception {
		MailLoader ml = new MailLoader();
		Session session = ml.getSession();
		this.props = session.getProperties();
		return session;
	}

	public void mail(String username, String password) throws Exception {
		LOGGER.info("mail out...");

		Session session = getSession();
		final String u = username;
		final String p = password;
		session = Session.getDefaultInstance(this.props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(u, p);
			}
		});
		if ((this.charset == null)
				|| ((this.charset != null) && (this.charset.equals("")))) {
			this.charset = "Big5";
		}

		MimeMessage message = new MimeMessage(session);
		if ((this.fromname != null) && (!this.fromname.trim().equals(""))) {
			message.setFrom(new InternetAddress(this.from, this.fromname,
					this.charset));
		} else {
			message.setFrom(new InternetAddress(this.from));
		}
		InternetAddress[] tos = InternetAddress.parse(this.to, true);
		InternetAddress[] newtos = new InternetAddress[tos.length];
		for (int i = 0; i < tos.length; i++) {
			InternetAddress newto = new InternetAddress(tos[i].getAddress(),
					tos[i].getPersonal(), this.charset);
			newtos[i] = newto;
		}
		message.addRecipients(Message.RecipientType.TO, newtos);

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

		if (this.filename != null) {
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			if ((this.htmlfile == null)
					|| ((this.htmlfile != null) && (this.htmlfile.equals("")))) {
				messageBodyPart.setText(this.content, this.charset);
				multipart.addBodyPart(messageBodyPart);
			} else {
				if (this.ishtmlfile) {
					StringBuffer sb = getHtmlContent(this.htmlfile,
							this.htmlcharset);
					messageBodyPart.setContent(sb.toString(),
							"text/html;charset=" + this.htmlcharset);
				} else {
					messageBodyPart.setContent(this.htmlfile,
							"text/html;charset=" + this.htmlcharset);
				}
				multipart.addBodyPart(messageBodyPart);
			}
			for (int i = 0; i < this.filename.length; i++) {
				DataSource source = new FileDataSource(this.filename[i]);
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				messageBodyPart2.setDataHandler(new DataHandler(source));
				String fn = this.filename[i].substring(this.filename[i]
						.lastIndexOf(File.separator) + 1);

				String f3 = MimeUtility.encodeText(fn);
				messageBodyPart2.setFileName(f3);

				multipart.addBodyPart(messageBodyPart2);
			}
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

		if ((this.reply != null) && (this.reply.intValue() == 1)) {
			InternetAddress str = new InternetAddress(username, this.fromname,
					this.charset);
			message.addHeader("Return-Receipt-To", str.toString());
		}

		if (this.ishigh) {
			message.addHeader("X-Priority", "1");
		}

		Transport.send(message);

		String sent_folder = session.getProperty("SentFolder");
		if (sent_folder != null) {
			Store store = session.getStore("imap");
			String host = session.getProperty("host");
			store.connect(host, username, password);
			Folder folder = store.getFolder(sent_folder);
			folder.open(2);
			message.setFlag(Flags.Flag.SEEN, true);
			folder.appendMessages(new Message[] { message });
			store.close();
		}
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
		this.props.setProperty("mail.smtp.socketFactory.class",	"javax.net.ssl.SSLSocketFactory");
		this.props.setProperty("mail.smtp.socketFactory.fallback", "false");
		this.props.put("mail.smtp.auth", "true");
	}

	public void setAuthority() {
		this.props.put("mail.smtp.auth", "true");
	}

	public void setPort(String port) {
		this.props.setProperty("mail.smtp.port", port);
		this.props.setProperty("mail.smtp.socketFactory.port", port);
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

	public String[] getFilename() {
		return this.filename;
	}

	public void setFilename(String[] filename) {
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

	public Integer getReply() {
		return this.reply;
	}

	public void setReply(Integer reply) {
		this.reply = reply;
	}

	public boolean isIshigh() {
		return this.ishigh;
	}

	public void setIshigh(boolean ishigh) {
		this.ishigh = ishigh;
	}
}
