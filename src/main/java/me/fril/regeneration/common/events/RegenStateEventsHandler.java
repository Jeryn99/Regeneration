package me.fril.regeneration.common.events;

import me.fril.regeneration.RegenerationMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

public class RegenStateEventsHandler {
	
	@Mod.EventBusSubscriber(modid = RegenerationMod.MODID, value=Side.SERVER)
	public static class Server {
		
		/*@SubscribeEvent
		public static void onEnterGrace(RegenGraceEvent.Enter ev) {
			
		}
		
		@SubscribeEvent
		public static void onChangeGrace(RegenGraceEvent.Change ev) {
			
		}
		
		
		@SubscribeEvent
		public static void onTriggerRegen(RegenTriggerRegenerationEvent ev) {
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
			PlayerUtil.sendHotbarMessage(player, new TextComponentTranslation("regeneration.messages.remaining_regens.notification", cap.getRegenerationsLeft()), true);
		}
		
		
		@SubscribeEvent
		public static void onDeath(RegenDeathEvent ev) {
			
		}
		
		@SubscribeEvent
		public static void onFinishRegen(RegenFinishRegenerationEvent ev) {
			
		}*/
		
	}
	
	
	
	
	@Mod.EventBusSubscriber(modid = RegenerationMod.MODID, value=Side.CLIENT)
	public static class Client {
		
		/*@SubscribeEvent
		public static void onEnterGrace(RegenGraceEvent.Enter ev) {
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS);
		}
		
		@SubscribeEvent
		public static void onChangeGrace(RegenGraceEvent.Change ev) {
			
		}
		
		
		@SubscribeEvent
		public static void onTriggerRegen(RegenTriggerRegenerationEvent ev) {
			PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.REGENERATION, SoundCategory.PLAYERS);
		}
		
		
		@SubscribeEvent
		public static void onDeath(RegenDeathEvent ev) {
			
		}
		
		@SubscribeEvent
		public static void onFinishRegen(RegenFinishRegenerationEvent ev) {
			
		}*/
		
	}
	
}
