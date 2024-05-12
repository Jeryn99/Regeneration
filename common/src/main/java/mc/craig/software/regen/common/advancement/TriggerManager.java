package mc.craig.software.regen.common.advancement;

import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.advancements.CriteriaTriggers;

public class TriggerManager {

    public static final BaseTrigger FIRST_REGENERATION = new BaseTrigger(RConstants.MODID, "first_regen");
    public static final BaseTrigger CHANGE_REFUSAL = new BaseTrigger(RConstants.MODID, "change_refusal");
    public static final BaseTrigger CRITICAL = new BaseTrigger(RConstants.MODID, "critical_period");
    public static final BaseTrigger TIMELORD_TRADE = new BaseTrigger(RConstants.MODID, "timelord_trade");
    public static final BaseTrigger HAND_CUT = new BaseTrigger(RConstants.MODID, "hand_cut");
    public static final BaseTrigger ZERO_ROOM = new BaseTrigger(RConstants.MODID, "zero_room");
    public static final BaseTrigger COUNCIL = new BaseTrigger(RConstants.MODID, "council");

    public static void init() {
        CriteriaTriggers.register("first_regeneration", FIRST_REGENERATION);
        CriteriaTriggers.register("change_refusal", CHANGE_REFUSAL);
        CriteriaTriggers.register("critical", CRITICAL);
        CriteriaTriggers.register("timelord_trade", TIMELORD_TRADE);
        CriteriaTriggers.register("hand_cut", HAND_CUT);
        CriteriaTriggers.register("zero_room", ZERO_ROOM);
        CriteriaTriggers.register("council", COUNCIL);
    }

}