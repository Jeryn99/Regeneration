package me.suff.regeneration.common.dna.negative;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.dna.DnaHandler;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Suffril
 * on 25/01/2019.
 */
public class DnaHydrophobic implements DnaHandler.IDna {
	
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "hydrophobic");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		
		if (player.world.canSeeSky(new BlockPos(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ))) {
			if (player.isInWater() || player.world.isRaining()) {
				PlayerUtil.applyPotionIfAbsent(player, MobEffects.NAUSEA, 300, 2, false, false);
				PlayerUtil.applyPotionIfAbsent(player, MobEffects.WEAKNESS, 300, 2, false, false);
			}
		}
		
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
		
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
		
	}
	
	@Override
	public String getLangKey() {
		return "dna." + ID.getPath() + ".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return ID;
	}
}
