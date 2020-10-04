package me.swirtzly.regen.util.image;

import me.swirtzly.regen.client.skin.SkinHandler;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TexUtil {

    public static ResourceLocation fileTotexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = NativeImage.read(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SkinHandler.loadImage(nativeImage);
    }
}
