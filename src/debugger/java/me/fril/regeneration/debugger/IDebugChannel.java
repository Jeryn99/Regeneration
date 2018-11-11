package me.fril.regeneration.debugger;

import me.fril.regeneration.util.RegenState.Transition;

public interface IDebugChannel {
	
	void notifyCancel(Transition action, long wasInTicks);
	void notifyExecution(Transition action, long atTick);
	void notifySchedule(Transition action, long inTicks);
	
	void warn(Transition action, String msg);
	void notifyLoaded();
	
}
