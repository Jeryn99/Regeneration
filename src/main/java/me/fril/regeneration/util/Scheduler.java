package me.fril.regeneration.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {
	
	private long ticks = 0; //this could theoretically overflow if you left the game running for >10 billion years
	private Map<Long, List<ScheduledTask>> schedule = new HashMap<>();
	
	public void tick() {
		ticks++;
		
		if (schedule.containsKey(ticks)) {
			schedule.get(ticks).forEach(r->r.run());
			schedule.remove(ticks);
		}
	}
	
	public ScheduledTask schedule(int inSeconds, Runnable callback) {
		long scheduledTick = ticks + (inSeconds * 20);
		
		List<ScheduledTask> list = schedule.containsKey(scheduledTick) ? schedule.get(scheduledTick) : new ArrayList<>();
		ScheduledTask task = new ScheduledTask(callback, scheduledTick);
		list.add(task);
		schedule.put(scheduledTick, list);
		
		return task;
	}
	
	
	
	public class ScheduledTask implements Runnable {
		private final Runnable callback;
		private final long scheduledTick;
		private boolean canceled; //mostly for debug purposes
		
		private ScheduledTask(Runnable callback, long scheduledTick) {
			this.callback = callback;
			this.scheduledTick = scheduledTick;
		}
		
		@Override
		public void run() {
			callback.run();
		}
		
		public void cancel() {
			if (canceled)
				System.err.println("WARNING: Cancelling already canceled action");
			canceled = true;
			
			Scheduler.this.schedule.get(scheduledTick).remove(this);
		}
		
	}
	
}
