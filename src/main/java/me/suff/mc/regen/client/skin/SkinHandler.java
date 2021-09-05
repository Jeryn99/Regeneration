package me.suff.mc.regen.client.skin;


import com.google.common.base.MoreObjects;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.NativeImage;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SkinMessage;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinHandler {


    //Skin Storage
    public static final HashMap<UUID, ResourceLocation> PLAYER_SKINS = new HashMap<>();

    public static void tick(AbstractClientPlayer playerEntity) {
        RegenCap.get(playerEntity).ifPresent(iRegen -> {
            boolean hasBeenModified = false;

            byte[] skin = iRegen.skin();
            UUID uuid = playerEntity.getUUID();

            boolean validSkin = iRegen.isSkinValidForUse();

            // Check if the player has a MOD skin and if the cache is present
            // if these conditions are true, we want to generate and cache the skin
            if (validSkin && !hasPlayerSkin(uuid) || iRegen.updateTicks() >= 140) {
                NativeImage skinImage = genSkinNative(skin);
                if (skinImage != null) {
                    addPlayerSkin(playerEntity.getUUID(), loadImage(skinImage));
                    hasBeenModified = true;
                }
            }

            //If the skin is invalid, we want to remove it and revert to Mojang
            if (!validSkin) {
                removePlayerSkin(playerEntity.getUUID());
                hasBeenModified = true;
            }

            //Update the skin if required.
            if (hasBeenModified || playerEntity.tickCount < 20) {
                ResourceLocation skinTexture = getSkinToUse(playerEntity);
                setPlayerSkin(playerEntity, skinTexture);
            }

            boolean isAlex = iRegen.isSkinValidForUse() ? iRegen.currentlyAlex() : getUnmodifiedSkinType(playerEntity);
            setPlayerSkinType(playerEntity, isAlex);
        });
    }

    public static boolean getUnmodifiedSkinType(AbstractClientPlayer abstractClientPlayerEntity) {
        if (ClientUtil.getPlayerInfo(abstractClientPlayerEntity) == null) return false;

        PlayerInfo info = ClientUtil.getPlayerInfo(abstractClientPlayerEntity);

        info.registerTextures();
        if (info.getModelName() == null) {
            return false;
        }
        if (info.getModelName().isEmpty()) {
            return false;
        }
        return info.getModelName().contentEquals("slim");
    }

    public static void setPlayerSkinType(AbstractClientPlayer player, boolean isAlex) {
        PlayerInfo playerInfo = ClientUtil.getPlayerInfo(player);
        if (playerInfo == null) return;
        playerInfo.skinModel = isAlex ? "slim" : "default";
    }

    public static void sendResetMessage() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            PlayerInfo info = ClientUtil.getPlayerInfo(player);
            info.registerTextures();
            boolean isAlex = info.getModelName().equals("slim");
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(new byte[0], isAlex));
        }
    }

    public static ResourceLocation loadImage(NativeImage nativeImage) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        return textureManager.register("player_", new DynamicTexture(nativeImage));
    }

    public static NativeImage genSkinNative(byte[] skinArray) {
        try {
            return NativeImage.read(new ByteArrayInputStream(skinArray));
        } catch (IOException e) {
            return null;
        }
    }

    //Set players skin
    public static void setPlayerSkin(AbstractClientPlayer player, ResourceLocation texture) {
        if (player.getSkinTextureLocation().equals(texture)) {
            return;
        }
        PlayerInfo playerInfo = ClientUtil.getPlayerInfo(player);
        if (playerInfo == null) return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.textureLocations;
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
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

    public static ResourceLocation getSkinToUse(AbstractClientPlayer playerEntity) {
        UUID uuid = playerEntity.getGameProfile().getId();
        if (PLAYER_SKINS.containsKey(uuid)) {
            return PLAYER_SKINS.get(uuid);
        }

        PlayerInfo info = ClientUtil.getPlayerInfo(playerEntity);

        if (info != null) {
            info.pendingTextures = false;
            info.registerTextures();
            return MoreObjects.firstNonNull(info.textureLocations.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(info.profile.getId()));
        }
        return playerEntity.getSkinTextureLocation();
    }

}
