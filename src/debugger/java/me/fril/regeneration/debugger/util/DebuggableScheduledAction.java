package me.fril.regeneration.debugger.util;

import me.fril.regeneration.Regeneration;
import me.fril.regeneration.util.RegenState.Transition;
import me.fril.regeneration.util.ScheduledAction;
import net.minecraft.entity.player.EntityPlayer;

public class DebuggableScheduledAction extends ScheduledAction {
	public final Transition action;
	private final EntityPlayer player;
	
	public DebuggableScheduledAction(Transition action, EntityPlayer player, Runnable callback, long inTicks) {
		super(callback, inTicks);
		this.action = action;
		this.player = player;

        Regeneration.DEBUGGER.getChannelFor(player).notifySchedule(action, inTicks);
	}
	
	@Override
	public boolean tick() {
		if (scheduledTick == -1)
            Regeneration.DEBUGGER.getChannelFor(player).warn("Ticking finsished/canceled ScheduledAction (" + action + ")");
		
		boolean willExecute = currentTick == scheduledTick;
		if (willExecute)
            Regeneration.DEBUGGER.getChannelFor(player).notifyExecution(action, currentTick);
		
		boolean executed = super.tick();
		if (willExecute != executed)
			throw new IllegalStateException("Execution prospect wasn't true (prospect: " + willExecute + ", result: " + executed + ", cTick: " + currentTick + ", scheduledTick: " + scheduledTick);
		
		return executed;
	}
	
	@Override
	public void cancel() {
        Regeneration.DEBUGGER.getChannelFor(player).notifyCancel(action, scheduledTick - currentTick);
		super.cancel();
	}
	
	@Override
	public double getProgress() {
		if (scheduledTick == -1)
            Regeneration.DEBUGGER.getChannelFor(player).warn("Querying progress of canceled/finished transition");
		return super.getProgress();
	}
	
}
