package mc.craig.software.regen.util;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class RegenSources extends DamageSource {

    public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenSources("regeneration_blast").bypassArmor(),
            REGEN_DMG_CRITICAL = new RegenSources("critical_period").bypassInvul().bypassArmor(),
            REGEN_DMG_KILLED = new RegenSources("mid_regeneration").bypassInvul().bypassArmor(),
            REGEN_DMG_FORCED = new RegenSources("forced_regeneration").bypassInvul().bypassArmor(),
            REGEN_DMG_RIFLE = new RegenSources("rifle_shot").bypassArmor(),
            REGEN_DMG_HAND = new RegenSources("severed_arm").bypassInvul().bypassArmor(),
            REGEN_DMG_STASER = new RegenSources("staser_shot");

    public RegenSources(Holder<DamageType> type) {
        super(type);
    }

    @Override
    public DamageSource bypassArmor() {
        return super.bypassArmor();
    }


}
