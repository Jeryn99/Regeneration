package me.swirtzly.regeneration.handlers.acting;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.common.advancements.TriggerManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.PlaySFXMessage;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;
import java.util.UUID;

class CommonActing implements Acting {
	
	public static final Acting INSTANCE = new CommonActing();
	private final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"), MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
	private final double HEART_REDUCTION = 0.5, SPEED_REDUCTION = 0.35;
    private final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE), heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE);

    public CommonActing() {
    }

	public static SoundEvent getRandomSound(Random random, IRegen cap) {
		SoundEvent[] SOUNDS = cap.getType().create().getRegeneratingSounds();
		return SOUNDS[random.nextInt(SOUNDS.length)];
	}
	
	@Override
    public void onRegenTick(IRegen cap) {
		LivingEntity player = cap.getLivingEntity();
		float stateProgress = (float) cap.getStateManager().getStateProgress();
		
		switch (cap.getState()) {
			case POST:
				if (player.ticksExisted % 210 == 0) {
					PlayerUtil.applyPotionIfAbsent(player, PlayerUtil.POTIONS.get(player.world.rand.nextInt(PlayerUtil.POTIONS.size())), player.world.rand.nextInt(400), 1, false, false);
				}
				break;
			case REGENERATING:
				float dm = Math.max(1, (player.world.getDifficulty().getId() + 1) / 3F); // compensating for hard difficulty
				player.heal(stateProgress * 0.3F * dm);
				player.setArrowCountInEntity(0);
				break;
			
			case GRACE_CRIT:
				float nauseaPercentage = 0.5F;

                if (stateProgress > nauseaPercentage) {
					PlayerUtil.applyPotionIfAbsent(player, Effects.NAUSEA, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - nauseaPercentage) * 1.5F), 0, false, false);
				}

                PlayerUtil.applyPotionIfAbsent(player, Effects.WEAKNESS, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - stateProgress)), 0, false, false);

                if (player.world.rand.nextDouble() < (RegenConfig.COMMON.criticalDamageChance.get() / 100F))
                    player.attackEntityFrom(RegenObjects.REGEN_DMG_CRITICAL, player.world.rand.nextFloat() + .5F);
				
				break;

            case GRACE:
				float weaknessPercentage = 0.5F;

                if (stateProgress > weaknessPercentage) {
					PlayerUtil.applyPotionIfAbsent(player, Effects.WEAKNESS, (int) (RegenConfig.COMMON.gracePhaseLength.get() * 20 * (1 - weaknessPercentage) + RegenConfig.COMMON.criticalPhaseLength.get() * 20), 0, false, false);
				}

                break;

            case ALIVE:
				break;
			default:
				throw new IllegalStateException("Unknown state " + cap.getState());
		}
	}

    @Override
    public void onEnterGrace(IRegen cap) {
		LivingEntity player = cap.getLivingEntity();
		RegenUtil.explodeKnockback(player, player.world, player.getPosition(), RegenConfig.COMMON.regenerativeKnockback.get() / 2, RegenConfig.COMMON.regenKnockbackRange.get());
		
		// Reduce number of hearts, but compensate with absorption
		player.setAbsorptionAmount(player.getMaxHealth() * (float) HEART_REDUCTION);
		
		if (!player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).hasModifier(heartModifier)) {
			player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(heartModifier);
		}
		
		TraitManager.IDna dna = TraitManager.getDnaEntry(cap.getDnaType());
		dna.onRemoved(cap);
		cap.setDnaActive(false);
		player.setHealth(player.getMaxHealth());
	}
	
	@Override
    public void onHandsStartGlowing(IRegen cap) {
		PlayerUtil.sendMessage(cap.getLivingEntity(), new TranslationTextComponent("regeneration.messages.regen_warning"), true);
	}
	
	@Override
    public void onGoCritical(IRegen cap) {

		if (cap.getLivingEntity() instanceof ServerPlayerEntity) {
			TriggerManager.CRITICAL.trigger((ServerPlayerEntity) cap.getLivingEntity());
		}

		if (!cap.getLivingEntity().getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) {
			cap.getLivingEntity().getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slownessModifier);
		}
	}
	
	@Override
    public void onRegenFinish(IRegen cap) {
		LivingEntity player = cap.getLivingEntity();
		if (cap.getLivingEntity() instanceof ServerPlayerEntity) {
			TriggerManager.FIRST_REGENERATION.trigger((ServerPlayerEntity) cap.getLivingEntity());
		}
		player.addPotionEffect(new EffectInstance(Effects.REGENERATION, RegenConfig.COMMON.postRegenerationDuration.get() * 2, RegenConfig.COMMON.postRegenerationLevel.get() - 1, false, false));
		player.setHealth(player.getMaxHealth());
		player.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);
		
		cap.setDnaType(TraitManager.getRandomDna(player.world.rand).getRegistryName());
		TraitManager.IDna newDna = TraitManager.getDnaEntry(cap.getDnaType());
		newDna.onAdded(cap);
		cap.setDnaActive(true);
		PlayerUtil.sendMessage(player, new TranslationTextComponent(newDna.getLangKey()), true);
        cap.setNextSkin(RegenUtil.NO_SKIN);
	}
	
	@Override
    public void onPerformingPost(IRegen cap) {
		PlayerUtil.handleCutOffhand(cap.getLivingEntity());
    }
	
	@Override
    public void onRegenTrigger(IRegen cap) {
		LivingEntity player = cap.getLivingEntity();
		NetworkDispatcher.sendPacketToAll(new PlaySFXMessage(getRandomSound(player.world.rand, cap).getRegistryName(), player.getUniqueID()));
		player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
		player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
		player.setHealth(Math.max(player.getHealth(), 8));
		player.setAbsorptionAmount(0);
		
		player.extinguish();
		player.removePassengers();
		player.clearActivePotions();
		player.stopRiding();

		if (player instanceof PlayerEntity) {
			PlayerEntity playerEntity = (PlayerEntity) player;
			if (RegenConfig.COMMON.resetHunger.get()) playerEntity.getFoodStats().setFoodLevel(20);
		}

        if (RegenConfig.COMMON.resetOxygen.get()) player.setAir(300);
		
		cap.extractRegeneration(1);
	}
	
}
