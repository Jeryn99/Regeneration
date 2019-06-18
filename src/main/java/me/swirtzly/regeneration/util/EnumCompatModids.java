package me.swirtzly.regeneration.util;

import net.minecraftforge.fml.common.Loader;

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
		return Loader.isModLoaded(getModid());
	}
}
