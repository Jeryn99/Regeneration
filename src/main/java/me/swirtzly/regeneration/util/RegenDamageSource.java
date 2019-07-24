package me.swirtzly.regeneration.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RegenDamageSource extends DamageSource {
	
	private String message;
	
	public RegenDamageSource(String name) {
		super(name);
		this.message = "regeneration.damagesrc." + name;
	}
	
	@Override
	public ITextComponent getDeathMessage(LivingEntity entity) {
		return new TranslationTextComponent(message, entity.getName());
	}
}
