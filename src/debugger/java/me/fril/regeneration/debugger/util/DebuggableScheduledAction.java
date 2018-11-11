package me.fril.regeneration.debugger.util;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.RegenState.Transition;
import me.fril.regeneration.util.ScheduledAction;

public class DebuggableScheduledAction extends ScheduledAction {
	public final Transition action;
	private final IDebugChannel debugChannel;
	
	public DebuggableScheduledAction(Transition action, IDebugChannel debugChannel, Runnable callback, long inTicks) {
		super(callback, inTicks);
		this.action = action;
		this.debugChannel = debugChannel;
		
		debugChannel.notifySchedule(action, inTicks);
	}
	
	@Override
	public boolean tick() {
		boolean willExecute = currentTick == scheduledTick;
		if (willExecute)
			debugChannel.notifyExecution(action, currentTick);
		
		boolean executed = super.tick();
		if (willExecute != executed)
			throw new IllegalStateException("Execution procpect wasn't true (prospect: "+willExecute+", result: "+executed+", cTick: "+currentTick+", scheduledTick: "+scheduledTick);
		
		return executed;
	}
	
	@Override
	public void cancel() {
		debugChannel.notifyCancel(action, scheduledTick-currentTick);
		super.cancel();
	}

}
