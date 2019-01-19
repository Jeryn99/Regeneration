package me.fril.regeneration.handlers;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.ExplosionUtil;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

import java.util.UUID;

class ActingServerHandler implements IActingHandler {

	public static final IActingHandler INSTANCE = new ActingServerHandler();
	private final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"),
			MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
	private final double HEART_REDUCTION = 0.5,
			SPEED_REDUCTION = 0.25;
	private final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, 1),
			heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, 1);

	public ActingServerHandler() {
	}

	@Override
	public void onRegenTick(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		float stateProgress = (float) cap.getStateManager().getStateProgress();

		switch (cap.getState()) {
			case REGENERATING:
				float dm = Math.max(1, (player.world.getDifficulty().getId() + 1) / 3F); // compensating for hard difficulty
				player.heal(stateProgress * 0.3F * dm);
				player.setArrowCountInEntity(0);
				ExplosionUtil.regenerationExplosion(player);
				break;

			case GRACE_CRIT:
				float nauseaPercentage = 0.5F;

				if (stateProgress > nauseaPercentage) {
					if (PlayerUtil.applyPotionIfAbsent(player, MobEffects.NAUSEA, (int) (RegenConfig.grace.criticalPhaseLength * 20 * (1 - nauseaPercentage) * 1.5F), 0, false, false)) {
						RegenerationMod.DEBUGGER.getChannelFor(player).out("Applied nausea");
					}
				}

				if (PlayerUtil.applyPotionIfAbsent(player, MobEffects.WEAKNESS, (int) (RegenConfig.grace.criticalPhaseLength * 20 * (1 - stateProgress)), 0, false, false)) {
					RegenerationMod.DEBUGGER.getChannelFor(player).out("Applied weakness");
				}

				if (player.world.rand.nextDouble() < (RegenConfig.grace.criticalDamageChance / 100F))
					player.attackEntityFrom(RegenObjects.REGEN_DMG_CRITICAL, player.world.rand.nextFloat() + .5F);

				break;

			case GRACE:
				float weaknessPercentage = 0.5F;

				if (stateProgress > weaknessPercentage) {
					if (PlayerUtil.applyPotionIfAbsent(player, MobEffects.WEAKNESS, (int) (RegenConfig.grace.gracePhaseLength * 20 * (1 - weaknessPercentage) + RegenConfig.grace.criticalPhaseLength * 20), 0, false, false)) {
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
	public void onEnterGrace(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		ExplosionUtil.explodeKnockback(player, player.world, player.getPosition(), RegenConfig.regenerativeKnockback / 2, RegenConfig.regenerativeKnockbackRange);

		// Reduce number of hearts, but compensate with absorption
		player.setAbsorptionAmount(player.getMaxHealth() * (float) HEART_REDUCTION);
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(heartModifier);
		RegenerationMod.DEBUGGER.getChannelFor(player).out("Applied health reduction");
		player.setHealth(player.getMaxHealth());
	}

	@Override
	public void onGoCritical(IRegeneration cap) {
		cap.getPlayer().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slownessModifier);
		RegenerationMod.DEBUGGER.getChannelFor(cap.getPlayer()).out("Applied speed reduction");
	}

	@Override
	public void onRegenTrigger(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();

		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
		RegenerationMod.DEBUGGER.getChannelFor(player).out("Removed health reduction");

		player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
		RegenerationMod.DEBUGGER.getChannelFor(player).out("Removed speed reduction");

		player.setHealth(Math.max(player.getHealth(), 8));
		player.setAbsorptionAmount(0);

		player.extinguish();
		player.removePassengers();
		player.clearActivePotions();
		player.dismountRidingEntity();

		player.world.playSound(null, player.posX, player.posY, player.posZ, RegenObjects.Sounds.REGENERATION_2, SoundCategory.PLAYERS, 1.0F, 1.0F);

		if (RegenConfig.resetHunger)
			player.getFoodStats().setFoodLevel(20);

		if (RegenConfig.resetOxygen)
			player.setAir(300);

		cap.extractRegeneration(1);
	}

	@Override
	public void onRegenFinish(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.postRegenerationDuration * 2, RegenConfig.postRegenerationLevel - 1, false, false));
		player.setHealth(player.getMaxHealth());
		player.setAbsorptionAmount(RegenConfig.absorbtionLevel * 2);
	}

}
