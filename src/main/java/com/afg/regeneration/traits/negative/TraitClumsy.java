package com.afg.regeneration.traits.negative;

import java.util.UUID;

import com.afg.regeneration.traits.positive.TraitSpry;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAttributeModifier;
import lucraft.mods.lucraftcore.util.attributes.LCAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by AFlyingGrayson on 8/10/17
 */
public class TraitClumsy extends AbilityAttributeModifier implements TraitINegativeTrait {
	
	public TraitClumsy(EntityPlayer player, UUID uuid, float factor, int operation) {
		super(player, uuid, factor, operation);
	}
	
	@Override
	public IAttribute getAttribute() {
		return LCAttributes.JUMP_HEIGHT;
	}
	
	@Override
	public Class<? extends Ability> getPositiveTrait() {
		return TraitSpry.class;
	}
}
