package me.fril.regeneration.util;

public enum RegenState { //NOW move to a simpeler, 4 state (3 channel) system
	
	ALIVE,
	GRACE_STD, GRACE_GLOWING, GRACE_CRIT,
	REGENERATING;
	
	public boolean isGraceful() {
		return toString().startsWith("GRACE_");
	}
	
}
