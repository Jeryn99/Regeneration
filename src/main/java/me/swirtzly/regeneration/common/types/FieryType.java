package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.rendering.types.FieryRenderer;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.Iterator;

/**
 * Created by Craig on 16/09/2018.
 */
public class FieryType implements RegenType<FieryRenderer> {

    private SoundEvent[] SOUNDS = new SoundEvent[]{RegenObjects.Sounds.REGENERATION_0.get(), RegenObjects.Sounds.REGENERATION_1.get(), RegenObjects.Sounds.REGENERATION_2.get(), RegenObjects.Sounds.REGENERATION_3.get(), RegenObjects.Sounds.REGENERATION_4.get(), RegenObjects.Sounds.REGENERATION_5.get(), RegenObjects.Sounds.REGENERATION_6.get(), RegenObjects.Sounds.REGENERATION_7.get()};

    @Override
    public void onUpdateMidRegen(IRegen capability) {

        LivingEntity livingEntity = capability.getLivingEntity();
        livingEntity.clearFire();

        if (!livingEntity.level.isClientSide) {
            if (capability.getLivingEntity() instanceof ServerPlayerEntity) {
                PlayerUtil.setPerspective((ServerPlayerEntity) livingEntity, true, false);
            }
        }

        if (livingEntity.level.isClientSide) return;

        if (livingEntity.level.getBlockState(livingEntity.getCommandSenderBlockPosition()).getBlock() instanceof FireBlock)
            livingEntity.level.removeBlock(livingEntity.getCommandSenderBlockPosition(), false);

        if (!PlayerUtil.isAboveZeroGrid(livingEntity)) {
            RegenUtil.regenerationExplosion(livingEntity);
            double x = livingEntity.x + livingEntity.getRandom().nextGaussian() * 2;
            double y = livingEntity.y + 0.5 + livingEntity.getRandom().nextGaussian() * 2;
            double z = livingEntity.z + livingEntity.getRandom().nextGaussian() * 2;
            livingEntity.level.explode(livingEntity, x, y, z, 0.1F, RegenConfig.COMMON.fieryRegen.get(), Explosion.Mode.NONE);
            Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(livingEntity.getCommandSenderBlockPosition().north().west(), livingEntity.getCommandSenderBlockPosition().south().east()).iterator();
            while (iterator.hasNext()) {
                iterator.forEachRemaining((blockPos -> {
                    if (livingEntity.level.getBlockState(blockPos).getBlock() instanceof FireBlock) {
                        livingEntity.level.removeBlock(blockPos, false);
                    }
                }));
            }
        }
    }

    @Override
    public void onFinishRegeneration(IRegen capability) {
        if (capability.getLivingEntity() instanceof ServerPlayerEntity) {
            PlayerUtil.setPerspective((ServerPlayerEntity) capability.getLivingEntity(), false, true);
        }
        capability.setAnimationTicks(0);
    }

    @Override
    public int getAnimationLength() {
        return 280; // 14 seconds of 20 ticks
    }

    @Override
    public double getAnimationProgress(IRegen cap) {
        return Math.min(1, cap.getAnimationTicks() / (double) getAnimationLength());
    }


    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return SOUNDS;
    }

    @Override
    public Vec3d getDefaultPrimaryColor() {
        return new Vec3d(0.93F, 0.61F, 0F);
    }

    @Override
    public Vec3d getDefaultSecondaryColor() {
        return new Vec3d(1F, 0.5F, 0.18F);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(Regeneration.MODID, "fiery");
    }

    @Override
    public FieryRenderer getRenderer() {
        return FieryRenderer.INSTANCE;
    }

}
