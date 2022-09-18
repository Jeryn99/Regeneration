package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class SlowFallingTrait extends TraitBase{
    @Override
    public int getPotionColor() {
        return Color.YELLOW.getRGB();
    }

    @Override
    public void onAdded(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void onRemoved(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void tick(LivingEntity livingEntity, IRegen data) {
        if(livingEntity.fallDistance > 1) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
        }
    }
}
