package com.afg.regeneration.traits.negative;

import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by AFlyingGrayson on 9/3/17
 */
@Mod.EventBusSubscriber
public class Dumb extends AbilityConstant
{
	public Dumb(EntityPlayer player)
	{
		super(player);
	}

	@Override public void updateTick()
	{

	}

	@SubscribeEvent
	public static void onExperienceGain(PlayerPickupXpEvent event){
		List<Ability> abilityList = SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()).getAbilities();

		for (Ability ability : abilityList)
		{
			if(ability instanceof Dumb && ability.isUnlocked()){
				event.getOrb().xpValue *= 0.5;
			}
		}
	}
}