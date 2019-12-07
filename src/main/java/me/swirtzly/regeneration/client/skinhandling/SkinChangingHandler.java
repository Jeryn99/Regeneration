package me.swirtzly.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.image.ImageBufferDownloadAlt;
import me.swirtzly.regeneration.client.image.ImageDownloadAlt;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.IRegenType;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.network.MessageUpdateSkin;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.FileUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
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
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {

    public static final File SKIN_DIRECTORY = new File(RegenConfig.skins.skinDir + "/Regeneration/skins/");
    public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
    public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
    public static final Logger SKIN_LOG = LogManager.getLogger("Regeneration Skin Handler");
    private static final Random RAND = new Random();


    /**
     * Encode image to string
     *
     * @param imageFile The image to encode
     * @return encoded string
     */
    public static String imageToPixelData(File imageFile) throws IOException {
        byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(imageFile));
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    /**
     * Decode string to image
     *
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage toImage(String imageString) throws IOException {
        BufferedImage image = null;
        byte[] imageByte;
        Base64.Decoder decoder = Base64.getDecoder();
        imageByte = decoder.decode(imageString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        bis.close();

        if (image == null) {
            throw new IllegalStateException("The image data was " + imageString + " but the image became null...");
        }

        return image;
    }

    public static void sendSkinUpdate(Random random, EntityPlayer player) {
        if (Minecraft.getMinecraft().player.getUniqueID() != player.getUniqueID()) //Not our Player
            return;

        IRegeneration cap = CapabilityRegeneration.getForPlayer(player);

        if (RegenConfig.skins.changeMySkin) {

            String pixelData = "NONE";
            File skin = null;

            if (cap.getNextSkin().equals("NONE")) {
                boolean isAlex = cap.getPreferredModel().isAlex();
                skin = SkinChangingHandler.getRandomSkin(random, isAlex);
                RegenerationMod.LOG.info(skin + " was choosen");
                try {
                    pixelData = SkinChangingHandler.imageToPixelData(skin);
                    NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData, isAlex));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                pixelData = cap.getNextSkin();
                NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData, cap.getNextSkinType() == SkinInfo.SkinType.ALEX));
            }

        } else {
            ClientUtil.sendSkinResetPacket();
        }

    }

    private static File getRandomSkin(Random rand, boolean isAlex) {
        List<File> skins = FileUtil.listAllSkins(isAlex ? EnumChoices.ALEX : EnumChoices.STEVE);
        if (skins.isEmpty()) {
            FileUtil.doSetupOnThread();
            skins = FileUtil.listAllSkins(isAlex ? EnumChoices.ALEX : EnumChoices.STEVE);
        }

        for (File skin : skins) {
            if (!skin.getName().contains(".png")) {
                skins.remove(skin);
            }
        }

        SKIN_LOG.info("There were " + skins.size() + " skins to chose from");
        return (File) skins.toArray()[rand.nextInt(skins.size())];
    }


    public static SkinInfo update(AbstractClientPlayer player) {
        IRegeneration data = CapabilityRegeneration.getForPlayer(player);
        SkinInfo skinData = PlayerDataPool.get(player);
        boolean shouldBeMojang = data.getEncodedSkin().toLowerCase().equals("none") || data.getEncodedSkin().equals(" ") || data.getEncodedSkin().equals("");
        if (shouldBeMojang) {
            //Mojang stuff here
            setPlayerSkin(player, null);
            skinData.setTextureLocation(getMojangSkin(player));
        } else {
            //Generate custom skin
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = toImage(data.getEncodedSkin());
            } catch (IOException e) {
                e.printStackTrace();
            }
            bufferedImage = ClientUtil.ImageFixer.convertSkinTo64x64(bufferedImage);
            if (bufferedImage != null) {
                DynamicTexture tex = new DynamicTexture(bufferedImage);
                ResourceLocation location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(player.getName().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
                RegenerationMod.LOG.warn("Generating Skin file for " + player.getName() + " || " + location);
                skinData.setTextureLocation(location);
            }
        }

        SkinInfo newData = skinData.setSkintype(getSkinType(player, false)).setUpdateRequired(false);
        PlayerDataPool.updatePlayer(player, newData);
        return newData;
    }

    public static ResourceLocation createGuiTexture(File file) {
        BufferedImage bufferedImage = null;
        try {
            if (file != null) {
                bufferedImage = ImageIO.read(file);
            } else {
                return DefaultPlayerSkin.getDefaultSkinLegacy();
            }
            if (bufferedImage != null) {
                return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("gui_skin_" + System.currentTimeMillis(), new DynamicTexture(bufferedImage));
            } else {
                return DefaultPlayerSkin.getDefaultSkinLegacy();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
    }


    /**
     * This is used when the clients skin is reset
     *
     * @param player - Player to get the skin of themselves
     * @return ResourceLocation from Mojang
     * @throws IOException
     */
    private static ResourceLocation getMojangSkin(AbstractClientPlayer player) {
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(player.getGameProfile());
        if (map.isEmpty()) {
            map = Minecraft.getMinecraft().getSessionService().getTextures(Minecraft.getMinecraft().getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
        }
        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            MinecraftProfileTexture profile = map.get(MinecraftProfileTexture.Type.SKIN);
            File dir = new File((File) ObfuscationReflectionHelper.getPrivateValue(SkinManager.class, Minecraft.getMinecraft().getSkinManager(), 2), profile.getHash().substring(0, 2));
            File file = new File(dir, profile.getHash());
            if (file.exists())
                file.delete();
            ResourceLocation location = new ResourceLocation("skins/" + profile.getHash());
            loadTexture(file, location, DefaultPlayerSkin.getDefaultSkinLegacy(), profile.getUrl(), player);
            setPlayerSkin(player, location);
            return player.getLocationSkin();
        }
        return DefaultPlayerSkin.getDefaultSkinLegacy();
    }

    private static ITextureObject loadTexture(File file, ResourceLocation resource, ResourceLocation def, String par1Str, AbstractClientPlayer player) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject object = texturemanager.getTexture(resource);
        if (object == null) {
            object = new ImageDownloadAlt(file, par1Str, def, new ImageBufferDownloadAlt(true));
            texturemanager.loadTexture(resource, object);
        }
        return object;
    }

    /**
     * Changes the ResourceLocation of a Players skin
     *
     * @param player  - Player instance involved
     * @param texture - ResourceLocation of intended texture
     */
    public static void setPlayerSkin(AbstractClientPlayer player, ResourceLocation texture) {
        if (player.getLocationSkin() == texture) {
            return;
        }
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null)
            return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.playerTextures;
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null)
            playerInfo.playerTexturesLoaded = false;
    }

    public static void setSkinType(AbstractClientPlayer player, SkinInfo.SkinType skinType) {
        NetworkPlayerInfo playerInfo = player.playerInfo;
        playerInfo.skinType = skinType.getMojangType();
    }

    public static SkinInfo.SkinType getSkinType(AbstractClientPlayer player, boolean forceMojang) {
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(player.getGameProfile());
        if (map.isEmpty()) {
            map = Minecraft.getMinecraft().getSessionService().getTextures(Minecraft.getMinecraft().getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
        }
        MinecraftProfileTexture profile = map.get(MinecraftProfileTexture.Type.SKIN);

        IRegeneration data = CapabilityRegeneration.getForPlayer(player);


        if (data.getEncodedSkin().toLowerCase().equals("none") || forceMojang) {
            if (profile == null) {
                return SkinInfo.SkinType.STEVE;
            }
            if (profile.getMetadata("model") == null) {
                return SkinInfo.SkinType.STEVE;
            }
        } else {
            return data.getSkinType();
        }

        return SkinInfo.SkinType.ALEX;
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post e) {
        AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
        IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
        IRegenType type = TypeHandler.getTypeInstance(cap.getType());

        if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
            type.getRenderer().onRenderRegeneratingPlayerPost(type, e, cap);
        }

    }

    @SubscribeEvent
    public void clickTick(TickEvent.ClientTickEvent e) {
        if (Minecraft.getMinecraft().world == null) {
            PlayerDataPool.wipeAllData();
        }
    }


    /**
     * Subscription to RenderPlayerEvent.Pre to set players model and texture from hashmap
     *
     * @param e - RenderPlayer Pre Event
     */
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre e) {
        if (MinecraftForgeClient.getRenderPass() == -1) return;
        AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
        IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
        IRegenType type = TypeHandler.getTypeInstance(cap.getType());
        SkinInfo skinData = PlayerDataPool.get(player);

        if (skinData == null) {
            skinData = update(player);
            PlayerDataPool.addPlayer(player, skinData);
        }

        if (player.ticksExisted < 20 || skinData.isUpdateRequired()) {
            update(player);
        }

        setPlayerSkin(player, skinData.getTextureLocation());
        setSkinType(player, skinData.getSkintype());

        if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
            if (type.getAnimationProgress(cap) > 0.7) {
                update(player);
            }
            type.getRenderer().onRenderRegeneratingPlayerPre(type, e, cap);
        }
    }

    public enum EnumChoices implements FileUtil.IEnum {
        ALEX(true), STEVE(false), EITHER(true);

        private boolean isAlex;

        EnumChoices(boolean b) {
            this.isAlex = b;
        }

        public boolean isAlex() {
            if (this == EITHER) {
                return RAND.nextBoolean();
            }
            return isAlex;
        }
    }

}
