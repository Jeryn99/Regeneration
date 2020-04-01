package me.swirtzly.regeneration.common.traits.negative;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

public class HungerTrait extends TraitManager.IDna {

    public HungerTrait() {
        super("hunger");
    }

    @Override
    public void onUpdate(IRegen cap) {
        PlayerEntity player = cap.getPlayer();
        if (player.ticksExisted % 2400 == 0 && player.world.rand.nextBoolean()) {
            PlayerUtil.applyPotionIfAbsent(player, Effects.HUNGER, 200, 1, true, false);
        }
    }

    @Override
    public void onAdded(IRegen cap) {
        PlayerEntity player = cap.getPlayer();
        PlayerUtil.applyPotionIfAbsent(player, Effects.HUNGER, 200, 1, true, false);
    }

    @Override
    public void onRemoved(IRegen cap) {
        PlayerEntity player = cap.getPlayer();
        player.removePotionEffect(Effects.HUNGER);
    }
}
