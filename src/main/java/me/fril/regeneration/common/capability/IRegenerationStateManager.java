package me.fril.regeneration.common.capability;

import me.fril.regeneration.util.RegenState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {
	
	//Event proxy methods
	boolean onKilled();
	//void onPunchBlock();
	void onPunchEntity(EntityLivingBase entity);
	
	//Other
	RegenState getState();
	
}
