package me.craig.software.regen.util;

import me.craig.software.regen.Regeneration;
import net.minecraft.client.renderer.texture.NativeImage;

public class TexUtil {

    public static NativeImage processLegacySkin(NativeImage nativeImage) {
        if(nativeImage == null) return null;
        int i = nativeImage.getHeight();
        int j = nativeImage.getWidth();
        if (j == 64 && (i == 32 || i == 64)) {
            boolean flag = i == 32;
            if (flag) {
                NativeImage nativeimage = new NativeImage(64, 64, true);
                nativeimage.copyFrom(nativeImage);
                nativeImage.close();
                nativeImage = nativeimage;
                nativeimage.fillRect(0, 32, 64, 32, 0);
                nativeimage.copyRect(4, 16, 16, 32, 4, 4, true, false);
                nativeimage.copyRect(8, 16, 16, 32, 4, 4, true, false);
                nativeimage.copyRect(0, 20, 24, 32, 4, 12, true, false);
                nativeimage.copyRect(4, 20, 16, 32, 4, 12, true, false);
                nativeimage.copyRect(8, 20, 8, 32, 4, 12, true, false);
                nativeimage.copyRect(12, 20, 16, 32, 4, 12, true, false);
                nativeimage.copyRect(44, 16, -8, 32, 4, 4, true, false);
                nativeimage.copyRect(48, 16, -8, 32, 4, 4, true, false);
                nativeimage.copyRect(40, 20, 0, 32, 4, 12, true, false);
                nativeimage.copyRect(44, 20, -8, 32, 4, 12, true, false);
                nativeimage.copyRect(48, 20, -16, 32, 4, 12, true, false);
                nativeimage.copyRect(52, 20, -8, 32, 4, 12, true, false);
            }

            setNoAlpha(nativeImage, 0, 0, 32, 16);
            if (flag) {
                doNotchTransparencyHack(nativeImage, 32, 0, 64, 32);
            }

            setNoAlpha(nativeImage, 0, 16, 64, 32);
            setNoAlpha(nativeImage, 16, 48, 48, 64);
        } else {
            nativeImage.close();
            Regeneration.LOG.warn("Discarding incorrectly sized ({}x{}) skin texture", j, i);
        }
        //nativeImage.writeToFile(file);
        return nativeImage;
    }

    private static void doNotchTransparencyHack(NativeImage p_229158_0_, int p_229158_1_, int p_229158_2_, int p_229158_3_, int p_229158_4_) {
        for (int i = p_229158_1_; i < p_229158_3_; ++i) {
            for (int j = p_229158_2_; j < p_229158_4_; ++j) {
                int k = p_229158_0_.getPixelRGBA(i, j);
                if ((k >> 24 & 255) < 128) {
                    return;
                }
            }
        }

        for (int l = p_229158_1_; l < p_229158_3_; ++l) {
            for (int i1 = p_229158_2_; i1 < p_229158_4_; ++i1) {
                p_229158_0_.setPixelRGBA(l, i1, p_229158_0_.getPixelRGBA(l, i1) & 16777215);
            }
        }

    }

    private static void setNoAlpha(NativeImage p_229161_0_, int p_229161_1_, int p_229161_2_, int p_229161_3_, int p_229161_4_) {
        for (int i = p_229161_1_; i < p_229161_3_; ++i) {
            for (int j = p_229161_2_; j < p_229161_4_; ++j) {
                p_229161_0_.setPixelRGBA(i, j, p_229161_0_.getPixelRGBA(i, j) | -16777216);
            }
        }

    }

}
