package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;

public class WaterBreathingTrait extends TraitBase{
    @Override
    public int getPotionColor() {
        return 1950417;
    }

    @Override
    public void onAdded(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void onRemoved(LivingEntity livingEntity, IRegen data) {

    }

    @Override //TODO Add Tick to Regeneration Data
    public void tick(LivingEntity livingEntity, IRegen data) {
        LivingEntity living = data.getLiving();
        if (living.isInWater()) {
            living.setAirSupply(300);
        }
    }
}
