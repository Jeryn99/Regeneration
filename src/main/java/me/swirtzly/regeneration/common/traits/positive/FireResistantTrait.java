package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.util.common.PlayerUtil;
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
