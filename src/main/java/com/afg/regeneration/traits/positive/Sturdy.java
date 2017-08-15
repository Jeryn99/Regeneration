package com.afg.regeneration.traits.positive;

import lucraft.mods.lucraftcore.abilities.AbilityAttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by AFlyingGrayson on 8/10/17
 */
public class Sturdy extends AbilityAttributeModifier
{
	public Sturdy(EntityPlayer player, UUID uuid, float factor, int operation)
	{
		super(player, uuid, factor, operation);
	}

	@Override public IAttribute getAttribute()
	{
		return SharedMonsterAttributes.KNOCKBACK_RESISTANCE;
	}
}
