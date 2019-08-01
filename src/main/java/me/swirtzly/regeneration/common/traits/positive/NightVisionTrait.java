package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

public class NightVisionTrait extends TraitManager.IDna {

    public NightVisionTrait() {
        super("nightvision");
    }

    @Override
    public void onUpdate(IRegen cap) {
        PlayerEntity player = cap.getPlayer();
        PlayerUtil.applyPotionIfAbsent(player, Effects.NIGHT_VISION, 1200, 2, true, false);
    }

    @Override
    public void onAdded(IRegen cap) {

    }

    @Override
    public void onRemoved(IRegen cap) {
        PlayerEntity player = cap.getPlayer();
        player.removePotionEffect(Effects.NIGHT_VISION);
    }

}
