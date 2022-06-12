package me.suff.mc.regen.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class RegenSources extends DamageSource {

    public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenSources("regen_energy").bypassArmor(),
            REGEN_DMG_HEALING = new RegenSources("regen_heal").bypassInvul().bypassArmor(), // The irony lmao
            REGEN_DMG_CRITICAL = new RegenSources("regen_crit").bypassInvul().bypassArmor(),
            REGEN_DMG_KILLED = new RegenSources("regen_killed").bypassInvul().bypassArmor(),
            REGEN_DMG_FORCED = new RegenSources("forced").bypassInvul().bypassArmor(),
            REGEN_DMG_RIFLE = new RegenSources("rifle").bypassArmor(),
            REGEN_DMG_HAND = new RegenSources("hand_cut").bypassInvul().bypassArmor(),
            REGEN_DMG_STASER = new RegenSources("staser");


    private String message = "";

    public RegenSources(String damageTypeIn) {
        super(damageTypeIn);
        message = damageTypeIn;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        return Component.translatable("regen.source." + message, entityLivingBaseIn.getName());
    }
}
