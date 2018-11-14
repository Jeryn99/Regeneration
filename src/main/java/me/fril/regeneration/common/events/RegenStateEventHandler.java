package me.fril.regeneration.common.events;

import java.util.UUID;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.events.RegenStateEvents.RegenEnterGraceEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenFinishEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenGoCriticalEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenTickEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenTriggerEvent;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.ExplosionUtil;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RegenStateEventHandler {
	
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new SidedEventHandlerProxy(ev->!ev.player.world.isRemote, Client.class, Server.class));
	}
	
	
	
	public static class Server {
		
		private static final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782");
		private static final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -0.5D, 1);
		
		@SubscribeEvent
		public static void onEnterGrace(RegenEnterGraceEvent ev) {
			//NOW some healing stuff, thoughness?
			//NOW re-implement slowness, only in grace?
			//NOW yellow vingette to make sure there's always a grace indicator? Or the heart timer?
			
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS);
		}
		
		@SubscribeEvent
		public static void onGoCritical(RegenGoCriticalEvent ev) {
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.CRITICAL_STAGE, SoundCategory.PLAYERS);
			ev.player.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 800, 0, false, false)); // could be removed with milk, but I think that's not that bad
		}
		
		@SubscribeEvent
		public static void onRegenTrigger(RegenTriggerEvent ev) {
			//NOW message in chat?
			IRegeneration cap = ev.capability;
			EntityPlayer player = ev.player;
			
			player.extinguish();
			player.removePassengers();
			player.clearActivePotions();
			player.dismountRidingEntity();
			player.setArrowCountInEntity(0);
			player.setAbsorptionAmount(RegenConfig.absorbtionLevel * 2);
			
			cap.extractRegeneration(1);
			ExplosionUtil.regenerationExplosion(player);
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.REGENERATION, SoundCategory.PLAYERS);
		}
		
		@SubscribeEvent
		public static void onRegenTick(RegenTickEvent ev) {
			//NOW random damage in critical period
			
			if (ev.capability.getState() == RegenState.REGENERATING && ev.player.getHealth() < ev.player.getMaxHealth()) {
				ev.player.heal(1.0F);
			}
		}
		
		@SubscribeEvent
		public static void onRegenFinish(RegenFinishEvent ev) {
			if (RegenConfig.resetHunger) {
				FoodStats foodStats = ev.player.getFoodStats();
				foodStats.setFoodLevel(foodStats.getFoodLevel() + 1);
			}
			
			if (RegenConfig.resetOxygen)
				ev.player.setAir(10); //CHECK what's the max for this?
			
			ev.player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.postRegenerationDuration * 2, RegenConfig.postRegenerationLevel - 1, false, false));
			
			if (ev.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) {
				ev.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
			}
		}
		
	}
	
	
	
	public static class Client {
		
		//FUTURE Toasts: PlayerUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerations_left"), new TextComponentTranslation(getRegenerationsLeft() + ""), RegenState.REGENERATING);
		
		@SubscribeEvent
		public static void onEnterGrace(RegenEnterGraceEvent ev) {
			//NOW toast notification for entering grace
		}
		
		@SubscribeEvent
		public static void onRegenFinish(RegenFinishEvent ev) {
			//NOW toast notification for finishing regeneration
		}
		
		@SubscribeEvent
		public static void onRegenTrigger(RegenTriggerEvent ev) {
			//NOW toast notification for triggering regeneration
			PlayerUtil.sendHotbarMessage(ev.player, new TextComponentTranslation("regeneration.messages.remaining_regens.notification", ev.capability.getRegenerationsLeft()), true);
			
		}
		
		@SubscribeEvent
		public static void onGoCritical(RegenGoCriticalEvent ev) {
			//NOW toast notification for entering critical phase
			//NOW red vingette in critical phase
		}
		
	}
	
}
