package com.afg.regeneration.traits.positive;

import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by AFlyingGrayson on 9/3/17
 */
public class Sneaky extends AbilityConstant
{
	public Sneaky(EntityPlayer player)
	{
		super(player);
	}

	@Override public void updateTick() {}

	@SubscribeEvent
	public static void onVisibilityCalc(PlayerEvent.Visibility event){
		List<Ability> abilityList = SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()).getAbilities();

		for (Ability ability : abilityList)
		{
			if(ability instanceof Sneaky && ability.isUnlocked()){
				event.modifyVisibility(0.5);
			}
		}
	}
}

