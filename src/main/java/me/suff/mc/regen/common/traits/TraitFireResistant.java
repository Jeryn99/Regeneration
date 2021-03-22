package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.entity.LivingEntity;

public class TraitFireResistant extends TraitRegistry.AbstractTrait {
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
