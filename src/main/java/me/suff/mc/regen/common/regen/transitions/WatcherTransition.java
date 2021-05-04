package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.entities.WatcherEntity;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.POVMessage;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public final class WatcherTransition extends TransitionType {

    public static void createWatcher(LivingEntity player) {
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.transitionType() == TransitionTypes.WATCHER.get()) {
                Direction facing = player.getMotionDirection();
                BlockPos playerPos = player.blockPosition();
                BlockPos spawnPos = playerPos.relative(facing, 4);
                WatcherEntity watcherEntity = new WatcherEntity(player.level);
                watcherEntity.setTarget(player);
                watcherEntity.teleportTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                player.level.addFreshEntity(watcherEntity);
            }
        });
    }

    @Override
    public void tick(IRegen cap) {
        LivingEntity living = cap.getLiving();
        World world = living.level;
        List< WatcherEntity > watchers = world.getEntities(REntities.WATCHER.get(), living.getBoundingBox().inflate(64), watcherEntity -> watcherEntity.getTarget() == living);

        if (watchers.isEmpty()) {
            WatcherTransition.createWatcher(living);
        }
    }

    @Override
    public int getAnimationLength() {
        return 340;
    }


    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_WATCHER.get()};
    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return Vector3d.ZERO;
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return Vector3d.ZERO;
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (living instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();

    }
}
