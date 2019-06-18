package me.swirtzly.regeneration.common.types;

import java.util.HashMap;

public class TypeHandler {

    private static HashMap<RegenType, IRegenType> TYPES = new HashMap<>();

    public static void init() {
        TYPES.put(RegenType.FIERY, new TypeFiery());
        TYPES.put(RegenType.CONFUSED, new TypeElixir());
    }

    public static IRegenType getTypeInstance(RegenType type) {
        if (TYPES.containsKey(type)) {
            return TYPES.get(type);
        }
        return TYPES.get(RegenType.FIERY);
    }


    public enum RegenType {
        FIERY, CONFUSED
    }
}
