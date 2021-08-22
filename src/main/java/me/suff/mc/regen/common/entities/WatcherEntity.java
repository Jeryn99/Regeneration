package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.util.ViewUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class WatcherEntity extends Mob {

    public WatcherEntity(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
    }

    public WatcherEntity(Level worldIn) {
        super(REntities.WATCHER.get(), worldIn);
    }

    @Override
    public void tick() {
        super.tick();

        setNoAi(true);
        setDeltaMovement(new Vec3(0, 0, 0));
        RegenCap.get(this).ifPresent(iRegen -> iRegen.setRegens(0));

        if (getTarget() == null) {
            for (Player worldPlayer : level.players()) {
                RegenCap.get(worldPlayer).ifPresent(iRegen -> {
                    if (iRegen.regenState().isGraceful() && iRegen.transitionType() == TransitionTypes.WATCHER.get()) {
                        setTarget(worldPlayer);
                    }
                });

                if (getTarget() == null) {
                    remove(RemovalReason.DISCARDED);
                }
            }
        } else {
            RegenCap.get(getTarget()).ifPresent(iRegen -> {
                if (iRegen.transitionType() != TransitionTypes.WATCHER.get() || iRegen.regenState() == RegenStates.POST || iRegen.regenState() == RegenStates.ALIVE || getTarget().level.dimension() != level.dimension()) {
                    remove(RemovalReason.DISCARDED);
                } else {

                    if (iRegen.regenState() == RegenStates.REGENERATING) {
                        remove(RemovalReason.DISCARDED);
                    } else {
                        lookAt(EntityAnchorArgument.Anchor.EYES, getTarget().position());
                        if (tickCount % 100 == 0 && !ViewUtil.isInSight(getTarget(), this)) {
                            teleportRandomly();
                        }
                    }
                }
            });
        }

    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void knockback(double p_147241_, double p_147242_, double p_147243_) {

    }

    @Override
    protected AABB getBoundingBoxForPose(Pose pose) {
        return new AABB(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    protected boolean teleportRandomly() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = getTarget().getX() + this.random.nextInt(5);
            double d1 = getTarget().getY() + this.random.nextInt(5);
            double d2 = getTarget().getZ() + this.random.nextInt(5);
            return this.randomTeleport(d0, d1, d2, false);
        } else {
            return false;
        }
    }
}
