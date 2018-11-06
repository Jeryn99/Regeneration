package me.fril.regeneration.debugger;

import java.io.PrintStream;

import me.fril.regeneration.util.TimerChannel;

public interface IDebugChannel {
	
	//STUB add report messages
	
	void updateCurrentTick(long tick);
	
	void notifyExecution(TimerChannel channel, long tick);
	void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick);
	
	void notifyScheduleBlank(TimerChannel channel);
	void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick);
	
	PrintStream getConsole();
	
}
