package com.vteam.common.page;

import java.util.LinkedHashMap;

public class PageUtil {
	
	public static final String PAGENUM = "pagenum=";

	public static String getLinkpath(String linkpath, String originstr,
			String changestr) {
		int lastidx = linkpath.indexOf("&", linkpath.indexOf(originstr) + originstr.length());
		String pageidx = "";
		if (lastidx >= 0) {
			pageidx = linkpath.substring(linkpath.indexOf(originstr) + originstr.length(), lastidx);
		} else {
			pageidx = linkpath.substring(linkpath.indexOf(originstr) + originstr.length());
		}
		String str = originstr + pageidx;
		linkpath = linkpath.replaceAll(str, changestr);

		return linkpath;
	}

	public static Page createPage(int totalcount, int pagenum, int pagesize) {
		Page page = new Page();

		if (pagenum <= 0){
			pagenum = 1;
		}
		int totalpages = 0;
		if(totalcount % pagesize == 0){
			totalpages = totalcount / pagesize;
		}else{
			totalpages = totalcount / pagesize + 1;
		}
		if (pagenum > totalpages){
			pagenum = totalpages;
		}
		page.setCurrentPage(pagenum);
		page.setTotalcount(totalcount);
		page.setTotalPage(totalpages);
		page.setPageSize(pagesize);

		boolean hasNext10Page = (totalpages - (pagenum - 1) / 10 * 10) / 10 > 0;
		boolean hasPrev10Page = (pagenum - 1) / 10 > 0;
		page.setHasNext10Page(hasNext10Page);
		page.setHasPrev10Page(hasPrev10Page);

		return page;
	}

	public static Page setPath(Page page, String pathuri, String querystring) {
		int pagenum = page.getCurrentPage();
		int totalpages = page.getTotalPage();

		int nowrange = (pagenum - 1) / 10;
		int totalrange = (totalpages - 1) / 10;
		int i = nowrange * 10 + 1;
		String paramstr = "";
		if (querystring == null) {
			paramstr = "?pagenum=";
		} else if (querystring.indexOf("pagenum=") < 0) {
			paramstr = "?" + querystring + "&" + "pagenum=";
		} else {
			paramstr = "?" + querystring;
		}

		String linkpath = pathuri + paramstr;
		String originstr = "pagenum=";

		LinkedHashMap<String, String> ht = new LinkedHashMap<String, String>();
		if (totalrange > nowrange) {
			for (int j = i; j < i + 10; j++) {
				String changestr = originstr + j;
				String path = getLinkpath(linkpath, originstr, changestr);
				ht.put(String.valueOf(j), path);
			}
		} else {
			for (int j = i; j < totalpages + 1; j++) {
				String changestr = originstr + j;
				String path = getLinkpath(linkpath, originstr, changestr);
				ht.put(String.valueOf(j), path);
			}
		}
		page.setLinkstrs(ht);
		return page;
	}
}