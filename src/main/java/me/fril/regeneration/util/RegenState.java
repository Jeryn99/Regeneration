package me.fril.regeneration.util;

import java.awt.Color;

public enum RegenState {
	
	ALIVE,
	GRACE, GRACE_CRIT,
	REGENERATING;//, CORRUPT;
	
	public boolean isGraceful() {
		return this == GRACE || this == GRACE_CRIT;
	}
	
	/*public boolean isCorrupt() {
		return this == CORRUPT;
	}*/
	
	public enum Transition {
		HAND_GLOW_START(Color.YELLOW.darker()), HAND_GLOW_TRIGGER(Color.ORANGE),
		ENTER_CRITICAL(Color.BLUE),
		CRITICAL_DEATH(Color.RED),
		FINISH_REGENERATION(Color.GREEN.darker());
		
		public final Color color;
		
		Transition(Color col) {
			this.color = col;
		}
	}
	
}
