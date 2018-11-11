package me.fril.regeneration.debugger.util;

import java.awt.EventQueue;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.RegenState.Transition;

/** Delegates all methods to the {@link #target} using {@link EventQueue#invokeLater(Runnable)} */
public class EventQueueDebugChannelProxy implements IDebugChannel {
	
	private final IDebugChannel target;
	
	public EventQueueDebugChannelProxy(IDebugChannel target) {
		this.target = target;
	}
	
	
	@Override
	public void notifyLoaded() {
		EventQueue.invokeLater(()-> {
			target.notifyLoaded();
		});
	}
	
	@Override
	public void notifyExecution(Transition action, long tick) {
		EventQueue.invokeLater(()-> {
			target.notifyExecution(action, tick);
		});
	}
	
	@Override
	public void notifyCancel(Transition action, long wasInTicks) {
		EventQueue.invokeLater(()-> {
			target.notifyCancel(action, wasInTicks);
		});
	}
	
	@Override
	public void notifySchedule(Transition action, long inTicks) {
		EventQueue.invokeLater(()-> {
			target.notifySchedule(action, inTicks);
		});
	}
	
	@Override
	public void warn(Transition action, String msg) {
		EventQueue.invokeLater(()-> {
			target.warn(action, msg);
		});
	}
	
	@Override
	public void out(String msg) {
		EventQueue.invokeLater(()-> {
			target.out(msg);
		});
	}
	
}
