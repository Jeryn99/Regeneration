package com.afg.regeneration.traits.negative;

import com.afg.regeneration.traits.positive.Lucky;
import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.abilities.AbilityAttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class Unlucky extends AbilityAttributeModifier implements INegativeTrait
{
	public Unlucky(EntityPlayer player, UUID uuid, float factor, int operation)
	{
		super(player, uuid, factor, operation);
	}

	@Override public IAttribute getAttribute()
	{
		return SharedMonsterAttributes.LUCK;
	}

	@Override public Class<? extends Ability> getPositiveTrait()
	{
		return Lucky.class;
	}
}
