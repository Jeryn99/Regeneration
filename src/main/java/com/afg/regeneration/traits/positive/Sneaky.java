package com.afg.regeneration.traits.positive;

import java.util.List;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by AFlyingGrayson on 9/3/17
 */
public class Sneaky extends AbilityConstant {
	public Sneaky(EntityPlayer player) {
		super(player);
	}
	
	@Override
	public void updateTick() {
	}
	
	@SubscribeEvent
	public static void onVisibilityCalc(PlayerEvent.Visibility event) {
		
		if (SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()) != null) {
			List<Ability> abilityList = SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()).getAbilities();
			if (abilityList != null) {
				for (Ability ability : abilityList) {
					if (ability instanceof Sneaky && ability.isUnlocked()) {
						event.modifyVisibility(0.5);
					}
				}
			}
		}
	}
}
