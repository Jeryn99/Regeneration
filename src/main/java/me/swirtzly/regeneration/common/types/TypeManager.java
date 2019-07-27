package me.swirtzly.regeneration.common.types;

import java.util.HashMap;

public class TypeManager {

    private static HashMap<Type, RegenType> TYPES = new HashMap<>();

    public static void init() {
        TYPES.put(Type.FIERY, new FieryType());
        TYPES.put(Type.CONFUSED, new ElixirType());
    }

    public static RegenType getTypeInstance(TypeManager.Type type) {
        if (TYPES.containsKey(type)) {
            return TYPES.get(type);
        }
        return TYPES.get(Type.FIERY);
    }


    public enum Type {
        FIERY, CONFUSED
    }
}
