package me.swirtzly.regen.common.regen.acting;

import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.network.Dispatcher;
import me.swirtzly.regen.network.messages.SFXMessage;
import me.swirtzly.regen.network.messages.SyncMessage;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RegenSources;
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
        SoundEvent[] SOUNDS = cap.getTransitionType().create().getRegeneratingSounds();
        return SOUNDS[random.nextInt(SOUNDS.length)];
    }

    @Override
    public void onRegenTick(IRegen cap) {
        LivingEntity player = cap.getLiving();
        float stateProgress = (float) cap.getStateManager().getStateProgress();

        switch (cap.getCurrentState()) {
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
        if (!player.getAttribute(Attributes.field_233818_a_).hasModifier(heartModifier)) {
            player.getAttribute(Attributes.field_233818_a_).func_233769_c_(heartModifier);
        }
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        PlayerUtil.sendMessage(cap.getLiving(), new TranslationTextComponent("regeneration.messages.regen_warning"), true);
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (!cap.getLiving().getAttribute(Attributes.field_233821_d_).hasModifier(slownessModifier)) {
            cap.getLiving().getAttribute(Attributes.field_233821_d_).func_233769_c_(slownessModifier);
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {
        LivingEntity player = cap.getLiving();
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, RegenConfig.COMMON.postRegenerationDuration.get() * 2, RegenConfig.COMMON.postRegenerationLevel.get() - 1, false, false));
        player.setHealth(player.getMaxHealth());
        player.setAbsorptionAmount(RegenConfig.COMMON.absorbtionLevel.get() * 2);
        //cap.setNextSkin(RegenUtil.NO_SKIN);
    }

    @Override
    public void onPerformingPost(IRegen cap) {
    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        LivingEntity living = cap.getLiving();
        Dispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> living.getEntityWorld().func_234923_W_()), new SFXMessage(getRandomSound(living.getRNG(), cap).getRegistryName(), living.getUniqueID()));
        living.getAttribute(Attributes.field_233818_a_).removeModifier(MAX_HEALTH_ID);
        living.getAttribute(Attributes.field_233821_d_).removeModifier(SLOWNESS_ID);
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
