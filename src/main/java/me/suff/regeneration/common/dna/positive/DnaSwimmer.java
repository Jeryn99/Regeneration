package me.suff.regeneration.common.dna.positive;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.dna.DnaHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Suffril
 * on 25/01/2019.
 */
public class DnaSwimmer implements DnaHandler.IDna {
	
	
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "swimmer");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.isInWater()) {
			if (player.ticksExisted % 20 == 0 && cap.isDnaActive()) {
				player.setAir(player.getAir() + 1);
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
		return "dna." + getRegistryName().getPath() + ".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return ID;
	}
}
