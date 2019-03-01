package me.suff.regeneration.handlers;

import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraftforge.common.util.LazyOptional;

public interface IActingHandler {
	
	/**
	 * Called for every tick a Player is regenerating
	 * WARNING: Server only!
	 */
	void onRegenTick(LazyOptional<IRegeneration> cap);
	
	/**
	 * Called just after the player has been killed
	 * It is only called ONCE, once the player enters a grace period
	 */
	void onEnterGrace(LazyOptional<IRegeneration> cap);
	
	/**
	 * Called ONCE when the players hands begin to glow
	 */
	void onHandsStartGlowing(LazyOptional<IRegeneration> cap);
	
	/**
	 * Called when the player enters the critical stage of a grace period
	 */
	void onGoCritical(LazyOptional<IRegeneration> cap);
	
	/**
	 * Called on the first tick of a players Regeneration
	 */
	void onRegenTrigger(LazyOptional<IRegeneration> cap);
	
	/**
	 * Called on the last tick of a players Regeneration
	 */
	void onRegenFinish(LazyOptional<IRegeneration> cap);
	
}
