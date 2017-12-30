package com.afg.regeneration.capability;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by AFlyingGrayson on 12/30/17
 */
public interface ITimelordCapability
{
	NBTTagCompound writeNBT();
	void readNBT(NBTTagCompound nbt);
	void syncToPlayer();
	boolean isTimelord();
	int getRegenTicks();
	int getRegenCount();
	void setRegenTicks(int ticks);
	void setRegenCount(int count);
	void setTimelord(boolean timelord);
}
