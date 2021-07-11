package me.suff.mc.regen.common.traits.negative;

import me.suff.mc.regen.common.capability.IRegeneration;
import me.suff.mc.regen.common.traits.DnaHandler;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;

public class DnaHunger extends DnaHandler.IDna {

    public DnaHunger() {
        super("hunger");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (player.ticksExisted % 2400 == 0 && player.world.rand.nextBoolean()) {
            PlayerUtil.applyPotionIfAbsent(player, MobEffects.HUNGER, 200, 1, true, false);
        }
    }

    @Override
    public void onAdded(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        PlayerUtil.applyPotionIfAbsent(player, MobEffects.HUNGER, 200, 1, true, false);
    }

    @Override
    public void onRemoved(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        player.removePotionEffect(MobEffects.HUNGER);
    }
}
