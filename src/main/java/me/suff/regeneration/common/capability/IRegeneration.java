package me.suff.regeneration.common.capability;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.types.IRegenType;
import me.suff.regeneration.util.RegenState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegeneration extends INBTSerializable<NBTTagCompound> {
	
	EntityPlayer getPlayer();
	
	int getRegenerationsLeft();
	
	/**
	 * Only for debug purposes!
	 */
	@Deprecated
	void setRegenerationsLeft(int amount);
	
	void triggerRegeneration();
	
	void tick();
	
	void synchronise();
	
	NBTTagCompound getStyle();
	
	void setStyle(NBTTagCompound nbt);
	
	Vec3d getPrimaryColor();
	
	Vec3d getSecondaryColor();
	
	/**
	 * Returns if the player is currently <i>able to</i> regenerate
	 */
	default boolean canRegenerate() {
		return (RegenConfig.infiniteRegeneration || getRegenerationsLeft() > 0) && getPlayer().posY > 0;
	}
	
	void receiveRegenerations(int amount);
	
	void extractRegeneration(int amount);
	
	RegenState getState();
	
	IRegenType<?> getType();
	
	IRegenerationStateManager getStateManager();
	
	byte[] getEncodedSkin();
	
	void setEncodedSkin(byte[] string);
	
	SkinInfo.SkinType getSkinType();
	
	void setSkinType(String skinType);
	
	SkinChangingHandler.EnumChoices getPreferredModel();
	
	void setPreferredModel(String skinType);
	
	boolean areHandsGlowing();
	//void setGlowing(boolean glowing);
	
	String getDeathSource();
	
	void setDeathSource(String source);
	
	ResourceLocation getDnaType();
	
	void setDnaType(ResourceLocation resgitryName);
	
	boolean isDnaActive();
	
	void setDnaActive(boolean alive);
	
	//ONLY USED IN LCCORE
	int getReserve();
	
	void setReserve(int reserve);
}
