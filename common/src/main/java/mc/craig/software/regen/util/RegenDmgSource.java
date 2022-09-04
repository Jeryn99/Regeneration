package mc.craig.software.regen.util;

import net.minecraft.world.damagesource.DamageSource;

public class RegenDmgSource extends DamageSource {

    public static DamageSource REGEN_DMG_FORCED = new RegenDmgSource("forced");
    public static RegenDmgSource REGEN_DMG_CRITICAL = new RegenDmgSource("critical");
    public static RegenDmgSource REGEN_DMG_KILLED = new RegenDmgSource("killed");

    public RegenDmgSource(String string) {
        super(string);
    }


}
