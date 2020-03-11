package me.swirtzly.regeneration.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class RegenTriggers {

    public static final CustomTrigger FIRST_REGENERATION = new CustomTrigger("first_regen");
    public static final CustomTrigger CHANGE_REFUSAL = new CustomTrigger("change_refusal");
    public static final CustomTrigger CRITICAL = new CustomTrigger("critical_period");
    public static final CustomTrigger LINDOS_VIAL = new CustomTrigger("lindos_vial");
    public static final CustomTrigger HAND = new CustomTrigger("hand");
    public static final CustomTrigger HAND_JAR_FIRST = new CustomTrigger("hand_jar");

    public static void init() {
        CriteriaTriggers.register(FIRST_REGENERATION);
        CriteriaTriggers.register(CHANGE_REFUSAL);
        CriteriaTriggers.register(CRITICAL);
        CriteriaTriggers.register(LINDOS_VIAL);
        CriteriaTriggers.register(HAND);
        CriteriaTriggers.register(HAND_JAR_FIRST);
    }
	
}
