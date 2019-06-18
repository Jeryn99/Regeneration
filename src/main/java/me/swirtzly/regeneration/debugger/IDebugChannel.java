package me.swirtzly.regeneration.debugger;

import me.swirtzly.regeneration.util.RegenState;

public interface IDebugChannel {
	
	void notifyCancel(RegenState.Transition action, long wasInTicks);
	
	void notifyExecution(RegenState.Transition action, long atTick);
	
	void notifySchedule(RegenState.Transition action, long inTicks);
	
	void warn(RegenState.Transition action, String msg);
	
	void warn(String msg);
	
	void out(RegenState.Transition action, String msg);
	
	void out(String msg);
	
	void notifyLoaded();
	
}
