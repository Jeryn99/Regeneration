package me.fril.regeneration.util;

public class ScheduledAction {
	
	protected long currentTick, scheduledTick;
	private final Runnable callback;
	
	public ScheduledAction(Runnable callback, long inTicks) {
		this.callback = callback;
		this.scheduledTick = inTicks;
	}
	
	/** @return If the callback was executed */
	public boolean tick() {
		if (scheduledTick == -1)
			return false;
		
		if (currentTick == scheduledTick) {
			callback.run();
			currentTick = -1;
			return true;
		} else if (currentTick > scheduledTick) {
			throw new IllegalStateException("Task wasn't executed at "+scheduledTick+", but we're on "+currentTick);
		} else {
			currentTick++;
			return false;
		}
	}
	
	public void cancel() {
		scheduledTick = -1;
	}
	
	public long getTicksLeft() {
		if (scheduledTick == -1)
			return -1;
		else
			return scheduledTick - currentTick;
	}
	
}
