package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.NetworkDispatcher;
import mc.craig.software.regen.network.messages.POVMessage;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.Iterator;

public class FieryTransition extends TransitionType {


    @Override
    public void onUpdateMidRegen(IRegen capability) {

        LivingEntity livingEntity = capability.getLiving();
        livingEntity.clearFire();

        if (!livingEntity.level.isClientSide) {
            if (capability.getLiving() instanceof ServerPlayer) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) capability.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
            }
        }

        if (livingEntity.level.isClientSide) return;

        if (livingEntity.getType() == EntityType.PLAYER) {
            BlockPos livingPos = new BlockPos(livingEntity.position());
            if (livingEntity.level.getBlockState(livingPos).getBlock() instanceof FireBlock)
                livingEntity.level.removeBlock(livingPos, false);


            PlayerUtil.regenerationExplosion(livingEntity);
            double x = livingEntity.getX() + livingEntity.getRandom().nextGaussian() * 2;
            double y = livingEntity.getY() + 0.5 + livingEntity.getRandom().nextGaussian() * 2;
            double z = livingEntity.getZ() + livingEntity.getRandom().nextGaussian() * 2;
            if (!PlayerUtil.isPlayerAboveZeroGrid(livingEntity)) {
                livingEntity.level.explode(livingEntity, x, y, z, 0.1F, RegenConfig.COMMON.fieryRegen.get(), Explosion.BlockInteraction.NONE);
            }
            Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(new BlockPos(livingEntity.position()).north().west(), new BlockPos(livingEntity.position()).south().east()).iterator();
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
        if (capability.getLiving() instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) capability.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
        capability.setUpdateTicks(0);
        capability.syncToClients(null);
    }

    @Override
    public int getAnimationLength() {
        return 280; // 14 seconds of 20 ticks
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_0.get(), RSounds.REGENERATION_1.get(), RSounds.REGENERATION_2.get(), RSounds.REGENERATION_3.get(), RSounds.REGENERATION_4.get(), RSounds.REGENERATION_5.get(), RSounds.REGENERATION_6.get(), RSounds.REGENERATION_7.get()};
    }

    @Override
    public Vec3 getDefaultPrimaryColor() {
        return new Vec3(0.69411767f, 0.74509805f, 0.23529412f);
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return new Vec3(0.7137255f, 0.75686276f, 0.25490198f);
    }

}
