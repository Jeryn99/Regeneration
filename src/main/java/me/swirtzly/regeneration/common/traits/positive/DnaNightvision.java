package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

public class DnaNightvision extends DnaHandler.IDna {

    public DnaNightvision() {
        super("nightvision");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        PlayerEntity player = cap.getPlayer();
        PlayerUtil.applyPotionIfAbsent(player, Effects.NIGHT_VISION, 1200, 2, true, false);
    }

    @Override
    public void onAdded(IRegeneration cap) {

    }

    @Override
    public void onRemoved(IRegeneration cap) {
        PlayerEntity player = cap.getPlayer();
        player.removePotionEffect(Effects.NIGHT_VISION);
    }

}
