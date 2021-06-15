package me.swirtzly.regeneration.common.traits.negative;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Suffril on 25/01/2019.
 */
public class HydrophobicTrait extends TraitManager.IDna {

    public HydrophobicTrait() {
        super("hydrophobic");
    }

    @Override
    public void onUpdate(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();

        if (player.isInWater() || player.level.isRaining() && player.level.canSeeSkyFromBelowWater(new BlockPos(player.x, player.y + (double) player.getEyeHeight(), player.z))) {
            PlayerUtil.applyPotionIfAbsent(player, Effects.CONFUSION, 300, 2, false, false);
            PlayerUtil.applyPotionIfAbsent(player, Effects.WEAKNESS, 300, 2, false, false);
        }

    }

    @Override
    public void onAdded(IRegen cap) {

    }

    @Override
    public void onRemoved(IRegen cap) {

    }

}
