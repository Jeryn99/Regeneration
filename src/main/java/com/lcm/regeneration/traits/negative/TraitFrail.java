package com.lcm.regeneration.traits.negative;

import java.util.UUID;

import com.lcm.regeneration.traits.positive.TraitThickSkinned;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by AFlyingGrayson on 8/10/17
 */
public class TraitFrail extends AbilityAttributeModifier implements INegativeTrait {
	
	public TraitFrail(EntityPlayer player, UUID uuid, float factor, int operation) {
		super(player, uuid, factor, operation);
	}
	
	@Override
	public IAttribute getAttribute() {
		return SharedMonsterAttributes.ARMOR;
	}
	
	@Override
	public Class<? extends Ability> getPositiveTrait() {
		return TraitThickSkinned.class;
	}
}
