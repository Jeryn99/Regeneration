package me.swirtzly.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.image.ImageDownloadBuffer;
import me.swirtzly.regeneration.client.image.ImageDownloader;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.RegenType;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.network.UpdateSkinMessage;
import me.swirtzly.regeneration.network.NetworkDispatcher;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class SkinChangingHandler {

	//	public static final File SKIN_DIRECTORY = new File(RegenConfig.CLIENT.skinDir.get() + "./Regeneration Data/skins/");
	public static final File SKIN_DIRECTORY = new File("./Regeneration Data/skins/");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	public static final Logger SKIN_LOG = LogManager.getLogger(SkinChangingHandler.class);
	public static final Map<UUID, SkinInfo.SkinType> TYPE_BACKUPS = new HashMap<>();
	private static final Random RAND = new Random();

	private static String imageToPixelData(File file) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		return Base64.getEncoder().encodeToString(fileContent);
	}

	/**
	 * Converts a array of Bytes into a Buffered Image and caches to the cache directory
	 * with the file name of "cache-%PLAYERUUID%.png"
	 *
	 * @param player    - Player to be involved
	 * @param imageData - Pixel data to be converted to a Buffered Image
	 * @return Buffered image that will later be converted to a Dynamic texture
	 */
	private static NativeImage toImage(PlayerEntity player, String imageData, boolean write) throws IOException {
		NativeImage image = decodeToImage(imageData);
		if (image != null && write) {
			//TODO write
		}
		return image;
	}

	public static NativeImage decodeToImage(String imageString) throws IOException {
		try (MemoryStack memorystack = MemoryStack.stackPush()) {
			ByteBuffer bytebuffer = memorystack.UTF8(imageString, false);
			ByteBuffer decoded = Base64.getDecoder().decode(bytebuffer);
			ByteBuffer toImage = memorystack.malloc(decoded.remaining());
			toImage.put(decoded);
			toImage.rewind();
			return NativeImage.read(toImage);
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
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (RegenConfig.CLIENT.changeMySkin.get()) {
				boolean isAlex = cap.getPreferredModel().isAlex();

				File skin = SkinChangingHandler.chooseRandomSkin(random, isAlex);
				RegenerationMod.LOG.info(skin + " was choosen");
				String pixelData = "none";
				try {
					pixelData = SkinChangingHandler.imageToPixelData(skin);
				} catch (IOException e) {
					e.printStackTrace();
				}
				cap.setEncodedSkin(pixelData);
				NetworkDispatcher.sendToServer(new UpdateSkinMessage(pixelData, isAlex));
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
	private static SkinInfo getSkinInfo(AbstractClientPlayerEntity player, IRegeneration data, boolean write) throws
			IOException {
		ResourceLocation resourceLocation;
		SkinInfo.SkinType skinType = null;

		if (data.getEncodedSkin().equals("none") || data.getEncodedSkin().equals(" ") || data.getEncodedSkin().equals("")) {
			resourceLocation = getMojangSkin(player);
			if (wasAlex(player)) {
				skinType = SkinInfo.SkinType.ALEX;
			} else {
				skinType = SkinInfo.SkinType.STEVE;
			}
		} else {
			NativeImage bufferedImage = toImage(player, data.getEncodedSkin(), write);
			bufferedImage = ImageDownloadBuffer.convert(bufferedImage);
			if (bufferedImage == null) {
				resourceLocation = DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			} else {
				DynamicTexture tex = new DynamicTexture(bufferedImage);
				resourceLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(player.getName().getUnformattedComponentText().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
				skinType = data.getSkinType();
			}
		}
		return new SkinInfo(resourceLocation, skinType);
	}

	public static boolean wasAlex(PlayerEntity player) {
		if (TYPE_BACKUPS.containsKey(player.getUniqueID())) {
			return TYPE_BACKUPS.get(player.getUniqueID()).getMojangType().equals("slim");
		}
		return true;
	}

	/**
	 * This is used when the clients skin is reset
	 *
	 * @param player - Player to get the skin of themselves
	 * @return ResourceLocation from Mojang
	 */
	private static ResourceLocation getMojangSkin(AbstractClientPlayerEntity player) {
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(player.getGameProfile());
		if (map.isEmpty()) {
			map = Minecraft.getInstance().getSessionService().getTextures(Minecraft.getInstance().getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
		}
		if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
			MinecraftProfileTexture profile = map.get(MinecraftProfileTexture.Type.SKIN);
			File dir = new File((File) ObfuscationReflectionHelper.getPrivateValue(SkinManager.class, Minecraft.getInstance().getSkinManager(), 2), profile.getHash().substring(0, 2));
			File file = new File(dir, profile.getHash());
			if (file.exists())
				file.delete();
			ResourceLocation location = new ResourceLocation("skins/" + profile.getHash());
			loadTexture(file, location, DefaultPlayerSkin.getDefaultSkinLegacy(), profile.getUrl());
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
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayerEntity.class, player, 0);
		if (playerInfo == null)
			return;
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		if (texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
	}

	public static void setPlayerSkinType(AbstractClientPlayerEntity player, SkinInfo.SkinType skinType) {
		if (skinType.getMojangType().equals(player.getSkinType())) return;
		if (!TYPE_BACKUPS.containsKey(player.getUniqueID())) {
			TYPE_BACKUPS.put(player.getUniqueID(), player.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
		}
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayerEntity.class, player, 0);
		ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, skinType.getMojangType(), 5);
	}

	public static void addType(AbstractClientPlayerEntity player) {
		if (player == null || TYPE_BACKUPS.containsKey(player.getUniqueID())) return;
		TYPE_BACKUPS.put(player.getUniqueID(), player.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
	}

	/**
	 * Subscription to RenderPlayerEvent.Pre to set players model and texture from hashmap
	 *
	 * @param e - RenderPlayer Pre Event
	 */
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre e) {
		AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) e.getEntityPlayer();

		addType(player);

		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (player.ticksExisted == 20) {
				SkinInfo oldSkinInfo = PLAYER_SKINS.get(player.getUniqueID());
				if (oldSkinInfo != null) {
					//oldSkinInfo.dispose();
				}
				PLAYER_SKINS.remove(player.getUniqueID());
			}

			if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {

				if (cap.getAnimationTicks() > 0.7) {
					setSkinFromData(player, CapabilityRegeneration.getForPlayer(player), false);
				}

				TypeManager.getTypeInstance(cap.getType()).getRenderer().onRenderRegeneratingPlayerPre(TypeManager.getTypeInstance(cap.getType()), e, cap);


			} else if (!PLAYER_SKINS.containsKey(player.getUniqueID())) {
				setSkinFromData(player, CapabilityRegeneration.getForPlayer(player), true);
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
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
				RegenType type = TypeManager.getTypeInstance(cap.getType());
				type.getRenderer().onRenderRegeneratingPlayerPost(TypeManager.getTypeInstance(cap.getType()), e, cap);
			}
		});
	}

	/**
	 * Called by onRenderPlayer, sets model, sets texture, adds player and SkinInfo to map
	 *
	 * @param player - Player instance
	 * @param cap    - Players Regen capability instance
	 */
	private void setSkinFromData(AbstractClientPlayerEntity player, LazyOptional<IRegeneration> cap, boolean write) {
		cap.ifPresent((data) -> {
			SkinInfo skinInfo = null;
			try {
				skinInfo = SkinChangingHandler.getSkinInfo(player, data, write);
			} catch (IOException e1) {
				RegenerationMod.LOG.error("Error creating skin for: " + player.getName().getUnformattedComponentText() + " " + e1.getMessage());
			}
			if (skinInfo != null) {
				SkinChangingHandler.setPlayerSkin(player, skinInfo.getSkinTextureLocation());
				SkinChangingHandler.setPlayerSkinType(player, skinInfo.getSkintype());
				PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
			}

		});
	}

	public enum EnumChoices implements RegenUtil.IEnum {
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