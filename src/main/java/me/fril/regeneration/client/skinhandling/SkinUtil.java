package me.fril.regeneration.client.skinhandling;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class SkinUtil {
	
	public static final File SKIN_DIRECTORY = new File("./mods/regeneration/skins/");/*,
		                     SKIN_CACHE_DIRECTORY = new File("./mods/regeneration/skincache/" + Minecraft.getMinecraft().getSession().getProfile().getId() + "/skins"),
		                     SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve"),
		                     SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");*/
	
	public static final ModelBiped STEVE_MODEL = new ModelPlayer(0.1F, false), ALEX_MODEL = new ModelPlayer(0.1F, true);
	
	
	
	public static ResourceLocation generateSkinResource(String name, byte[] encodedSkin, SkinType type) {
		if (encodedSkin.length >= 16383) {
			BufferedImage img = SkinUtil.decodePixelData(encodedSkin);
			return img == null ? null : Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(name + "_skin", new DynamicTexture(img));
		} else return null; //too small
	}
	
	
	
	public static byte[] encodeToPixelData(File file) throws IOException {
		return encodeToPixelData(ImageIO.read(file));
	}
	
	/**
	 * Converts a buffered image to Pixel data
	 *
	 * @param bufferedImage - Buffered image to be converted to Pixel data
	 */
	public static byte[] encodeToPixelData(BufferedImage bufferedImage) {
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
	private static BufferedImage decodePixelData(byte[] imageData) {
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);
		img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(imageData, imageData.length), new Point()));
		return img;
		
		//NOW shouldn't save in here
		/*File file = new File(SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png");
		
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		
		ImageIO.write(img, "png", file);*/
	}
	
	
	
	/**
	 * Changes the ResourceLocation of a Players skin
	 *
	 * @param player  - Player instance involved
	 * @param texture - ResourceLocation of intended texture
	 */
	public static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		if (playerInfo == null)
			return;
		
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		
		if (texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4); //set 'isTextureLoaded' to false
	}
	
	public static ResourceLocation getPlayerTexture(AbstractClientPlayer player) {
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		if (playerInfo == null)
			return null;
		
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		return playerTextures.get(MinecraftProfileTexture.Type.SKIN);
	}
	
	
	
	public enum ModelPreference {
		ALEX(true), STEVE(false), EITHER(true);
		
		private boolean isAlex;
		
		ModelPreference(boolean b) {
			this.isAlex = b;
		}
		
		public boolean isAlex() {
			return this == EITHER ? new Random().nextBoolean() : isAlex;
		}
	}
	
	public enum SkinType {
		ALEX, STEVE
	}
	
}
