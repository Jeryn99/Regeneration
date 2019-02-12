package me.suff.regeneration.util;

import net.minecraftforge.fml.ModList;

public enum EnumCompatModids {
	TARDIS("tardis"), LCCORE("lucraftcore");
	
	private final String modid;
	
	EnumCompatModids(String modid) {
		this.modid = modid;
	}
	
	public String getModid() {
		return modid;
	}
	
	public boolean isLoaded() {
		return ModList.get().isLoaded(getModid());
	}
}
