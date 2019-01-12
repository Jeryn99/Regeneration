package me.fril.regeneration.client;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {


    private static final FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png");
    private static File skinDir = new File("./mods/regeneration/assets/regen/skins/");
    private static File skinCacheDir = new File("./mods/regeneration/assets/regen/cache/");

    public static void registerResources() {

        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }

        if (!skinCacheDir.exists()) {
            skinCacheDir.mkdirs();
        }
    }


    public static ResourceLocation getSkin(AbstractClientPlayer player) throws IOException {
        File imageFile = new File(skinCacheDir.getPath() + player.getUniqueID() + ".png");
        ResourceLocation skinLocation = null;
        if (imageFile.exists()) {
            BufferedImage bf = ImageIO.read(imageFile);
            skinLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("skin", new DynamicTexture(bf));
            return skinLocation;
        } else {
            if (!CapabilityRegeneration.getForPlayer(player).getEncodedSkin().equals("NONE")) {
                cacheImage(player, CapabilityRegeneration.getForPlayer(player).getEncodedSkin());
            } else {
                return player.getLocationSkin();
            }
            BufferedImage bf = ImageIO.read(imageFile);
            skinLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("skin", new DynamicTexture(bf));
            return skinLocation;
        }
    }

    public static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedfile;
    }


    public static ResourceLocation retrieveSkin(AbstractClientPlayer player) throws IOException {
        ResourceLocation resourceLocation = new ResourceLocation("regen", "cache/" + player.getUniqueID() + ".png");
        File check = new File(resourceLocation.getPath());
        if (check.exists()) {
            return resourceLocation;
        } else {
            cacheImage(player, CapabilityRegeneration.getForPlayer(player).getEncodedSkin());
        }
        return resourceLocation;
    }


    public static void skinChangeRandom(AbstractClientPlayer player) throws IOException {
        File skin = SkinChangingHandler.getRandomSkinFile();
        String string = SkinChangingHandler.encodeFileToBase64Binary(skin);
        BufferedImage img = ImageIO.read(SkinChangingHandler.getRandomSkinFile());
        ResourceLocation skinLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("skin", new DynamicTexture(img));
        ClientUtil.setPlayerTexture(player, skinLocation);
        NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(string));
        SkinChangingHandler.cacheImage(player, string);
    }


    public static File getRandomSkinFile() {
        File[] files = skinDir.listFiles(IMAGE_FILTER);
        Random rand = new Random();
        File file = files[rand.nextInt(files.length)];
        System.out.println(file.getName());
        return file;
    }


    public static void cacheImage(AbstractClientPlayer player, String base64) throws IOException {
        byte[] btDataFile = new BASE64Decoder().decodeBuffer(base64);
        File of = new File(skinCacheDir.getPath() + player.getUniqueID() + ".png");
        FileOutputStream osf = new FileOutputStream(of);
        osf.write(btDataFile);
    }


}
