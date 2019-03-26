package me.suff.regeneration.common.traits.positive;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.traits.DnaHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class DnaFireResistant implements DnaHandler.IDna {
	
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "fire");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.isBurning() && cap.isDnaActive()) {
			player.extinguish();
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
		return "traits." + ID.getPath() + ".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return ID;
	}
}
