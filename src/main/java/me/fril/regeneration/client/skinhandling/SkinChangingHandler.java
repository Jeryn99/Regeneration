package me.fril.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {

    public static String currentSkin = "banana.png";
    public static File skinDir = new File("./mods/regeneration/skins/");
    public static File skinCacheDir = new File("./mods/regeneration/skincache/" + Minecraft.getMinecraft().getSession().getProfile().getId() + "/skins");
    public static File skinDirSteve = new File(skinDir, "/steve");
    public static File skinDirAlex = new File(skinDir, "/alex");
    public static Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
    private static FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(currentSkin);
    public ModelBase steve = new ModelPlayer(0.1F, false);
    public ModelBase alex = new ModelPlayer(0.1F, true);

    public static void registerResources() {

        if (!skinCacheDir.exists()) {
            skinCacheDir.mkdirs();
        }

        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }

        skinDirAlex.mkdirs();
        skinDirSteve.mkdirs();


        if (Objects.requireNonNull(skinDirAlex.listFiles()).length < 1 || Objects.requireNonNull(skinDirSteve.listFiles()).length < 1) {
            try {
                createDefaultImages();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        if (RegenConfig.changeMySkin) {
            boolean isAlex = random.nextBoolean();
            File skin = SkinChangingHandler.getRandomSkinFile(random, isAlex);
            BufferedImage image = ImageIO.read(skin);
            currentSkin = skin.getName();
            IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(currentSkin);
            byte[] pixelData = SkinChangingHandler.encodeToPixelData(image);
            CapabilityRegeneration.getForPlayer(player).setEncodedSkin(pixelData);
            NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData, isAlex));
        } else {
            ClientUtil.sendResetPacket();
        }
    }

    public static SkinInfo getSkin(EntityPlayer pl, IRegeneration data) throws IOException {
        AbstractClientPlayer player = (AbstractClientPlayer) pl;
        byte[] encodedSkin = CapabilityRegeneration.getForPlayer(pl).getEncodedSkin();
        ResourceLocation resourceLocation = null;
        SkinInfo.SkinType skinType = null;

        if (Arrays.equals(data.getEncodedSkin(), new byte[0]) || encodedSkin.length < 16383) {
            resourceLocation = getSkinFromMojang(player);

            if (player.getSkinType().equals("slim")) {
                skinType = SkinInfo.SkinType.ALEX;
            } else {
                skinType = SkinInfo.SkinType.STEVE;
            }
        } else {
            BufferedImage bufferedImage = toImage(pl, encodedSkin);
            resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(pl.getName() + "_skin", new DynamicTexture(bufferedImage));
            skinType = CapabilityRegeneration.getForPlayer(player).getSkinType();
        }

        return new SkinInfo(resourceLocation, skinType);
    }

    private static ResourceLocation getSkinFromMojang(AbstractClientPlayer player) throws IOException {
        setPlayerTexture(player, null);
        Minecraft minecraft = Minecraft.getMinecraft();
        URL url = new URL(String.format(RegenConfig.downloadUrl, StringUtils.stripControlCodes(player.getUniqueID().toString())));
        BufferedImage img = ImageIO.read(url);
        ImageIO.write(img, "png", new File(skinCacheDir, "cache-" + player.getUniqueID() + ".png"));
        return minecraft.getTextureManager().getDynamicTextureLocation(player.getName() + "_skin", new DynamicTexture(img));
    }

    private static void downloadImages(URL url, File file, String filename) throws IOException {
        RegenerationMod.LOG.info("Downloading Skin from:" + url.toString());
        BufferedImage img = ImageIO.read(url);
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    public static File getRandomSkinFile(Random rand, boolean isAlex) {
        File skinDirectory = null;
        if (isAlex) {
            skinDirectory = skinDirAlex;
        } else {
            skinDirectory = skinDirSteve;
        }
        File[] files = skinDirectory.listFiles(IMAGE_FILTER);

        if (files.length <= 1) {
            files = skinDirectory.listFiles();
        }

        File file = files[rand.nextInt(files.length)];
        return file;
    }

    public static void createDefaultImages() throws IOException {
        for (DefaultSkins value : DefaultSkins.values()) {
            File dummy;
            if (value.isAlexDir()) {
                dummy = skinDirAlex;
            } else {
                dummy = skinDirSteve;
            }

            downloadImages(new URL(value.getURL()), dummy, value.name().toLowerCase());
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

    public static void setPlayerModel(RenderPlayer renderer, ModelBase model) {
        ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, renderer, model, 2);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre e) {
        AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
        IRegeneration data = CapabilityRegeneration.getForPlayer(player);
        UUID uuid = player.getGameProfile().getId();
        ModelBase model;

        if (RegenState.ALIVE == data.getState()) {
            try {
                if (!PLAYER_SKINS.containsKey(player.getUniqueID())) {
                    PLAYER_SKINS.put(player.getGameProfile().getId(), SkinChangingHandler.getSkin(player, data));
                    SkinChangingHandler.setPlayerTexture(player, PLAYER_SKINS.get(uuid).getTextureLocation());

                    if (PLAYER_SKINS.get(uuid).getSkintype() == SkinInfo.SkinType.ALEX) {
                        model = alex;
                    } else {
                        model = steve;
                    }
                    SkinChangingHandler.setPlayerModel(e.getRenderer(), model);
                }
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

}
