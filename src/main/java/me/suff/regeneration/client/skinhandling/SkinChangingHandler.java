package me.suff.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.network.MessageUpdateSkin;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.FileUtil;
import me.suff.regeneration.util.IEnum;
import me.suff.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {
	
	public static final File SKIN_DIRECTORY = new File(RegenConfig.skins.skinDir + "/regeneration/skins/");
	public static final File SKIN_CACHE_DIRECTORY = new File(RegenConfig.skins.skinDir + "/regeneration/skincache/" + Minecraft.getMinecraft().getSession().getProfile().getId() + "/skins");
	public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	public static final Logger SKIN_LOG = LogManager.getLogger("Regeneration Skin Handler");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	public static final Map<UUID, SkinInfo.SkinType> TYPE_BACKUPS = new HashMap<>();
	private static final Random RAND = new Random();
	
	/**
	 * Converts a buffered image to Pixel data
	 *
	 * @param bufferedImage - Buffered image to be converted to Pixel data
	 */
	private static byte[] imageToPixelData(BufferedImage bufferedImage) {
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		return buffer.getData();
	}
	
	/**
	 * Converts a array of Bytes into a Buffered Image and caches to the cache directory
	 * with the file name of "cache-%PLAYERUUID%.png"
	 *
	 * @param player    - Player to be invovled
	 * @param imageData - Pixel data to be converted to a Buffered Image
	 * @return Buffered image that will later be converted to a Dynamic texture
	 */
	private static BufferedImage toImage(EntityPlayer player, byte[] imageData) throws IOException {
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);
		img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(imageData, imageData.length), new Point()));
		File file = new File(SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png");
		
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		
		ImageIO.write(img, "png", file);
		
		return img;
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
	public static void sendSkinUpdate(Random random, EntityPlayer player) throws IOException {
		if (Minecraft.getMinecraft().player.getUniqueID() != player.getUniqueID())
			return;
		
		if (RegenConfig.skins.changeMySkin) {
			boolean isAlex = CapabilityRegeneration.getForPlayer(player).getPreferredModel().isAlex();
			File skin = SkinChangingHandler.getRandomSkin(random, isAlex);
			RegenerationMod.LOG.info(skin.getName() + " was choosen");
			BufferedImage image = ImageIO.read(skin);
			byte[] pixelData = SkinChangingHandler.imageToPixelData(image);
			CapabilityRegeneration.getForPlayer(player).setEncodedSkin(pixelData);
			if (pixelData.length >= 32767) {
				ClientUtil.sendSkinResetPacket();
				RegenerationMod.LOG.error("CLIENT TRIED TO SEND IMAGE THAT EXCEEDS PERMITTED REQUIREMENTS");
			} else {
				NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData, isAlex));
			}
		} else {
			ClientUtil.sendSkinResetPacket();
		}
	}
	
	private static File getRandomSkin(Random rand, boolean isAlex) throws IOException {
		File skins;
		if (isAlex) {
			skins = SKIN_DIRECTORY_ALEX;
		} else {
			skins = SKIN_DIRECTORY_STEVE;
		}
		Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		if (folderFiles.isEmpty()) {
			FileUtil.doDownloadsOnThread();
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
	private static SkinInfo getSkinInfo(AbstractClientPlayer player, IRegeneration data) throws IOException {
		byte[] encodedSkin = CapabilityRegeneration.getForPlayer(player).getEncodedSkin();
		ResourceLocation resourceLocation;
		SkinInfo.SkinType skinType = null;
		
		if (Arrays.equals(data.getEncodedSkin(), new byte[0]) || encodedSkin.length < 16383) {
			resourceLocation = getMojangSkin(player);
			
			if (wasAlex(player)) {
				skinType = SkinInfo.SkinType.ALEX;
			} else {
				skinType = SkinInfo.SkinType.STEVE;
			}
		} else {
			
			BufferedImage bufferedImage = null;
			if (player != null) {
				bufferedImage = toImage(player, encodedSkin);
				bufferedImage = ClientUtil.ImageFixer.convertSkinTo64x64(bufferedImage);
			}
			
			if (bufferedImage == null) {
				resourceLocation = DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			} else {
				ResourceLocation tempLocation = new ResourceLocation(player.getName() + "_skin_" + System.currentTimeMillis());
				Minecraft.getMinecraft().getTextureManager().loadTexture(tempLocation, new DynamicTexture(bufferedImage));
				resourceLocation = tempLocation;
				skinType = CapabilityRegeneration.getForPlayer(player).getSkinType();
			}
		}
		return new SkinInfo(resourceLocation, skinType);
	}
	
	public static boolean wasAlex(EntityPlayer player) {
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
	 * @throws IOException
	 */
	private static ResourceLocation getMojangSkin(AbstractClientPlayer player) throws IOException {
		Minecraft minecraft = Minecraft.getMinecraft();
		Map map = minecraft.getSkinManager().loadSkinFromCache(player.getGameProfile());
		if (map.isEmpty()) {
			map = minecraft.getSessionService().getTextures(minecraft.getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
		}
		if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
			MinecraftProfileTexture profile = (MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN);
			
			BufferedImage image = null;
			try {
				image = ImageIO.read(new URL(profile.getUrl()));
				image = ClientUtil.ImageFixer.convertSkinTo64x64(image);
				RegenerationMod.LOG.info("Grabbing Mojang skin from: " + profile.getUrl());
			} catch (Exception e) {
				RegenerationMod.LOG.error("Error creating skin from Mojang Servers, attempting to bring it in from elsewhere: " + e);
				RegenerationMod.LOG.info("Attempting to grab skin from: https://minotar.net/download/" + player.getUniqueID());
				image = ImageIO.read(new URL("https://minotar.net/download/" + player.getUniqueID()));
				image = ClientUtil.ImageFixer.convertSkinTo64x64(image);
			}
			
			if (image == null) {
				return DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			}
			
			File file = new File(SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png");
			ImageIO.write(image, "png", file);
			ResourceLocation tempLocation = new ResourceLocation(player.getName() + "_skin_" + System.currentTimeMillis());
			minecraft.getTextureManager().loadTexture(tempLocation, new DynamicTexture(image));
			return tempLocation;
		}
		
		return DefaultPlayerSkin.getDefaultSkinLegacy();
	}
	
	/**
	 * Changes the ResourceLocation of a Players skin
	 *
	 * @param player  - Player instance involved
	 * @param texture - ResourceLocation of intended texture
	 */
	public static void setPlayerSkin(AbstractClientPlayer player, ResourceLocation texture) {
		if (player.getLocationSkin() == texture || texture == null) {
			if (texture == null) {
				RegenerationMod.LOG.error("Skin data for " + player.getName() + "was null");
			}
			return;
		}
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		if (playerInfo == null)
			return;
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		if (texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
	}
	
	public static void setSkinType(AbstractClientPlayer player, SkinInfo.SkinType skinType) {
		if (skinType.getMojangType().equals(player.getSkinType())) return;
		if (!TYPE_BACKUPS.containsKey(player.getUniqueID())) {
			TYPE_BACKUPS.put(player.getUniqueID(), player.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
		}
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, skinType.getMojangType(), 5);
	}
	
	/**
	 * Subscription to RenderPlayerEvent.Pre to set players model and texture from hashmap
	 *
	 * @param e - RenderPlayer Pre Event
	 */
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre e) {
		if (MinecraftForgeClient.getRenderPass() == -1) return; //Don't do this hacky skin shit in inventory
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		if (player.ticksExisted == 20) {
			PLAYER_SKINS.remove(player.getUniqueID());
		}
		
		if (cap.getState() == RegenState.REGENERATING) {
			cap.getType().getRenderer().onRenderRegeneratingPlayerPre(cap.getType(), e, cap);
		} else if (!PLAYER_SKINS.containsKey(player.getUniqueID())) {
			setSkinFromData(player, cap);
		} else {
			SkinInfo skin = PLAYER_SKINS.get(player.getUniqueID());
			if (skin != null) {
				if (skin.getSkinTextureLocation() == null) {
					setPlayerSkin(player, skin.getSkinTextureLocation());
				}
				if (skin != null) {
					setSkinType(player, skin.getSkintype());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Post e) {
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		if (cap.getState() == RegenState.REGENERATING) {
			cap.getType().getRenderer().onRenderRegeneratingPlayerPost(cap.getType(), e, cap);
		}
		
	}
	
	@SubscribeEvent
	public void onRelog(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof AbstractClientPlayer) {
			AbstractClientPlayer clientPlayer = (AbstractClientPlayer) e.getEntity();
			PLAYER_SKINS.remove(clientPlayer.getUniqueID());
		}
	}
	
	/**
	 * Called by onRenderPlayer, sets model, sets texture, adds player and SkinInfo to map
	 *
	 * @param player - Player instance
	 * @param cap    - Players Regen capability instance
	 */
	private void setSkinFromData(AbstractClientPlayer player, IRegeneration cap) {
		SkinInfo skinInfo = null;
		try {
			skinInfo = SkinChangingHandler.getSkinInfo(player, cap);
		} catch (IOException e1) {
			RegenerationMod.LOG.error("Error creating skin for: " + player.getName() + " " + e1.getMessage());
		}
		if (skinInfo != null) {
			SkinChangingHandler.setPlayerSkin(player, skinInfo.getSkinTextureLocation());
		}
		
		if (skinInfo != null) {
			SkinChangingHandler.setSkinType(player, skinInfo.getSkintype());
		}
		PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
	}
	
	public enum EnumChoices implements IEnum {
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
