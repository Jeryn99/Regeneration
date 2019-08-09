package me.swirtzly.regeneration.common.traits.negative;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Suffril
 * on 25/01/2019.
 */
public class DnaHydrophobic extends DnaHandler.IDna {


    public DnaHydrophobic() {
        super("hydrophobic");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();

        if (player.isInWater() || player.world.isRaining() && player.world.canSeeSky(new BlockPos(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ))) {
            PlayerUtil.applyPotionIfAbsent(player, MobEffects.NAUSEA, 300, 2, false, false);
            PlayerUtil.applyPotionIfAbsent(player, MobEffects.WEAKNESS, 300, 2, false, false);
        }

    }

    @Override
    public void onAdded(IRegeneration cap) {

    }

    @Override
    public void onRemoved(IRegeneration cap) {

    }

}
