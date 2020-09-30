package me.swirtzly.regen.util;

import net.minecraft.util.ResourceLocation;

public class RConstants {

    //Internal
    public static final String MODID = "regen";

    //NBT
    public static final String STATE_MANAGER = "state_manager";
    public static final String TRANSITION_TYPE = "transition_type";
    public static final String SKIN = "skin";
    public static String REGENS_LEFT = "regens_left";
    public static String CURRENT_STATE = "current_state";
    public static String PRIMARY_RED = "p_red";
    public static String PRIMARY_GREEN = "p_green";
    public static String PRIMARY_BLUE = "p_blue";
    public static String SECONDARY_RED = "s_red";
    public static String SECONDARY_GREEN = "s_green";
    public static String SECONDARY_BLUE = "s_blue";
    public static String STYLE = "style";
    public static String ANIMATION_TICKS = "ticks_animating";


    //ResourceLocations
    public static final ResourceLocation CAP_REGEN_ID = new ResourceLocation(RConstants.MODID, RConstants.MODID);

}
