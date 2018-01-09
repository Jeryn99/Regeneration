package com.afg.regeneration.traits.negative;

import java.util.UUID;

import com.afg.regeneration.traits.positive.TraitLucky;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class TraitUnlucky extends AbilityAttributeModifier implements TraitINegativeTrait {
	public TraitUnlucky(EntityPlayer player, UUID uuid, float factor, int operation) {
		super(player, uuid, factor, operation);
	}
	
	@Override
	public IAttribute getAttribute() {
		return SharedMonsterAttributes.LUCK;
	}
	
	@Override
	public Class<? extends Ability> getPositiveTrait() {
		return TraitLucky.class;
	}
}
