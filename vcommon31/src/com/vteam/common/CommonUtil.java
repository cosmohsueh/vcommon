package com.vteam.common;

import idv.util.StringExpress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

	private static final int BUFFER_SIZE = 16384;

	public static Date getNowDate() {
		Calendar c = Calendar.getInstance();
		c.set(c.get(1), c.get(2), c.get(5), 0, 0, 0);
		Date d = c.getTime();
		long l = d.getTime();
		d.setTime(l / 1000L * 1000L);
		return d;
	}

	public static String getNowDate(String dateformat) throws Exception {
		String strdate = "";
		Date dateTimeNow = Calendar.getInstance().getTime();
		DateFormat dateTimeFormat1 = new SimpleDateFormat(dateformat);
		strdate = dateTimeFormat1.format(dateTimeNow);

		return strdate;
	}

	public static java.sql.Date getNowSQLDate() {
		Calendar c = Calendar.getInstance();
		java.sql.Date sqldate = new java.sql.Date(c.getTime().getTime());
		return sqldate;
	}

	public static Date getNowDateTime() {
		Calendar c = Calendar.getInstance();
		Date d = c.getTime();
		return d;
	}

	public static Date addMinute(Date d, int min) {
		long l = d.getTime();
		long min1 = min * 60L * 1000L;
		Date tmpd = new Date(d.getTime());
		tmpd.setTime(l + min1);
		return tmpd;
	}

	public static Date addHour(Date d, int hour) {
		long l = d.getTime();
		long hour1 = hour * 60L * 60L * 1000L;
		Date tmpd = new Date(d.getTime());
		tmpd.setTime(l + hour1);
		return tmpd;
	}

	public static Date addDay(Date d, int day) {
		long l = d.getTime();
		long day1 = day * 24L * 60L * 60L * 1000L;
		Date tmpd = new Date(d.getTime());
		tmpd.setTime(l + day1);
		return tmpd;
	}

	public static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src));
			out = new BufferedOutputStream(new FileOutputStream(dst));
			byte buffer[] = new byte[BUFFER_SIZE];
			int byteRead = 0;
			while ((byteRead = in.read(buffer)) != 0) {
				out.write(buffer, 0, byteRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void copy14(File src, File dst) {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel sfc = null;
		FileChannel tfc = null;
		try {
			fin = new FileInputStream(src);
			fout = new FileOutputStream(dst);
			sfc = fin.getChannel();
			tfc = fout.getChannel();
			sfc.transferTo(0L, sfc.size(), tfc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sfc != null) {
				try {
					sfc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (tfc != null) {
				try {
					tfc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fin != null) {
				try {
					fin.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fout != null) {
				try {
					fout.flush();
					fout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String addBreaktag(String inputstr) {
		String beforestr = "\n";
		String afterstr = "\n<br/>";
		inputstr = inputstr.replaceAll(beforestr, afterstr);
		return inputstr;
	}

	public static String trancate(String str, int tranchars, String lastdash) {
		String bs = str;
		if (str.getBytes().length > tranchars * 2) {
			int idx = 0;
			int charidx = 0;
			StringBuilder tmpstr = new StringBuilder("");
			while (idx < tranchars * 2) {
				if (str.codePointAt(charidx) > 126) {
					idx += 2;
				} else {
					idx++;
				}
				tmpstr.append(str.substring(charidx, charidx + 1));
				charidx++;
			}
			bs = tmpstr.toString() + lastdash;
		}
		return bs;
	}

	public static Calendar stringToCalendar(String strdate) {
		Calendar date = null;

		if (strdate.length() == 8) {
			try {
				int year = Integer.parseInt(strdate.substring(0, 4));
				int month = Integer.parseInt(strdate.substring(4, 6)) - 1;
				int day = Integer.parseInt(strdate.substring(6, 8));
				date = Calendar.getInstance();
				date.set(year, month, day, 0, 0, 0);
				date.set(14, 0);
			} catch (Exception e) {
				return null;
			}

		} else if ((strdate.indexOf("-") >= 0)
				&& (StringExpress.stringCount(strdate, "-") == 2)) {
			try {
				int idx1 = strdate.indexOf("-");
				int idx2 = strdate.indexOf("-", idx1 + 1);
				int year = Integer.parseInt(strdate.substring(0, idx1));
				int month = Integer.parseInt(strdate.substring(idx1 + 1, idx2)) - 1;
				int day = Integer.parseInt(strdate.substring(idx2 + 1));
				date = Calendar.getInstance();
				date.set(year, month, day, 0, 0, 0);
				date.set(14, 0);
			} catch (Exception e) {
				return null;
			}

		} else if ((strdate.indexOf("/") >= 0)
				&& (StringExpress.stringCount(strdate, "/") == 2)) {
			try {
				int idx1 = strdate.indexOf("/");
				int idx2 = strdate.indexOf("/", idx1 + 1);
				int year = Integer.parseInt(strdate.substring(0, idx1));
				int month = Integer.parseInt(strdate.substring(idx1 + 1, idx2)) - 1;
				int day = Integer.parseInt(strdate.substring(idx2 + 1));
				date = Calendar.getInstance();
				date.set(year, month, day, 0, 0, 0);
				date.set(14, 0);
			} catch (Exception e) {
				return null;
			}
		}

		return date;
	}

	public static Date string2Date(String datestr, String pattern) {
		Date d = null;
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			d = df.parse(datestr);
		} catch (Exception e) {
			System.out.println("error string2date!");
		}
		return d;
	}

	public static String date2String(Date d, String pattern) {
		String s = "";
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		if (d != null)
			try {
				s = df.format(d);
			} catch (Exception e) {
				System.out.println("error date2string!");
			}
		return s;
	}

	public static int getStrictAge(Calendar nowdate, Calendar birthdate) {
		int year = 0;
		int md1 = nowdate.get(2) * 100 + nowdate.get(5);
		int md2 = birthdate.get(2) * 100 + birthdate.get(5);
		year = nowdate.get(1) - birthdate.get(1);
		if (md1 < md2)
			year--;
		return year;
	}

	public static int getStrictAgeByMonth(Calendar nowdate, Calendar birthdate) {
		int year = 0;
		int m1 = nowdate.get(2);
		int m2 = birthdate.get(2);
		year = nowdate.get(1) - birthdate.get(1);
		if (m1 < m2)
			year--;
		return year;
	}

	public static String getSqlSearchText(String text) {
		text = text.trim();
		if (text.length() == 0)
			return "";
		StringBuffer txt = new StringBuffer();

		char c0 = '%';
		char c1 = '_';
		char c2 = '[';
		for (int j = 0; j < text.length(); j++) {
			char s = text.charAt(j);
			if (s == c0) {
				txt.append("[%]");
			} else if (s == c1) {
				txt.append("[_]");
			} else if (s == c2) {
				txt.append("[[]");
			} else {
				txt.append(s);
			}
		}
		text = txt.toString();

		int flagcnt = StringExpress.stringCount(text, "\"");
		txt = new StringBuffer();
		String tmpstr = text;
		while (flagcnt >= 2) {
			int flag0 = tmpstr.indexOf("\"");
			int flag1 = tmpstr.indexOf("\"", flag0 + 1);
			if (flag0 > 0) {
				txt.append(tmpstr.substring(0, flag0));
			}
			String tmpstr2 = tmpstr.substring(flag0 + 1, flag1);
			tmpstr2 = StringExpress.replace(tmpstr2, " ", "|_|");
			txt.append(tmpstr2);
			tmpstr = tmpstr.substring(flag1 + 1);
			flagcnt = StringExpress.stringCount(tmpstr, "\"");
		}
		txt.append(tmpstr);

		text = txt.toString();
		text = StringExpress.replace(text, " ", "_");
		text = StringExpress.replace(text, "|_|", " ");

		return text;
	}

	public static String[] getSqlSearchTextArray(String text) {
		text = text.trim();
		if (text.length() == 0)
			return new String[] { "" };
		StringBuffer txt = new StringBuffer();

		char c0 = '%';
		char c1 = '_';
		char c2 = '[';
		for (int j = 0; j < text.length(); j++) {
			char s = text.charAt(j);
			if (s == c0) {
				txt.append("[%]");
			} else if (s == c1) {
				txt.append("[_]");
			} else if (s == c2) {
				txt.append("[[]");
			} else {
				txt.append(s);
			}
		}
		text = txt.toString();

		int flagcnt = StringExpress.stringCount(text, "\"");
		txt = new StringBuffer();
		String tmpstr = text;
		while (flagcnt >= 2) {
			int flag0 = tmpstr.indexOf("\"");
			int flag1 = tmpstr.indexOf("\"", flag0 + 1);
			if (flag0 > 0) {
				txt.append(tmpstr.substring(0, flag0));
			}
			String tmpstr2 = tmpstr.substring(flag0 + 1, flag1);
			tmpstr2 = StringExpress.replace(tmpstr2, " ", "|_|");
			txt.append(tmpstr2);
			tmpstr = tmpstr.substring(flag1 + 1);
			flagcnt = StringExpress.stringCount(tmpstr, "\"");
		}
		txt.append(tmpstr);

		text = txt.toString();

		String[] texts = text.split(" ");
		for (int i = 0; i < texts.length; i++) {
			String str = texts[i];
			texts[i] = StringExpress.replace(str, "|_|", " ");
		}
		return texts;
	}

	public static String date2TaiwanString(Date d, String pattern,
			int yearlength) {
		String s = "";
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		if (d != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int year = c.get(1);
			c.set(1, year - 1911);
			d = c.getTime();
			try {
				s = df.format(d);
				if ((yearlength == 3) || (yearlength == 2)) {
					int y = 4 - yearlength;
					s = s.substring(y);
				}
			} catch (Exception e) {
				System.out.println("error date2string!");
			}
		}
		return s;
	}
}