package me.swirtzly.regen.client.skin;


import com.google.common.base.MoreObjects;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.network.Dispatcher;
import me.swirtzly.regen.network.messages.SkinMessage;
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
    private static final HashMap<UUID, ResourceLocation> PLAYER_SKINS = new HashMap<>();

    public static void tick(AbstractClientPlayerEntity playerEntity) {
        RegenCap.get(playerEntity).ifPresent(iRegen -> {
            boolean forceUpdate = false;

            byte[] skin = iRegen.getSkin();
            if (iRegen.isSkinValidForUse()) {
                NativeImage skinImage = genSkinNative(skin);
                addPlayerSkin(playerEntity.getUniqueID(), loadImage(skinImage));
            } else {
                removePlayerSkin(playerEntity.getUniqueID());
                forceUpdate = true;
            }

            boolean shouldUpdate = forceUpdate || playerEntity.ticksExisted < 20 || iRegen.getTicksAnimating() >= 100;

            if (shouldUpdate) {
                ResourceLocation skinTexture = getSkinToUse(playerEntity);
                setPlayerSkin(playerEntity, skinTexture);
            }
        });
    }

    public static void sendResetMessage() {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        Dispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(player, new byte[0]));
    }

    public static ResourceLocation loadImage(NativeImage nativeImage) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        return textureManager.getDynamicTextureLocation("player_", new DynamicTexture(nativeImage));
    }

    public static NativeImage genSkinNative(byte[] skinArray) {
        try {
            return NativeImage.read(new ByteArrayInputStream(skinArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        return MoreObjects.firstNonNull(info.playerTextures.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(info.gameProfile.getId()));
    }


    //File


}
