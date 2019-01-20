package me.fril.regeneration.client.skinhandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.client.skinhandling.SkinUtil.SkinType;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkinChangingHandlerOLD { //FIXME resetting skin doesn't work sometimes? Haven't seen it working but I assume it did
	
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	private static final Logger SKIN_LOG = LogManager.getLogger(SkinChangingHandlerOLD.class); //TODO move to debugger
	
	//private static final Random RAND = new Random();
	private static String CURRENT_SKIN = "banana.png";
	private static FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(CURRENT_SKIN);
	
	/**
	 * Creates skin folders
	 * Proceeds to download skins to the folders if they are empty
	 * If the download doesn't happen, NPEs will occur later on
	 */
	public static void init() {
		
		if (!SkinUtil.SKIN_CACHE_DIRECTORY.exists()) {
			SkinUtil.SKIN_CACHE_DIRECTORY.mkdirs();
		}
		
		if (!SkinUtil.SKIN_DIRECTORY.exists()) {
			SkinUtil.SKIN_DIRECTORY.mkdirs();
		}
		
		SkinUtil.SKIN_DIRECTORY_ALEX.mkdirs();
		SkinUtil.SKIN_DIRECTORY_STEVE.mkdirs();
		
		if (SkinUtil.SKIN_DIRECTORY_ALEX.listFiles().length < 1 || SkinUtil.SKIN_DIRECTORY_STEVE.listFiles().length < 1) {
			/*try {
				createDefaultImages();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
	}
	
	
	private static File getRandomSkinFile(Random rand, boolean isAlex) throws IOException {
		File skins = null;
		if (isAlex) {
			skins = SkinUtil.SKIN_DIRECTORY_ALEX;
		} else {
			skins = SkinUtil.SKIN_DIRECTORY_STEVE;
		}
		File[] files = skins.listFiles(IMAGE_FILTER);
		
		if (files.length == 0) {
			//createDefaultImages();
		}
		
		File file = files[rand.nextInt(files.length)];
		return file;
	}
	
	/**
	 * Creates a SkinInfo object for later use
	 *
	 * @param player - Player instance involved
	 * @param data   - The players regeneration capability instance
	 * @return SkinInfo - A class that contains the SkinType and the resource location to use as a skin
	 * @throws IOException
	 */
	private static SkinInfo createSkinInfoForPlayer(AbstractClientPlayer player) throws IOException {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		byte[] encodedSkin = cap.getEncodedSkin();
		
		ResourceLocation resourceLocation = null;
		SkinType skinType = null;
		
		if (encodedSkin.length < 16383) { //too small
			resourceLocation = getSkinFromMojang(player);
			skinType = player.getSkinType().equals("slim") ? SkinType.ALEX : SkinType.STEVE;
		} else {
			BufferedImage img = SkinUtil.dataToImage(player, encodedSkin);
			
			if (img == null) {
				SKIN_LOG.warn("Falling back to steve/alex because skin data couldn't be converted to a BufferedImage ({})", player.getUniqueID());
				resourceLocation = DefaultPlayerSkin.getDefaultSkin(player.getUniqueID()); //fall back to alex/steve
				skinType = player.getSkinType().equals("slim") ? SkinType.ALEX : SkinType.STEVE;
			} else {
				resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(player.getName() + "_skin", new DynamicTexture(img));
				skinType = CapabilityRegeneration.getForPlayer(player).getSkinType();
			}
		}
		
		return new SkinInfo(resourceLocation, skinType);
	}
	
	/**
	 * This is used when the clients skin is reset
	 *
	 * @param player - Player to get the skin of
	 * @return ResourceLocation consisting of a DynamicTexture that is the players real skin
	 * @throws IOException
	 */
	//TODO Actually get the skin from Mojang instead of a API website
	private static ResourceLocation getSkinFromMojang(AbstractClientPlayer player) throws IOException {
		setPlayerTexture(player, null);
		Minecraft minecraft = Minecraft.getMinecraft();
		URL url = new URL(String.format(RegenConfig.skins.downloadUrl, StringUtils.stripControlCodes(player.getUniqueID().toString())));
		File out = new File(SkinUtil.SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png");
		BufferedImage img = ImageIO.read(url);
		SKIN_LOG.info("Downloading Mojang skin from: {} to {}", url.toString(), out.getAbsolutePath());
		ImageIO.write(img, "png", out);
		
		if (img == null) {
			return DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
		}
		return minecraft.getTextureManager().getDynamicTextureLocation(player.getName() + "_skin", new DynamicTexture(img));
	}
	
	/**
	 * @param url      - URL to download image from
	 * @param file     - Directory of where to save the image to [SHOULD NOT CONTAIN THE FILES NAME]
	 * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
	 * @throws IOException
	 */
	private static void downloadImages(URL url, File file, String filename) throws IOException {
		File out = new File(file, filename + ".png");
		SKIN_LOG.info("Downloading Skin from: {} to {}", url.toString(), out.getAbsolutePath());
		BufferedImage img = ImageIO.read(url);
		ImageIO.write(img, "png", out);
	}
	
	/**
	 * Downloads a set of default images to their correct directories
	 *
	 * @throws IOException
	 * /
	private static void createDefaultImages() throws IOException {
		for (DefaultSkins value : DefaultSkins.values()) {
			File dummy;
			if (value.isAlexDir()) {
				dummy = SKIN_DIRECTORY_ALEX;
			} else {
				dummy = SKIN_DIRECTORY_STEVE;
			}
			
			downloadImages(new URL(value.getURL()), dummy, value.name().toLowerCase());
		}
	}*/
	
	/**
	 * Changes the ResourceLocation of a Players skin
	 *
	 * @param player  - Player instance involved
	 * @param texture - ResourceLocation of intended texture
	 */
	private static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		if (playerInfo == null)
			return;
		
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		
		if (texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4); //set 'isTextureLoaded' to false
	}
	
	/**
	 * Set's a players Player Model
	 * WARNING: MUST EXTEND MODEL BIPED AND YOU SHOULD USE CACHED MODELS
	 *
	 * @param renderer
	 * @param model
	 */
	private static void setPlayerModel(RenderPlayer renderer, ModelBiped model) {
		renderer.mainModel = model;
	}
	
	/**
	 * Called by onRenderPlayer, sets model, sets texture, adds player and SkinInfo to map
	 *
	 * @param player       - Player instance
	 * @param cap          - Players Regen capability instance
	 * @param renderPlayer - Player instances renderer
	 */
	public static void setSkinFromData(AbstractClientPlayer player, RenderPlayer renderPlayer) {
		SkinInfo skinInfo = null;
		try {
			skinInfo = SkinChangingHandlerOLD.createSkinInfoForPlayer(player);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SkinChangingHandlerOLD.setPlayerTexture(player, skinInfo.getTextureLocation());
		
		if (skinInfo.getSkintype() == SkinInfo.SkinType.ALEX) {
			SkinChangingHandlerOLD.setPlayerModel(renderPlayer, SkinUtil.ALEX_MODEL);
		} else {
			SkinChangingHandlerOLD.setPlayerModel(renderPlayer, SkinUtil.STEVE_MODEL);
		}
		PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
	}
	
}
