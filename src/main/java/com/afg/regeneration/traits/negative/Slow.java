package com.afg.regeneration.traits.negative;

import com.afg.regeneration.traits.positive.Quick;
import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.abilities.AbilityAttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class Slow extends AbilityAttributeModifier implements INegativeTrait
{

	public Slow(EntityPlayer player, UUID uuid, float factor, int operation)
	{
		super(player, uuid, factor, operation);
	}

	@Override public IAttribute getAttribute()
	{
		return SharedMonsterAttributes.MOVEMENT_SPEED;
	}

	@Override public Class<? extends Ability> getPositiveTrait()
	{
		return Quick.class;
	}
}
