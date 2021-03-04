package me.swirtzly.regen.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RegenSources extends DamageSource {

    public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenSources("regen_energy").setDamageBypassesArmor(),
            REGEN_DMG_HEALING = new RegenSources("regen_heal").setDamageAllowedInCreativeMode().setDamageBypassesArmor(), // The irony lmao
            REGEN_DMG_CRITICAL = new RegenSources("regen_crit").setDamageAllowedInCreativeMode().setDamageBypassesArmor(),
            REGEN_DMG_KILLED = new RegenSources("regen_killed").setDamageAllowedInCreativeMode().setDamageBypassesArmor(),
            REGEN_DMG_FORCED = new RegenSources("forced").setDamageAllowedInCreativeMode().setDamageBypassesArmor(),
            REGEN_DMG_RIFLE = new RegenSources("rifle").setDamageAllowedInCreativeMode().setDamageBypassesArmor(),
            REGEN_DMG_STASER = new RegenSources("staser");


    private String message = "";

    public RegenSources(String damageTypeIn) {
        super(damageTypeIn);
        message = damageTypeIn;
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        return new TranslationTextComponent("regen.source." + message, entityLivingBaseIn.getName());
    }
}
