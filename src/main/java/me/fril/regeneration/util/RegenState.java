package me.fril.regeneration.util;

public enum RegenState {
	
	ALIVE,
	GRACE, GRACE_CRIT,
	REGENERATING;

	public boolean isGraceful() {
		return this == GRACE || this == GRACE_CRIT;
	}
	
}
