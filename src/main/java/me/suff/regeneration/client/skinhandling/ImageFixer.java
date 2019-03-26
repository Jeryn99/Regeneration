package me.suff.regeneration.client.skinhandling;

import net.minecraft.client.renderer.texture.NativeImage;

public class ImageFixer {
	
	
	public static NativeImage parseUserSkin(NativeImage nativeImageIn) {
		boolean flag = nativeImageIn.getHeight() == 32;
		if (flag) {
			NativeImage nativeimage = new NativeImage(64, 64, true);
			nativeimage.copyImageData(nativeImageIn);
			nativeImageIn.close();
			nativeImageIn = nativeimage;
			nativeimage.fillAreaRGBA(0, 32, 64, 32, 0);
			nativeimage.copyAreaRGBA(4, 16, 16, 32, 4, 4, true, false);
			nativeimage.copyAreaRGBA(8, 16, 16, 32, 4, 4, true, false);
			nativeimage.copyAreaRGBA(0, 20, 24, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(4, 20, 16, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(8, 20, 8, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(12, 20, 16, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(44, 16, -8, 32, 4, 4, true, false);
			nativeimage.copyAreaRGBA(48, 16, -8, 32, 4, 4, true, false);
			nativeimage.copyAreaRGBA(40, 20, 0, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(44, 20, -8, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(48, 20, -16, 32, 4, 12, true, false);
			nativeimage.copyAreaRGBA(52, 20, -8, 32, 4, 12, true, false);
		}
		
		setAreaOpaque(nativeImageIn, 0, 0, 32, 16);
		if (flag) {
			setAreaTransparent(nativeImageIn, 32, 0, 64, 32);
		}
		
		setAreaOpaque(nativeImageIn, 0, 16, 64, 32);
		setAreaOpaque(nativeImageIn, 16, 48, 48, 64);
		return nativeImageIn;
	}
	
	private static void setAreaTransparent(NativeImage image, int x, int y, int width, int height) {
		for (int i = x; i < width; ++i) {
			for (int j = y; j < height; ++j) {
				int k = image.getPixelRGBA(i, j);
				if ((k >> 24 & 255) < 128) {
					return;
				}
			}
		}
		
		for (int l = x; l < width; ++l) {
			for (int i1 = y; i1 < height; ++i1) {
				image.setPixelRGBA(l, i1, image.getPixelRGBA(l, i1) & 16777215);
			}
		}
		
	}
	
	private static void setAreaOpaque(NativeImage image, int x, int y, int width, int height) {
		for (int i = x; i < width; ++i) {
			for (int j = y; j < height; ++j) {
				image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) | -16777216);
			}
		}
		
	}
}
