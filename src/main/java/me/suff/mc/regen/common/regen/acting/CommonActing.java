package me.suff.mc.regen.common.regen.acting;

import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.transitions.WatcherTransition;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SFXMessage;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

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
        SoundEvent[] soundEvents = cap.getTransitionType().get().getRegeneratingSounds();
        return soundEvents[random.nextInt(soundEvents.length)];
    }

    @Override
    public void onRegenTick(IRegen cap) {
        LivingEntity player = cap.getLiving();
        float stateProgress = (float) cap.getStateManager().getStateProgress();

        switch (cap.getCurrentState()) {
            case POST:
                if (!PlayerUtil.POTIONS.isEmpty()) {
                    if (player.ticksExisted % 210 == 0) {
                        PlayerUtil.applyPotionIfAbsent(player, PlayerUtil.POTIONS.get(player.world.rand.nextInt(PlayerUtil.POTIONS.size())), player.world.rand.nextInt(400), 1, false, false);
                    }
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
                    player.attackEntityFrom(RegenSources.REGEN_DMG_CRITICAL, player.world.rand.nextFloat() + .5F);

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
                throw new IllegalStateException("Unknown state " + cap.getCurrentState());
        }
    }

    @Override
    public void onEnterGrace(IRegen cap) {
        LivingEntity player = cap.getLiving();
        PlayerUtil.explodeKnockback(player, player.world, new BlockPos(player.getPositionVec()), RegenConfig.COMMON.regenerativeKnockback.get() / 2, RegenConfig.COMMON.regenKnockbackRange.get());
        // Reduce number of hearts, but compensate with absorption
        player.setAbsorptionAmount(player.getMaxHealth() * (float) HEART_REDUCTION);
        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(heartModifier)) {
            player.getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(heartModifier);
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
            cap.getLiving().getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(slownessModifier);
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {
        LivingEntity player = cap.getLiving();
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, RegenConfig.COMMON.postRegenerationDuration.get(), RegenConfig.COMMON.postRegenerationLevel.get() - 1, false, false));
        player.setHealth(player.getMaxHealth());
        player.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);
        cap.setNextSkin(new byte[0]);

        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(heartModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slownessModifier);

        //Trait
        if (RegenConfig.COMMON.traitsEnabled.get() && cap.getLiving().getType() == EntityType.PLAYER) {

            //Reset old Trait
            Traits.ITrait old = cap.getTrait();
            old.reset(cap);

            //Get the new Trait
            Traits.ITrait next = cap.getNextTrait();
            if (next.getRegistryName().toString().equals(Traits.BORING.getRegistryName().toString())) {
                next = Traits.getRandomTrait(cap.getLiving().getRNG(), !(cap.getLiving() instanceof PlayerEntity));
            }
            next.apply(cap);

            //Set new Trait & reset next trait
            cap.setTrait(next);
            cap.setNextTrait(Traits.BORING.get());

            PlayerUtil.sendMessage(player, new TranslationTextComponent("regen.messages.new_trait", next.getTranslation().getString()), true);
        } else {
            cap.getTrait().reset(cap);
            cap.setTrait(Traits.BORING.get());
            cap.getTrait().apply(cap);
        }
    }

    @Override
    public void onPerformingPost(IRegen cap) {
    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (cap.getLiving() instanceof PlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> living.getEntityWorld().getDimensionKey()), new SFXMessage(getRandomSound(living.getRNG(), cap).getRegistryName(), living.getUniqueID()));
        } else {
            living.playSound(getRandomSound(living.getRNG(), cap), 1, 1);
        }
        living.getAttribute(Attributes.MAX_HEALTH).removeModifier(MAX_HEALTH_ID);
        living.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
        living.setHealth(Math.max(living.getHealth(), 8));
        living.setAbsorptionAmount(0);

        living.extinguish();
        living.removePassengers();
        living.clearActivePotions();
        living.stopRiding();

        if (living instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) living;
            if (RegenConfig.COMMON.resetHunger.get()) playerEntity.getFoodStats().setFoodLevel(20);
        }

        if (RegenConfig.COMMON.resetOxygen.get()) living.setAir(300);

        cap.extractRegens(1);
    }

}
