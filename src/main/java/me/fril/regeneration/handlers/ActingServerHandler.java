package me.fril.regeneration.handlers;

import java.util.UUID;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.ActingForwarder.IActingHandler;
import me.fril.regeneration.util.ExplosionUtil;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

class ActingServerHandler implements IActingHandler { // XXX feel free to rename this, I couldn't think of anything better
	
	private final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"),
	                   MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
	
	private final double HEART_REDUCTION = 0.5,
	                     SPEED_REDUCTION = 0.25;
	
	private final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, 1),
	                                heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, 1);
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		double stateProgress = cap.getStateManager().getStateProgress();
		
		switch (cap.getState()) {
			case REGENERATING:
				float health = (float) stateProgress * player.getMaxHealth();
				float absorption = RegenConfig.absorbtionLevel * 2 * (float) stateProgress;
				
				player.setHealth(Math.max(player.getHealth(), health)); // using max because it sometimes damages the player due to rounding errors
				player.setAbsorptionAmount(Math.max(player.getAbsorptionAmount(), absorption));
				break;
			
			case GRACE_CRIT:
				float nauseaPercentage = 0.5F;
				
				if (stateProgress > nauseaPercentage) {
					if (PlayerUtil.applyPotionIfAbsent(player, 9, (int) (RegenConfig.Grace.criticalPhaseLength * 20 * (1 - nauseaPercentage) * 1.5F), 0, false, false)) {
						RegenerationMod.DEBUGGER.getChannelFor(player).out("Applied nausea");
					}
				}
				
				if (PlayerUtil.applyPotionIfAbsent(player, 18, (int) (RegenConfig.Grace.criticalPhaseLength * 20 * (1 - stateProgress)), 0, false, false)) {
					RegenerationMod.DEBUGGER.getChannelFor(player).out("Applied weakness");
				}
				
				if (player.world.rand.nextDouble() < (RegenConfig.Grace.criticalDamageChance / 100F))
					player.attackEntityFrom(RegenObjects.REGEN_DMG_CRITICAL, player.world.rand.nextFloat() + .5F);
				
				break;
			
			case GRACE:
				float weaknessPercentage = 0.5F;
				
				if (stateProgress > weaknessPercentage) {
					if (PlayerUtil.applyPotionIfAbsent(player, 18, (int) (RegenConfig.Grace.gracePhaseLength * 20 * (1 - weaknessPercentage) + RegenConfig.Grace.criticalPhaseLength * 20), 0, false, false)) {
						RegenerationMod.DEBUGGER.getChannelFor(player).out("Applied weakness");
					}
				}
				
				break;
			
			case ALIVE:
				break;
			default:
				throw new IllegalStateException("Unknown state " + cap.getState());
		}
	}
	
	
	
	
	@Override
	public void onEnterGrace(IRegeneration cap) { // FIXME there's a lag spike the first time this happens
		// SOON yellow vingette to make sure there's always a grace indicator? Or the heart timer?
		
		// Reduce number of hearts, but compensate with absorption
		cap.getPlayer().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(heartModifier);
		cap.getPlayer().setAbsorptionAmount(cap.getPlayer().getMaxHealth() * (float) HEART_REDUCTION * 2);
		
		PlayerUtil.playMovingSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS);
	}
	
	
	
	
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		cap.getPlayer().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slownessModifier);
	}
	
	
	
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		// SOON message in chat?
		EntityPlayer player = cap.getPlayer();
		
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
		player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
		
		// FIXME you die way too fast at the start, but it's almost impossible to die 20% in
		player.setHealth(1);
		player.setAbsorptionAmount(0);
		
		player.extinguish();
		player.removePassengers();
		player.clearActivePotions();
		player.dismountRidingEntity();
		player.setArrowCountInEntity(0);
		
		if (RegenConfig.resetHunger)
			player.getFoodStats().setFoodLevel(20);
		
		if (RegenConfig.resetOxygen)
			player.setAir(300);
		
		cap.extractRegeneration(1);
		ExplosionUtil.regenerationExplosion(player);
		PlayerUtil.playMovingSound(player, RegenObjects.Sounds.REGENERATION, SoundCategory.PLAYERS); // NOW regenerations don't move
	}
	
	
	
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		cap.getPlayer().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.postRegenerationDuration * 2, RegenConfig.postRegenerationLevel - 1, false, false));
	}
	
}
