package me.fril.regeneration.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class RegenTriggers {
	
	public static final CustomTrigger FIRST_REGENERATION = new CustomTrigger("first_regen");
	public static final CustomTrigger CHANGE_REFUSAL = new CustomTrigger("change_refusal");
	
	
	public static void init() {
		CriteriaTriggers.register(FIRST_REGENERATION);
		CriteriaTriggers.register(CHANGE_REFUSAL);
	}
	
}
