package mc.craig.software.regen.util;

import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;

public class TextureFixer {

    public static NativeImage processLegacySkin(NativeImage image, String urlString) {
        // Check if the input image is null or has an invalid size
        if (image == null || (image.getWidth() != 64 && (image.getHeight() != 32 || image.getHeight() != 64))) {
            // Log a warning message to the console
            Regeneration.LOGGER.warn("Invalid image size. Expected (64x32) or (64x64), got ({}x{})", image.getWidth(), image.getHeight());
            // Return null
            return null;
        }

        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        // Check if the image needs patching to match the modern format
        boolean needsPatch = imageHeight == 32;
        if (needsPatch) {
            Regeneration.LOGGER.warn("Fixing incorrectly sized ({}x{}) skin texture from {}", imageWidth, imageHeight, urlString);

            // Create a new image with the correct size
            NativeImage patchedImage = new NativeImage(64, 64, true);

            // Copy the original image into the new image
            patchedImage.copyFrom(image);

            // Fill the bottom half of the new image with transparent pixels
            patchedImage.fillRect(0, 32, 64, 32, 0);

            // Copy the top half of the original image into the top half of the new image
            patchedImage.copyRect(4, 16, 16, 32, 4, 4, true, false);
            patchedImage.copyRect(8, 16, 16, 32, 4, 4, true, false);
            patchedImage.copyRect(0, 20, 24, 32, 4, 12, true, false);
            patchedImage.copyRect(4, 20, 16, 32, 4, 12, true, false);
            patchedImage.copyRect(8, 20, 8, 32, 4, 12, true, false);
            patchedImage.copyRect(12, 20, 16, 32, 4, 12, true, false);
            patchedImage.copyRect(44, 16, -8, 32, 4, 4, true, false);
            patchedImage.copyRect(48, 16, -8, 32, 4, 4, true, false);
            patchedImage.copyRect(40, 20, 0, 32, 4, 12, true, false);
            patchedImage.copyRect(44, 20, -8, 32, 4, 12, true, false);
            patchedImage.copyRect(48, 20, -16, 32, 4, 12, true, false);
            patchedImage.copyRect(52, 20, -8, 32, 4, 12, true, false);

            // Set the original image to the patched image
            image = patchedImage;
        }

        // Set the transparent pixels in the image to match the modern format
        setNoAlpha(image, 0, 0, 32, 16);
        if (needsPatch) {
            doNotchTransparencyHack(image, 32, 0, 64, 32);
        }
        setNoAlpha(image, 0, 16, 64, 32);
        setNoAlpha(image, 16, 48, 48, 64);

        return image;
    }

    /**
     * Applies a workaround for a transparency issue in Minecraft skin textures.
     *
     * @param image  The image to modify.
     * @param x      The x-coordinate of the top-left corner of the rectangular area to modify.
     * @param y      The y-coordinate of the top-left corner of the rectangular area to modify.
     * @param width  The width of the rectangular area to modify.
     * @param height The height of the rectangular area to modify.
     */
    public static void doNotchTransparencyHack(NativeImage image, int x, int y, int width, int height) {
        // Iterate over the pixels in the specified area
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                // Set the alpha value of the pixel to 0 (fully transparent)
                image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) & 0x00FFFFFF);
            }
        }
    }

    /**
     * Sets the transparent pixels in an image to fully opaque.
     *
     * @param image  The image to modify.
     * @param x      The x-coordinate of the top-left corner of the rectangular area to modify.
     * @param y      The y-coordinate of the top-left corner of the rectangular area to modify.
     * @param width  The width of the rectangular area to modify.
     * @param height The height of the rectangular area to modify.
     */
    public static void setNoAlpha(NativeImage image, int x, int y, int width, int height) {
        // Iterate over the pixels in the specified area
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                // Set the alpha value of the pixel to 255 (fully opaque)
                image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) | 0xFF000000);
            }
        }
    }

}
