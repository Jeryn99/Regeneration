package com.afg.regeneration;

import com.afg.regeneration.sounds.SoundReg;
import com.afg.regeneration.superpower.TimelordSuperpower;
import com.afg.regeneration.superpower.TimelordSuperpowerHandler;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegenerationEventHandler {
	
	@SubscribeEvent
	public static void onAttacked(LivingAttackEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		if ((e.getSource().isExplosion() || e.getSource().isFireDamage()) && handler.regenTicks >= 100) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		handler.regenTicks = 0;
	}
	
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer) || ((EntityPlayer) e.getEntity()).getHealth() - e.getAmount() > 0) return;
		
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		
		
		if (handler.regenCount < 12 && handler.regenTicks == 0) {
			e.setCanceled(true);
			((EntityPlayer) e.getEntity()).setHealth(1.5f);
			((EntityPlayer) e.getEntity()).addPotionEffect(new PotionEffect(Potion.getPotionById(10), 200, 1, false, false));
			if (handler.regenTicks == 0) handler.regenTicks = 1;
			SuperpowerHandler.syncToAll(player);
			
			String time = "" + (handler.regenCount + 1);
			switch (handler.regenCount + 1) {
				case 1:
					time = time + "st";
					break;
				case 2:
					time = time + "nd";
					break;
				case 3:
					time = time + "rd";
					break;
				default:
					time = time + "th";
					break;
			}
			handler.getPlayer().sendStatusMessage(new TextComponentString("You're regenerating for the " + time + " time, you have " + (11 - handler.regenCount) + " regenerations left."), true);
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundReg.SHORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		} else if (handler.regenCount >= 12) {
			handler.getPlayer().sendStatusMessage(new TextComponentString("You're out of regenerations. You're dying for real this time."), true);
			SuperpowerHandler.removeSuperpower(handler.getPlayer());
		}
	}
}
