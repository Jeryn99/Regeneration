package com.lcm.regeneration.traits.positive;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by AFlyingGrayson on 9/3/17
 */
public class TraitSneaky extends AbilityConstant {
	
	public TraitSneaky(EntityPlayer player) {
		super(player);
	}
	
	@SubscribeEvent
	public static void onVisibilityCalc(PlayerEvent.Visibility event) {
		if (SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()) == null) return;
		List<Ability> abilityList = SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()).getAbilities();
		if (abilityList == null) return;
		for (Ability ability : abilityList) if (ability instanceof TraitSneaky && ability.isUnlocked()) {
			event.modifyVisibility(0.5);
		}
	}
	
	@Override public void updateTick() {}

	@Override
	public boolean showInAbilityBar() {
		return false;
	}
}
