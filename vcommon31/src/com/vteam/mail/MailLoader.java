package com.vteam.mail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPStore;

public class MailLoader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MailLoader.class);

	private String protocol = "POP3";
	private String mailbox = "INBOX";
	private String host;
	private String charset;
	private int mailcount;
	private Integer page;

	public MailLoader() {
	}

	public MailLoader(String host, String charset) {
		this.host = host;
		this.charset = charset;
	}

	public Session getSession() throws Exception {
		LOGGER.info("get session...");
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		Session session = null;
		try {
			session = (Session) envCtx.lookup("mail/Session");
			LOGGER.info("session address:" + session);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		if (session == null) {
			Properties props = null;
			try {
				props = System.getProperties();
			} catch (SecurityException sex) {
				props = new Properties();
			}
			session = Session.getInstance(props, null);
		}
		if (this.host == null) {
			this.host = session.getProperty("host");
		}
		if (this.charset == null) {
			this.charset = session.getProperty("charset");
		}

		return session;
	}

	public Integer count(String useraccount, String password) throws Exception {
		Session session = getSession();
		URLName url = new URLName(this.protocol, this.host, -1, this.mailbox,
				useraccount, password);
		Store store = new IMAPStore(session, url);
		store.connect();
		Folder folder = store.getFolder(url);
		folder.open(1);
		this.mailcount = folder.getMessageCount();
		logout(store);
		return Integer.valueOf(this.mailcount);
	}

	public List<MailContent> list(String useraccount, String password,
			Integer page) throws Exception {
		LOGGER.info("list mail...");
		Session session = getSession();
		int pagelist = Integer.parseInt(session.getProperty("pagelist"));
		if (page == null) {
			page = Integer.valueOf(1);
		}
		URLName url = new URLName(this.protocol, this.host, -1, this.mailbox,
				useraccount, password);

		Store store = new IMAPStore(session, url);
		store.connect();
		Folder folder = store.getFolder(url);
		folder.open(1);
		this.mailcount = folder.getMessageCount();
		int ucount = folder.getUnreadMessageCount();
		int ncount = folder.getNewMessageCount();
		LOGGER.info("get mail count=" + this.mailcount + ", new=" + ncount
				+ ", unread=" + ucount);
		List<MailContent> lst = new ArrayList<MailContent>();

		int minvalue = this.mailcount - pagelist * page.intValue() + 1;
		minvalue = minvalue < 1 ? 1 : minvalue;
		for (int i = minvalue; i <= this.mailcount - pagelist
				* (page.intValue() - 1); i++) {
			MailContent ctx = new MailContent();
			Message msg = folder.getMessage(i);
			int msgid = msg.getMessageNumber();
			InternetAddress[] addrs = (InternetAddress[]) msg.getFrom();
			String sender = "";
			if (addrs != null) {
				for (int j = 0; j < addrs.length; j++) {
					sender = sender + addrs[j].getPersonal() + ";";
				}
				sender = sender.substring(0, sender.length() - 1);
			}
			String subject = msg.getSubject();
			Date date = msg.getSentDate();
			String s = msg.getContentType();

			if (s.toLowerCase().indexOf("multipart/mixed") >= 0) {
				ctx.setIsfile(true);
			}

			boolean bread = msg.isSet(Flags.Flag.SEEN);

			String[] str = msg.getHeader("X-Priority");
			if ((str != null) && (str[0].equals("1"))) {
				ctx.setIshigh(true);
			}

			ctx.setMsgid(Integer.valueOf(msgid));
			ctx.setReciever(sender);
			ctx.setSubject(subject);
			ctx.setSenddate(date);
			ctx.setIsread(bread);

			lst.add(ctx);
		}
		logout(store);
		return lst;
	}

	public List<MailContent> listSent(String useraccount, String password,
			Integer page) throws Exception {
		LOGGER.info("sent items mail...");
		Session session = getSession();
		int pagelist = Integer.parseInt(session.getProperty("pagelist"));
		if (page == null) {
			page = Integer.valueOf(1);
		}
		String sent_folder = session.getProperty("SentFolder");
		URLName url = new URLName(this.protocol, this.host, -1, sent_folder,
				useraccount, password);

		Store store = new IMAPStore(session, url);
		store.connect();
		Folder folder = store.getFolder(url);
		folder.open(1);
		this.mailcount = folder.getMessageCount();
		int ucount = folder.getUnreadMessageCount();
		int ncount = folder.getNewMessageCount();
		LOGGER.info("get mail count=" + this.mailcount + ", new=" + ncount
				+ ", unread=" + ucount);
		List<MailContent> lst = new ArrayList<MailContent>();

		int minvalue = this.mailcount - pagelist * page.intValue() + 1;
		minvalue = minvalue < 1 ? 1 : minvalue;
		for (int i = minvalue; i <= this.mailcount - pagelist
				* (page.intValue() - 1); i++) {
			MailContent ctx = new MailContent();
			Message msg = folder.getMessage(i);
			int msgid = msg.getMessageNumber();
			InternetAddress[] addrs = (InternetAddress[]) msg.getFrom();
			String sender = "";
			if (addrs != null) {
				for (int j = 0; j < addrs.length; j++) {
					sender = sender + addrs[j].getPersonal() + ";";
				}
				sender = sender.substring(0, sender.length() - 1);
			}
			String subject = msg.getSubject();
			Date date = msg.getSentDate();
			String s = msg.getContentType();

			if (s.toLowerCase().indexOf("multipart/mixed") >= 0) {
				ctx.setIsfile(true);
			}

			boolean bread = msg.isSet(Flags.Flag.SEEN);

			String[] str = msg.getHeader("X-Priority");
			if ((str != null) && (str[0].equals("1"))) {
				ctx.setIshigh(true);
			}

			ctx.setMsgid(Integer.valueOf(msgid));
			ctx.setReciever(sender);
			ctx.setSubject(subject);
			ctx.setSenddate(date);
			ctx.setIsread(bread);

			lst.add(ctx);
		}
		logout(store);
		return lst;
	}

	public MailContent onemail(int msgid, String useraccount, String password)
			throws Exception {
		LOGGER.info("one mail...");
		Session session = getSession();
		URLName url = new URLName(this.protocol, this.host, -1, this.mailbox,
				useraccount, password);

		Store store = new IMAPStore(session, url);
		store.connect();
		Folder folder = store.getFolder(url);

		folder.open(2);
		MailContent ctx = new MailContent();
		Message msg = folder.getMessage(msgid);
		String[] str = msg.getHeader("X-Priority");
		if ((str != null) && (str[0].equals("1"))) {
			ctx.setIshigh(true);
		}
		InternetAddress[] addrs = (InternetAddress[]) msg.getFrom();
		InternetAddress[] toaddrs = (InternetAddress[]) msg
				.getRecipients(Message.RecipientType.TO);
		String sender = "";
		if (addrs != null) {
			for (int j = 0; j < addrs.length; j++) {
				String addr = addrs[j].getAddress();
				sender = sender + addrs[j].getPersonal() + "|"
						+ addr.substring(0, addr.indexOf("@")) + ";";
			}
			sender = sender.substring(0, sender.length() - 1);
		}
		String[] tos = null;
		if (toaddrs != null) {
			tos = new String[toaddrs.length];
			for (int i = 0; i < toaddrs.length; i++) {
				String name = toaddrs[i].getPersonal();
				String addr = toaddrs[i].getAddress();
				if (name != null) {
					tos[i] = (name + "|" + addr.substring(0, addr.indexOf("@")));
				} else {
					tos[i] = addr;
				}
			}
		}
		String subject = msg.getSubject();
		Date date = msg.getSentDate();
		String s = msg.getContentType();
		if (s.indexOf("multipart/mixed") >= 0) {
			ctx.setIsfile(true);
		}
		ctx.setMsgid(Integer.valueOf(msgid));
		ctx.setReciever(sender);
		ctx.setSubject(subject);
		ctx.setSenddate(date);
		ctx.setTos(tos);
		if (!msg.isSet(Flags.Flag.SEEN)) {
			String[] strs = msg.getHeader("Return-Receipt-To");
			String replyto = "";
			if (strs != null) {
				for (int i = 0; i < strs.length; i++) {
					replyto = strs[i];
					replyto = MimeUtility.decodeText(replyto);
				}
			}
			ctx.setReplyto(replyto);
			msg.setFlag(Flags.Flag.SEEN, true);
		}

		List<String> filenames = new ArrayList<String>();
		List<Integer> partids = new ArrayList<Integer>();
		Object obj = msg.getContent();
		String cnt = "";
		if ((obj instanceof String)) {
			cnt = ((String) obj).trim();
		} else if ((obj instanceof MimeMultipart)) {
			MimeMultipart mp = (MimeMultipart) obj;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				MimeBodyPart mbp = (MimeBodyPart) mp.getBodyPart(i);
				String sctype = mbp.getContentType();
				String filename = mbp.getFileName();
				if (sctype.toLowerCase().indexOf("text/plain") >= 0) {
					if (filename == null) {
						cnt = (String) mbp.getContent();
					} else {
						String f3 = MimeUtility.decodeText(filename);

						filenames.add(f3);
						partids.add(Integer.valueOf(i));
					}

				} else if (filename != null) {
					String f3 = MimeUtility.decodeText(filename);

					filenames.add(f3);
					partids.add(Integer.valueOf(i));
				}
			}
		}

		if (filenames.size() > 0) {
			ctx.setFilenames(filenames);
			ctx.setPartids(partids);
		}
		ctx.setContent(cnt);
		logout(store);
		return ctx;
	}

	public int delmulti(int[] msgids, String useraccount, String password)
			throws Exception {
		LOGGER.info("delete one mail...");
		int msgcount = 0;
		Session session = getSession();
		URLName url = new URLName(this.protocol, this.host, -1, this.mailbox,
				useraccount, password);

		Store store = new IMAPStore(session, url);
		store.connect();
		Folder folder = store.getFolder(url);
		folder.open(2);
		for (int i = 0; i < msgids.length; i++) {
			Message msg = folder.getMessage(msgids[i]);
			msg.setFlag(Flags.Flag.DELETED, true);
		}
		msgcount = folder.getMessageCount();
		folder.close(true);
		logout(store);
		return msgcount;
	}

	public int delone(int msgid, String useraccount, String password)
			throws Exception {
		LOGGER.info("delete one mail...");
		int msgcount = 0;
		Session session = getSession();
		URLName url = new URLName(this.protocol, this.host, -1, this.mailbox,
				useraccount, password);

		Store store = new IMAPStore(session, url);
		store.connect();
		Folder folder = store.getFolder(url);
		folder.open(2);
		Message msg = folder.getMessage(msgid);
		msg.setFlag(Flags.Flag.DELETED, true);
		msgcount = folder.getMessageCount();
		folder.close(true);
		logout(store);
		return msgcount;
	}

	public void getFile(int msgid, int partid, HttpServletResponse response,
			String useraccount, String password) throws Exception {
		URLName url;
		Store store = null;
		InputStream in = null;
		ServletOutputStream out = null;
		try {
			LOGGER.info("downfile...");
			Session session = getSession();
			url = new URLName(protocol, host, -1, mailbox, useraccount,
					password);
			store = new IMAPStore(session, url);
			store.connect();
			Folder folder = store.getFolder(url);
			folder.open(1);
			Message msg = folder.getMessage(msgid);
			Object obj = msg.getContent();
			MimeMultipart mp = (MimeMultipart) obj;
			MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(partid);
			String filename = part.getFileName();
			filename = MimeUtility.decodeText(filename);
			LOGGER.info(filename);
			filename = new String(filename.getBytes("big5"), "iso-8859-1");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", (new StringBuilder(
					"attachment; filename=\"")).append(filename).append("\"")
					.toString());
			out = response.getOutputStream();
			in = part.getInputStream();
			int i;
			while ((i = in.read()) != -1)
				out.write(i);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			logout(store);
			response.flushBuffer();
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	public String getMailHost() throws Exception {
		String mailhost = "";
		Session session = getSession();
		mailhost = session.getProperty("host");
		return mailhost;
	}

	public void logout(Store store) throws Exception {
		store.close();
		store = null;
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getMailbox() {
		return this.mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getMailcount() {
		return this.mailcount;
	}

	public void setMailcount(int mailcount) {
		this.mailcount = mailcount;
	}

	public Integer getPage() {
		return this.page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
