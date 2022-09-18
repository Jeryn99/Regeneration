package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;

public class BlankTrait extends TraitBase {

    private final int color;

    public BlankTrait(int color) {
        this.color = color;
    }

    @Override
    public int getPotionColor() {
        return color;
    }

    @Override
    public void onAdded(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void onRemoved(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void tick(LivingEntity livingEntity, IRegen data) {

    }
}
