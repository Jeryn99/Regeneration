package craig.software.mc.regen.common.entities.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class FixedMeleeGoal extends MeleeAttackGoal {

    public FixedMeleeGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_) {
        super(p_25552_, p_25553_, p_25554_);
    }

    @Override
    public void tick() {
        if (mob == null || this.mob.getTarget() == null) return;
        super.tick();
    }
}
