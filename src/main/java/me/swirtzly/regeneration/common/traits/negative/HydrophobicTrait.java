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
		
		if (player.isInWater() || player.world.isRaining() && player.world.canBlockSeeSky(new BlockPos(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ))) {
            PlayerUtil.applyPotionIfAbsent(player, Effects.NAUSEA, 300, 2, false, false);
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
