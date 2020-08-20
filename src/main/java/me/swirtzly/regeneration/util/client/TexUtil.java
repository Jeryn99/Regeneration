package me.swirtzly.regeneration.util.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.skin.HandleSkins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

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

    /*  This is great! It loads based on the Players Gameprofile and works the exact same way Minecraft Skulls do!
     *   This makes updating really quickly, it also doesn't redownload the skin a load of times!
     *   You may pass null in, but you will result in a Steve/Alex skin */
    public static ResourceLocation getSkinFromGameProfile(@Nullable GameProfile gameProfile) {
        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        if (gameProfile != null) {
            Minecraft minecraft = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(gameProfile);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourcelocation = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
                resourcelocation = DefaultPlayerSkin.getDefaultSkin(PlayerEntity.getUUID(gameProfile));
            }
        }

        return resourcelocation;
    }

    /*  This loads a external link into a Native image and returns it as a ResourceLocation
        NOTE: This is not tracked and is never cleaned up at all manually by me, it may be cleaned by
        the Garbage collector when not used, but this is not guaranteed */
    public static ResourceLocation urlToTexture(URL url) {
        URLConnection uc = null;
        NativeImage image = null;
        try {
            uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
            Regeneration.LOG.warn("Downloading Image from: {}", url.toString());
            image = NativeImage.read(uc.getInputStream());
            return Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("download_", new DynamicTexture(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DefaultPlayerSkin.getDefaultSkinLegacy();
    }

    public static String getEncodedMojangSkin(AbstractClientPlayerEntity pl) {
        try {
            long current = System.currentTimeMillis();
            URL url = new URL("https://crafatar.com/skins/" + pl.getUniqueID().toString());
            URLConnection openConnection = url.openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            InputStream is = openConnection.getInputStream();
            File file = new File("./regen_temp/" + current + ".png");
            FileUtils.copyInputStreamToFile(is, file);
            String skin = HandleSkins.imageToPixelData(file);
            file.delete();
            return skin;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "CRASH ME";
    }

}
