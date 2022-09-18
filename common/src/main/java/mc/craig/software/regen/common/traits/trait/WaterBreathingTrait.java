package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

    @Override
    public void tick(LivingEntity livingEntity, IRegen data) {
        LivingEntity living = data.getLiving();
        if (!living.isEyeInFluid(FluidTags.WATER)) {
            PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.WATER_BREATHING, 200, 0, false, false);
        }
    }
}
