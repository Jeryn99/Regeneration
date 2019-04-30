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
import me.suff.regeneration.util.RegenState;
import me.suff.regeneration.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class SkinChangingHandler {
	
	public static final File SKIN_DIRECTORY = new File(RegenConfig.skins.skinDir + "/Regeneration/skins/");
	public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
	public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
	public static final Logger SKIN_LOG = LogManager.getLogger("Regeneration Skin Handler");
	public static final Map<UUID, SkinInfo> PLAYER_SKINS = new HashMap<>();
	public static final Map<UUID, SkinInfo.SkinType> TYPE_BACKUPS = new HashMap<>();
	private static final Random RAND = new Random();
	
	
	/**
	 * Encode image to string
	 *
	 * @param imageFile The image to encode
	 * @return encoded string
	 */
	public static String imageToPixelData(File imageFile) throws IOException {
		byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(imageFile));
		return Base64.getEncoder().encodeToString(imageBytes);
	}
	
	
	/**
	 * Decode string to image
	 *
	 * @param imageString The string to decode
	 * @return decoded image
	 */
	public static BufferedImage toImage(String imageString) throws IOException {
		BufferedImage image = null;
		byte[] imageByte;
		BASE64Decoder decoder = new BASE64Decoder();
		imageByte = decoder.decodeBuffer(imageString);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		image = ImageIO.read(bis);
		bis.close();
		
		if(image == null){
			throw new IllegalStateException("The image data was " + imageString + " but the image became null...");
		}
		
		return image;
	}
	
	/**
	 * Chooses a random png file from Steve/Alex Directory (This really depends on the Clients preference)
	 * It also checks image size of the select file, if it's too large, we'll just reset the player back to their Mojang skin,
	 * else they will be kicked from their server. If the player has disabled skin changing on the client, it will just send a reset packet
	 *
	 * @param random - This kinda explains itself, doesn't it?
	 * @param player - Player instance, used to check UUID to ensure it is the client player being involved in the scenario
	 * @throws IOException
	 */
	public static void sendSkinUpdate(Random random, EntityPlayer player) {
		if (Minecraft.getMinecraft().player.getUniqueID() != player.getUniqueID())
			return;
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		if (RegenConfig.skins.changeMySkin) {
			boolean isAlex = cap.getPreferredModel().isAlex();
			
			File skin = null;
			skin = SkinChangingHandler.getRandomSkin(random, isAlex);
			RegenerationMod.LOG.info(skin + " was choosen");
			
			String pixelData = "NONE";
			try {
				pixelData = SkinChangingHandler.imageToPixelData(skin);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cap.setEncodedSkin(pixelData);
			NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(pixelData, isAlex));
		} else {
			ClientUtil.sendSkinResetPacket();
		}
		
	}
	
	private static File getRandomSkin(Random rand, boolean isAlex) {
		File skins = isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE;
		Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		if (folderFiles.isEmpty()) {
			SKIN_LOG.info("The Skin folder was empty....Downloading some skins...");
			FileUtil.doSetupOnThread();
			folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		}
		SKIN_LOG.info("There were " + folderFiles.size() + " to chose from");
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
	private static SkinInfo getSkinInfo(AbstractClientPlayer player, IRegeneration data, boolean cache) throws IOException {
		
		if (player == null || data == null || player.getName() == null || player.getUniqueID() == null) {
			return new SkinInfo(null, SkinInfo.SkinType.ALEX);
		}
		
		ResourceLocation resourceLocation;
		SkinInfo.SkinType skinType = null;
		
		if (data.getEncodedSkin().toLowerCase().equals("none") || data.getEncodedSkin().equals(" ") || data.getEncodedSkin().equals("")) {
			resourceLocation = getMojangSkin(player);
			skinType = TYPE_BACKUPS.get(player.getUniqueID());
		} else {
			BufferedImage bufferedImage = toImage(data.getEncodedSkin());
			bufferedImage = ClientUtil.ImageFixer.convertSkinTo64x64(bufferedImage);
			if (bufferedImage == null) {
				resourceLocation = DefaultPlayerSkin.getDefaultSkin(player.getUniqueID());
			} else {
				DynamicTexture tex = new DynamicTexture(bufferedImage);
				resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(player.getName().toLowerCase() + "_skin_" + System.currentTimeMillis(), tex);
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
	
	public static void addType(AbstractClientPlayer player) {
		if (player == null || TYPE_BACKUPS.containsKey(player.getUniqueID())) return;
		TYPE_BACKUPS.put(player.getUniqueID(), player.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
	}
	
	/**
	 * This is used when the clients skin is reset
	 *
	 * @param player - Player to get the skin of themselves
	 * @return ResourceLocation from Mojang
	 * @throws IOException
	 */
	private static ResourceLocation getMojangSkin(AbstractClientPlayer player) {
		Map map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(player.getGameProfile());
		if (map.isEmpty()) {
			map = Minecraft.getMinecraft().getSessionService().getTextures(Minecraft.getMinecraft().getSessionService().fillProfileProperties(player.getGameProfile(), false), false);
		}
		if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
			MinecraftProfileTexture profile = (MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN);
			File dir = new File((File) ObfuscationReflectionHelper.getPrivateValue(SkinManager.class, Minecraft.getMinecraft().getSkinManager(), RegenUtil.ReflectionNames.NETWORKPLAYERINFO_F_SKINDIR.getSrg()), profile.getHash().substring(0, 2));
			File file = new File(dir, profile.getHash());
			if (file.exists())
				file.delete();
			ResourceLocation location = new ResourceLocation("skins/" + profile.getHash());
			loadTexture(file, location, DefaultPlayerSkin.getDefaultSkinLegacy(), profile.getUrl(), player);
			setPlayerSkin(player, location);
			return player.getLocationSkin();
		}
		return DefaultPlayerSkin.getDefaultSkinLegacy();
	}
	
	private static ITextureObject loadTexture(File file, ResourceLocation resource, ResourceLocation def, String par1Str, AbstractClientPlayer player) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject object = texturemanager.getTexture(resource);
		if (object == null) {
			object = new ImageDownloadAlt(file, par1Str, def, new ImageBufferDownloadAlt(true), player);
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
	public static void setPlayerSkin(AbstractClientPlayer player, ResourceLocation texture) {
		if (player.getLocationSkin() == texture) {
			return;
		}
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, RegenUtil.ReflectionNames.ABSTRACTCLIENTPLAYER_F_PLAYERINFO.getSrg());
		if (playerInfo == null)
			return;
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, RegenUtil.ReflectionNames.NETWORKPLAYERINFO_F_TEXTURES.getSrg());
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		if (texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, RegenUtil.ReflectionNames.NETWORKPLAYERINFO_F_TEXTURESLOADED.getSrg());
	}
	
	public static void setSkinType(AbstractClientPlayer player, SkinInfo.SkinType skinType) {
		if (skinType.getMojangType().equals(player.getSkinType())) return;
		if (!TYPE_BACKUPS.containsKey(player.getUniqueID())) {
			TYPE_BACKUPS.put(player.getUniqueID(), player.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
		}
		NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, RegenUtil.ReflectionNames.ABSTRACTCLIENTPLAYER_F_PLAYERINFO.getSrg());
		ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, skinType.getMojangType(), RegenUtil.ReflectionNames.NETWORKPLAYERINFO_F_SKINTYPE.getSrg());
	}
	
	/**
	 * Subscription to RenderPlayerEvent.Pre to set players model and texture from hashmap
	 *
	 * @param e - RenderPlayer Pre Event
	 */
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre e) {
		if (MinecraftForgeClient.getRenderPass() == -1) return;
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		addType(player);
		
		if (player.ticksExisted == 20) {
			PLAYER_SKINS.remove(player.getUniqueID());
		}
		
		if (cap.getState() == RegenState.REGENERATING) {
			if (cap.getType().getAnimationProgress() > 0.7) {
				setSkinFromData(player, cap, false);
			}
			cap.getType().getRenderer().onRenderRegeneratingPlayerPre(cap.getType(), e, cap);
		} else if (!PLAYER_SKINS.containsKey(player.getUniqueID())) {
			setSkinFromData(player, cap, true);
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
	private void setSkinFromData(AbstractClientPlayer player, IRegeneration cap, boolean cache) {
		SkinInfo skinInfo = null;
		try {
			skinInfo = SkinChangingHandler.getSkinInfo(player, cap, cache);
		} catch (IOException e1) {
			SKIN_LOG.error("Error creating skin for: " + player.getName() + " " + e1.getMessage());
		}
		if (skinInfo != null) {
			SkinChangingHandler.setPlayerSkin(player, skinInfo.getSkinTextureLocation());
		}
		
		if (skinInfo != null) {
			SkinChangingHandler.setSkinType(player, skinInfo.getSkintype());
		}
		PLAYER_SKINS.put(player.getGameProfile().getId(), skinInfo);
		
	}
	
	public enum EnumChoices implements FileUtil.IEnum {
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
