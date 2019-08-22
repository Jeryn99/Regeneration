package me.swirtzly.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.image.ImageDownloadBuffer;
import me.swirtzly.regeneration.client.image.ImageDownloader;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.RegenType;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.UpdateSkinMessage;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class SkinManipulation {

	//	public static final File SKIN_DIRECTORY = new File(RegenConfig.CLIENT.skinDir.get() + "./Regeneration Data/skins/");
	public static final File SKIN_DIRECTORY = new File("./Regeneration Data/skins/");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	private static final Random RAND = new Random();

	public static String imageToPixelData(final BufferedImage img) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "PNG", Base64.getEncoder().wrap(os));
			return os.toString(StandardCharsets.ISO_8859_1.name());
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	public static NativeImage decodeToImage(final String base64String) {
		try {
			return NativeImage.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	/**
	 * Choosens a random png file from Steve/Alex Directory (This really depends on the Clients preference)
	 * It also checks image size of the select file, if it's too large, we'll just reset the player back to their Mojang skin,
	 * else they will be kicked from their server. If the player has disabled skin changing on the client, it will just send a reset packet
	 *
	 * @param random - This kinda explains itself, doesn't it?
	 * @param player - Player instance, used to check UUID to ensure it is the client player being involved in the scenario
	 * @throws IOException
	 */
	public static void sendSkinUpdate(Random random, PlayerEntity player) {
		if (Minecraft.getInstance().player.getUniqueID() != player.getUniqueID())
			return;
		RegenCap.get(player).ifPresent((data) -> {

		if (RegenConfig.CLIENT.changeMySkin.get()) {

			String pixelData = RegenUtil.NO_SKIN;
			File skin = null;

			if (data.getNextSkin().equals(RegenUtil.NO_SKIN)) {
				boolean isAlex = data.getPreferredModel().isAlex();
				skin = SkinManipulation.chooseRandomSkin(random, isAlex);
				RegenerationMod.LOG.info(skin + " was choosen");
				try {
					pixelData = SkinManipulation.imageToPixelData(ImageIO.read(skin));
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	private static File chooseRandomSkin(Random rand, boolean isAlex) {
		File skins = isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE;
		Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		if (folderFiles.isEmpty()) {
			folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		}
		return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
	}

	/**
	 * Creates a SkinInfo object for later use
	 *
	 * @param player - Player instance involved
	 * @param data   - The players regeneration capability instance
	 * @return SkinInfo - A class that contains the SkinType and the resource location to use as a skin
	 * @throws IOException
	 */
    private static SkinInfo getSkinInfo(AbstractClientPlayerEntity player, IRegen data) throws IOException {
		ResourceLocation resourceLocation;
		SkinInfo.SkinType skinType = null;

		if (data == null || player.getName() == null || player.getUniqueID() == null) {
			return new SkinInfo(null, getSkinType(player));
		}

		if (data.getEncodedSkin().equals(RegenUtil.NO_SKIN) || data.getEncodedSkin().equals(" ") || data.getEncodedSkin().equals("")) {
			resourceLocation = getMojangSkin(player);
			skinType = getSkinType(player);
		} else {
            NativeImage nativeImage = decodeToImage(data.getEncodedSkin());
			nativeImage = ImageDownloadBuffer.convert(nativeImage);
			if (nativeImage == null) {
				resourceLocation = DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			} else {
				DynamicTexture tex = new DynamicTexture(nativeImage);
				resourceLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(player.getName().getUnformattedComponentText().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
				skinType = data.getSkinType();
			}
		}
		return new SkinInfo(resourceLocation, skinType);
	}

	public static SkinInfo.SkinType getSkinType(AbstractClientPlayerEntity player) {
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = getVanillaMap(player);
		MinecraftProfileTexture profile = map.get(MinecraftProfileTexture.Type.SKIN);
		if (profile.getMetadata("model") == null) {
			return SkinInfo.SkinType.STEVE;
		}
		return SkinInfo.SkinType.ALEX;
	}

	/**
	 * This is used when the clients skin is reset
	 *
	 * @param player - Player to get the skin of themselves
	 * @return ResourceLocation from Mojang
	 */
	private static ResourceLocation getMojangSkin(AbstractClientPlayerEntity player) {
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = getVanillaMap(player);
		forceLoad(map);
		if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
			MinecraftProfileTexture profile = map.get(MinecraftProfileTexture.Type.SKIN);
			File dir = new File((File) ObfuscationReflectionHelper.getPrivateValue(SkinManager.class, Minecraft.getInstance().getSkinManager(), 2), profile.getHash().substring(0, 2));
			File file = new File(dir, profile.getHash());
			ResourceLocation location = new ResourceLocation("skins/" + profile.getHash());
			setPlayerSkin(player, location);
			return player.getLocationSkin();
		}
		return DefaultPlayerSkin.getDefaultSkinLegacy();
	}

	private static ITextureObject loadTexture(File file, ResourceLocation resource, ResourceLocation def, String par1Str) {
		TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
		ITextureObject object = texturemanager.getTexture(resource);
		if (object == null) {
			object = new ImageDownloader(file, par1Str, def, new ImageDownloadBuffer());
			texturemanager.loadTexture(resource, object);
		}
		return object;
	}

	public static Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getVanillaMap(AbstractClientPlayerEntity player) {
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(player.getGameProfile());
		if (map.isEmpty()) {
			map = Minecraft.getInstance().getSessionService().getTextures(Minecraft.getInstance().getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
		}
		return map;
	}


	/**
	 * Changes the ResourceLocation of a Players skin
	 *
	 * @param player  - Player instance involved
	 * @param texture - ResourceLocation of intended texture
	 */
	public static void setPlayerSkin(AbstractClientPlayerEntity player, ResourceLocation texture) {
		if (player.getLocationSkin() == texture) {
			return;
		}
        NetworkPlayerInfo playerInfo = player.playerInfo;
		if (playerInfo == null)
			return;
        Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = playerInfo.playerTextures;
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
        if (texture == null) {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
        }
	}

	public static void setPlayerSkinType(AbstractClientPlayerEntity player, SkinInfo.SkinType skinType) {
		if (skinType.getMojangType().equals(player.getSkinType())) return;
        NetworkPlayerInfo playerInfo = player.playerInfo;
		ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, skinType.getMojangType(), 5);
	}

	/**
	 * Subscription to RenderPlayerEvent.Pre to set players model and texture from hashmap
	 *
	 * @param e - RenderPlayer Pre Event
	 */
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre e) {
		AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) e.getEntityPlayer();

        RegenCap.get(player).ifPresent((cap) -> {
			if (player.ticksExisted == 20) {
				SkinInfo oldSkinInfo = PLAYER_SKINS.get(player.getUniqueID());
				if (oldSkinInfo != null) {
					//oldSkinInfo.dispose();
				}
				PLAYER_SKINS.remove(player.getUniqueID());
			}

			if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {

				if (cap.getAnimationTicks() > 100) {
                    setSkinFromData(player, RegenCap.get(player));
				}

				TypeManager.getTypeInstance(cap.getType()).getRenderer().onRenderRegeneratingPlayerPre(TypeManager.getTypeInstance(cap.getType()), e, cap);


			} else if (!PLAYER_SKINS.containsKey(player.getUniqueID())) {
                setSkinFromData(player, RegenCap.get(player));
			} else {
				SkinInfo skin = PLAYER_SKINS.get(player.getUniqueID());

				if (skin == null) {
					return;
				}
				if (skin.getSkinTextureLocation() != null) {
					setPlayerSkin(player, skin.getSkinTextureLocation());
				}

				if (skin.getSkintype() != null) {
					setPlayerSkinType(player, skin.getSkintype());
				}
			}
		});
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Post e) {
		AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) e.getEntityPlayer();
        RegenCap.get(player).ifPresent((cap) -> {
			if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
				RegenType type = TypeManager.getTypeInstance(cap.getType());
				type.getRenderer().onRenderRegeneratingPlayerPost(TypeManager.getTypeInstance(cap.getType()), e, cap);
			}
		});
	}

	//Experiment
	public static ResourceLocation forceLoad(Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map) {
		if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
			return Minecraft.getInstance().getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, null);
		}
		return null;
	}

	public static ResourceLocation createGuiTexture(File file) {
		NativeImage nativeImage = null;
		try {
			nativeImage = NativeImage.read(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("gui_skin_" + System.currentTimeMillis(), new DynamicTexture(nativeImage));
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

	public enum EnumChoices implements RegenUtil.IEnum {
		ALEX(true), STEVE(false), EITHER(true);

		private boolean isAlex;

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

	/**
	 * Called by onRenderPlayer, sets model, sets texture, adds player and SkinInfo to map
	 *
	 * @param player - Player instance
	 * @param cap    - Players Regen capability instance
	 */
	private void setSkinFromData(AbstractClientPlayerEntity player, LazyOptional<IRegen> cap) {
		cap.ifPresent((data) -> {
			SkinInfo skinInfo = null;
			try {
				skinInfo = SkinManipulation.getSkinInfo(player, data);
			} catch (IOException e1) {
				if (!data.getEncodedSkin().equals(RegenUtil.NO_SKIN)) {
					RegenerationMod.LOG.error("Error creating skin for: " + player.getName().getUnformattedComponentText() + " " + e1.getMessage());
				}
			}
			if (skinInfo != null) {
				SkinManipulation.setPlayerSkin(player, skinInfo.getSkinTextureLocation());
				SkinManipulation.setPlayerSkinType(player, skinInfo.getSkintype());
				PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
			}
		});
	}


}