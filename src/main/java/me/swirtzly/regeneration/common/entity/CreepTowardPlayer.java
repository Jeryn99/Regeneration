package me.swirtzly.regeneration.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector3d;

/**
 * Created by Swirtzly
 * on 22/09/2019 @ 21:37
 */
public class CreepTowardPlayer extends EntityAIAttackMelee {
    private final EntityWatcher eyes;

    public CreepTowardPlayer(EntityWatcher creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
        eyes = creature;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (isPlayerLookingInMyGeneralDirection())
            return false;
        return super.shouldContinueExecuting();
    }

    private boolean isPlayerLookingInMyGeneralDirection() {
        BlockPos position = eyes.getPosition().up();
        float blockLight = eyes.world.getLight(position, false);
        if (blockLight >= 8)
            return true;

        Vector3d selfPos = new Vector3d(eyes.posX, eyes.posY, eyes.posZ);
        EntityLivingBase target = eyes.getAttackTarget();
        if (target == null)
            return false;
        Vector3d playerPos = new Vector3d(target.posX, target.posY, target.posZ);
        Vec3d lookVec = target.getLookVec();
        Vector3d playerLook = new Vector3d(lookVec.x, lookVec.y, lookVec.z);
        playerLook.normalize();

        playerPos.sub(selfPos);
        playerPos.normalize();

        return playerLook.dot(playerPos) < 0;
    }

    @Override
    public boolean shouldExecute() {
        if (isPlayerLookingInMyGeneralDirection())
            return false;
        return super.shouldExecute();
    }
}