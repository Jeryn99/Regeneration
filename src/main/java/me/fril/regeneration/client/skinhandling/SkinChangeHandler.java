package me.fril.regeneration.client.skinhandling;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import me.fril.regeneration.client.skinhandling.SkinUtil.SkinType;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageSkinChange;
import me.fril.regeneration.network.NetworkHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class SkinChangeHandler implements INBTSerializable<NBTTagCompound> {
	
	private final IRegeneration cap;
	
	private SkinType skinType = SkinType.ALEX;
	private byte[] skinData = new byte[0];
	
	//Client only
	private final ResourceLocation defaultTexture;
	private ResourceLocation textureResource;
	
	public SkinChangeHandler(IRegeneration cap, ResourceLocation defaultTexture) {
		this.cap = cap;
		this.defaultTexture = defaultTexture;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByteArray("encoded_skin", skinData);
		nbt.setString("skinType", skinType.name());
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		skinType = SkinType.valueOf(nbt.getString("skinType"));
		
		byte[] newSkinData = nbt.getByteArray("encoded_skin");
		if (!Arrays.equals(skinData, newSkinData)) { //new skin
			skinData = newSkinData;
			
			if (cap.getPlayer().world.isRemote) {
				textureResource = SkinUtil.generateSkinResource(cap.getPlayer().getUniqueID().toString(), skinData, skinType);
				SkinUtil.setPlayerTexture((AbstractClientPlayer)cap.getPlayer(), textureResource == null ? defaultTexture : textureResource);
			}
		}
	}
	
	
	
	public void updateServerside(byte[] encodedSkin, boolean isAlex) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("Updating serverside on the client");
		
		this.skinData = encodedSkin;
		this.skinType = isAlex ? SkinType.ALEX : SkinType.STEVE;
		
		cap.synchronise(); //this runs deserializeNBT on every client, causing the new skin to be set
	}
	
	
	
	public void randomizeSkin(Random rand) {
		if (!cap.getPlayer().world.isRemote)
			throw new IllegalStateException("Randomizing skin on server");
		
		try {
			byte[] pixelData = SkinUtil.encodeToPixelData(new File("D:/Pictures/mc-skin.png"));
			NetworkHandler.INSTANCE.sendToServer(new MessageSkinChange(pixelData, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//STUB NOW implement
		/*
				/**
				 * Chooses a random png file from Steve/Alex Directory (This really depends on the Clients preference)
				 * It also checks image size of the select file, if it's too large, we'll just reset the player back to their Mojang skin,
				 * 	or else they will be kicked from their server.
				 * If the player has disabled skin changing on the client, it will just send a reset packet
				 *
				 * @param random - This kinda explains itself, doesn't it?
				 * @param player - Player instance, used to check UUID to ensure it is the client player being involved in the scenario
				 * @throws IOException
				 * /
				public void skinChangeRandom(Random random) throws IOException {
					if (RegenConfig.skins.changeMySkin) {
						boolean isAlex = RegenConfig.skins.prefferedModel.isAlex();
						File skin = SkinChangingHandlerOLD.getRandomSkinFile(random, isAlex);
						BufferedImage image = ImageIO.read(skin);
						CURRENT_SKIN = skin.getName();
						IMAGE_FILTER = (dir, name) -> name.endsWith(".png") && !name.equals(CURRENT_SKIN);
						byte[] pixelData = SkinUtil.encodeToPixelData(image);
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
		 */
	}

}
