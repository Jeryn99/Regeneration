package me.fril.regeneration.client;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.fril.regeneration.RegenerationMod;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.binary.Base64;
import org.lwjgl.Sys;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {


    public static HashMap<UUID, String> CSKINNED_PLAYERS = new HashMap<>();
    private static final FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png");
    private static File skinDir = new File("./mods/regeneration/skins/");
    public static File skinCacheDir = new File("./mods/regeneration/skincache/" + Minecraft.getMinecraft().getSession().getProfile().getId() + "/skins");

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

    public static void skinChangeRandom(Random random) throws IOException {
        File skin = SkinChangingHandler.getRandomSkinFile(random);
        String string = SkinChangingHandler.encodeFileToBase64Binary(skin);
            NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(string));
    }


    public static void getSkin(EntityPlayer pl, IRegeneration data) throws IOException {
        AbstractClientPlayer player = (AbstractClientPlayer) pl;

        if (data.getEncodedSkin().equals("NONE")) {
            CSKINNED_PLAYERS.put(pl.getUniqueID(), data.getEncodedSkin());
            setPlayerTexture((AbstractClientPlayer) pl, null);
            return;
        }

        if (!CSKINNED_PLAYERS.containsKey(pl.getUniqueID()) && CapabilityRegeneration.getForPlayer(pl).getState().equals(RegenState.ALIVE)) {
            RegenerationMod.LOG.warn(pl.getName());
            ResourceLocation location = null;
            File file = new File(skinCacheDir, "cache-" + player.getUniqueID() + ".png");

            if (!file.exists()) {
                cacheImageForPlayer(player, CapabilityRegeneration.getForPlayer(pl).getEncodedSkin(), file);
            }

            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                setPlayerTexture(player, player.getLocationSkin());
                return;
            }

            location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("skin", new DynamicTexture(bufferedImage));
            setPlayerTexture(player, location);
            CSKINNED_PLAYERS.put(pl.getUniqueID(), data.getEncodedSkin());
            }
        }

    public static File getRandomSkinFile(Random rand) throws IOException {
        File[] files = skinDir.listFiles(IMAGE_FILTER);

        if (files.length == 0) {
            return createDummyImage("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");
        }

        File file = files[rand.nextInt(files.length)];
        return file;
    }

    public static File createDummyImage(String base64) throws IOException {
        File dummy = new File(skinDir, "dummy.png");
        FileOutputStream osf = new FileOutputStream(dummy);
        byte[] btDataFile = new BASE64Decoder().decodeBuffer(base64);
        osf.write(btDataFile);
        return dummy;
    }

    private static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
        RegenerationMod.LOG.error(player.getName() + " was updated");
        NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
        if (playerInfo == null)
            return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null)
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
    }


    public static void cacheImageForPlayer(AbstractClientPlayer player, String base64, File file) throws IOException {

        if (CapabilityRegeneration.getForPlayer(player).getEncodedSkin().equals("NONE")) {
            RegenerationMod.LOG.info("No need to do anything!");
            return;
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            file.delete();
        }

        byte[] btDataFile = new BASE64Decoder().decodeBuffer(base64);
        FileOutputStream osf = new FileOutputStream(file);
        osf.write(btDataFile);
    }


}
