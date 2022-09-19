package mc.craig.software.regen.util;

import net.minecraft.resources.ResourceLocation;

public class RConstants {

    //Internal
    public static final String MODID = "regen";
    public static final String STEVE_SKINTYPE = "default";
    public static final String ALEX_SKINTYPE = "alex";

    //NBT
    public static final String STATE_MANAGER = "state_manager";
    public static final String TRANSITION_TYPE = "transition_type";
    public static final String SKIN = "skin";
    public static final String IS_ALEX = "is_alex";

    public static final ResourceLocation CAP_REGEN_ID = new ResourceLocation(RConstants.MODID, RConstants.MODID);
    public static final String COLORS = "colors";
    public static final String PREFERENCE = "model_pref";
    public static final String GLOWING = "glowing";
    public static final String IS_TRAIT_ACTIVE = "is_trait_active";
    public static final String CURRENT_TRAIT = "current_trait";
    public static final String NEXT_TRAIT = "next_trait";
    public static final String REGENS_LEFT = "regens_left";
    public static final String CURRENT_STATE = "current_state";
    public static final String PRIMARY_RED = "p_red";
    public static final String PRIMARY_GREEN = "p_green";
    public static final String PRIMARY_BLUE = "p_blue";
    public static final String SECONDARY_RED = "s_red";
    public static final String SECONDARY_GREEN = "s_green";
    public static final String SECONDARY_BLUE = "s_blue";
    public static final String ANIMATION_TICKS = "ticks_animating";

    public static final String FIRST_PERSON = "FIRST_PERSON";
    public static final String THIRD_PERSON_FRONT = "THIRD_PERSON_FRONT";
    public static final String SOUND_SCHEME = "sound_scheme";
    public static final String HAND_STATE = "severed_state";

    public enum SpriteSheet {
        ALIVE(0, 0), GRACE(32, 0), GRACE_CRIT(48, 0), POST(16, 0), REGENERATING(0, 0), HAND_GLOW(64, 0), MISSING_ARM(80, 0);

        private final int offsetY, offsetX;

        SpriteSheet(int texOffsetX, int texOffsetY) {
            this.offsetX = texOffsetX;
            this.offsetY = texOffsetY;
        }
        public float getYOffset() {
            return this.offsetY;
        }

        public float getUOffset() {
            return this.offsetX;
        }

    }

}
