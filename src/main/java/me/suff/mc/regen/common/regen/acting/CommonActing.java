package me.suff.mc.regen.common.regen.acting;

import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.transitions.WatcherTransition;
import me.suff.mc.regen.common.tiles.BioContainerBlockEntity;
import me.suff.mc.regen.common.traits.AbstractTrait;
import me.suff.mc.regen.common.traits.RegenTraitRegistry;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SFXMessage;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;

import java.util.Iterator;
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

                if (livingEntity instanceof ServerPlayer playerEntity) {
                    TriggerManager.FIRST_REGENERATION.trigger(playerEntity);
                }

                float dm = Math.max(1, (livingEntity.level.getDifficulty().getId() + 1) / 3F); // compensating for hard difficulty
                livingEntity.heal(stateProgress * 0.3F * dm);
                livingEntity.setArrowCount(0);

                AABB box = livingEntity.getBoundingBox().inflate(25);
                for (Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(new BlockPos(box.maxX, box.maxY, box.maxZ), new BlockPos(box.minX, box.minY, box.minZ)).iterator(); iterator.hasNext(); ) {
                    BlockPos pos = iterator.next();
                    ServerLevel serverWorld = (ServerLevel) livingEntity.level;
                    BlockState blockState = serverWorld.getBlockState(pos);
                    if (blockState.getBlock() instanceof JarBlock) {
                        BioContainerBlockEntity bioContainerBlockEntity = (BioContainerBlockEntity) serverWorld.getBlockEntity(pos);
                        if (!bioContainerBlockEntity.isValid(BioContainerBlockEntity.Action.ADD)) {
                            continue;
                        }
                        if (livingEntity.level.random.nextBoolean() && serverWorld.getGameTime() % 5 == 0) {
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

                if (livingEntity.level.random.nextDouble() < (RegenConfig.COMMON.criticalDamageChance.get() / 100F)) {
                    livingEntity.hurt(RegenSources.REGEN_DMG_CRITICAL, livingEntity.level.random.nextFloat() + .5F);
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
        PlayerUtil.sendMessage(cap.getLiving(), new TranslatableComponent("regen.messages.regen_warning"), true);
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

        //Trait
        if (RegenConfig.COMMON.traitsEnabled.get() && cap.getLiving().getType() == EntityType.PLAYER) {

            //Reset old Trait
            AbstractTrait old = cap.trait();
            old.remove(cap);

            //Get the new Trait
            AbstractTrait next = cap.getNextTrait();
            if (next.getRegistryName().toString().equals(RegenTraitRegistry.BORING.get().getRegistryName().toString())) {
                next = RegenTraitRegistry.getRandomTrait(cap.getLiving().getRandom(), !(cap.getLiving() instanceof Player));
            }
            next.apply(cap);

            //Set new Trait & reset next trait
            cap.setTrait(next);
            cap.setNextTrait(RegenTraitRegistry.BORING.get());

            PlayerUtil.sendMessage(player, new TranslatableComponent("regen.messages.new_trait", next.translation().getString()), true);
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
        NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> living.getCommandSenderWorld().dimension()), new SFXMessage(getRandomSound(living.getRandom(), cap).getRegistryName(), living.getId()));

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
    }

}
