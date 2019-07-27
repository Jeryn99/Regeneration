package me.swirtzly.regeneration.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class TriggerManager {
	
	public static final BaseTrigger FIRST_REGENERATION = new BaseTrigger("first_regen");
	public static final BaseTrigger CHANGE_REFUSAL = new BaseTrigger("change_refusal");
	public static final BaseTrigger CRITICAL = new BaseTrigger("critical_period");
	public static final BaseTrigger LINDOS_VIAL = new BaseTrigger("lindos_vial");
	
	
	public static void init() {
		CriteriaTriggers.register(FIRST_REGENERATION);
		CriteriaTriggers.register(CHANGE_REFUSAL);
		CriteriaTriggers.register(CRITICAL);
		CriteriaTriggers.register(LINDOS_VIAL);
	}
	
}
