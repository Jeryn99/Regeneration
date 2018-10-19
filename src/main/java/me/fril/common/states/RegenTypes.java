package me.fril.common.states;

import java.util.HashMap;

/**
 * Created by Sub
 * on 25/09/2018.
 */
public class RegenTypes {

    public static IRegenType FIERY, LAYDOWN;
    private static HashMap<String, IRegenType> TYPES = new HashMap<>();

    public static void init() {
        FIERY = registerType(new TypeFiery());
        LAYDOWN = registerType(new TypeLayFade());
    }

    private static IRegenType registerType(IRegenType type) {
        TYPES.put(type.getName(), type);
        return type;
    }

    public static IRegenType getTypeByName(String name) {
        if (TYPES.containsKey(name)) {
            return TYPES.get(name);
        }
        return FIERY;
    }

}
