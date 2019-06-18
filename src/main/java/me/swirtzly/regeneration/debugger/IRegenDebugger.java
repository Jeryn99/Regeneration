package me.swirtzly.regeneration.debugger;

import net.minecraft.entity.player.EntityPlayer;

public interface IRegenDebugger {
	
	/**
	 * <B>NEVER EVER SAVE THE RESULT OF THIS IN A REFERENCE!</B>
	 */
	IDebugChannel getChannelFor(EntityPlayer player);
	
	void open();
	
	void dispose();
	
}
