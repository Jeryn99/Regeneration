package me.swirtzly.regen.client.skin;


import com.google.common.base.MoreObjects;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.SkinMessage;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinHandler {


    //Skin Storage
    public static final HashMap<UUID, ResourceLocation> PLAYER_SKINS = new HashMap<>();

    public static void tick(AbstractClientPlayerEntity playerEntity) {
        RegenCap.get(playerEntity).ifPresent(iRegen -> {
            boolean forceUpdate = false;

            byte[] skin = iRegen.getSkin();
            UUID uuid = playerEntity.getUniqueID();

            boolean validSkin = iRegen.isSkinValidForUse();

            // Check if the player has a MOD skin and if the cache is present
            // if these conditions are true, we want to generate and cache the skin
            if (validSkin && !hasPlayerSkin(uuid) || iRegen.getTicksAnimating() >= 140) {
                NativeImage skinImage = genSkinNative(skin);
                if (skinImage != null) {
                    addPlayerSkin(playerEntity.getUniqueID(), loadImage(skinImage));
                    forceUpdate = true;
                }
            }

            //If the skin is invalid, we want to remove it and revert to Mojang
            if (!validSkin) {
                removePlayerSkin(playerEntity.getUniqueID());
                forceUpdate = true;
            }

            //Update the skin if required.
            if (forceUpdate || playerEntity.ticksExisted < 20) {
                ResourceLocation skinTexture = getSkinToUse(playerEntity);
                setPlayerSkin(playerEntity, skinTexture);
            }

            boolean isAlex = false;

            if (iRegen.isSkinValidForUse()) {
                isAlex = iRegen.isAlexSkinCurrently();
            } else {
                playerEntity.playerInfo.loadPlayerTextures();
                isAlex = playerEntity.playerInfo.getSkinType().contentEquals("slim");
            }

            setPlayerSkinType(playerEntity, isAlex);
        });
    }

    public static void setPlayerSkinType(AbstractClientPlayerEntity player, boolean isAlex) {
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        playerInfo.skinType = isAlex ? "slim" : "default";
    }

    public static void sendResetMessage() {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.playerInfo.loadPlayerTextures();
            boolean isAlex = player.playerInfo.getSkinType().equals("slim");
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(new byte[0], isAlex));
        }
    }

    public static ResourceLocation loadImage(NativeImage nativeImage) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        return textureManager.getDynamicTextureLocation("player_", new DynamicTexture(nativeImage));
    }

    public static NativeImage genSkinNative(byte[] skinArray) {
        try {
            return NativeImage.read(new ByteArrayInputStream(skinArray));
        } catch (IOException e) {
        /*    CrashReport crashreport = CrashReport.makeCrashReport(e, "Regeneration Skin Creation");
            crashreport = RegenUtil.crashReport(crashreport);
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Skin Creation");
            crashreportcategory.addDetail("Skin bytes", Arrays.toString(skinArray));
            throw new ReportedException(crashreport);*/
            return null;
        }
    }

    //Set players skin
    public static void setPlayerSkin(AbstractClientPlayerEntity player, ResourceLocation texture) {
        if (player.getLocationSkin().equals(texture)) {
            return;
        }
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.playerTextures;
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null) {
            playerInfo.playerTexturesLoaded = false;
        }
    }

    public static boolean hasPlayerSkin(UUID uuid) {
        return PLAYER_SKINS.containsKey(uuid);
    }

    public static void addPlayerSkin(UUID uuid, ResourceLocation texture) {
        PLAYER_SKINS.put(uuid, texture);
    }

    public static void removePlayerSkin(UUID uuid) {
        PLAYER_SKINS.remove(uuid);
    }

    public static ResourceLocation getSkinToUse(AbstractClientPlayerEntity playerEntity) {
        if (PLAYER_SKINS.containsKey(playerEntity.getGameProfile().getId())) {
            return PLAYER_SKINS.get(playerEntity.getGameProfile().getId());
        }
        NetworkPlayerInfo info = playerEntity.playerInfo;
        info.playerTexturesLoaded = false;
        info.loadPlayerTextures();
        return MoreObjects.firstNonNull(info.playerTextures.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(info.gameProfile.getId()));
    }

}
