package me.fril.regeneration.debugger;

import me.fril.regeneration.util.RegenState.Transition;

public interface IDebugChannel { //FIXME don't do the whole GUI thing when it's not visible to save on memory consumption
	
	void notifyCancel(Transition action, long wasInTicks);
	void notifyExecution(Transition action, long atTick);
	void notifySchedule(Transition action, long inTicks);
	
	void warn(Transition action, String msg);
	void warn(String msg);
	
	void out(Transition action, String msg);
	void out(String msg);
	
	void notifyLoaded();
	
}
