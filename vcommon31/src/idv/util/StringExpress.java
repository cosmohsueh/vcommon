package idv.util;

import java.util.Scanner;

public class StringExpress {
	
	public static String replace(String origin, String oldsx, String newsx) {
		if (origin.equals("")) {
			return origin;
		}
		int flag = 0;
		StringBuffer SB = new StringBuffer(origin);
		while (origin.indexOf(oldsx, flag) != -1) {
			SB = SB.replace(origin.indexOf(oldsx, flag),
					origin.indexOf(oldsx, flag) + oldsx.length(), newsx);
			origin = SB.toString();
			flag = origin.indexOf(newsx, flag) + newsx.length();
		}
		return origin;
	}

	public static StringBuffer replaceBuffer(StringBuffer sb, String oldsx,
			String newsx) {
		int flag = 0;
		while (sb.indexOf(oldsx, flag) != -1) {
			sb = sb.replace(sb.indexOf(oldsx, flag), sb.indexOf(oldsx, flag)
					+ oldsx.length(), newsx);
			flag = sb.indexOf(newsx, flag) + newsx.length();
		}
		return sb;
	}

	public static int stringCount(String origin, String countIndex) {
		int flag = 0;
		int countInt = 0;
		while (origin.indexOf(countIndex, flag) != -1) {
			countInt++;
			flag = origin.indexOf(countIndex, flag) + 1;
		}
		return countInt;
	}

	public static String[] StringtoArray(String origin, String countIndex) {
		if (origin.lastIndexOf(countIndex) + 1 != origin.length())
			origin = origin + countIndex;
		int countInt = stringCount(origin, countIndex);
		int flag = 0;
		String[] newString = new String[countInt];
		for (int i = 0; i < countInt; i++) {
			newString[i] = origin.substring(flag,
					origin.indexOf(countIndex, flag));
			flag = origin.indexOf(countIndex, flag) + 1;
		}
		return newString;
	}

	public static String toBig5(String str) {
		String newStr = "";
		try {
			byte[] bt = str.getBytes("8859_1");
			newStr = new String(bt, "Big5");
		} catch (Exception localException) {
		}

		return newStr;
	}

	public static String ConvertDate(String in) {
		if (in == null)
			in = "";
		if ((in.indexOf("-") != -1) || (in.indexOf("/") != -1)) {
			in = in.replace('-', '/');
			in = Integer.parseInt(in.substring(0, 4)) - 1911 + in.substring(4);
		}
		return in;
	}

	public static String getFileName(String filename) {
		String str = filename;
		if (filename.indexOf(".") >= 0) {
			str = filename.substring(0, filename.lastIndexOf("."));
		}
		return str;
	}

	public static String getFileExtName(String filename) {
		String str = "";
		if (filename.indexOf(".") >= 0) {
			str = filename.substring(filename.lastIndexOf(".") + 1);
		}
		return str;
	}

	public static boolean isInteger(String s, int radix) {
		boolean flag = true;
		Scanner sc = new Scanner(s.trim());
		if (!sc.hasNextInt(radix)) {
			flag = false;
		}
		sc.nextInt(radix);
		flag = !sc.hasNext();
		sc.close();
		return flag;
	}
}