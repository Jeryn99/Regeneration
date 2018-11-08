package me.fril.regeneration.debugger;

import me.fril.regeneration.util.TimerChannel;

public interface IDebugChannel {
	
	void update(long currentTick);
	
	void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick);
	void notifyExecution(TimerChannel channel, long tick);
	void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick);
	void notifyScheduleBlank(TimerChannel channel);
	
	void warn(String msg);
	
}
