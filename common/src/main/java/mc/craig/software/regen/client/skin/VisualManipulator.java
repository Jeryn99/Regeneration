package mc.craig.software.regen.client.skin;


import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.network.messages.SkinMessage;
import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.TextureFixer;
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

public class VisualManipulator {


    //Skin Storage
    public static final HashMap<UUID, ResourceLocation> PLAYER_SKINS = new HashMap<>();
    public static final HashMap<UUID, Boolean> MOJANG_BACKUP = new HashMap<>();

    public static void tick(AbstractClientPlayer playerEntity) {
        RegenerationData.get(playerEntity).ifPresent(iRegen -> {

            byte[] skin = iRegen.skin();
            UUID uuid = playerEntity.getUUID();

            boolean validSkin = iRegen.isSkinValidForUse();
            if(!validSkin){
                PLAYER_SKINS.remove(uuid);
                setPlayerSkinType(playerEntity, mojangIsAlex(playerEntity));
                return;
            }

            // Only time a skin update should occur is if the player does not have a skin cached or the player is mid-regeneration
            boolean isHalfWay = iRegen.updateTicks() >= (iRegen.transitionType().getAnimationLength() / 2);
            if (iRegen.regenState() == RegenStates.REGENERATING && isHalfWay || iRegen.regenState() != RegenStates.REGENERATING && !hasPlayerSkin(uuid)) {
                NativeImage skinImage = genSkinNative(skin);
                if (skinImage != null) {
                    boolean isAlex = iRegen.currentlyAlex();
                    setPlayerSkinType(playerEntity, isAlex);
                    addPlayerSkin(playerEntity.getUUID(), loadImage(skinImage));
                }
            }
        });
    }

    public static boolean mojangIsAlex(AbstractClientPlayer abstractClientPlayerEntity) {

        if(MOJANG_BACKUP.containsKey(abstractClientPlayerEntity.getUUID())){
            return MOJANG_BACKUP.get(abstractClientPlayerEntity.getUUID());
        }

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

        if(!MOJANG_BACKUP.containsKey(player.getUUID())) {
            boolean skinType = (playerInfo.getModelName() == null || playerInfo.getModelName().isEmpty());
            MOJANG_BACKUP.put(player.getUUID(), !skinType);
        }

        playerInfo.skinModel = isAlex ? "slim" : "default";
    }

    public static void sendResetMessage() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            boolean info = VisualManipulator.mojangIsAlex(player);
            new SkinMessage(new byte[0], info).send();
        }
    }

    public static ResourceLocation loadImage(NativeImage nativeImage) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        return textureManager.register("player_", new DynamicTexture(nativeImage));
    }

    public static NativeImage genSkinNative(byte[] skinArray) {
        try {
            return TextureFixer.processLegacySkin(NativeImage.read(new ByteArrayInputStream(skinArray)), "@");
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

}
