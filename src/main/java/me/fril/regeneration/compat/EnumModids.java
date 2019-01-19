package me.fril.regeneration.compat;

public enum EnumModids {
	TARDIS("tardis"), LCCORE("lucraftcore");
	
	private final String modid;
	
	EnumModids(String modid) {
		this.modid = modid;
	}
	
	public String getModid() {
		return modid;
	}
}
