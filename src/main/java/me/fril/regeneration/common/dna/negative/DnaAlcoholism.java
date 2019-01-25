package me.fril.regeneration.common.dna.negative;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.dna.DnaHandler;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaAlcoholism implements DnaHandler.IDna {
	
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "alcoholism");
	
	@Override
	public void onUpdate(IRegeneration cap) {
	
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
