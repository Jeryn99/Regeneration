package mc.craig.software.regen.common.regen.state;

public enum RegenStates {

    ALIVE, GRACE, GRACE_CRIT, POST, REGENERATING;

    public boolean isGraceful() {
        return this == GRACE || this == GRACE_CRIT;
    }

    public enum Transition {
        HAND_GLOW_START, HAND_GLOW_TRIGGER, ENTER_CRITICAL, CRITICAL_DEATH, FINISH_REGENERATION, END_POST
    }

}
