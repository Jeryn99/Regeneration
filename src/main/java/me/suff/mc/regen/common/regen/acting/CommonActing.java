package me.suff.mc.regen.common.regen.acting;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.transitions.WatcherTransition;
import me.suff.mc.regen.common.tiles.JarTile;
import me.suff.mc.regen.common.traits.AbstractTrait;
import me.suff.mc.regen.common.traits.RegenTraitRegistry;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SFXMessage;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

class CommonActing implements Acting {

    public static final Acting INSTANCE = new CommonActing();
    private final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"), MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
    private final double HEART_REDUCTION = 0.5, SPEED_REDUCTION = 0.35;
    private final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE), heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE);

    public CommonActing() {
    }

    public static SoundEvent getRandomSound(Random random, IRegen cap) {
        SoundEvent[] soundEvents = cap.transitionType().getRegeneratingSounds();
        return soundEvents[random.nextInt(soundEvents.length)];
    }

    @Override
    public void onRegenTick(IRegen cap) {
        LivingEntity livingEntity = cap.getLiving();
        float stateProgress = (float) cap.stateManager().stateProgress();

        switch (cap.regenState()) {
            case POST:
                if (!PlayerUtil.POTIONS.isEmpty()) {
                    if (livingEntity.tickCount % 210 == 0 && !PlayerUtil.isPlayerAboveZeroGrid(livingEntity)) {
                        PlayerUtil.applyPotionIfAbsent(livingEntity, PlayerUtil.POTIONS.get(livingEntity.level.random.nextInt(PlayerUtil.POTIONS.size())), livingEntity.level.random.nextInt(400), 1, false, false);
                    }
                }

                if (PlayerUtil.isPlayerAboveZeroGrid(livingEntity)) {
                    PlayerUtil.handleZeroGrid(livingEntity);
                }


                break;
            case REGENERATING:

                if (livingEntity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) livingEntity;
                    TriggerManager.FIRST_REGENERATION.trigger(playerEntity);
                }

                float dm = Math.max(1, (livingEntity.level.getDifficulty().getId() + 1) / 3F); // compensating for hard difficulty
                livingEntity.heal(stateProgress * 0.3F * dm);
                livingEntity.setArrowCount(0);

                AxisAlignedBB box = livingEntity.getBoundingBox().inflate(25);
                for (Iterator< BlockPos > iterator = BlockPos.betweenClosedStream(new BlockPos(box.maxX, box.maxY, box.maxZ), new BlockPos(box.minX, box.minY, box.minZ)).iterator(); iterator.hasNext(); ) {
                    BlockPos pos = iterator.next();
                    ServerWorld serverWorld = (ServerWorld) livingEntity.level;
                    BlockState blockState = serverWorld.getBlockState(pos);
                    if (blockState.getBlock() instanceof JarBlock) {
                        JarTile jarTile = (JarTile) serverWorld.getBlockEntity(pos);
                        if (!jarTile.isValid(JarTile.Action.ADD)) {
                            continue;
                        }
                        if (livingEntity.level.random.nextBoolean() && serverWorld.getGameTime() % 5 == 0) {
                            jarTile.setLindos(jarTile.getLindos() + 0.7F);
                        }
                        jarTile.sendUpdates();
                        return;
                    }
                }


                break;

            case GRACE_CRIT:
                float nauseaPercentage = 0.5F;

                if (stateProgress > nauseaPercentage) {
                    PlayerUtil.applyPotionIfAbsent(livingEntity, Effects.CONFUSION, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - nauseaPercentage) * 1.5F), 0, false, false);
                }

                PlayerUtil.applyPotionIfAbsent(livingEntity, Effects.WEAKNESS, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - stateProgress)), 0, false, false);

                if (livingEntity.level.random.nextDouble() < (RegenConfig.COMMON.criticalDamageChance.get() / 100F)) {
                    livingEntity.hurt(RegenSources.REGEN_DMG_CRITICAL, livingEntity.level.random.nextFloat() + .5F);
                }
                break;

            case GRACE:
                float weaknessPercentage = 0.5F;
                if (stateProgress > weaknessPercentage) {
                    PlayerUtil.applyPotionIfAbsent(livingEntity, Effects.WEAKNESS, (int) (RegenConfig.COMMON.gracePhaseLength.get() * 20 * (1 - weaknessPercentage) + RegenConfig.COMMON.criticalPhaseLength.get() * 20), 0, false, false);
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
        PlayerUtil.explodeKnockback(player, player.level, new BlockPos(player.position()), RegenConfig.COMMON.regenerativeKnockback.get() / 2, RegenConfig.COMMON.regenKnockbackRange.get());
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
        PlayerUtil.sendMessage(cap.getLiving(), new TranslationTextComponent("regen.messages.regen_warning"), true);
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
        player.addEffect(new EffectInstance(Effects.REGENERATION, RegenConfig.COMMON.postRegenerationDuration.get(), RegenConfig.COMMON.postRegenerationLevel.get() - 1, false, false));
        player.setHealth(player.getMaxHealth());
        player.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);
        cap.setNextSkin(new byte[0]);

        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(heartModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slownessModifier);

        //Trait
        if (RegenConfig.COMMON.traitsEnabled.get() && cap.getLiving().getType() == EntityType.PLAYER) {

            //Reset old Trait
            AbstractTrait old = cap.trait();
            old.remove(cap);

            //Get the new Trait
            AbstractTrait next = cap.getNextTrait();
            if (next.getRegistryName().toString().equals(RegenTraitRegistry.BORING.get().getRegistryName().toString())) {
                next = RegenTraitRegistry.getRandomTrait(cap.getLiving().getRandom(), !(cap.getLiving() instanceof PlayerEntity));
            }
            next.apply(cap);

            //Set new Trait & reset next trait
            cap.setTrait(next);
            cap.setNextTrait(RegenTraitRegistry.BORING.get());

            PlayerUtil.sendMessage(player, new TranslationTextComponent("regen.messages.new_trait", next.translation().getString()), true);
        } else {
            cap.trait().remove(cap);
            cap.setTrait(RegenTraitRegistry.BORING.get());
            cap.trait().apply(cap);
        }
    }

    @Override
    public void onPerformingPost(IRegen cap) {
    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (cap.getLiving() instanceof PlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> living.getCommandSenderWorld().dimension()), new SFXMessage(getRandomSound(living.getRandom(), cap).getRegistryName(), living.getUUID()));
        } else {
            living.playSound(getRandomSound(living.getRandom(), cap), 1, 1);
        }
        living.getAttribute(Attributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
        living.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
        living.setHealth(Math.max(living.getHealth(), 8));
        living.setAbsorptionAmount(0);

        living.clearFire();
        living.ejectPassengers();
        living.removeAllEffects();
        living.stopRiding();

        if (living instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) living;
            if (RegenConfig.COMMON.resetHunger.get()) playerEntity.getFoodData().setFoodLevel(20);
        }

        if (RegenConfig.COMMON.resetOxygen.get()) living.setAirSupply(300);

        cap.extractRegens(1);
    }

}
