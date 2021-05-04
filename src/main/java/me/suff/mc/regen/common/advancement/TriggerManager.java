package me.suff.mc.regen.common.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;

public class TriggerManager {

    public static final BaseTrigger FIRST_REGENERATION = new BaseTrigger("first_regen");
    public static final BaseTrigger CHANGE_REFUSAL = new BaseTrigger("change_refusal");//698 88
    public static final BaseTrigger CRITICAL = new BaseTrigger("critical_period");
    public static final BaseTrigger TIMELORD_TRADE = new BaseTrigger("timelord_trade");
    public static final BaseTrigger HAND_CUT = new BaseTrigger("hand_cut");
    public static final BaseTrigger ZERO_ROOM = new BaseTrigger("zero_room");
    public static final BaseTrigger COUNCIL = new BaseTrigger("council");

    public static void init() {
        CriteriaTriggers.register(FIRST_REGENERATION);
        CriteriaTriggers.register(CHANGE_REFUSAL);
        CriteriaTriggers.register(CRITICAL);
        CriteriaTriggers.register(TIMELORD_TRADE);
        CriteriaTriggers.register(HAND_CUT);
        CriteriaTriggers.register(ZERO_ROOM);
        CriteriaTriggers.register(COUNCIL);
    }

}