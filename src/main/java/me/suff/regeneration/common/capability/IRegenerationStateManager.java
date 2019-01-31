package me.suff.regeneration.common.capability;

import me.suff.regeneration.util.RegenState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.commons.lang3.tuple.Pair;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {
	
	// Event proxy methods
	boolean onKilled(DamageSource source);
	
	void onPunchEntity(EntityLivingBase entity);
	
	void onPunchBlock(PlayerInteractEvent.LeftClickBlock e);
	
	// Proxy methods for timing related stuff
	double getStateProgress();
	
	// Debug things
	@Deprecated
	Pair<RegenState.Transition, Long> getScheduledEvent();
	
	@Deprecated
	void fastForward();
	
	@Deprecated
	void fastForwardHandGlow();
	
}
