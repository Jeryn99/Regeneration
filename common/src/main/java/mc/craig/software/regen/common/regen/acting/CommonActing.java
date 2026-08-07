package mc.craig.software.regen.common.regen.acting;

import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.transitions.WatcherTransition;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.messages.SFXMessage;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CommonActing implements Acting {

    public static final Acting INSTANCE = new CommonActing();
    private static final Logger LOGGER = LoggerFactory.getLogger("RegenLogger");

    private static final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782");
    private static final UUID MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");

    private static final double HEART_REDUCTION = 0.5;
    private static final double SPEED_REDUCTION = 0.35;

    private static final AttributeModifier SLOWNESS_MODIFIER =
            new AttributeModifier(SLOWNESS_ID, "regen_slowdown", -SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier HEART_MODIFIER =
            new AttributeModifier(MAX_HEALTH_ID, "regen_heart_reduction", -HEART_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final float SAFE_MIN_HEALTH = 0.5f;

    private CommonActing() {
    }

    public static SoundEvent getRandomSound(RandomSource random, IRegen cap) {
        SoundEvent[] sounds = cap.transitionType().getRegeneratingSounds();
        SoundEvent chosen = sounds[random.nextInt(sounds.length)];
        LOGGER.debug("[Regen] Random sound selected for regen: {}", chosen.getLocation());
        return chosen;
    }

    @Override
    public void onRegenTick(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (entity == null) return;

        float progress = (float) cap.stateManager().stateProgress();
        float difficultyMultiplier = Math.max(1, (entity.level().getDifficulty().getId() + 1) / 3F);

        LOGGER.trace("[Regen] Tick: {} in state {} | Progress: {} | Multiplier: {} | Health: {}",
                entity.getName().getString(), cap.regenState(), progress, difficultyMultiplier, entity.getHealth());

        switch (cap.regenState()) {
            case POST -> handlePostRegen(entity);
            case REGENERATING -> handleRegenerating(entity, cap, progress, difficultyMultiplier);
            case GRACE_CRIT -> handleGraceCritical(entity, progress);
            case GRACE -> handleGrace(entity, progress, difficultyMultiplier);
            case ALIVE -> LOGGER.trace("[Regen] {} is ALIVE. No special handling.", entity.getName().getString());
            default -> throw new IllegalStateException("Unknown regen state: " + cap.regenState());
        }
    }

    private void handlePostRegen(LivingEntity entity) {
        LOGGER.debug("[Regen] Handling POST regen for {}", entity.getName().getString());

        RegenerationData.get(entity).ifPresent(regenerationData ->
                regenerationData.setRetryGracePeriod(false));

        if (entity.tickCount % 210 == 0 && !PlayerUtil.isPlayerAboveZeroGrid(entity)) {
            HolderSet.Named<MobEffect> potions = BuiltInRegistries.MOB_EFFECT.getTag(RegenUtil.POST_REGEN_POTIONS).orElse(null);
            if (potions != null) {
                potions.getRandomElement(RandomSource.create()).ifPresent(holder -> {
                    LOGGER.info("[Regen] Applying post-regen effect {} to {}", holder.value().getDescriptionId(), entity.getName().getString());
                    PlayerUtil.applyPotionIfAbsent(entity, holder.value(), entity.level().random.nextInt(400), 1, false, false);
                });
            }
        }

        if (PlayerUtil.isPlayerAboveZeroGrid(entity)) {
            LOGGER.debug("[Regen] {} is above zero grid. Handling Zero Grid logic.", entity.getName().getString());
            PlayerUtil.handleZeroGrid(entity);
        }

    }

    private void handleRegenerating(LivingEntity entity, IRegen cap, float progress, float multiplier) {
        LOGGER.debug("[Regen] {} is REGENERATING. Progress: {}", entity.getName().getString(), progress);
        RegenerationData.get(entity).ifPresent(regenerationData ->
                regenerationData.setRetryGracePeriod(true));
        if (entity instanceof ServerPlayer serverPlayer) {
            TriggerManager.FIRST_REGENERATION.trigger(serverPlayer);
        }

        float healAmount = progress * 0.3F * multiplier;
        healSafely(entity, healAmount);
        entity.setArrowCount(0);
        LOGGER.trace("[Regen] {} healed {} HP during regen. New HP: {}", entity.getName().getString(), healAmount, entity.getHealth());
    }

    private void handleGraceCritical(LivingEntity entity, float progress) {
        LOGGER.warn("[Regen] {} entered GRACE_CRIT phase. Progress: {}", entity.getName().getString(), progress);
        RegenerationData.get(entity).ifPresent(regenerationData ->
                regenerationData.setRetryGracePeriod(true));
        float nauseaThreshold = 0.5F;
        if (progress > nauseaThreshold) {
            int duration = (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - nauseaThreshold) * 1.5F);
            LOGGER.info("[Regen] Applying CONFUSION to {} for {} ticks.", entity.getName().getString(), duration);
            PlayerUtil.applyPotionIfAbsent(entity, MobEffects.CONFUSION, duration, 0, false, false);
        }

        int weaknessDuration = (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - progress));
        LOGGER.info("[Regen] Applying WEAKNESS to {} for {} ticks.", entity.getName().getString(), weaknessDuration);
        PlayerUtil.applyPotionIfAbsent(entity, MobEffects.WEAKNESS, weaknessDuration, 0, false, false);

        if (entity.level().random.nextDouble() < (RegenConfig.COMMON.criticalDamageChance.get() / 100F)) {
            float damage = Math.min(entity.getHealth() - SAFE_MIN_HEALTH, entity.level().random.nextFloat() + 0.5F);
            if (damage > 0) {
                LOGGER.error("[Regen] {} taking CRITICAL damage: {} HP.", entity.getName().getString(), damage);
                entity.hurt(new DamageSource(RegenDamageTypes.getHolder(entity, RegenDamageTypes.REGEN_DMG_CRITICAL)), damage);
            }
        }
    }

    private void handleGrace(LivingEntity entity, float progress, float multiplier) {
        LOGGER.debug("[Regen] {} is in GRACE phase. Progress: {}", entity.getName().getString(), progress);

        RegenerationData.get(entity).ifPresent(regenerationData ->
                regenerationData.setRetryGracePeriod(true));

        float weaknessThreshold = 0.5F;
        if (progress > weaknessThreshold) {
            int duration = (int) (
                    RegenConfig.COMMON.gracePhaseLength.get() * 20 * (1 - weaknessThreshold)
                            + RegenConfig.COMMON.criticalPhaseLength.get() * 20
            );
            LOGGER.info("[Regen] Applying WEAKNESS to {} for {} ticks during GRACE.", entity.getName().getString(), duration);
            PlayerUtil.applyPotionIfAbsent(entity, MobEffects.WEAKNESS, duration, 0, false, false);
        }

        if (entity.getHealth() < 8) {
            float healAmount = progress * 0.3F * multiplier;
            LOGGER.debug("[Regen] {} healing during GRACE phase: {} HP.", entity.getName().getString(), healAmount);
            healSafely(entity, healAmount);
        }
    }

    private void healSafely(LivingEntity entity, float amount) {
        float applied = Math.max(0, amount);
        entity.heal(applied);
        LOGGER.trace("[Regen] {} healed safely: {} HP. Current HP: {}", entity.getName().getString(), applied, entity.getHealth());
    }

    @Override
    public void onEnterGrace(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (entity == null) return;

        LOGGER.info("[Regen] {} entering GRACE state.", entity.getName().getString());
        PlayerUtil.explodeKnockback(entity, entity.level(), entity.blockPosition(),
                RegenConfig.COMMON.regenerativeKnockback.get() / 2,
                RegenConfig.COMMON.regenKnockbackRange.get());

        entity.setAbsorptionAmount(entity.getMaxHealth() * (float) HEART_REDUCTION);

        if (!entity.getAttribute(Attributes.MAX_HEALTH).hasModifier(HEART_MODIFIER)) {
            LOGGER.debug("[Regen] Applying HEART_MODIFIER to {}.", entity.getName().getString());
            entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(HEART_MODIFIER);
        }

        entity.setHealth(Math.max(entity.getMaxHealth(), SAFE_MIN_HEALTH));
        WatcherTransition.createWatcher(entity);
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        LOGGER.info("[Regen] {}’s hands started glowing!", cap.getLiving().getName().getString());
        PlayerUtil.sendMessage(cap.getLiving(), Component.translatable(RMessages.PUNCH_WARNING), true);
    }

    @Override
    public void onGoCritical(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (entity != null && !entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SLOWNESS_MODIFIER)) {
            LOGGER.warn("[Regen] {} entering CRITICAL state. Applying SLOWNESS_MODIFIER.", entity.getName().getString());
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(SLOWNESS_MODIFIER);
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (entity == null) return;

        LOGGER.info("[Regen] {} finished REGENERATION. Applying post effects.", entity.getName().getString());

        entity.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                RegenConfig.COMMON.postRegenerationDuration.get(),
                RegenConfig.COMMON.postRegenerationLevel.get() - 1,
                false, false)
        );

        entity.setHealth(Math.max(entity.getMaxHealth(), SAFE_MIN_HEALTH));
        entity.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);

        cap.setNextSkin(new byte[0]);

        entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEART_MODIFIER);
        entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_MODIFIER);
    }

    @Override
    public void onPerformingPost(IRegen cap) {
        LOGGER.debug("[Regen] {} performing POST actions (hook).", cap.getLiving().getName().getString());
    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (entity == null) return;

        LOGGER.info("[Regen] {} triggered REGEN start.", entity.getName().getString());
        new SFXMessage(getRandomSound(entity.getRandom(), cap).getLocation(), entity.getId()).sendToDimension(entity.level());

        entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
        entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);

        healSafely(entity, 1);
        entity.setAbsorptionAmount(0);
        entity.clearFire();
        entity.ejectPassengers();
        entity.removeAllEffects();
        entity.stopRiding();

        if (entity instanceof Player player && RegenConfig.COMMON.resetHunger.get()) {
            LOGGER.debug("[Regen] Resetting hunger for {}.", player.getName().getString());
            player.getFoodData().setFoodLevel(20);
        }

        if (RegenConfig.COMMON.resetOxygen.get()) {
            LOGGER.debug("[Regen] Resetting oxygen for {}.", entity.getName().getString());
            entity.setAirSupply(300);
        }

        cap.extractRegens(1);
        cap.syncToClients(null);
    }

}
