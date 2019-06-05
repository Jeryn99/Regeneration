package me.suff.regeneration.common.traits.positive;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.traits.DnaHandler;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;

public class DnaNightvision extends DnaHandler.IDna {

    public DnaNightvision() {
        super("nightvision");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (!player.world.checkLight(player.getPosition()) || !player.world.isDaytime()) {
            PlayerUtil.applyPotionIfAbsent(player, MobEffects.NIGHT_VISION, 12, 1, true, false);
        }
    }

    @Override
    public void onAdded(IRegeneration cap) {

    }

    @Override
    public void onRemoved(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        player.removePotionEffect(MobEffects.NIGHT_VISION);
    }

}
