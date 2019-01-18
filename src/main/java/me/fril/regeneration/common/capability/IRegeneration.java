package me.fril.regeneration.common.capability;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.client.skinhandling.SkinInfo;
import me.fril.regeneration.common.types.IRegenType;
import me.fril.regeneration.util.RegenState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegeneration extends INBTSerializable<NBTTagCompound> {
	
	EntityPlayer getPlayer();
	int getRegenerationsLeft();
	void triggerRegeneration();
	
	void tick();
	void synchronise();
	
	NBTTagCompound getStyle();
	void setStyle(NBTTagCompound nbt);
	
	Vec3d getPrimaryColor();
	Vec3d getSecondaryColor();
	
	/** Only for debug purposes! */
	@Deprecated
	void setRegenerationsLeft(int amount);
	
	/** Returns if the player is currently <i>able to</i> regenerate */
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
	
	/** Only for debug purposes! */ //TODO add to debugger
	@Deprecated
	int getTicksGlowing();
	
	/** Only for debug purposes! */
	@Deprecated
	void setTicksGlowing(int ticksGlowing);
	
	boolean areHandsGlowing();
	//void setGlowing(boolean glowing);
	
	String getDeathSource();
	void setDeathSource(String source);
}
