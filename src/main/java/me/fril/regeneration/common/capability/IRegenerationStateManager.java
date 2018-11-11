package me.fril.regeneration.common.capability;

import org.apache.commons.lang3.tuple.Pair;

import me.fril.regeneration.util.RegenState.Transition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {
	
	//Event proxy methods
	boolean onKilled();
	//void onPunchBlock();
	void onPunchEntity(EntityLivingBase entity);
	
	Pair<Transition, Long> getScheduledEvent();
	
}
