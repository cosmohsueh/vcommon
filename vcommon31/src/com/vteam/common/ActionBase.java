package com.vteam.common;

import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ActionBase extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	public static final int ADDOK = 1;
	public static final int UPDATEOK = 2;
	public static final int DELETEOK = 3;
	
	private String titlename;
	private int pagesize;
	private int pagenum = 1;
	private int resultcount = 0;

	public String getTitlename() {
		return this.titlename;
	}

	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}

	public Map<String, Object> getSession() {
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		return session;
	}

	public static String getRealpath() {
		String realpath = ServletActionContext.getServletContext().getRealPath("");
		return realpath;
	}

	public static String getRealpath(String path) {
		String realpath = ServletActionContext.getServletContext().getRealPath(path);
		return realpath;
	}

	public boolean isLogin(String key) {
		boolean flag = false;
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session.get(key) != null) {
			flag = true;
		}
		return flag;
	}

	public Object getSessionAttribute(String key) {
		Object obj = null;
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session.get(key) != null) {
			obj = session.get(key);
		}
		return obj;
	}

	public int getPagesize() {
		if (this.pagesize == 0){
			this.pagesize = 10;
		}
		return this.pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPagenum() {
		return this.pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getResultcount() {
		return this.resultcount;
	}

	public void setResultcount(int resultcount) {
		this.resultcount = resultcount;
	}
}