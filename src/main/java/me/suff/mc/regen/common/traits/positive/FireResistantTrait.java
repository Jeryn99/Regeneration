package me.suff.mc.regen.common.traits.positive;

import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.traits.TraitManager;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;

public class FireResistantTrait extends TraitManager.IDna {

    public FireResistantTrait() {
        super("fire");
    }

    @Override
    public void onUpdate(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();
        if (player.isOnFire()) {
            PlayerUtil.applyPotionIfAbsent(player, Effects.FIRE_RESISTANCE, 1200, 2, true, false);
        }
    }

    @Override
    public void onAdded(IRegen cap) {

    }

    @Override
    public void onRemoved(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();
        player.removeEffect(Effects.FIRE_RESISTANCE);
    }

}
