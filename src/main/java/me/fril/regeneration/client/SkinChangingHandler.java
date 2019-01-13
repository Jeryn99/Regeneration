package me.fril.regeneration.client;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.plugin.javascript.navig.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {


    private static final FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png");
    private static File skinDir = new File("./mods/regeneration/skins/");
    private static File skinCacheDir = new File("./mods/regeneration/skincache/skins");

    public static void registerResources() {

        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }

        if (!skinCacheDir.exists()) {
            skinCacheDir.mkdirs();
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

    public static void skinChangeRandom(boolean update) {
        File skin = SkinChangingHandler.getRandomSkinFile();
        System.out.println(skin.getName());
        String string = SkinChangingHandler.encodeFileToBase64Binary(skin);
        if (update) {
            NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(string));
        }
    }

    public static void loadPlayerResource(EntityPlayer pl, IRegeneration data) throws IOException {
        Minecraft minecraft = Minecraft.getMinecraft();
        AbstractClientPlayer player = (AbstractClientPlayer) pl;
        if (true) {
            Map map = minecraft.getSkinManager().loadSkinFromCache(pl.getGameProfile());
            if (map.isEmpty()) {
                map = minecraft.getSessionService().getTextures(minecraft.getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
            }
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                MinecraftProfileTexture profile = (MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN);
                File dir = new File((File) ObfuscationReflectionHelper.getPrivateValue(SkinManager.class, minecraft.getSkinManager(), 2), profile.getHash().substring(0, 2));
                File file = new File(dir, profile.getHash());
                if (file.exists()) {
                    file.delete();
                }
                cacheImage((AbstractClientPlayer) pl, data.getEncodedSkin(), file);
                cacheImage((AbstractClientPlayer) pl, data.getEncodedSkin(), new File(skinCacheDir, "cache-" + pl.getUniqueID() + ".png"));
                ResourceLocation location = new ResourceLocation("skins/" + profile.getHash());
                //  BufferedImage image = ImageIO.read(new File(skinCacheDir, "cache-" + pl.getUniqueID() + ".png"));
                //   location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("skin", new DynamicTexture(image));
                loadTexture(location);
                setPlayerTexture(player, location);
                data.setSkinLoaded(true);
                return;
            }
        }
        // setPlayerTexture(player, null);
    }

    private static ITextureObject loadTexture(ResourceLocation resource) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        return texturemanager.getTexture(resource);
    }

    public static NetworkPlayerInfo getNetworkPlayerInfo(AbstractClientPlayer player) {
        return ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
    }

    public static Map<MinecraftProfileTexture.Type, ResourceLocation> getSkinMap(AbstractClientPlayer player) {
        NetworkPlayerInfo playerInfo = getNetworkPlayerInfo(player);
        if (playerInfo == null) return null; // XXX NPE?
        return ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
    }

    public static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
        getSkinMap(player).put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null)
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, getNetworkPlayerInfo(player), false, 4);
    }


    public static File getRandomSkinFile() {
        File[] files = skinDir.listFiles(IMAGE_FILTER);
        Random rand = new Random();
        File file = files[rand.nextInt(files.length)];
        return file;
    }


    public static void cacheImage(AbstractClientPlayer player, String base64, File file) throws IOException {

        if (CapabilityRegeneration.getForPlayer(player).getEncodedSkin().equals("NONE")) {
            RegenerationMod.LOG.info("No need to do anything!");
            return;
        }

        byte[] btDataFile = new BASE64Decoder().decodeBuffer(base64);
        FileOutputStream osf = new FileOutputStream(file);
        osf.write(btDataFile);
        RegenerationMod.LOG.info("FILE WAS WRITTEN TO: " + file.getAbsolutePath());
    }


}
