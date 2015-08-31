package com.vteam.common;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFile {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogFile.class);

	public static void write(String filestr, String logstr) {
		File f = new File(filestr);
		FileOutputStream fos = null;
		try {
			if (!f.isFile()) {
				f.createNewFile();
			}
			fos = new FileOutputStream(f, true);
			logstr = (new StringBuilder(String.valueOf(logstr.trim()))).append("\n").toString();
			fos.write(logstr.getBytes());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
					fos = null;
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}
}
