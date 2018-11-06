package me.fril.regeneration.debugger;

import me.fril.regeneration.util.TimerChannel;

public interface IDebugChannel {
	
	void updateCurrentTick(long tick);
	
	void notifyExecution(TimerChannel channel, long tick);
	void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick);
	
	void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick);
	void notifyScheduleBlank(TimerChannel channel);
	
	void warn(String msg);
	
}
