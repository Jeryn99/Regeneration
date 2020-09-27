package me.swirtzly.regen.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RegenSources extends DamageSource {

    public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenSources("regen_energy"),
            REGEN_DMG_HEALING = new RegenSources("regen_heal").setDamageAllowedInCreativeMode(), // The irony lmao
            REGEN_DMG_CRITICAL = new RegenSources("regen_crit").setDamageAllowedInCreativeMode(),
            REGEN_DMG_KILLED = new RegenSources("regen_killed"),
            REGEN_DMG_FORCED = new RegenSources("forced").setDamageAllowedInCreativeMode();



    private String message = "";
    public RegenSources(String damageTypeIn) {
        super(damageTypeIn);
        message = damageTypeIn;
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        return new TranslationTextComponent("source.regen."+message, entityLivingBaseIn.getName());
    }
}
