package me.fril.regeneration.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import me.fril.regeneration.debugger.IDebugChannel;

public class Scheduler { //TODO document why we can't instiate it with a debug channel
	
	private final Map<TimerChannel, ScheduledTask> schedule = new ConcurrentHashMap<>();
	private final IDebugChannel debugChannel;
	
	private long currentTick = 0;
	
	public void setDebugChannel(IDebugChannel debugChannel) {
		this.debugChannel = debugChannel;
	}
	
	public void tick() {
		currentTick++;
		debugChannel.updateCurrentTick(currentTick);
		
		//iterator needed because we modify the collection while iterating through it
		Iterator<Entry<TimerChannel, ScheduledTask>> it = schedule.entrySet().iterator();
		while (it.hasNext()) {
			Entry<TimerChannel, ScheduledTask> en = it.next();
			
			if (en.getValue().scheduledTick == currentTick) {
				debugChannel.notifyExecution(en.getKey(), currentTick);
				
				en.getValue().run();
				it.remove();
			}
		}
	}
	
	public ScheduledTask scheduleBlank(TimerChannel channel) {
		if (schedule.get(channel).ticksLeft() >= 0)
			throw new IllegalStateException("Overwriting non-completed action with blank on channel "+channel+" (old: "+schedule.get(channel)+")");
		
		ScheduledTask task = new BlankScheduledTask(channel);
		schedule.put(channel, task);
		
		debugChannel.notifyScheduleBlank(channel);
		return task;
	}
	
	
	
	public ScheduledTask scheduleInTicks(TimerChannel channel, long inTicks, Runnable callback) {
		if (schedule.get(channel).ticksLeft() >= 0)
			throw new IllegalStateException("Overwriting non-completed action on channel "+channel+" (old: "+schedule.get(channel)+", new would be in "+inTicks+")");
		
		if (inTicks < 0)
			return scheduleBlank(channel);
		else {
			ScheduledTask task = new ScheduledTask(channel, callback, currentTick+inTicks);
			schedule.put(channel, task);
			
			debugChannel.notifySchedule(channel, inTicks, task.scheduledTick);
			return task;
		}
	}
	
	public ScheduledTask scheduleInSeconds(TimerChannel channel, long inSeconds, Runnable callback) {
		return scheduleInTicks(channel, inSeconds*20, callback);
	}
	
	
	
	public void cancel(TimerChannel channel) {
		debugChannel.notifyCancel(channel, schedule.get(channel).ticksLeft(), schedule.get(channel).scheduledTick);
		schedule.get(channel).cancel();
	}
	
	
	
	public long getTicksLeft(TimerChannel channel) {
		return schedule.get(channel).ticksLeft();
	}
	
	public boolean hasDebugChannel() {
		return debugChannel != null;
	}
	
	public void reset() {
		currentTick = 0;
		schedule.clear();
	}
	
	
	
	
	
	
	public class ScheduledTask implements Runnable {
		
		private final TimerChannel channel;
		private final Runnable callback;
		private final long scheduledTick;
		private boolean canceled;
		
		private ScheduledTask(TimerChannel channel, Runnable callback, long scheduledTick) {
			this.channel = channel;
			this.callback = callback;
			this.scheduledTick = scheduledTick;
		}
		
		@Override
		public void run() {
			if (canceled)
				throw new IllegalStateException("Running cancelled action: "+this);
			
			callback.run();
		}
		
		public void cancel() {
			if (canceled)
				System.err.println("WARNING: Cancelling already canceled action"); //TODO debug channel
			canceled = true;
			
			if (scheduledTick - Scheduler.this.currentTick > 0) //still running
				Scheduler.this.schedule.remove(channel);
			else
				System.err.println("WARNING: Cancelling already completed action"); //TODO debug channel
		}
		
		public long ticksLeft() {
			return canceled ? -1 : scheduledTick - Scheduler.this.currentTick;
		}

		@Override
		public String toString() {
			return "ScheduledTask[channel=" + channel + ", scheduledTick=" + scheduledTick + ", ticksLeft=" + ticksLeft() + ", canceled=" + canceled + "]";
		}
		
	}
	
	private class BlankScheduledTask extends ScheduledTask {
		
		public BlankScheduledTask(TimerChannel channel) {
			super(channel, ()->{}, -1);
		}
		
		@Override public void run() {}
		@Override public void cancel() {}
		
		@Override
		public long ticksLeft() {
			return -1;
		}
		
		@Override
		public String toString() {
			return "BlankScheduledTask";
		}
		
	}
	
	/*@Deprecated
	public long currentTick() {
		return ticks;
	}*/

}
