package com.vteam.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageSize {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageSize.class);
	
	public static void small(String srcFile, String dscFile, int limitw,
			int limith) throws Exception {
		try {
			BufferedImage bi = ImageIO.read(new File(srcFile));
			int width = bi.getWidth();
			int height = bi.getHeight();
			int nwidth = width;
			int nheight = height;

			if ((width >= limitw) || (height >= limith)) {
				double scalew = 1.0D * width / limitw;
				double scaleh = 1.0D * height / limith;
				double scale = scalew > scaleh ? scalew : scaleh;
				nwidth = (int) (width / scale);
				nheight = (int) (height / scale);
			}

			BufferedImage bi2 = new BufferedImage(nwidth, nheight, 1);
			Graphics2D g2d = bi2.createGraphics();
			g2d.drawImage(bi, 0, 0, nwidth, nheight, null);
			g2d.dispose();
			ImageIO.write(bi2, "JPEG", new File(dscFile));
		} catch (IOException e) {
			throw new Exception("error scale:" + e);
		}
	}

	public static void convert(String source, String result) {
		try {
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, "JPG", new File(result));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}