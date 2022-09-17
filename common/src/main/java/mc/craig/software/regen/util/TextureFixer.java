package mc.craig.software.regen.util;

import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;

public class TextureFixer {

    public static NativeImage processLegacySkin(NativeImage image, String urlString) {
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        if (imageWidth == 64 && (imageHeight == 32 || imageHeight == 64)) {
            boolean shouldPatch = imageHeight == 32;
            if (shouldPatch) {
                Regeneration.LOGGER.warn("Fixing incorrectly sized ({}x{}) skin texture from {}", imageWidth, imageHeight, urlString);
                NativeImage nativeImage = new NativeImage(64, 64, true);
                nativeImage.copyFrom(image);
                image.close();
                image = nativeImage;
                nativeImage.fillRect(0, 32, 64, 32, 0);
                nativeImage.copyRect(4, 16, 16, 32, 4, 4, true, false);
                nativeImage.copyRect(8, 16, 16, 32, 4, 4, true, false);
                nativeImage.copyRect(0, 20, 24, 32, 4, 12, true, false);
                nativeImage.copyRect(4, 20, 16, 32, 4, 12, true, false);
                nativeImage.copyRect(8, 20, 8, 32, 4, 12, true, false);
                nativeImage.copyRect(12, 20, 16, 32, 4, 12, true, false);
                nativeImage.copyRect(44, 16, -8, 32, 4, 4, true, false);
                nativeImage.copyRect(48, 16, -8, 32, 4, 4, true, false);
                nativeImage.copyRect(40, 20, 0, 32, 4, 12, true, false);
                nativeImage.copyRect(44, 20, -8, 32, 4, 12, true, false);
                nativeImage.copyRect(48, 20, -16, 32, 4, 12, true, false);
                nativeImage.copyRect(52, 20, -8, 32, 4, 12, true, false);
            }

            setNoAlpha(image, 0, 0, 32, 16);
            if (shouldPatch) {
                doNotchTransparencyHack(image, 32, 0, 64, 32);
            }

            setNoAlpha(image, 0, 16, 64, 32);
            setNoAlpha(image, 16, 48, 48, 64);
            return image;
        } else {
            image.close();
            Regeneration.LOGGER.warn("Discarding incorrectly sized ({}x{}) skin texture from {}", imageWidth, imageHeight, urlString);
            return null;
        }
    }

    private static void doNotchTransparencyHack(NativeImage image, int x, int y, int width, int height) {
        int i;
        int j;
        for (i = x; i < width; ++i) {
            for (j = y; j < height; ++j) {
                int k = image.getPixelRGBA(i, j);
                if ((k >> 24 & 255) < 128) {
                    return;
                }
            }
        }

        for (i = x; i < width; ++i) {
            for (j = y; j < height; ++j) {
                image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) & 16777215);
            }
        }

    }

    private static void setNoAlpha(NativeImage image, int x, int y, int width, int height) {
        for (int i = x; i < width; ++i) {
            for (int j = y; j < height; ++j) {
                image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) | -16777216);
            }
        }

    }

}
