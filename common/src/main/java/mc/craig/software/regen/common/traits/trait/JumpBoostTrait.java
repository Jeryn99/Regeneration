package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.util.PlayerUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class JumpBoostTrait extends TraitBase {

    @Override
    public int getPotionColor() {
        return Color.CYAN.getRGB();
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
        PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.JUMP, 200, 0, false, false);
    }
}
