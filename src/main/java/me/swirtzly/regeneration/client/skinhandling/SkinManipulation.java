package me.swirtzly.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.image.ImageDownloadBuffer;
import me.swirtzly.regeneration.client.rendering.types.ATypeRenderer;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.skin.HandleSkins;
import me.swirtzly.regeneration.common.types.RegenType;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.UpdateSkinMessage;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.client.TexUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static me.swirtzly.regeneration.util.common.RegenUtil.NO_SKIN;

@OnlyIn(Dist.CLIENT)
public class SkinManipulation {

	public static final File SKIN_DIRECTORY = new File(RegenConfig.CLIENT.skinDir.get() + "/Regeneration Data/skins/");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	public static final HashMap<UUID, ResourceLocation> MOJANG = new HashMap<>();
	private static final Random RAND = new Random();


	public static NativeImage decodeToImage(String base64String) {
		if (base64String.equalsIgnoreCase(NO_SKIN)) {
			return null;
		}
		try {
			return NativeImage.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String.replaceAll("-", ""))));
		} catch (final IOException ioe) {
			Regeneration.LOG.error("ERROR MAKING IMAGE FOR: " + base64String);
			throw new UncheckedIOException(ioe);
		}
	}


	private static SkinInfo getSkinInfo(AbstractClientPlayerEntity player, IRegen data) {
		ResourceLocation resourceLocation;
		SkinInfo.SkinType skinType = SkinInfo.SkinType.ALEX;
		if (data == null) {
			return new SkinInfo(player, null, getSkinType(player, true));
		}
		if (data.getEncodedSkin().equals(NO_SKIN)) {
			resourceLocation = MOJANG.get(player.getUniqueID());
			skinType = getSkinType(player, true);
		} else {
			NativeImage nativeImage = decodeToImage(data.getEncodedSkin());
			nativeImage = ImageDownloadBuffer.convert(nativeImage);
			if (nativeImage == null) {
				resourceLocation = MOJANG.get(player.getUniqueID());
			} else {
				DynamicTexture tex = new DynamicTexture(nativeImage);
				resourceLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(player.getName().getUnformattedComponentText().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
				skinType = data.getSkinType();
			}
		}
		return new SkinInfo(player, resourceLocation, skinType);
	}


	public static SkinInfo.SkinType getSkinType(PlayerEntity player, boolean forceMojang) {
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(player.getGameProfile());
		if (map.isEmpty()) {
			map = Minecraft.getInstance().getSessionService().getTextures(Minecraft.getInstance().getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
		}
		MinecraftProfileTexture profile = map.get(MinecraftProfileTexture.Type.SKIN);
		AtomicReference<SkinInfo.SkinType> skinType = new AtomicReference<>();
		skinType.set(SkinInfo.SkinType.ALEX);
		RegenCap.get(player).ifPresent((data) -> {
			if (data.getEncodedSkin().toLowerCase().equals(NO_SKIN) || forceMojang) {
				if (profile == null) {
					skinType.set(SkinInfo.SkinType.STEVE);
				}
				if (profile != null && profile.getMetadata("model") == null) {
					skinType.set(SkinInfo.SkinType.STEVE);
				}
			} else {
				skinType.set(data.getSkinType());
			}
		});

		return skinType.get();
	}

	public static void setPlayerSkin(AbstractClientPlayerEntity player, ResourceLocation texture) {
		if (player.getLocationSkin() == texture) {
			return;
		}
		NetworkPlayerInfo playerInfo = player.playerInfo;
		if (playerInfo == null) return;
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.playerTextures;
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		if (texture == null) {
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
		}
	}


	public static void setPlayerSkinType(AbstractClientPlayerEntity player, SkinInfo.SkinType skinType) {
		if (skinType.getMojangType().equals(player.getSkinType())) return;
		NetworkPlayerInfo playerInfo = player.playerInfo;
		if (playerInfo == null) return;
		ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, skinType.getMojangType(), 5);
	}


	public static List<File> listAllSkins(EnumChoices choices) {
		List<File> resultList = new ArrayList<>();
		File directory = null;

		switch (choices) {
			case EITHER:
				directory = SKIN_DIRECTORY;
				break;
			case ALEX:
				directory = SKIN_DIRECTORY_ALEX;
				break;
			case STEVE:
				directory = SKIN_DIRECTORY_STEVE;
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
		if (Minecraft.getInstance().player.getUniqueID() != player.getUniqueID()) return;
		RegenCap.get(player).ifPresent((data) -> {

			if (RegenConfig.CLIENT.changeMySkin.get()) {

				String pixelData = NO_SKIN;
				File skin = null;

				if (data.getNextSkin().equals(NO_SKIN)) {
					boolean isAlex = data.getPreferredModel().isAlex();
					skin = HandleSkins.chooseRandomSkin(random, isAlex);
					Regeneration.LOG.info(skin + " was selected");
					pixelData = HandleSkins.imageToPixelData(skin);
					data.setEncodedSkin(pixelData);
					NetworkDispatcher.sendToServer(new UpdateSkinMessage(pixelData, isAlex));
				} else {
					pixelData = data.getNextSkin();
					data.setEncodedSkin(pixelData);
					NetworkDispatcher.sendToServer(new UpdateSkinMessage(pixelData, data.getNextSkinType().getMojangType().equals("slim")));
				}
			} else {
				ClientUtil.sendSkinResetPacket();
			}
		});
	}

	public static void createSkin(UUID uuid) {
		try {
			MOJANG.put(uuid, TexUtil.urlToTexture(new URL("https://crafatar.com/skins/" + uuid.toString())));
		} catch (MalformedURLException e) {
			MOJANG.put(uuid, DefaultPlayerSkin.getDefaultSkin(uuid));
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre renderPlayerEvent) {
		AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) renderPlayerEvent.getPlayer();

		RegenCap.get(player).ifPresent((cap) -> {

			/* When the player is in a Post Regenerative state and above a 3x3 grid of Zero Rounde Blocks,
			 *  We want them to float up and down slightly*/
			if (cap.getState() == PlayerUtil.RegenState.POST && PlayerUtil.isAboveZeroGrid(player)) {
				float floatingOffset = MathHelper.cos(player.ticksExisted * 0.1F) * -0.09F + 0.5F;
				GlStateManager.translated(0, floatingOffset, 0);
			}

			/* Sometimes when the player is teleported, the Mojang skin becomes re-downloaded and resets to either Steve,
			 or the Mojang Skin, so once they have been re-created, we remove the cache we have on them, causing it to be renewed */
			if (player.ticksExisted < 20) {
				PLAYER_SKINS.remove(player.getUniqueID());
			}

			/* Grab the SkinInfo of a player and set their SkinType and Skin location from it */
			if (cap.getState() != PlayerUtil.RegenState.REGENERATING) {
				SkinInfo skin = PLAYER_SKINS.get(player.getUniqueID());
				if (skin != null) {
					setPlayerSkin(player, skin.getTextureLocation());
					setPlayerSkinType(player, skin.getSkintype());
				} else {
					createSkinData(player, RegenCap.get(player));
                }
            }

            /* 	When the player regenerates, we want the skin to change midway through Regeneration
             *	We only do this midway through, we will destroy the data and re-create it */
            boolean isMidRegeneration = cap.getState() == PlayerUtil.RegenState.REGENERATING && cap.getAnimationTicks() >= 100;
            if (isMidRegeneration || player.ticksExisted < 10) {
                createSkinData(player, RegenCap.get(player));
            }

            /* Render the living entities Pre-Regeneration effect */
            if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
                RegenType typeInstance = cap.getType().create();
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
				RegenType type = cap.getType().create();
				ATypeRenderer typeRenderer = type.getRenderer();
                typeRenderer.onRenderPost(type, renderPlayerEventPost, cap);
			}
		});
	}

	private void createSkinData(AbstractClientPlayerEntity player, LazyOptional<IRegen> cap) {
		cap.ifPresent((data) -> {
			SkinInfo skinInfo = SkinManipulation.getSkinInfo(player, data);

			/* Set player skin and SkinType and cache it so we don't keep re-making it */
			SkinManipulation.setPlayerSkin(player, skinInfo.getTextureLocation());
			SkinManipulation.setPlayerSkinType(player, skinInfo.getSkintype());
			PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
		});
	}

	public enum EnumChoices implements RegenUtil.IEnum {
		ALEX(true), STEVE(false), EITHER(true);

		private final boolean isAlex;

		EnumChoices(boolean isAlex) {
			this.isAlex = isAlex;
		}

		public boolean isAlex() {
			if (this == EITHER) {
				return RAND.nextBoolean();
			}
			return isAlex;
		}
	}

}
