package me.fril.regeneration.common.events;

import java.util.UUID;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.sound.ConditionalSound;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RegenStateEventHandler {
	
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new SidedEventHandlerProxy(ev->!ev.player.world.isRemote, Client.class, Server.class));
	}
	
	
	// REMEMBER THAT THESE EVENT HANDLERS ONLY CATCH RegenStateBaseEvent SUBCLASSES!
	
	
	//FIXME sounds don't play when logging mid-regen
	public static class Server {
		
		private static final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"),
		                          MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
		
		private static final double HEART_REDUCTION = 0.5,
									SPEED_REDUCTION = 0.25;
		
		private static final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, 1),
		                                       heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, 1);
		
		@SubscribeEvent
		public static void onEnterGrace(RegenEnterGraceEvent ev) { //FIXME there's a lag spike the first time this happens
			//SOON yellow vingette to make sure there's always a grace indicator? Or the heart timer?
			
			//Reduce number of hearts, but compensate with absorption
			ev.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(heartModifier);
			ev.player.setAbsorptionAmount(ev.player.getMaxHealth() * (float)HEART_REDUCTION * 2);
			
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS);
		}
		
		@SubscribeEvent
		public static void onGoCritical(RegenGoCriticalEvent ev) {
			ev.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slownessModifier);
		}
		
		@SubscribeEvent
		public static void onRegenTrigger(RegenTriggerEvent ev) {
			//SOON message in chat?
			IRegeneration cap = ev.capability;
			EntityPlayer player = ev.player;
			
			player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
			ev.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
			
			//FIXME you die way too fast at the start, but it's almost impossible to die 20% in
			player.setHealth(1);
			player.setAbsorptionAmount(0);
			
			player.extinguish();
			player.removePassengers();
			player.clearActivePotions();
			player.dismountRidingEntity();
			player.setArrowCountInEntity(0);
			
			if (RegenConfig.resetHunger)
				ev.player.getFoodStats().setFoodLevel(20);
			
			if (RegenConfig.resetOxygen)
				ev.player.setAir(300);
			
			cap.extractRegeneration(1);
			ExplosionUtil.regenerationExplosion(player);
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.REGENERATION, SoundCategory.PLAYERS); //NOW regenerations don't move
		}
		
		@SubscribeEvent
		public static void onRegenTick(RegenTickEvent ev) {
			switch (ev.state) {
				case REGENERATING:
					float health = (float)ev.stateProgress * ev.player.getMaxHealth();
					float absorption = RegenConfig.absorbtionLevel * 2 * (float)ev.stateProgress;
					
					ev.player.setHealth(Math.max(ev.player.getHealth(), health)); //using max because it sometimes damages the player due to rounding errors
					ev.player.setAbsorptionAmount(Math.max(ev.player.getAbsorptionAmount(), absorption));
					break;
					
				case GRACE_CRIT:
					float nauseaPercentage = 0.5F;
					
					if (ev.stateProgress > nauseaPercentage) {
						if (PlayerUtil.applyPotionIfAbsent(ev.player, 9, (int)(RegenConfig.Grace.criticalPhaseLength * 20 * (1-nauseaPercentage) * 1.5F), 0, false, false)) {
							RegenerationMod.DEBUGGER.getChannelFor(ev.player).out("Applied nausea");
						}
					}
					
					if (PlayerUtil.applyPotionIfAbsent(ev.player, 18, (int)(RegenConfig.Grace.criticalPhaseLength * 20 * (1-ev.stateProgress)), 0, false, false)) {
						RegenerationMod.DEBUGGER.getChannelFor(ev.player).out("Applied weakness");
					}
					
					if (ev.player.world.rand.nextDouble() < (RegenConfig.Grace.criticalDamageChance / 100F))
						ev.player.attackEntityFrom(RegenObjects.REGEN_DMG_CRITICAL, ev.player.world.rand.nextFloat() + .5F);
					
					break;
					
				case GRACE:
					float weaknessPercentage = 0.5F;
					
					if (ev.stateProgress > weaknessPercentage) {
						if (PlayerUtil.applyPotionIfAbsent(ev.player, 18, (int)(RegenConfig.Grace.gracePhaseLength * 20 * (1-weaknessPercentage) + RegenConfig.Grace.criticalPhaseLength * 20), 0, false, false)) {
							RegenerationMod.DEBUGGER.getChannelFor(ev.player).out("Applied weakness");
						}
					}
					
					break;
					
				case ALIVE: break;
				default: throw new IllegalStateException("Unknown state "+ev.state);
			}
		}
		
		@SubscribeEvent
		public static void onRegenFinish(RegenFinishEvent ev) {
			ev.player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.postRegenerationDuration * 2, RegenConfig.postRegenerationLevel - 1, false, false));
		}
		
	}
	
	
	
	public static class Client {
		
		//FUTURE Toasts: PlayerUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerations_left"), new TextComponentTranslation(getRegenerationsLeft() + ""), RegenState.REGENERATING);
		
		@SubscribeEvent
		public static void onEnterGrace(RegenEnterGraceEvent ev) {
			//SOON toast notification for entering grace
		}
		
		@SubscribeEvent
		public static void onRegenFinish(RegenFinishEvent ev) {
			//SOON toast notification for finishing regeneration
		}
		
		@SubscribeEvent
		public static void onRegenTrigger(RegenTriggerEvent ev) {
			//SOON toast notification for triggering regeneration
			PlayerUtil.sendHotbarMessage(ev.player, new TextComponentTranslation("regeneration.messages.remaining_regens.notification", ev.capability.getRegenerationsLeft()), true);
			
		}
		
		@SubscribeEvent
		public static void onGoCritical(RegenGoCriticalEvent ev) {
			//SOON toast notification for entering critical phase
			//SOON red vingette in critical phase
			
			//PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.CRITICAL_STAGE, SoundCategory.PLAYERS); //NOW should be a player-only sound
			Minecraft.getMinecraft().getSoundHandler().playSound(new ConditionalSound(PositionedSoundRecord.getRecord(RegenObjects.Sounds.CRITICAL_STAGE, 1.0F, 2.0F), ()->ev.capability.getState() != RegenState.GRACE_CRIT));
			
		}
		
	}
	
}
