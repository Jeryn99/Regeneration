package me.sub.common.states;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public enum EnumRegenType {
    FIERY(new TypeFiery());

    private final IRegenType type;

    EnumRegenType(IRegenType type) {
        this.type = type;
    }

    public IRegenType getType() {
        return type;
    }
}
