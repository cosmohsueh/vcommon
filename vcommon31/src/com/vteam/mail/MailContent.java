package com.vteam.mail;

import java.util.Date;
import java.util.List;

public class MailContent {
	private Integer msgid;
	private String reciever;
	private String[] tos;
	private String subject;
	private boolean isfile;
	private Date senddate;
	private long filelength;
	private String content;
	private String error;
	private List<String> filenames;
	private List<Integer> partids;
	private boolean isread;
	private String replyto;
	private boolean ishigh;

	public Integer getMsgid() {
		return this.msgid;
	}

	public void setMsgid(Integer msgid) {
		this.msgid = msgid;
	}

	public String getReciever() {
		return this.reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isIsfile() {
		return this.isfile;
	}

	public void setIsfile(boolean isfile) {
		this.isfile = isfile;
	}

	public Date getSenddate() {
		return this.senddate;
	}

	public void setSenddate(Date senddate) {
		this.senddate = senddate;
	}

	public long getFilelength() {
		return this.filelength;
	}

	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<String> getFilenames() {
		return this.filenames;
	}

	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}

	public List<Integer> getPartids() {
		return this.partids;
	}

	public void setPartids(List<Integer> partids) {
		this.partids = partids;
	}

	public String[] getTos() {
		return this.tos;
	}

	public void setTos(String[] tos) {
		this.tos = tos;
	}

	public boolean isIsread() {
		return this.isread;
	}

	public void setIsread(boolean isread) {
		this.isread = isread;
	}

	public String getReplyto() {
		return this.replyto;
	}

	public void setReplyto(String replyto) {
		this.replyto = replyto;
	}

	public boolean isIshigh() {
		return this.ishigh;
	}

	public void setIshigh(boolean ishigh) {
		this.ishigh = ishigh;
	}
}