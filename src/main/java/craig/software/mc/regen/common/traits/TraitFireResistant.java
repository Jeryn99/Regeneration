package craig.software.mc.regen.common.traits;

import craig.software.mc.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;

public class TraitFireResistant extends AbstractTrait {
    @Override
    public void apply(IRegen data) {

    }

    @Override
    public void remove(IRegen data) {

    }

    @Override
    public void tick(IRegen data) {
        LivingEntity living = data.getLiving();
        if (living.isOnFire()) {
            living.clearFire();
        }
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public int color() {
        return 14981690;
    }
}
