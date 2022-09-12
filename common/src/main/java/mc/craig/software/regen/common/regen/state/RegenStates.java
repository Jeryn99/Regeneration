package mc.craig.software.regen.common.regen.state;

public enum RegenStates {

    ALIVE(0, 0), GRACE(32, 0), GRACE_CRIT(64, 0), POST(16, 0), REGENERATING(0, 0);

    private final int offsetY, offsetX;

    RegenStates(int texOffsetX, int texOffsetY) {
        this.offsetX = texOffsetX;
        this.offsetY = texOffsetY;
    }

    public boolean isGraceful() {
        return this == GRACE || this == GRACE_CRIT;
    }

    public float getYOffset() {
        return this.offsetY;
    }

    public float getUOffset() {
        return this.offsetX;
    }

    public enum Transition {
        HAND_GLOW_START, HAND_GLOW_TRIGGER, ENTER_CRITICAL, CRITICAL_DEATH, FINISH_REGENERATION, END_POST
    }

}
