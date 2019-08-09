package me.swirtzly.regeneration.util;

public class ScheduledAction {

    private final Runnable callback;
    // These are separate fields instead of a single countdown so we can have a progress indication
    protected long currentTick, scheduledTick;

    public ScheduledAction(Runnable callback, long inTicks) {
        if (inTicks < 0)
            throw new IllegalArgumentException("Can't schedule an action in the past (would be in " + inTicks + ")");

        this.callback = callback;
        this.scheduledTick = inTicks;
    }

    /**
     * @return If the callback was executed
     */
    public boolean tick() {
        if (scheduledTick == -1)
            return false;

        if (currentTick == scheduledTick) {
            callback.run();
            scheduledTick = -1;
            return true;
        } else if (currentTick > scheduledTick) {
            throw new IllegalStateException("Task wasn't executed at " + scheduledTick + ", but we're on " + currentTick);
        } else {
            currentTick++;
            return false;
        }
    }

    public void cancel() {
        scheduledTick = -1;
    }

    @Deprecated
    /** @deprecated Use with caution, tick timings can be changed! */
    public long getTicksLeft() {
        if (scheduledTick == -1)
            return -1;
        else
            return scheduledTick - currentTick;
    }

    public double getProgress() {
        return currentTick / (double) scheduledTick;
    }

}
