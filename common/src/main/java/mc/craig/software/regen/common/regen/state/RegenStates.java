package mc.craig.software.regen.common.regen.state;

import mc.craig.software.regen.util.RConstants;

public enum RegenStates {

    ALIVE(RConstants.SpriteSheet.ALIVE), GRACE(RConstants.SpriteSheet.GRACE), GRACE_CRIT(RConstants.SpriteSheet.GRACE_CRIT), POST(RConstants.SpriteSheet.POST), REGENERATING(RConstants.SpriteSheet.REGENERATING);

    private final RConstants.SpriteSheet spriteSheet;

    RegenStates(RConstants.SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public RConstants.SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public boolean isGraceful() {
        return this == GRACE || this == GRACE_CRIT;
    }

    public enum Transition {
        HAND_GLOW_START, HAND_GLOW_TRIGGER, ENTER_CRITICAL, CRITICAL_DEATH, FINISH_REGENERATION, END_POST
    }

}
