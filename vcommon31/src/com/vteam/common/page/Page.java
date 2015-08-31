package com.vteam.common.page;

import java.util.LinkedHashMap;

public class Page {
	
	private boolean hasPrev10Page;
	private boolean hasNext10Page;
	private int pageSize;
	private int totalPage;
	private int currentPage;
	private int beginIndex;
	private int totalcount;
	private LinkedHashMap<String, String> linkstrs;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(boolean hasPrev10Page, boolean hasNext10Page, int pageSize,
			int totalPage, int currentPage, int beginIndex) {
		this.hasPrev10Page = hasPrev10Page;
		this.hasNext10Page = hasNext10Page;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
		this.beginIndex = beginIndex;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean getHasNext10Page() {
		return this.hasNext10Page;
	}

	public void setHasNext10Page(boolean hasNext10Page) {
		this.hasNext10Page = hasNext10Page;
	}

	public boolean getHasPrev10Page() {
		return this.hasPrev10Page;
	}

	public void setHasPrev10Page(boolean hasPrev10Page) {
		this.hasPrev10Page = hasPrev10Page;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getBeginIndex() {
		return this.beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getTotalcount() {
		return this.totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public LinkedHashMap<String, String> getLinkstrs() {
		return this.linkstrs;
	}

	public void setLinkstrs(LinkedHashMap<String, String> linkstrs) {
		this.linkstrs = linkstrs;
	}
}