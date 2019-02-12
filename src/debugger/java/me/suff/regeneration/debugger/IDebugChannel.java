package me.suff.regeneration.debugger;

import me.suff.regeneration.util.RegenState.Transition;

public interface IDebugChannel {
	
	void notifyCancel(Transition action, long wasInTicks);
	
	void notifyExecution(Transition action, long atTick);
	
	void notifySchedule(Transition action, long inTicks);
	
	void warn(Transition action, String msg);
	
	void warn(String msg);
	
	void out(Transition action, String msg);
	
	void out(String msg);
	
	void notifyLoaded();
	
}
