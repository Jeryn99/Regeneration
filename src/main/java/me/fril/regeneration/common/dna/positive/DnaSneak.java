package me.fril.regeneration.common.dna.positive;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.dna.DnaHandler;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaSneak implements DnaHandler.IDna {
	
	private ResourceLocation LOCATION = new ResourceLocation(RegenerationMod.MODID, "sneaky");
	
	@Override
	public void onUpdate(IRegeneration cap) {
	
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
		cap.getPlayer().setSilent(true);
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
		cap.getPlayer().setSilent(false);
	}
	
	@Override
	public String getLangKey() {
		return "dna." + LOCATION.getPath() + ".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return LOCATION;
	}
}
