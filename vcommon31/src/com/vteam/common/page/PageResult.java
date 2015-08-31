package com.vteam.common.page;

import java.util.List;

public class PageResult<E> {
	
	private Page page;
	private List<E> content;

	public PageResult() {
	}

	public PageResult(Page page, List<E> content) {
		this.page = page;
		this.content = content;
	}

	public List<E> getContent() {
		return this.content;
	}

	public Page getPage() {
		return this.page;
	}

	public void setContent(List<E> content) {
		this.content = content;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
