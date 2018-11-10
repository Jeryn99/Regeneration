package me.fril.regeneration.util;

import me.fril.regeneration.debugger.IDebugChannel;

public class DebuggableScheduledAction extends ScheduledAction {
	public final String identifier;
	private final IDebugChannel debugChannel;
	
	public DebuggableScheduledAction(String identifier, IDebugChannel debugChannel, Runnable callback, long inTicks) {
		super(callback, inTicks);
		this.identifier = identifier;
		this.debugChannel = debugChannel;
	}
	
	@Override
	public boolean tick() {
		boolean executed = super.tick();
		if (executed)
			debugChannel.notifyExecution(identifier, currentTick);
		return executed;
	}
	
	@Override
	public void cancel() {
		super.cancel();
		debugChannel.notifyCancel(identifier, scheduledTick-currentTick, scheduledTick);
	}

}
