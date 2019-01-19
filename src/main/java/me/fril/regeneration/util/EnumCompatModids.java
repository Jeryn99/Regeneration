package me.fril.regeneration.util;

public enum EnumCompatModids {
	TARDIS("tardis"), LCCORE("lucraftcore");
	
	private final String modid;
	
	EnumCompatModids(String modid) {
		this.modid = modid;
	}
	
	public String getModid() {
		return modid;
	}
}
