package me.suff.mc.regen.common.types;

import me.suff.mc.regen.util.FileUtil;

import java.util.HashMap;

public class TypeHandler {

    private static HashMap<RegenType, IRegenType> TYPES = new HashMap<>();

    public static void init() {
        TYPES.put(RegenType.FIERY, new TypeFiery());
        TYPES.put(RegenType.LAY_FADE, new TypeLayFade());
    }

    public static IRegenType getTypeInstance(RegenType type) {
        if (TYPES.containsKey(type)) {
            return TYPES.get(type);
        }
        return TYPES.get(RegenType.FIERY);
    }

    public enum RegenType implements FileUtil.IEnum {
        FIERY, LAY_FADE
    }
}
