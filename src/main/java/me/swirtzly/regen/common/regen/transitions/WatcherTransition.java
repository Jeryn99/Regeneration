package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.WatcherTransitionRenderer;
import me.swirtzly.regen.common.entities.WatcherEntity;
import me.swirtzly.regen.common.objects.REntities;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.POVMessage;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public final class WatcherTransition implements TransitionType<WatcherTransitionRenderer> {

    public static void createWatcher(LivingEntity player) {
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.getTransitionType() == TransitionTypes.WATCHER) {
                Direction facing = player.getAdjustedHorizontalFacing();
                BlockPos playerPos = player.getPosition();
                BlockPos spawnPos = playerPos.offset(facing, 4);
                WatcherEntity watcherEntity = new WatcherEntity(player.world);
                watcherEntity.setAttackTarget(player);
                watcherEntity.setPositionAndUpdate(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                player.world.addEntity(watcherEntity);
            }
        });
    }

    @Override
    public void tick(IRegen cap) {
        LivingEntity living = cap.getLiving();
        World world = living.world;
        List<WatcherEntity> watchers = world.getEntitiesWithinAABB(REntities.WATCHER.get(), living.getBoundingBox().grow(64), watcherEntity -> watcherEntity.getAttackTarget() == living);

        if (watchers.isEmpty()) {
            WatcherTransition.createWatcher(living);
        }
    }

    @Override
    public int getAnimationLength() {
        return 340;
    }

    @Override
    public WatcherTransitionRenderer getRenderer() {
        return WatcherTransitionRenderer.INSTANCE;
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
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(PointOfView.FIRST_PERSON));
        }
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(PointOfView.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();

    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "watcher");
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }
}
