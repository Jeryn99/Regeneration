package me.suff.regeneration.common.traits.positive;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.traits.DnaHandler;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaSimple implements DnaHandler.IDna {
	
	private ResourceLocation location;
	
	public DnaSimple(ResourceLocation location) {
		this.location = location;
	}
	
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
		return "traits." + location.getPath() + ".name";
	}
	
	
	@Override
	public ResourceLocation getRegistryName() {
		return location;
	}
}
