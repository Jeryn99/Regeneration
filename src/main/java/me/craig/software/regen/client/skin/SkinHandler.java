package me.craig.software.regen.client.skin;


import com.google.common.base.MoreObjects;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.craig.software.regen.network.NetworkDispatcher;
import me.craig.software.regen.network.messages.SkinMessage;
import me.craig.software.regen.util.TexUtil;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.common.regen.state.RegenStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinHandler {


    //Skin Storage
    public static final HashMap<UUID, ResourceLocation> PLAYER_SKINS = new HashMap<>();

    public static void tick(ClientPlayerEntity playerEntity) {


        RegenCap.get(playerEntity).ifPresent(iRegen -> {

            byte[] skin = iRegen.skin();
            UUID uuid = playerEntity.getUUID();

            boolean validSkin = iRegen.isSkinValidForUse();

            // Check if the player has a MOD skin and if the cache is present
            // if these conditions are true, we want to generate and cache the skin
            if (validSkin && !hasPlayerSkin(uuid) || iRegen.regenState() == RegenStates.REGENERATING && iRegen.updateTicks() >= 140) {
                NativeImage skinImage = genSkinNative(skin);
                if (skinImage != null) {
                    boolean isAlex = iRegen.isSkinValidForUse() ? iRegen.currentlyAlex() : getUnmodifiedSkinType(playerEntity);
                    setPlayerSkinType(playerEntity, isAlex);
                    addPlayerSkin(playerEntity.getUUID(), loadImage(skinImage));
                }
            }
        });
    }

    public static boolean getUnmodifiedSkinType(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        if (abstractClientPlayerEntity.playerInfo == null) return false;
        abstractClientPlayerEntity.playerInfo.registerTextures();
        if (abstractClientPlayerEntity.playerInfo.getModelName() == null) {
            return false;
        }
        if (abstractClientPlayerEntity.playerInfo.getModelName().isEmpty()) {
            return false;
        }
        return abstractClientPlayerEntity.playerInfo.getModelName().contentEquals("slim");
    }

    public static void setPlayerSkinType(AbstractClientPlayerEntity player, boolean isAlex) {
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        playerInfo.skinModel = isAlex ? "slim" : "default";
    }

    public static void sendResetMessage() {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.playerInfo.registerTextures();
            boolean isAlex = player.playerInfo.getModelName().equals("slim");
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(new byte[0], isAlex));
        }
    }

    public static ResourceLocation loadImage(NativeImage nativeImage) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        NativeImage converted = TexUtil.processLegacySkin(nativeImage);
        return textureManager.register("player_", new DynamicTexture(converted));
    }

    public static NativeImage genSkinNative(byte[] skinArray) {
        try {
            return NativeImage.read(new ByteArrayInputStream(skinArray));
        } catch (IOException e) {
            return null;
        }
    }

    //Set players skin
    public static void setPlayerSkin(AbstractClientPlayerEntity player, ResourceLocation texture) {
        if (player.getSkinTextureLocation().equals(texture)) {
            return;
        }
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.textureLocations;
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null) {
            playerInfo.pendingTextures = false;
        }
    }

    public static void setPlayerCape(AbstractClientPlayerEntity player, ResourceLocation texture) {
        if (player.getSkinTextureLocation().equals(texture)) {
            return;
        }
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.textureLocations;
        playerTextures.put(MinecraftProfileTexture.Type.CAPE, texture);
        if (texture == null) {
            playerInfo.pendingTextures = false;
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
        UUID uuid = playerEntity.getGameProfile().getId();
        if (PLAYER_SKINS.containsKey(uuid)) {
            return PLAYER_SKINS.get(uuid);
        }
        if (playerEntity.playerInfo != null) {
            NetworkPlayerInfo info = playerEntity.playerInfo;
            info.pendingTextures = false;
            info.registerTextures();
            return MoreObjects.firstNonNull(info.textureLocations.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(info.profile.getId()));
        }
        return playerEntity.getSkinTextureLocation();
    }

}
