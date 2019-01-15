package me.fril.regeneration.common.capability;

import org.apache.commons.lang3.tuple.Pair;

import me.fril.regeneration.util.RegenState.Transition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {
	
	//Event proxy methods
	boolean onKilled();
	void onPunchEntity(EntityLivingBase entity);
	
	void onPunchBlock(IBlockState blockState);
	
	//Proxy methods for timing related stuff
	double getStateProgress();
	
	//Debug things
	@Deprecated
	Pair<Transition, Long> getScheduledEvent();
	@Deprecated
	void fastForward();
	
	
}
