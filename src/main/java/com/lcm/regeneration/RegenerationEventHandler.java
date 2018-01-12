package com.lcm.regeneration;

import com.lcm.regeneration.superpower.TimelordSuperpower;
import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import net.minecraft.client.Minecraft;
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
		
		if (!e.getEntity().world.isRemote && (e.getSource().isExplosion() || e.getSource().isFireDamage()) && handler.regenTicks >= 100) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);

		if (handler.regenerating || player.getPosition().getY() < 0) { //death while regenerating / falling into the void which does the same but doesn't trigger a regeneration
			handler.reset(player);
			SuperpowerHandler.removeSuperpower(player);
		}
	}
	
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if(((EntityPlayer) e.getEntity()).getHealth() - e.getAmount() > 0 || player.getPosition().getY() < 0 || !SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		
		if (player.world.isRemote) return;
		if (handler.regenerationsLeft > 0 && handler.regenTicks == 0) {
			e.setCanceled(true);
			((EntityPlayer) e.getEntity()).setHealth(1.5f);
			((EntityPlayer) e.getEntity()).addPotionEffect(new PotionEffect(Potion.getPotionById(10), 200, 1, false, false));
			if (handler.regenTicks == 0) handler.regenerating = true;
			SuperpowerHandler.syncToAll(player);
			
			String time = "" + (handler.timesRegenerated + 1);
			switch (handler.timesRegenerated) {
				case 0:
					time = time + "st";
					break;
				case 1:
					time = time + "nd";
					break;
				case 2:
					time = time + "rd";
					break;
				default:
					time = time + "th";
					break;
			}
			handler.getPlayer().sendStatusMessage(new TextComponentString("You're regenerating for the " + time + " time, you have " + handler.regenerationsLeft + " regenerations left."), true);
			player.world.playSound(null, player.posX, player.posY, player.posZ, RegenerationSounds.SHORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		} else if (handler.regenerationsLeft <= 0) {
			handler.getPlayer().sendStatusMessage(new TextComponentString("You're out of regenerations. You're dying for real this time."), true); //XXX a message when dying for real?
			handler.reset(player);
			SuperpowerHandler.removeSuperpower(handler.getPlayer());
		}
	}
}
