package me.fril.regeneration.client.skinhandling;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@SideOnly(Side.CLIENT) //SUB as far as I know this is bad practice and shouldn't be used
public class SkinChangingHandler { //FIXME resetting skin doesn't work sometimes? Haven't seen it working but I assume it did

	public static final File SKIN_DIRECTORY = new File("./mods/regeneration/skins/");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	private static final File SKIN_CACHE_DIRECTORY = new File("./mods/regeneration/skincache/" + Minecraft.getMinecraft().getSession().getProfile().getId() + "/skins");
	private static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	private static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	private static final Logger SKIN_LOG = LogManager.getLogger(SkinChangingHandler.class); //TODO move to debugger
	private static final Random RAND = new Random();
	private static final ModelBiped STEVE_MODEL = new ModelPlayer(0.1F, false);
	private static final ModelBiped ALEX_MODEL = new ModelPlayer(0.1F, true);
	private static String CURRENT_SKIN = "banana.png";
	private static FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(CURRENT_SKIN);

	/**
	 * Creates skin folders
	 * Proceeds to download skins to the folders if they are empty
	 * If the download doesn't happen, NPEs will occur later on
	 */
	public static void registerResources() {

		if (!SKIN_CACHE_DIRECTORY.exists()) {
			SKIN_CACHE_DIRECTORY.mkdirs();
		}

		if (!SKIN_DIRECTORY.exists()) {
			SKIN_DIRECTORY.mkdirs();
		}

		SKIN_DIRECTORY_ALEX.mkdirs();
		SKIN_DIRECTORY_STEVE.mkdirs();

		if (Objects.requireNonNull(SKIN_DIRECTORY_ALEX.listFiles()).length < 1 || Objects.requireNonNull(SKIN_DIRECTORY_STEVE.listFiles()).length < 1) {
			try {
				createDefaultImages();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Converts a buffered image to Pixel data
	 *
	 * @param bufferedImage - Buffered image to be converted to Pixel data
	 */
	private static byte[] encodeToPixelData(BufferedImage bufferedImage) {
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
	public static void skinChangeRandom(Random random, EntityPlayer player) throws IOException {
		if (Minecraft.getMinecraft().player.getUniqueID() != player.getUniqueID())
			return;

		if (RegenConfig.changeMySkin) {
			boolean isAlex = RegenConfig.prefferedModel.isAlex();
			File skin = SkinChangingHandler.getRandomSkinFile(random, isAlex);
			BufferedImage image = ImageIO.read(skin);
			CURRENT_SKIN = skin.getName();
			IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(CURRENT_SKIN);
			byte[] pixelData = SkinChangingHandler.encodeToPixelData(image);
			CapabilityRegeneration.getForPlayer(player).setEncodedSkin(pixelData);
			if (pixelData.length >= 32767) {
				ClientUtil.sendSkinResetPacket();
			} else {
				NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData, isAlex));
			}
		} else {
			ClientUtil.sendSkinResetPacket();
		}
	}

	private static File getRandomSkinFile(Random rand, boolean isAlex) throws IOException {
		File skins = null;
		if (isAlex) {
			skins = SKIN_DIRECTORY_ALEX;
		} else {
			skins = SKIN_DIRECTORY_STEVE;
		}
		File[] files = skins.listFiles(IMAGE_FILTER);

		if (files.length == 0) {
			createDefaultImages();
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
	private static SkinInfo getSkin(AbstractClientPlayer player, IRegeneration data) throws IOException {
		byte[] encodedSkin = CapabilityRegeneration.getForPlayer(player).getEncodedSkin();
		ResourceLocation resourceLocation = null;
		SkinInfo.SkinType skinType = null;

		if (Arrays.equals(data.getEncodedSkin(), new byte[0]) || encodedSkin.length < 16383) {
			resourceLocation = getSkinFromMojang(player);

			if (player.getSkinType().equals("slim")) {
				skinType = SkinInfo.SkinType.ALEX;
			} else {
				skinType = SkinInfo.SkinType.STEVE;
			}
		} else {
			BufferedImage bufferedImage = toImage(player, encodedSkin);
			resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(player.getName() + "_skin", new DynamicTexture(bufferedImage));
			skinType = CapabilityRegeneration.getForPlayer(player).getSkinType();
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
		URL url = new URL(String.format(RegenConfig.downloadUrl, StringUtils.stripControlCodes(player.getUniqueID().toString())));
		BufferedImage img = ImageIO.read(url);
		SKIN_LOG.info("Downloading Skin from: {}", url.toString());
		ImageIO.write(img, "png", new File(SKIN_CACHE_DIRECTORY, "cache-" + player.getUniqueID() + ".png"));
		return minecraft.getTextureManager().getDynamicTextureLocation(player.getName() + "_skin", new DynamicTexture(img));
	}

	/**
	 * @param url      - URL to download image from
	 * @param file     - Directory of where to save the image to [SHOULD NOT CONTAIN THE FILES NAME]
	 * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
	 * @throws IOException
	 */
	private static void downloadImages(URL url, File file, String filename) throws IOException {
		SKIN_LOG.info("Downloading Skin from: {}", url.toString());
		BufferedImage img = ImageIO.read(url);
		ImageIO.write(img, "png", new File(file, filename + ".png"));
	}

	/**
	 * Downloads a set of default images to their correct directories
	 *
	 * @throws IOException
	 */
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
	}

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
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
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
	 * Subscription to RenderPlayerEvent.Pre to set players model and texture from hashmap
	 *
	 * @param e - RenderPlayer Pre Event
	 */
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre e) {
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);

		if (cap.getState() == RegenState.REGENERATING) {
			cap.getType().getRenderer().onRenderRegeneratingPlayerPre(cap.getType(), e, cap);
		} else if (cap.getState() == RegenState.ALIVE && !PLAYER_SKINS.containsKey(player.getUniqueID())) {
			setSkinFromData(player, cap, e.getRenderer());
		}
	}

	/**
	 * Called by onRenderPlayer, sets model, sets texture, adds player and SkinInfo to map
	 *
	 * @param player       - Player instance
	 * @param cap          - Players Regen capability instance
	 * @param renderPlayer - Player instances renderer
	 */
	private void setSkinFromData(AbstractClientPlayer player, IRegeneration cap, RenderPlayer renderPlayer) {
		SkinInfo skinInfo = null;
		try {
			skinInfo = SkinChangingHandler.getSkin(player, cap);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SkinChangingHandler.setPlayerTexture(player, skinInfo.getTextureLocation());

		if (skinInfo.getSkintype() == SkinInfo.SkinType.ALEX) {
			SkinChangingHandler.setPlayerModel(renderPlayer, ALEX_MODEL);
		} else {
			SkinChangingHandler.setPlayerModel(renderPlayer, STEVE_MODEL);
		}
		PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
	}

	public enum EnumChoices {
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
