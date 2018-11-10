package me.fril.regeneration.util;

public enum RegenState {
	
	ALIVE,
	GRACE_STD, GRACE_GLOWING, GRACE_CRIT,
	REGENERATING;
	
	public boolean isGraceful() {
		return toString().startsWith("GRACE_");
	}
	
}
