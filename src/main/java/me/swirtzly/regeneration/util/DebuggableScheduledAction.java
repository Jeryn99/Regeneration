package me.swirtzly.regeneration.util;

import me.swirtzly.regeneration.Regeneration;
import net.minecraft.entity.LivingEntity;

public class DebuggableScheduledAction extends ScheduledAction {
	public final PlayerUtil.RegenState.Transition transition;
    private final LivingEntity player;

    public DebuggableScheduledAction(PlayerUtil.RegenState.Transition transition, LivingEntity player, Runnable callback, long inTicks) {
		super(callback, inTicks);
		this.transition = transition;
		this.player = player;
	}
	
	@Override
	public boolean tick() {
        if (scheduledTick == -1)
            Regeneration.LOG.warn(player.getName() + ": Ticking finsished/canceled ScheduledAction (" + transition + ")");
		
		boolean willExecute = currentTick == scheduledTick;
		
		boolean executed = super.tick();
        if (willExecute != executed)
            throw new IllegalStateException("Execution prospect wasn't true (prospect: " + willExecute + ", result: " + executed + ", cTick: " + currentTick + ", scheduledTick: " + scheduledTick);
		
		return executed;
	}
	
	@Override
	public void cancel() {
		super.cancel();
	}
	
	@Override
	public double getProgress() {
        if (scheduledTick == -1)
            Regeneration.LOG.warn(player.getName() + ": Querying progress of canceled/finished transition");
		return super.getProgress();
	}
	
	public PlayerUtil.RegenState.Transition getTransition() {
		return transition;
	}
	
}
