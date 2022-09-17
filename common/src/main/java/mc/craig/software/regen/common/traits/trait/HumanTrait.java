package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;

public class HumanTrait extends TraitBase{
    @Override
    public int getPotionColor() {
        return 0;
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
