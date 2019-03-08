package me.suff.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.network.MessageUpdateSkin;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.IEnum;
import me.suff.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class SkinChangingHandler {
	
	public static final File SKIN_DIRECTORY = new File("./mods/regeneration/skins/");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	public static final File SKIN_CACHE_DIRECTORY = new File("./mods/regeneration/skincache/" + Minecraft.getInstance().getSession().getProfile().getId() + "/skins");
	public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	public static final Logger SKIN_LOG = LogManager.getLogger(SkinChangingHandler.class); //TODO move to debugger
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
	 * @param player    - Player to be invovled
	 * @param imageData - Pixel data to be converted to a Buffered Image
	 * @return Buffered image that will later be converted to a Dynamic texture
	 */
	private static BufferedImage toImage(EntityPlayer player, String imageData) throws IOException {
		BufferedImage img = decodeToImage(imageData);
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
	
	public static BufferedImage decodeToImage(String imageString) throws IOException {
		BufferedImage image = null;
		byte[] imageByte;
		BASE64Decoder decoder = new BASE64Decoder();
		imageByte = decoder.decodeBuffer(imageString);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		image = ImageIO.read(bis);
		bis.close();
		return image;
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
	public static void sendSkinUpdate(Random random, EntityPlayer player) {
		if (Minecraft.getInstance().player.getUniqueID() != player.getUniqueID())
			return;
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (RegenConfig.CONFIG.changeMySkin.get()) {
				boolean isAlex = cap.getPreferredModel().isAlex();
				
				File skin = null;
				BufferedImage image = null;
				try {
					skin = SkinChangingHandler.chooseRandomSkin(random, isAlex);
					RegenerationMod.LOG.info(skin.getName() + " was choosen");
					image = ImageIO.read(skin);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String pixelData = String.valueOf("none");
				try {
					pixelData = SkinChangingHandler.imageToPixelData(skin);
				} catch (IOException e) {
					e.printStackTrace();
				}
				cap.setEncodedSkin(pixelData);
				System.out.println(pixelData);
					NetworkHandler.sendToServer(new MessageUpdateSkin(pixelData, isAlex));
				} else {
				ClientUtil.sendSkinResetPacket();
			}
		});
	}
	
	private static File chooseRandomSkin(Random rand, boolean isAlex) throws IOException {
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
	private static SkinInfo getSkinInfo(AbstractClientPlayer player, IRegeneration data) throws IOException {
		ResourceLocation resourceLocation;
		SkinInfo.SkinType skinType = null;
		
		if (data.getEncodedSkin().equals("none")) {
			resourceLocation = getMojangSkin(player);
			if (wasAlex(player)) {
				skinType = SkinInfo.SkinType.ALEX;
			} else {
				skinType = SkinInfo.SkinType.STEVE;
			}
		} else {
			BufferedImage bufferedImage = toImage(player, data.getEncodedSkin());
			bufferedImage = ImageFixer.convertSkinTo64x64(bufferedImage);
			System.out.println("CLIENT " + data.getEncodedSkin());
			if (bufferedImage == null) {
				resourceLocation = DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			} else {
				File file = new File(SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png");
				ImageIO.write(bufferedImage, "png", file);
				DynamicTexture tex = new DynamicTexture(NativeImage.read(new FileInputStream(file)));
				resourceLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(player.getName().getUnformattedComponentText().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
				skinType = data.getSkinType();
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
		Minecraft minecraft = Minecraft.getInstance();
		Map map = minecraft.getSkinManager().loadSkinFromCache(player.getGameProfile());
		if (map.isEmpty()) {
			map = minecraft.getSessionService().getTextures(minecraft.getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
		}
		if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
			MinecraftProfileTexture profile = (MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN);
			
			BufferedImage image = ImageIO.read(new URL(profile.getUrl()));
			image = ImageFixer.convertSkinTo64x64(image);
			
			if (image == null) {
				return DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			}
			
			File file = new File(SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png");
			ImageIO.write(image, "png", file);
			DynamicTexture tex = new DynamicTexture(NativeImage.read(new FileInputStream(file)));
			System.out.println(tex);
			System.out.println(NativeImage.read(new FileInputStream(file)));
			return minecraft.getTextureManager().getDynamicTextureLocation(player.getName().getUnformattedComponentText().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
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
	
	public static void setPlayerSkinType(AbstractClientPlayer player, SkinInfo.SkinType skinType) {
		if (skinType.getMojangType().equals(player.getSkinType())) return;
		if(!TYPE_BACKUPS.containsKey(player.getUniqueID())){
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
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (player.ticksExisted == 20) {
				SkinInfo oldSkinInfo = PLAYER_SKINS.get(player.getUniqueID());
				if (oldSkinInfo != null) {
					Minecraft.getInstance().getTextureManager().deleteTexture(oldSkinInfo.getSkinTextureLocation());
				}
				PLAYER_SKINS.remove(player.getUniqueID());
			}
			
			if (cap.getState() == RegenState.REGENERATING) {
				cap.getType().getRenderer().onRenderRegeneratingPlayerPre(cap.getType(), e, cap);
			} else if (!PLAYER_SKINS.containsKey(player.getUniqueID())) {
				setSkinFromData(player, cap);
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
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (cap.getState() == RegenState.REGENERATING) {
				cap.getType().getRenderer().onRenderRegeneratingPlayerPost(cap.getType(), e, cap);
			}
		});
	}
	
	@SubscribeEvent
	public void onRelog(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			PLAYER_SKINS.remove(e.getEntity().getUniqueID());
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
			SkinChangingHandler.setPlayerSkinType(player, skinInfo.getSkintype());
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
