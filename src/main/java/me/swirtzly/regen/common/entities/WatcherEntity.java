package me.swirtzly.regen.common.entities;

import me.swirtzly.regen.common.objects.REntities;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.util.ViewUtil;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
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

        setNoAI(true);
        setMotion(new Vector3d(0, 0, 0));
        RegenCap.get(this).ifPresent(iRegen -> iRegen.setRegens(0));

        if (getAttackTarget() == null) {
            for (PlayerEntity worldPlayer : world.getPlayers()) {
                RegenCap.get(worldPlayer).ifPresent(iRegen -> {
                    if (iRegen.getCurrentState().isGraceful() && iRegen.getTransitionType() == TransitionTypes.WATCHER) {
                        setAttackTarget(worldPlayer);
                    }
                });

                if (getAttackTarget() == null) {
                    remove();
                }
            }
        } else {
            RegenCap.get(getAttackTarget()).ifPresent(iRegen -> {
                if (iRegen.getTransitionType() != TransitionTypes.WATCHER || iRegen.getCurrentState() == RegenStates.POST || iRegen.getCurrentState() == RegenStates.ALIVE || getAttackTarget().world.getDimensionKey() != world.getDimensionKey()) {
                    remove();
                } else {

                    if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                        remove();
                    } else {
                        lookAt(EntityAnchorArgument.Type.EYES, getAttackTarget().getPositionVec());
                        if (ticksExisted % 100 == 0 && !ViewUtil.isInSight(getAttackTarget(), this)) {
                            teleportRandomly();
                        }
                    }
                }
            });
        }

    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void applyKnockback(float strength, double ratioX, double ratioZ) {
        //no
    }

    @Override
    protected AxisAlignedBB getBoundingBox(Pose pose) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected boolean teleportRandomly() {
        if (!this.world.isRemote() && this.isAlive()) {
            double d0 = getAttackTarget().getPosX() + this.rand.nextInt(5);
            double d1 = getAttackTarget().getPosY() + this.rand.nextInt(5);
            double d2 = getAttackTarget().getPosZ() + this.rand.nextInt(5);
            return this.attemptTeleport(d0, d1, d2, false);
        } else {
            return false;
        }
    }
}
