package mc.craig.software.regen.common.regeneration.state;

public enum RegenStates {

    ALIVE, GRACE, GRACE_CRIT, POST, REGENERATING;

    public boolean isGraceful() {
        return this == GRACE || this == GRACE_CRIT;
    }

    public String getId(){
        return name().toLowerCase();
    }

    public static RegenStates find(String string){
        for (RegenStates value : values()) {
            if(value.name().equalsIgnoreCase(string)){
                return value;
            }
        }
        return ALIVE;
    }
    public enum Transition {
        HAND_GLOW_START, HAND_GLOW_TRIGGER, ENTER_CRITICAL, CRITICAL_DEATH, FINISH_REGENERATION, END_POST
    }

}