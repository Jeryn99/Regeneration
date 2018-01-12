package com.lcm.regeneration.traits.negative;

import com.lcm.regeneration.traits.positive.TraitSmart;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by AFlyingGrayson on 9/3/17
 */
@Mod.EventBusSubscriber
public class TraitDumb extends AbilityConstant implements INegativeTrait {
	
	public TraitDumb(EntityPlayer player) {
		super(player);
	}
	
	@SubscribeEvent
	public static void onExperienceGain(PlayerPickupXpEvent event) {
		if (SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()) == null) return;
		List<Ability> abilityList = SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()).getAbilities();
		if (abilityList == null) return;
		
		for (Ability ability : abilityList) if (ability instanceof TraitDumb && ability.isUnlocked()) {
			event.getOrb().xpValue *= 0.5;
		}
	}
	
	@Override public void updateTick() {}

	@Override
	public boolean showInAbilityBar() {
		return false;
	}

	@Override public Class<? extends Ability> getPositiveTrait()
	{
		return TraitSmart.class;
	}
}