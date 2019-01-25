package me.fril.regeneration.common.dna.negative;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.dna.DnaHandler;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Suffril
 * on 25/01/2019.
 */
public class DnaHydrophobic implements DnaHandler.IDna {
	
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "hydrophobic");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.isInWater() || player.world.isRaining()) {
			PlayerUtil.applyPotionIfAbsent(player, MobEffects.NAUSEA, 300, 0, false, false);
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
