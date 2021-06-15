package me.suff.mc.regen.common.traits.negative;

import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.traits.TraitManager;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;

public class HungerTrait extends TraitManager.IDna {

    public HungerTrait() {
        super("hunger");
    }

    @Override
    public void onUpdate(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();
        if (player.tickCount % 2400 == 0 && player.level.random.nextBoolean()) {
            PlayerUtil.applyPotionIfAbsent(player, Effects.HUNGER, 200, 1, true, false);
        }
    }

    @Override
    public void onAdded(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();
        PlayerUtil.applyPotionIfAbsent(player, Effects.HUNGER, 200, 1, true, false);
    }

    @Override
    public void onRemoved(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();
        player.removeEffect(Effects.HUNGER);
    }
}
