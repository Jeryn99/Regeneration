package me.fril.regeneration.debugger;

import me.fril.regeneration.util.RegenState.Transition;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Used in headless environments
 */
public class DummyRegenDebugger implements IRegenDebugger {
	
	@Override
	public IDebugChannel getChannelFor(EntityPlayer player) {
		return new IDebugChannel() {
			
			@Override
			public void warn(String msg) {
			}
			
			@Override
			public void warn(Transition action, String msg) {
			}
			
			@Override
			public void out(String msg) {
			}
			
			@Override
			public void out(Transition action, String msg) {
			}
			
			@Override
			public void notifySchedule(Transition action, long inTicks) {
			}
			
			@Override
			public void notifyLoaded() {
			}
			
			@Override
			public void notifyExecution(Transition action, long atTick) {
			}
			
			@Override
			public void notifyCancel(Transition action, long wasInTicks) {
			}
		};
	}
	
	@Override
	public void open() {
	}
	
	@Override
	public void dispose() {
	}
	
}
