package com.afg.regeneration.traits.negative;

import com.afg.regeneration.traits.positive.Spry;
import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.abilities.AbilityAttributeModifier;
import lucraft.mods.lucraftcore.attributes.LCAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by AFlyingGrayson on 8/10/17
 */
public class Clumsy extends AbilityAttributeModifier implements INegativeTrait
{

	public Clumsy(EntityPlayer player, UUID uuid, float factor, int operation)
	{
		super(player, uuid, factor, operation);
	}

	@Override public IAttribute getAttribute()
	{
		return LCAttributes.JUMP_HEIGHT;
	}

	@Override public Class<? extends Ability> getPositiveTrait()
	{
		return Spry.class;
	}
}
