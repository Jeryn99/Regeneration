package com.lcm.regeneration.traits.negative;

import java.util.UUID;

import com.lcm.regeneration.traits.positive.TraitBouncy;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAttributeModifier;
import lucraft.mods.lucraftcore.util.attributes.LCAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public class TraitRigid extends AbilityAttributeModifier implements INegativeTrait {
	
	public TraitRigid(EntityPlayer player, UUID uuid, float factor, int operation) {
		super(player, uuid, factor, operation);
	}
	
	@Override
	public IAttribute getAttribute() {
		return LCAttributes.FALL_RESISTANCE;
	}
	
	@Override
	public Class<? extends Ability> getPositiveTrait() {
		return TraitBouncy.class;
	}
}
