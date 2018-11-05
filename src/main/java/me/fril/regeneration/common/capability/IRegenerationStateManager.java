package me.fril.regeneration.common.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {
	
	boolean onKilled();
	void onPunchBlock();
	
}
