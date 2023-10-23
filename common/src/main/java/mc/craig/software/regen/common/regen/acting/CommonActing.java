package mc.craig.software.regen.common.regen.acting;

import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.block.JarBlock;
import mc.craig.software.regen.common.blockentity.BioContainerBlockEntity;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.transitions.WatcherTransition;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.messages.SFXMessage;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Iterator;
import java.util.UUID;

public class CommonActing implements Acting {

    public static final Acting INSTANCE = new CommonActing();
    private final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"), MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
    private final double HEART_REDUCTION = 0.5, SPEED_REDUCTION = 0.35;
    private final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE), heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE);

    public CommonActing() {
    }

    public static SoundEvent getRandomSound(RandomSource random, IRegen cap) {
        SoundEvent[] soundEvents = cap.transitionType().getRegeneratingSounds();
        return soundEvents[random.nextInt(soundEvents.length)];
    }

    @Override
    public void onRegenTick(IRegen cap) {
        LivingEntity livingEntity = cap.getLiving();
        float stateProgress = (float) cap.stateManager().stateProgress();

        switch (cap.regenState()) {
            case POST:
                HolderSet.Named<MobEffect> mobEffects = BuiltInRegistries.MOB_EFFECT.getTag(RegenUtil.POST_REGEN_POTIONS).get();
                if (livingEntity.tickCount % 210 == 0 && !PlayerUtil.isPlayerAboveZeroGrid(livingEntity)) {
                    mobEffects.getRandomElement(RandomSource.create()).ifPresent(mobEffectHolder -> PlayerUtil.applyPotionIfAbsent(livingEntity, mobEffectHolder.value(), livingEntity.level().random.nextInt(400), 1, false, false));
                }

                if (PlayerUtil.isPlayerAboveZeroGrid(livingEntity)) {
                    PlayerUtil.handleZeroGrid(livingEntity);
                }
                break;
            case REGENERATING:

                if (livingEntity instanceof ServerPlayer playerEntity) {
                    TriggerManager.FIRST_REGENERATION.trigger(playerEntity);
                }

                float dm = Math.max(1, (livingEntity.level().getDifficulty().getId() + 1) / 3F); // compensating for hard difficulty
                livingEntity.heal(stateProgress * 0.3F * dm);
                livingEntity.setArrowCount(0);

                AABB box = livingEntity.getBoundingBox().inflate(25);
                for (Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(new BlockPos((int) box.maxX, (int) box.maxY, (int) box.maxZ), new BlockPos((int) box.minX, (int) box.minY, (int) box.minZ)).iterator(); iterator.hasNext(); ) {
                    BlockPos pos = iterator.next();
                    ServerLevel serverWorld = (ServerLevel) livingEntity.level();
                    BlockState blockState = serverWorld.getBlockState(pos);
                    if (blockState.getBlock() instanceof JarBlock) {
                        BioContainerBlockEntity bioContainerBlockEntity = (BioContainerBlockEntity) serverWorld.getBlockEntity(pos);
                        if (!bioContainerBlockEntity.isValid(BioContainerBlockEntity.Action.ADD)) {
                            continue;
                        }
                        if (livingEntity.level().random.nextBoolean() && serverWorld.getGameTime() % 5 == 0) {
                            bioContainerBlockEntity.setLindos(bioContainerBlockEntity.getLindos() + 0.7F);
                        }
                        bioContainerBlockEntity.sendUpdates();
                        return;
                    }
                }


                break;

            case GRACE_CRIT:
                float nauseaPercentage = 0.5F;

                if (stateProgress > nauseaPercentage) {
                    PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.CONFUSION, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - nauseaPercentage) * 1.5F), 0, false, false);
                }

                PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.WEAKNESS, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - stateProgress)), 0, false, false);

                if (livingEntity.level().random.nextDouble() < (RegenConfig.COMMON.criticalDamageChance.get() / 100F)) {
                    Registry<DamageType> damageTypeRegistry = livingEntity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get();
                    livingEntity.hurt(new DamageSource(damageTypeRegistry.getHolderOrThrow(RegenDamageTypes.REGEN_DMG_CRITICAL)), livingEntity.level().random.nextFloat() + .5F);
                }
                break;

            case GRACE:
                float weaknessPercentage = 0.5F;
                if (stateProgress > weaknessPercentage) {
                    PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.WEAKNESS, (int) (RegenConfig.COMMON.gracePhaseLength.get() * 20 * (1 - weaknessPercentage) + RegenConfig.COMMON.criticalPhaseLength.get() * 20), 0, false, false);
                }
                break;
            case ALIVE:
                break;
            default:
                throw new IllegalStateException("Unknown state " + cap.regenState());
        }
    }

    @Override
    public void onEnterGrace(IRegen cap) {
        LivingEntity player = cap.getLiving();
        PlayerUtil.explodeKnockback(player, player.level(), player.blockPosition(), RegenConfig.COMMON.regenerativeKnockback.get() / 2, RegenConfig.COMMON.regenKnockbackRange.get());
        // Reduce number of hearts, but compensate with absorption
        player.setAbsorptionAmount(player.getMaxHealth() * (float) HEART_REDUCTION);
        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(heartModifier)) {
            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(heartModifier);
        }
        player.setHealth(player.getMaxHealth());
        WatcherTransition.createWatcher(player);

    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        PlayerUtil.sendMessage(cap.getLiving(), Component.translatable(RMessages.PUNCH_WARNING), true);
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (!cap.getLiving().getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) {
            cap.getLiving().getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(slownessModifier);
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {
        LivingEntity player = cap.getLiving();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, RegenConfig.COMMON.postRegenerationDuration.get(), RegenConfig.COMMON.postRegenerationLevel.get() - 1, false, false));
        player.setHealth(player.getMaxHealth());
        player.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);
        cap.setNextSkin(new byte[0]);

        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(heartModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slownessModifier);
    }

    @Override
    public void onPerformingPost(IRegen cap) {
    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        LivingEntity living = cap.getLiving();
        new SFXMessage(getRandomSound(living.getRandom(), cap).getLocation(), living.getId()).sendToDimension(living.level());

        living.getAttribute(Attributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
        living.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
        living.setHealth(Math.max(living.getHealth(), 8));
        living.setAbsorptionAmount(0);

        living.clearFire();
        living.ejectPassengers();
        living.removeAllEffects();
        living.stopRiding();

        if (living instanceof Player playerEntity) {
            if (RegenConfig.COMMON.resetHunger.get()) playerEntity.getFoodData().setFoodLevel(20);
        }

        if (RegenConfig.COMMON.resetOxygen.get()) living.setAirSupply(300);

        cap.extractRegens(1);

        cap.getCurrentTrait().onRemoved(living, cap);

        if(cap.getNextTrait() != TraitRegistry.HUMAN.get()){
            cap.setCurrentTrait(cap.getNextTrait());
            cap.setNextTrait(TraitRegistry.HUMAN.get());
        } else {
            TraitRegistry.getRandomTrait().ifPresent(cap::setCurrentTrait);
        }

        cap.getCurrentTrait().onAdded(living, cap);
        cap.syncToClients(null);

    }

}
