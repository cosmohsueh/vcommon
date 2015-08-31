package com.vteam.mail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class MailBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public boolean isLegalMailAddress(String mailaddr) throws Exception {
		boolean b = false;
		try {
			InternetAddress ia = new InternetAddress(mailaddr, true);
			String mailserver = ia.getAddress().substring(
					ia.getAddress().lastIndexOf("@") + 1);
			InetAddress.getAllByName(mailserver);
			b = true;
		} catch (AddressException e) {
			throw new Exception("收信人信箱不合法!取消寄送...");
		} catch (UnknownHostException ue) {
			throw new Exception("收信人信箱不合法!取消寄送...");
		}
		return b;
	}

	public StringBuffer getHtmlCode(String filename, String charset)
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
}
