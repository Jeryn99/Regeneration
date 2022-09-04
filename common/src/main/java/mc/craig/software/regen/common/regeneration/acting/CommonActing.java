package mc.craig.software.regen.common.regeneration.acting;

import mc.craig.software.regen.RegenConfig;
import mc.craig.software.regen.common.regeneration.RegenerationData;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDmgSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

class CommonActing implements Acting {

    public static final Acting INSTANCE = new CommonActing();
    private final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782"), MAX_HEALTH_ID = UUID.fromString("5d6f0ba2-1286-46fc-b896-461c5cfd99cc");
    private final double HEART_REDUCTION = 0.5, SPEED_REDUCTION = 0.35;
    private final AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE), heartModifier = new AttributeModifier(MAX_HEALTH_ID, "short-heart", -HEART_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE);

    public CommonActing() {
    }


    @Override
    public void onRegenTick(RegenerationData cap) {
        LivingEntity livingEntity = cap.getPlayer();
        float stateProgress = (float) cap.stateManager().stateProgress();

        switch (cap.getCurrentState()) {
            case POST:

                break;
            case REGENERATING:
                float dm = Math.max(1, (livingEntity.level.getDifficulty().getId() + 1) / 3F); // compensating for hard difficulty
                livingEntity.heal(stateProgress * 0.3F * dm);
                livingEntity.setArrowCount(0);
                break;

            case GRACE_CRIT:
                float nauseaPercentage = 0.5F;

                if (stateProgress > nauseaPercentage) {
                    PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.CONFUSION, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - nauseaPercentage) * 1.5F), 0, false, false);
                }

                PlayerUtil.applyPotionIfAbsent(livingEntity, MobEffects.WEAKNESS, (int) (RegenConfig.COMMON.criticalPhaseLength.get() * 20 * (1 - stateProgress)), 0, false, false);

                if (livingEntity.level.random.nextDouble() < (RegenConfig.COMMON.criticalDamageChance.get() / 100F)) {
                    livingEntity.hurt(RegenDmgSource.REGEN_DMG_CRITICAL, livingEntity.level.random.nextFloat() + .5F);
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
                throw new IllegalStateException("Unknown state " + cap.getCurrentState());
        }
    }

    @Override
    public void onEnterGrace(RegenerationData cap) {
        LivingEntity player = cap.getPlayer();
        PlayerUtil.explodeKnockback(player, player.level, new BlockPos(player.position()), RegenConfig.COMMON.regenerativeKnockback.get() / 2, RegenConfig.COMMON.regenKnockbackRange.get());
        // Reduce number of hearts, but compensate with absorption
        player.setAbsorptionAmount(player.getMaxHealth() * (float) HEART_REDUCTION);
        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(heartModifier)) {
            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(heartModifier);
        }
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public void onHandsStartGlowing(RegenerationData cap) {
        PlayerUtil.sendMessage(cap.getPlayer(), Component.translatable("regen.messages.regen_warning"), true);
    }

    @Override
    public void onGoCritical(RegenerationData cap) {
        if (!cap.getPlayer().getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) {
            cap.getPlayer().getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(slownessModifier);
        }
    }

    @Override
    public void onRegenFinish(RegenerationData cap) {
        LivingEntity player = cap.getPlayer();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, RegenConfig.COMMON.postRegenerationDuration.get(), RegenConfig.COMMON.postRegenerationLevel.get() - 1, false, false));
        player.setHealth(player.getMaxHealth());
        player.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);

        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(heartModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slownessModifier);
    }

    @Override
    public void onPerformingPost(RegenerationData cap) {
    }

    @Override
    public void onRegenTrigger(RegenerationData cap) {
        LivingEntity living = cap.getPlayer();
      //TODO  NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> living.getCommandSenderWorld().dimension()), new SFXMessage(getRandomSound(living.getRandom(), cap).getLocation(), living.getId()));

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
