package me.fril.regeneration.debugger.util;

import java.awt.EventQueue;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.TimerChannel;

/** Delegates all methods to the {@link #target} using {@link EventQueue#invokeLater(Runnable)} */
public class EventQueueDebugChannelProxy implements IDebugChannel {
	
	private final IDebugChannel target;
	
	public EventQueueDebugChannelProxy(IDebugChannel target) {
		this.target = target;
	}
	
	
	
	@Override
	public void update(long currentTick) {
		EventQueue.invokeLater(()-> {
			target.update(currentTick);
		});
	}
	
	@Override
	public void notifyExecution(TimerChannel channel, long tick) {
		EventQueue.invokeLater(()-> {
			target.notifyExecution(channel, tick);
		});
	}
	
	@Override
	public void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()-> {
			target.notifyCancel(channel, inTicks, scheduledTick);
		});
	}
	
	@Override
	public void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()-> {
			target.notifySchedule(channel, inTicks, scheduledTick);
		});
	}
	
	@Override
	public void notifyScheduleBlank(TimerChannel channel) {
		EventQueue.invokeLater(()-> {
			target.notifyScheduleBlank(channel);
		});
	}
	
	@Override
	public void warn(String msg) {
		EventQueue.invokeLater(()-> {
			target.warn(msg);
		});
	}

}
