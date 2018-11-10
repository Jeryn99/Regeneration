package me.fril.regeneration.debugger.util;

import java.awt.EventQueue;

import me.fril.regeneration.debugger.IDebugChannel;

/** Delegates all methods to the {@link #target} using {@link EventQueue#invokeLater(Runnable)} */
public class EventQueueDebugChannelProxy implements IDebugChannel {
	
	private final IDebugChannel target;
	
	public EventQueueDebugChannelProxy(IDebugChannel target) {
		this.target = target;
	}
	
	
	
	/*@Override
	public void update(long currentTick) {
		EventQueue.invokeLater(()-> {
			target.update(currentTick);
		});
	}*/
	
	@Override
	public void notifyExecution(String identifier, long tick) {
		EventQueue.invokeLater(()-> {
			target.notifyExecution(identifier, tick);
		});
	}
	
	@Override
	public void notifyCancel(String identifier, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()-> {
			target.notifyCancel(identifier, inTicks, scheduledTick);
		});
	}
	
	@Override
	public void notifySchedule(String identifier, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()-> {
			target.notifySchedule(identifier, inTicks, scheduledTick);
		});
	}
	
	@Override
	public void notifyScheduleBlank(String identifier) {
		EventQueue.invokeLater(()-> {
			target.notifyScheduleBlank(identifier);
		});
	}
	
	@Override
	public void warn(String identifier, String msg) {
		EventQueue.invokeLater(()-> {
			target.warn(identifier, msg);
		});
	}
	
}
