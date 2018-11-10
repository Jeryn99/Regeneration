package me.fril.regeneration.debugger;

public interface IDebugChannel {
	
	//void update(String identifier, long currentTick);
	
	void notifyCancel(String identifier, long inTicks, long scheduledTick);
	void notifyExecution(String identifier, long tick);
	void notifySchedule(String identifier, long inTicks, long scheduledTick);
	void notifyScheduleBlank(String identifier);
	
	void warn(String identifier, String msg);
	
}
