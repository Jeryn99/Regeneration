package me.suff.mc.regen.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.rendering.types.ATypeRenderer;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.item.DyeableClothingItem;
import me.suff.mc.regen.common.skin.HandleSkins;
import me.suff.mc.regen.common.types.RegenType;
import me.suff.mc.regen.network.messages.UpdateSkinMessage;
import me.suff.mc.regen.client.image.ImageDownloadBuffer;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.util.client.ClientUtil;
import me.suff.mc.regen.util.client.TexUtil;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static me.suff.mc.regen.util.common.RegenUtil.NO_SKIN;

@OnlyIn(Dist.CLIENT)
public class SkinManipulation {

    public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();

    public static NativeImage decodeToImage(String base64String) {
        if (base64String.equalsIgnoreCase(NO_SKIN)) {
            return null;
        }
        try {
            return NativeImage.read(new ByteArrayInputStream(Base64.getMimeDecoder().decode(base64String.replaceAll("-", ""))));
        } catch (IOException ioe) {
            Regeneration.LOG.error("ERROR MAKING IMAGE FOR: " + base64String);
            throw new UncheckedIOException(ioe);
        }
    }


    private static SkinInfo getSkinInfo(AbstractClientPlayerEntity player, IRegen data) {
        ResourceLocation resourceLocation;
        SkinInfo.SkinType skinType = SkinInfo.SkinType.ALEX;
        if (data == null) {
            return new SkinInfo(player, null, getSkinType(data));
        }

        String skin = data.getEncodedSkin();


        if (NO_SKIN.equalsIgnoreCase(skin)) {
            skin = TexUtil.getEncodedMojangSkin(player);
        }

        if (skin.equals("CRASH ME")) {
            resourceLocation = TexUtil.getSkinFromGameProfile(player.getGameProfile());
        } else {
            NativeImage nativeImage = decodeToImage(skin);
            if (nativeImage == null) {
                return new SkinInfo(player, null, getSkinType(data));
            }

            nativeImage = ImageDownloadBuffer.convert(nativeImage);
            DynamicTexture tex = new DynamicTexture(nativeImage);
            resourceLocation = Minecraft.getInstance().getTextureManager().register(player.getName().getContents().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
        }
        skinType = getSkinType(data);
        return new SkinInfo(player, resourceLocation, skinType);
    }

    public static SkinInfo.SkinType getSkinType(IRegen cap) {
        LivingEntity living = cap.getLivingEntity();
        if (living instanceof AbstractClientPlayerEntity) {
            if (cap.getEncodedSkin().equalsIgnoreCase(NO_SKIN)) {
                AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) living;
                playerEntity.playerInfo.pendingTextures = false;
                boolean isSlim = playerEntity.playerInfo.getModelName().equalsIgnoreCase("slim");
                return isSlim ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE;
            } else {
                return cap.getSkinType();
            }
        }
        return SkinInfo.SkinType.STEVE;
    }

    public static void setPlayerSkin(AbstractClientPlayerEntity player, ResourceLocation texture) {
        if (player.getSkinTextureLocation() == texture) {
            return;
        }
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.textureLocations;
        playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null) {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
        }
    }


    public static void setPlayerSkinType(AbstractClientPlayerEntity player, SkinInfo.SkinType skinType) {
        if (skinType.getMojangType().equals(player.getModelName())) return;
        NetworkPlayerInfo playerInfo = player.playerInfo;
        if (playerInfo == null) return;
        ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, skinType.getMojangType(), 5);
    }


    public static List<File> listAllSkins(PlayerUtil.EnumChoices choices) {
        List<File> resultList = new ArrayList<>();
        File directory = null;

        switch (choices) {
            case EITHER:
                directory = HandleSkins.SKIN_DIRECTORY;
                break;
            case ALEX:
                directory = HandleSkins.SKIN_DIRECTORY_ALEX;
                break;
            case STEVE:
                directory = HandleSkins.SKIN_DIRECTORY_STEVE;
                break;
        }
        try {
            Files.find(Paths.get(directory.toString()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).forEach((file) -> resultList.add(file.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static void sendSkinUpdate(Random random, PlayerEntity player) {
        if (Minecraft.getInstance().player.getUUID() != player.getUUID()) return;
        RegenCap.get(player).ifPresent((data) -> {

            if (RegenConfig.CLIENT.changeMySkin.get()) {

                String pixelData = NO_SKIN;
                File skin = null;

                if (data.getNextSkin().equalsIgnoreCase(NO_SKIN)) {
                    boolean isAlex = data.getPreferredModel().isAlex();
                    skin = HandleSkins.chooseRandomSkin(random, isAlex);
                    Regeneration.LOG.info(skin + " was selected");
                    pixelData = HandleSkins.imageToPixelData(skin);
                    NetworkDispatcher.sendToServer(new UpdateSkinMessage(pixelData, isAlex));
                } else {
                    pixelData = data.getNextSkin();
                    NetworkDispatcher.sendToServer(new UpdateSkinMessage(pixelData, data.getNextSkinType().getMojangType().equals("slim")));
                }
            } else {
                ClientUtil.sendSkinResetPacket();
            }
        });
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre renderPlayerEvent) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) renderPlayerEvent.getPlayer();

        PlayerModel<AbstractClientPlayerEntity> model = renderPlayerEvent.getRenderer().getModel();

        boolean isWearingChest = player.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegenObjects.Items.GUARD_CHEST.get() || player.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegenObjects.Items.ROBES_CHEST.get();

        boolean isWearingLeggings = player.getItemBySlot(EquipmentSlotType.LEGS).getItem() == RegenObjects.Items.GUARD_LEGGINGS.get();
        model.rightPants.neverRender = isWearingLeggings;
        model.leftPants.neverRender = isWearingLeggings;

        RegenCap.get(player).ifPresent((cap) -> {
            /* When the player is in a Post Regenerative state and above a 3x3 grid of Zero Rounde Blocks,
             *  We want them to float up and down slightly*/
            if (cap.getState() == PlayerUtil.RegenState.POST && PlayerUtil.isAboveZeroGrid(player)) {
                float floatingOffset = MathHelper.cos(player.tickCount * 0.1F) * -0.09F + 0.5F;
                GlStateManager.translated(0, floatingOffset, 0);
                GlStateManager.translated(0, 0, -1);
            }


            //Fixes First person arm
            if (Minecraft.getInstance().player.getUUID() == player.getUUID()) {
                if (Minecraft.getInstance().options.thirdPersonView == 0 && isWearingChest) {
                    model.leftArm.neverRender = player.getUUID() != Minecraft.getInstance().player.getUUID();
                    model.rightArm.neverRender = player.getUUID() != Minecraft.getInstance().player.getUUID();
                }
            }

			/* Sometimes when the player is teleported, the Mojang skin becomes re-downloaded and resets to either Steve,
			 or the Mojang Skin, so once they have been re-created, we remove the cache we have on them, causing it to be renewed */
            if (player.tickCount < 20) {
                PLAYER_SKINS.remove(player.getUUID());
            }

            /* Grab the SkinInfo of a player and set their SkinType and Skin location from it */
            if (cap.getState() != PlayerUtil.RegenState.REGENERATING) {
                SkinInfo skin = PLAYER_SKINS.get(player.getUUID());
                if (skin != null) {
                    boolean swift = player.getItemBySlot(EquipmentSlotType.CHEST).getOrCreateTag().contains(DyeableClothingItem.SWIFT_KEY);
                    boolean forceAlex = !swift && isWearingChest;
                    setPlayerSkin(player, skin.getTextureLocation());
                    setPlayerSkinType(player, forceAlex ? SkinInfo.SkinType.ALEX : skin.getSkintype());
                } else {
                    createSkinData(player, RegenCap.get(player));
                }
            }

            /* 	When the player regenerates, we want the skin to change midway through Regeneration
             *	We only do this midway through, we will destroy the data and re-create it */
            Minecraft.getInstance().submitAsync(() -> {
                boolean isMidRegeneration = cap.getState() == PlayerUtil.RegenState.REGENERATING && cap.getAnimationTicks() >= 100;
                if (isMidRegeneration || player.tickCount < 10) {
                    createSkinData(player, RegenCap.get(player));
                }
            });


            /* Render the living entities Pre-Regeneration effect */
            if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
                RegenType typeInstance = cap.getRegenType().create();
                ATypeRenderer typeRenderer = typeInstance.getRenderer();
                typeRenderer.onRenderPre(typeInstance, renderPlayerEvent, cap);
            }
        });
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post renderPlayerEventPost) {

        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) renderPlayerEventPost.getPlayer();

        RegenCap.get(player).ifPresent((cap) -> {
            if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
                RegenType type = cap.getRegenType().create();
                ATypeRenderer typeRenderer = type.getRenderer();
                typeRenderer.onRenderPost(type, renderPlayerEventPost, cap);
            }

            if (cap.getState() == PlayerUtil.RegenState.POST && PlayerUtil.isAboveZeroGrid(player)) {
                GlStateManager.popMatrix();
            }
        });
    }

    private void createSkinData(AbstractClientPlayerEntity player, LazyOptional<IRegen> cap) {
        cap.ifPresent((data) -> {
            Minecraft.getInstance().submitAsync(() -> {
                SkinInfo skinInfo = SkinManipulation.getSkinInfo(player, data);
                /* Set player skin and SkinType and cache it so we don't keep re-making it */
                SkinManipulation.setPlayerSkin(player, skinInfo.getTextureLocation());
                SkinManipulation.setPlayerSkinType(player, skinInfo.getSkintype());
                PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
            });
        });
    }

}
