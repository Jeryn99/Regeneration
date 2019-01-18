package me.fril.regeneration.debugger.util;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.util.RegenState.Transition;
import me.fril.regeneration.util.ScheduledAction;
import net.minecraft.entity.player.EntityPlayer;

public class DebuggableScheduledAction extends ScheduledAction {
	public final Transition transition;
	private final EntityPlayer player;
	
	public DebuggableScheduledAction(Transition transition, EntityPlayer player, Runnable callback, long inTicks) {
		super(callback, inTicks);
		this.transition = transition;
		this.player = player;
		
		RegenerationMod.DEBUGGER.getChannelFor(player).notifySchedule(transition, inTicks);
	}
	
	@Override
	public boolean tick() {
		if (scheduledTick == -1)
			RegenerationMod.DEBUGGER.getChannelFor(player).warn("Ticking finsished/canceled ScheduledAction (" + transition + ")");
		
		boolean willExecute = currentTick == scheduledTick;
		if (willExecute)
			RegenerationMod.DEBUGGER.getChannelFor(player).notifyExecution(transition, currentTick);
		
		boolean executed = super.tick();
		if (willExecute != executed)
			throw new IllegalStateException("Execution prospect wasn't true (prospect: " + willExecute + ", result: " + executed + ", cTick: " + currentTick + ", scheduledTick: " + scheduledTick);
		
		return executed;
	}
	
	@Override
	public void cancel() {
		RegenerationMod.DEBUGGER.getChannelFor(player).notifyCancel(transition, scheduledTick - currentTick);
		super.cancel();
	}
	
	@Override
	public double getProgress() {
		if (scheduledTick == -1)
			RegenerationMod.DEBUGGER.getChannelFor(player).warn("Querying progress of canceled/finished transition");
		return super.getProgress();
	}

	public Transition getTransition() {
		return transition;
	}
	
}
