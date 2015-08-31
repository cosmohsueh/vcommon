package com.vteam.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Captcha {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Captcha.class);
	
	public static final int OUTNUMBER = 10;
	public static final int OUTSTRING = 36;
	
	public String encString;
	public int num;
	public int pictype;
	public int picwidth;

	public static String outFile(String filename) {
		int width = 100;
		int height = 30;
		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics2D graphics2D = image.createGraphics();

		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		String ch = token.substring(0, 6);
		Color c = new Color(0.3662F, 0.5569F, 0.8232F);
		GradientPaint gp = new GradientPaint(30.0F, 30.0F, c, 15.0F, 25.0F, Color.white, true);
		graphics2D.setPaint(gp);
		Font font = new Font("Courier New Bold", 1, 26);
		graphics2D.setFont(font);
		graphics2D.drawString(ch, 2, 25);
		graphics2D.dispose();
		try {
			OutputStream outputStream = new java.io.FileOutputStream(filename);
			ImageIO.write(image, "jpeg", outputStream);
			outputStream.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return ch;
	}

	public static String outStream(OutputStream output) {
		int width = 100;
		int height = 30;
		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics2D graphics2D = image.createGraphics();

		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		String ch = token.substring(0, 6);
		Color c = new Color(0.3662F, 0.5569F, 0.8232F);
		GradientPaint gp = new GradientPaint(30.0F, 30.0F, c, 15.0F, 25.0F, Color.white, true);
		graphics2D.setPaint(gp);
		Font font = new Font("Courier New Bold", 1, 26);
		graphics2D.setFont(font);
		graphics2D.drawString(ch, 2, 25);
		graphics2D.dispose();

		try {
			ImageIO.write(image, "jpeg", output);
			output.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return ch;
	}

	public void createFile(String filename) {
		int width = 100;
		if (this.picwidth > 0) {
			width = this.picwidth;
		}
		int height = 30;
		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics2D graphics2D = image.createGraphics();

		Random r = new Random();
		int pictype = getPictype();
		if (pictype == 0){
			pictype = 10;
		}
		String token = Long.toString(Math.abs(r.nextLong()), pictype);
		String ch = token.substring(0, getNum());
		Color c = new Color(0.3662F, 0.5569F, 0.8232F);
		GradientPaint gp = new GradientPaint(30.0F, 30.0F, c, 15.0F, 25.0F, Color.white, true);
		graphics2D.setPaint(gp);
		Font font = new Font("Courier New Bold", 1, 26);

		graphics2D.setFont(font);
		graphics2D.drawString(ch, 2, 25);
		graphics2D.dispose();
		try {
			OutputStream outputStream = new java.io.FileOutputStream(filename);
			ImageIO.write(image, "jpeg", outputStream);
			outputStream.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		this.encString = ch;
	}

	public byte[] createStream() {
		int width = 100;
		if (this.picwidth > 0) {
			width = this.picwidth;
		}
		int height = 30;
		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics2D graphics2D = image.createGraphics();

		Random r = new Random();
		int pictype = getPictype();
		if (pictype == 0){
			pictype = 10;
		}
		String token = Long.toString(Math.abs(r.nextLong()), pictype);
		String ch = token.substring(0, getNum());
		Color c = new Color(0.3662F, 0.5569F, 0.8232F);
		GradientPaint gp = new GradientPaint(30.0F, 30.0F, c, 15.0F, 25.0F, Color.white, true);
		graphics2D.setPaint(gp);
		Font font = new Font("Courier New Bold", 1, 26);
		graphics2D.setFont(font);
		graphics2D.drawString(ch, 2, 25);
		graphics2D.dispose();
		byte[] bb = null;
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ImageIO.write(image, "jpeg", bao);
			bb = bao.toByteArray();
			bao.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		this.encString = ch;
		return bb;
	}

	public String getEncString() {
		return this.encString;
	}

	public void setEncString(String encString) {
		this.encString = encString;
	}

	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPictype() {
		return this.pictype;
	}

	public void setPictype(int pictype) {
		this.pictype = pictype;
	}

	public int getPicwidth() {
		return this.picwidth;
	}

	public void setPicwidth(int picwidth) {
		this.picwidth = picwidth;
	}
}