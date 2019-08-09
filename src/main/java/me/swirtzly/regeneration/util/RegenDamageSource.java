package me.swirtzly.regeneration.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class RegenDamageSource extends DamageSource {

    private String message;

    public RegenDamageSource(String name) {
        super(name);
        this.message = "regeneration.damagesrc." + name;
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        return new TextComponentTranslation(message, entity.getName());
    }
}
