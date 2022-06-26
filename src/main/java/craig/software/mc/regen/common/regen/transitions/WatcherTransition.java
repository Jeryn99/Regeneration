package craig.software.mc.regen.common.regen.transitions;

import craig.software.mc.regen.common.entities.Watcher;
import craig.software.mc.regen.common.objects.REntities;
import craig.software.mc.regen.common.objects.RSounds;
import craig.software.mc.regen.common.regen.IRegen;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.network.NetworkDispatcher;
import craig.software.mc.regen.network.messages.POVMessage;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public final class WatcherTransition extends TransitionType {

    public static void createWatcher(LivingEntity player) {
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.transitionType() == TransitionTypes.WATCHER.get()) {
                Direction facing = player.getMotionDirection();
                BlockPos playerPos = player.blockPosition();
                BlockPos spawnPos = playerPos.relative(facing, 4);
                Watcher watcher = new Watcher(player.level);
                watcher.setTarget(player);
                watcher.teleportTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                player.level.addFreshEntity(watcher);
            }
        });
    }

    @Override
    public void tick(IRegen cap) {
        LivingEntity living = cap.getLiving();
        Level world = living.level;
        List<Watcher> watchers = world.getEntities(REntities.WATCHER.get(), living.getBoundingBox().inflate(64), watcher -> watcher.getTarget() == living);

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
    public Vec3 getDefaultPrimaryColor() {
        return Vec3.ZERO;
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return Vec3.ZERO;
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (living instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();

    }
}
