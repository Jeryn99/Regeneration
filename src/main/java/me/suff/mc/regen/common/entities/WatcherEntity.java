package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.util.ViewUtil;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WatcherEntity extends MobEntity {

    public WatcherEntity(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public WatcherEntity(World worldIn) {
        super(REntities.WATCHER.get(), worldIn);
    }

    @Override
    public void tick() {
        super.tick();

        setNoAi(true);
        setDeltaMovement(new Vector3d(0, 0, 0));
        RegenCap.get(this).ifPresent(iRegen -> iRegen.setRegens(0));

        if (getTarget() == null) {
            for (PlayerEntity worldPlayer : level.players()) {
                RegenCap.get(worldPlayer).ifPresent(iRegen -> {
                    if (iRegen.regenState().isGraceful() && iRegen.transitionType() == TransitionTypes.WATCHER.get()) {
                        setTarget(worldPlayer);
                    }
                });

                if (getTarget() == null) {
                    remove();
                }
            }
        } else {
            RegenCap.get(getTarget()).ifPresent(iRegen -> {
                if (iRegen.transitionType() != TransitionTypes.WATCHER.get() || iRegen.regenState() == RegenStates.POST || iRegen.regenState() == RegenStates.ALIVE || getTarget().level.dimension() != level.dimension()) {
                    remove();
                } else {

                    if (iRegen.regenState() == RegenStates.REGENERATING) {
                        remove();
                    } else {
                        lookAt(EntityAnchorArgument.Type.EYES, getTarget().position());
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
    public void knockback(float strength, double ratioX, double ratioZ) {
        //no
    }

    @Override
    protected AxisAlignedBB getBoundingBoxForPose(Pose pose) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
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
