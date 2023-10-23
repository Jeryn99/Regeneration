package mc.craig.software.regen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ViewUtil {

    private static final float headSize = 0.15f;

    /**
     * Returns whether or not the given target entity is in front of the given entity.
     *
     * @param entity The entity to check from.
     * @param target The target entity to check for.
     * @return True if the target is in front of the entity, false otherwise.
     */
    public static boolean isInFrontOfEntity(LivingEntity entity, Entity target) {
        Vec3 vecTargetsPos = target.position();
        Vec3 vecLook;
        vecLook = entity.getLookAngle();
        Vec3 vecFinal = vecTargetsPos.vectorTo(new Vec3(entity.getX(), entity.getY(), entity.getZ())).normalize();
        vecFinal = new Vec3(vecFinal.x, 0.0D, vecFinal.z);
        return vecFinal.dot(vecLook) < 0.0;
    }

    /**
     * Method that detects whether a tile is the view sight of viewer
     *
     * @param livingBase The viewer entity
     * @param angel      The entity being watched by viewer
     */
    public static boolean isInSight(LivingEntity livingBase, LivingEntity angel) {
        if (viewBlocked(livingBase, angel)) {
            return false;
        }
        return isInFrontOfEntity(livingBase, angel);
    }

    public static boolean viewBlocked(LivingEntity viewer, LivingEntity angel) {
        AABB viewerBoundBox = viewer.getBoundingBox();
        AABB angelBoundingBox = angel.getBoundingBox();
        Vec3[] viewerPoints = {new Vec3(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.minZ), new Vec3(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.minZ),};

        if (viewer instanceof Player) {
            Vec3 pos = new Vec3(viewer.getX(), viewer.getY() + 1.62f, viewer.getZ());
            viewerPoints[0] = pos.add(-headSize, -headSize, -headSize);
            viewerPoints[1] = pos.add(-headSize, -headSize, headSize);
            viewerPoints[2] = pos.add(-headSize, headSize, -headSize);
            viewerPoints[3] = pos.add(-headSize, headSize, headSize);
            viewerPoints[4] = pos.add(headSize, headSize, -headSize);
            viewerPoints[5] = pos.add(headSize, headSize, headSize);
            viewerPoints[6] = pos.add(headSize, -headSize, headSize);
            viewerPoints[7] = pos.add(headSize, -headSize, -headSize);
        }


        Vec3[] angelPoints = {new Vec3(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.minZ), new Vec3(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.minZ),};

        for (int i = 0; i < viewerPoints.length; i++) {
            if (viewer.level().clip(new ClipContext(viewerPoints[i], angelPoints[i], ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, viewer)).getType() == HitResult.Type.MISS) {
                return false;
            }
            if (rayTraceBlocks(viewer, viewer.level(), viewerPoints[i], angelPoints[i], pos -> {
                BlockState state = viewer.level().getBlockState(pos);
                return !canSeeThrough(state, viewer.level(), pos);
            }) == null) return false;
        }

        if (angel.tickCount % 1200 == 0) {
            if (angel.distanceTo(viewer) < 15) {
                viewer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15));
            }
        }

        return true;
    }

    public static boolean viewBlocked(LivingEntity viewer, BlockState blockState, BlockPos blockPos) {
        AABB viewerBoundBox = viewer.getBoundingBox();
        AABB angelBoundingBox = blockState.getShape(viewer.level(), blockPos).bounds();
        Vec3[] viewerPoints = {new Vec3(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.minZ), new Vec3(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.minZ),};

        if (viewer instanceof Player) {
            Vec3 pos = new Vec3(viewer.getX(), viewer.getY() + 1.62f, viewer.getZ());
            viewerPoints[0] = pos.add(-headSize, -headSize, -headSize);
            viewerPoints[1] = pos.add(-headSize, -headSize, headSize);
            viewerPoints[2] = pos.add(-headSize, headSize, -headSize);
            viewerPoints[3] = pos.add(-headSize, headSize, headSize);
            viewerPoints[4] = pos.add(headSize, headSize, -headSize);
            viewerPoints[5] = pos.add(headSize, headSize, headSize);
            viewerPoints[6] = pos.add(headSize, -headSize, headSize);
            viewerPoints[7] = pos.add(headSize, -headSize, -headSize);
        }


        Vec3[] angelPoints = {new Vec3(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.minZ), new Vec3(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.minZ),};

        for (int i = 0; i < viewerPoints.length; i++) {
            if (viewer.level().clip(new ClipContext(viewerPoints[i], angelPoints[i], ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, viewer)).getType() == HitResult.Type.MISS) {
                return false;
            }
            if (rayTraceBlocks(viewer, viewer.level(), viewerPoints[i], angelPoints[i], pos -> {
                BlockState state = viewer.level().getBlockState(pos);
                return !canSeeThrough(state, viewer.level(), pos);
            }) == null) return false;
        }
        return true;
    }


    /**
     * Performs a ray trace in the given world, starting at the given start position and ending at the
     * given end position. If the given stop condition is satisfied at any point along the trace, the
     * trace will be terminated and the result up to that point will be returned.
     *
     * @param livingEntity the entity performing the trace
     * @param world        the world in which the trace is performed
     * @param startPos     the starting position of the trace
     * @param endPos       the ending position of the trace
     * @param stopOn       a condition that, when satisfied, will cause the trace to stop and return the result
     *                     up to that point
     * @return the result of the trace, or null if the trace failed to find any satisfactory results
     */
    @Nullable
    private static HitResult rayTraceBlocks(LivingEntity livingEntity, Level world, Vec3 startPos, Vec3 endPos, Predicate<BlockPos> stopOn) {
        // Return null if the start or end positions are NaN
        if (Double.isNaN(startPos.x) || Double.isNaN(startPos.y) || Double.isNaN(startPos.z) ||
                Double.isNaN(endPos.x) || Double.isNaN(endPos.y) || Double.isNaN(endPos.z)) {
            return null;
        }

        int startX = Mth.floor(startPos.x);
        int startY = Mth.floor(startPos.y);
        int startZ = Mth.floor(startPos.z);
        int endX = Mth.floor(endPos.x);
        int endY = Mth.floor(endPos.y);
        int endZ = Mth.floor(endPos.z);

        // Create a BlockPos object representing the starting position of the trace
        BlockPos startBlockPos = new BlockPos(startX, startY, startZ);

        // If the stop condition is satisfied at the starting position, perform a clip and return the result
        if (stopOn.test(startBlockPos)) {
            HitResult raytraceresult = world.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
            if (raytraceresult != null) {
                return raytraceresult;
            }
        }

        // Perform the ray trace
        int maxSteps = 200;
        while (maxSteps-- >= 0) {
            // Return null if the start position becomes NaN
            if (Double.isNaN(startPos.x) || Double.isNaN(startPos.y) || Double.isNaN(startPos.z)) {
                return null;
            }

            // Return null if the start position has reached the end position
            if (startX == endX && startY == endY && startZ == endZ) {
                return null;
            }

            // Determine the direction of the trace
            boolean xStep = true;
            boolean yStep = true;
            boolean zStep = true;
            double xDist = 999.0D;
            double yDist = 999.0D;
            double zDist = 999.0D;

            if (endX > startX) {
                xDist = (double) startX + 1.0D;
            } else if (endX < startX) {
                xDist = (double) startX + 0.0D;
            } else {
                xStep = false;
            }

            if (endY > startY) {
                yDist = (double) startY + 1.0D;
            } else if (endY < startY) {
                yDist = (double) startY + 0.0D;
            } else {
                yStep = false;
            }

            if (endZ > startZ) {
                zDist = (double) startZ + 1.0D;
            } else if (endZ < startZ) {
                zDist = (double) startZ + 0.0D;
            } else {
                zStep = false;
            }

            double xMove = 999.0D;
            double yMove = 999.0D;
            double zMove = 999.0D;
            double xDelta = endPos.x - startPos.x;
            double yDelta = endPos.y - startPos.y;
            double zDelta = endPos.z - startPos.z;

            if (xStep) {
                xMove = (xDist - startPos.x) / xDelta;
            }

            if (yStep) {
                yMove = (yDist - startPos.y) / yDelta;
            }

            if (zStep) {
                zMove = (zDist - startPos.z) / zDelta;
            }

            if (xMove == -0.0D) {
                xMove = -1.0E-4D;
            }

            if (yMove == -0.0D) {
                yMove = -1.0E-4D;
            }

            if (zMove == -0.0D) {
                zMove = -1.0E-4D;
            }

            Direction facing;

            if (xMove < yMove && xMove < zMove) {
                facing = endX > startX ? Direction.WEST : Direction.EAST;
                startPos = new Vec3(xDist, startPos.y + yDelta * xMove, startPos.z + zDelta * xMove);
            } else if (yMove < zMove) {
                facing = endY > startY ? Direction.DOWN : Direction.UP;
                startPos = new Vec3(startPos.x + xDelta * yMove, yDist, startPos.z + zDelta * yMove);
            } else {
                facing = endZ > startZ ? Direction.NORTH : Direction.SOUTH;
                startPos = new Vec3(startPos.x + xDelta * zMove, startPos.y + yDelta * zMove, zDist);
            }

            startX = Mth.floor(startPos.x) - (facing == Direction.EAST ? 1 : 0);
            startY = Mth.floor(startPos.y) - (facing == Direction.UP ? 1 : 0);
            startZ = Mth.floor(startPos.z) - (facing == Direction.SOUTH ? 1 : 0);
            BlockPos blockpos = new BlockPos(startX, startY, startZ);
            BlockState blockstate = world.getBlockState(blockpos);

            if (stopOn.test(blockpos)) {
                HitResult raytraceresult = world.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
                if (raytraceresult != null) {
                    return raytraceresult;
                }
            }
        }

        // Return null if the trace failed to find any satisfactory results
        return null;
    }

    /**
     * Determines if a given block state can be seen through by the player.
     *
     * @param blockState the block state to be checked
     * @param world      the world in which the block exists
     * @param pos        the position of the block in the world
     * @return true if the block can be seen through, false otherwise
     */
    public static boolean canSeeThrough(BlockState blockState, Level world, BlockPos pos) {
        // Check if the block can be occluded and if it has a solid render in the world
        if (!blockState.canOcclude() || !blockState.isSolidRender(world, pos)) {
            return true;
        }

        Block block = blockState.getBlock();

        // Check if the block is a door in the upper half
        if (block instanceof DoorBlock) {
            return blockState.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER;
        }

        // Check if the block has an empty support shape in the world
        return blockState.getBlockSupportShape(world, pos) == Shapes.empty();
    }


}