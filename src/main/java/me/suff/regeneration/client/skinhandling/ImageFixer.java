package me.suff.regeneration.client.skinhandling;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageFixer {
	
	private static int[] imageData;
	private static int imageWidth;
	private static int imageHeight;
	
	public static BufferedImage convertSkinTo64x64(BufferedImage image) {
		if (image == null) {
			return null;
		} else {
			imageWidth = 64;
			imageHeight = 64;
			BufferedImage bufferedimage = new BufferedImage(imageWidth, imageHeight, 2);
			Graphics graphics = bufferedimage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			boolean flag = image.getHeight() == 32;
			
			if (flag) {
				graphics.setColor(new Color(0, 0, 0, 0));
				graphics.fillRect(0, 32, 64, 32);
				graphics.drawImage(bufferedimage, 24, 48, 20, 52, 4, 16, 8, 20, null);
				graphics.drawImage(bufferedimage, 28, 48, 24, 52, 8, 16, 12, 20, null);
				graphics.drawImage(bufferedimage, 20, 52, 16, 64, 8, 20, 12, 32, null);
				graphics.drawImage(bufferedimage, 24, 52, 20, 64, 4, 20, 8, 32, null);
				graphics.drawImage(bufferedimage, 28, 52, 24, 64, 0, 20, 4, 32, null);
				graphics.drawImage(bufferedimage, 32, 52, 28, 64, 12, 20, 16, 32, null);
				graphics.drawImage(bufferedimage, 40, 48, 36, 52, 44, 16, 48, 20, null);
				graphics.drawImage(bufferedimage, 44, 48, 40, 52, 48, 16, 52, 20, null);
				graphics.drawImage(bufferedimage, 36, 52, 32, 64, 48, 20, 52, 32, null);
				graphics.drawImage(bufferedimage, 40, 52, 36, 64, 44, 20, 48, 32, null);
				graphics.drawImage(bufferedimage, 44, 52, 40, 64, 40, 20, 44, 32, null);
				graphics.drawImage(bufferedimage, 48, 52, 44, 64, 52, 20, 56, 32, null);
			}
			
			graphics.dispose();
			imageData = ((DataBufferInt) bufferedimage.getRaster().getDataBuffer()).getData();
			setAreaOpaque(0, 0, 32, 16);
			
			if (flag) {
				setAreaTransparent(32, 0, 64, 32);
			}
			
			setAreaOpaque(0, 16, 64, 32);
			setAreaOpaque(16, 48, 48, 64);
			return bufferedimage;
		}
	}
	
	private static void setAreaTransparent(int x, int y, int width, int height) {
		for (int i = x; i < width; ++i) {
			for (int j = y; j < height; ++j) {
				int k = imageData[i + j * imageWidth];
				
				if ((k >> 24 & 255) < 128) {
					return;
				}
			}
		}
		
		for (int l = x; l < width; ++l) {
			for (int i1 = y; i1 < height; ++i1) {
				imageData[l + i1 * imageWidth] &= 16777215;
			}
		}
	}
	
	/**
	 * Makes the given area of the image opaque
	 */
	private static void setAreaOpaque(int x, int y, int width, int height) {
		for (int i = x; i < width; ++i) {
			for (int j = y; j < height; ++j) {
				imageData[i + j * imageWidth] |= -16777216;
			}
		}
	}
}
