package me.suff.mc.regen.util.schedule;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.regen.state.RegenStates;
import net.minecraft.world.entity.LivingEntity;

public class RegenScheduledAction extends ScheduledAction {
    public final RegenStates.Transition transition;
    private final LivingEntity player;

    public RegenScheduledAction(RegenStates.Transition transition, LivingEntity player, Runnable callback, long inTicks) {
        super(callback, inTicks);
        this.transition = transition;
        this.player = player;
    }

    @Override
    public boolean tick() {
        if (scheduledTick == -1)
            Regeneration.LOG.warn(player.getName() + ": Ticking finished/canceled ScheduledAction (" + transition + ")");

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

    public RegenStates.Transition getTransition() {
        return transition;
    }

}
