package me.swirtzly.regeneration.util.client;

import me.swirtzly.regeneration.Regeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Swirtzly
 * on 26/05/2020 @ 15:03
 */
public class TexUtil {


    public static ResourceLocation fileTotexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = NativeImage.read(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("file_" + System.currentTimeMillis(), new DynamicTexture(nativeImage));
    }


    public static ResourceLocation urlToTexture(URL url) {
        URLConnection uc = null;
        NativeImage image = null;
        try {
            uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
            Regeneration.LOG.warn("Downloading Skin from: {}", url.toString());
            image = NativeImage.read(uc.getInputStream());
            return Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("download_", new DynamicTexture(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DefaultPlayerSkin.getDefaultSkinLegacy();
    }

}
