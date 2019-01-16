package me.fril.regeneration.client;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.rendering.LayerRegeneration;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.binary.Base64;
import org.lwjgl.Sys;
import sun.awt.image.ToolkitImage;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {

    public static String currentSkin = "banana.png";

    private static final FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(currentSkin);
    public static File skinDir = new File("./mods/regeneration/skins/");
    public static File skinCacheDir = new File("./mods/regeneration/skincache/" + Minecraft.getMinecraft().getSession().getProfile().getId() + "/skins");

    public static void registerResources() {

        if (!skinCacheDir.exists()) {
            skinCacheDir.mkdirs();
        }

        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }

        try {
            createDefaultImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Convert buffered image to Pixel data
    public static byte[] encodeToPixelData(BufferedImage bufferedImage) {
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    //Convert Pixel data to BufferedImage
    private static BufferedImage toImage(EntityPlayer player, byte[] imageData) throws IOException {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(imageData, imageData.length), new Point()));
        File file = new File(skinCacheDir, "cache-" + player.getUniqueID() + ".png");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }


        ImageIO.write(img, "png", file);

        return img;
    }

    public static void skinChangeRandom(Random random, EntityPlayer player) throws IOException {
        if (Minecraft.getMinecraft().player.getUniqueID() != player.getUniqueID()) return;
        File skin = SkinChangingHandler.getRandomSkinFile(random);
        BufferedImage image = ImageIO.read(skin);
        currentSkin = skin.getName();
        byte[] pixelData = SkinChangingHandler.encodeToPixelData(image);
        CapabilityRegeneration.getForPlayer(player).setEncodedSkin(pixelData);
        NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData));
    }


    public static ResourceLocation getSkin(EntityPlayer pl, IRegeneration data) throws IOException {
        AbstractClientPlayer player = (AbstractClientPlayer) pl;
        byte[] encodedSkin = CapabilityRegeneration.getForPlayer(pl).getEncodedSkin();

        if (Arrays.equals(data.getEncodedSkin(), new byte[0])) {
            return fallBackOnMojang(player);
        }

        if (encodedSkin.length < 16383) {
            return fallBackOnMojang(player);
        }

        BufferedImage bufferedImage = toImage(pl, encodedSkin);
        return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(pl.getName() + "_skin", new DynamicTexture(bufferedImage));
    }

    private static ResourceLocation fallBackOnMojang(AbstractClientPlayer player) {
        Minecraft minecraft = Minecraft.getMinecraft();
        Map map = minecraft.getSkinManager().loadSkinFromCache(player.getGameProfile());
        if (map.isEmpty()) {
            map = minecraft.getSessionService().getTextures(minecraft.getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
        }
        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            MinecraftProfileTexture profile = (MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN);
            return new ResourceLocation("skins/" + profile.getHash());
        }
        return null;
    }

    public static File getRandomSkinFile(Random rand) {
        File[] files = skinDir.listFiles(IMAGE_FILTER);
        File file = files[rand.nextInt(files.length)];
        return file;
    }


    public static void createDefaultImages() throws IOException {
        RegenerationMod.LOG.error("No skins exist in the Skins folder, creating placeholders");
        for (DefaultSkins value : DefaultSkins.values()) {
            File dummy = new File(skinDir, value.name().toLowerCase() + ".png");
            FileOutputStream osf = new FileOutputStream(dummy);
            byte[] btDataFile = new BASE64Decoder().decodeBuffer(value.getSkinCode());
            osf.write(btDataFile);
        }
    }

    public static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
        NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
        if (playerInfo == null)
            return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null)
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
    }



}
