package craig.software.mc.regen.client.skin;


import com.mojang.blaze3d.platform.NativeImage;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.network.NetworkDispatcher;
import craig.software.mc.regen.network.messages.SkinMessage;
import craig.software.mc.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SkinHandler {


    //Skin Storage
    public static final HashMap<UUID, ResourceLocation> PLAYER_SKINS = new HashMap<>();

    public static void tick(AbstractClientPlayer playerEntity) {


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

        return playerEntity.getSkinTextureLocation();
    }

}
