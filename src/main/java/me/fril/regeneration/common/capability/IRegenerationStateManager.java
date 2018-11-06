package me.fril.regeneration.common.capability;

import me.fril.regeneration.util.Scheduler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {
	
	boolean onKilled();
	void onPunchBlock();
	
	@Deprecated
	Scheduler getScheduler();
	
}
