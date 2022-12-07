package mc.craig.software.regen.util;

import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;

public class TextureFixer {

    /**
     * Processes a legacy skin image by adding transparency, resizing, and cropping as necessary.
     *
     * @param image The legacy skin image to process.
     * @return The processed skin image, or null if the image is not the correct size.
     */
    public static NativeImage processLegacySkin(NativeImage image, String name) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width == 64 && (height == 32 || height == 64)) {
            boolean is32x32 = height == 32;
            if (is32x32) {
                // Resize the image to 64x64 and add transparent pixels to the bottom half
                NativeImage resizedImage = new NativeImage(64, 64, true);
                resizedImage.copyFrom(image);
                image.close();
                image = resizedImage;
                resizedImage.fillRect(0, 32, 64, 32, 0);

                // Add transparency to the left and right sides of the image
                resizedImage.copyRect(4, 16, 16, 32, 4, 4, true, false);
                resizedImage.copyRect(8, 16, 16, 32, 4, 4, true, false);
                resizedImage.copyRect(0, 20, 24, 32, 4, 12, true, false);
                resizedImage.copyRect(4, 20, 16, 32, 4, 12, true, false);
                resizedImage.copyRect(8, 20, 8, 32, 4, 12, true, false);
                resizedImage.copyRect(12, 20, 16, 32, 4, 12, true, false);
                resizedImage.copyRect(44, 16, -8, 32, 4, 4, true, false);
                resizedImage.copyRect(48, 16, -8, 32, 4, 4, true, false);
                resizedImage.copyRect(40, 20, 0, 32, 4, 12, true, false);
                resizedImage.copyRect(44, 20, -8, 32, 4, 12, true, false);
                resizedImage.copyRect(48, 20, -16, 32, 4, 12, true, false);
                resizedImage.copyRect(52, 20, -8, 32, 4, 12, true, false);
            }

            // Add transparency to the top and bottom parts of the image
            setNoAlpha(image, 0, 0, 32, 16);
            if (is32x32) {
                doNotchTransparencyHack(image, 32, 0, 64, 32);
            }
            setNoAlpha(image, 0, 16, 64, 32);
            setNoAlpha(image, 16, 48, 48, 64);
            return image;
        } else {
            image.close();
            Regeneration.LOGGER.warn("Discarding incorrectly sized ({}x{}) skin texture from {}", new Object[]{width, height, name});
            return null;
        }
    }

    /**
     * Applies a transparency hack for Notch-style skins. This hack removes the
     * transparency from pixels with an alpha value of 128 or higher in the given
     * region of the image.
     *
     * @param image The image to apply the hack to.
     * @param x The x-coordinate of the region to apply the hack to.
     * @param y The y-coordinate of the region to apply the hack to.
     * @param width The width of the region to apply the hack to.
     * @param height The height of the region to apply the hack to.
     */
    private static void doNotchTransparencyHack(NativeImage image, int x, int y, int width, int height) {
        // Check if there are any transparent pixels in the region
        for (int i = x; i < width; i++) {
            for (int j = y; j < height; j++) {
                int pixel = image.getPixelRGBA(i, j);
                if ((pixel >> 24 & 255) < 128) {
                    // If there are transparent pixels, then we don't need to apply the hack
                    return;
                }
            }
        }

        // If we reach this point, then there are no transparent pixels in the region,
        // so we apply the hack by removing the transparency from all pixels in the region
        for (int i = x; i < width; i++) {
            for (int j = y; j < height; j++) {
                image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) & 16777215);
            }
        }
    }

    /**
     * Removes the transparency from the pixels in the given region of the image.
     *
     * @param image The image to remove the transparency from.
     * @param x The x-coordinate of the region to remove transparency from.
     * @param y The y-coordinate of the region to remove transparency from.
     * @param width The width of the region to remove transparency from.
     * @param height The height of the region to remove transparency from.
     */
    private static void setNoAlpha(NativeImage image, int x, int y, int width, int height) {
        for (int i = x; i < width; i++) {
            for (int j = y; j < height; j++) {
                image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) | -16777216);
            }
        }
    }

}
