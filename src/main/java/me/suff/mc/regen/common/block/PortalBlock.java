package me.suff.mc.regen.common.block;

import me.suff.mc.regen.common.objects.RParticles;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import static me.suff.mc.regen.util.RConstants.MODID;

/* Created by Craig on 16/03/2021 */
public class PortalBlock extends Block {

    public static final RegistryKey< World > GALLIFREY = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MODID, "gallifrey"));


    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public PortalBlock(AbstractBlock.Properties p_i48406_1_) {
        super(p_i48406_1_);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        if (world instanceof ServerWorld && !entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions() && VoxelShapes.joinIsNotEmpty(VoxelShapes.create(entity.getBoundingBox().move((double) (-blockPos.getX()), (double) (-blockPos.getY()), (double) (-blockPos.getZ()))), blockState.getShape(world, blockPos), IBooleanFunction.AND)) {

            RegistryKey< World > registrykey = world.dimension() == GALLIFREY ? World.OVERWORLD : GALLIFREY;
            ServerWorld serverworld = ((ServerWorld) world).getServer().getLevel(registrykey);
            if (serverworld == null) {
                return;
            }

            serverworld.getServer().addTickable(() -> {
                        if (entity instanceof ServerPlayerEntity) {
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                            serverPlayerEntity.teleportTo(serverworld, blockPos.getX(), serverworld.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, world.getChunkAt(blockPos).getPos().x, world.getChunkAt(blockPos).getPos().z), blockPos.getZ(), 1, 1);
                        }
                    }
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        double d0 = (double) p_180655_3_.getX() + p_180655_4_.nextDouble() * 2;
        double d1 = (double) p_180655_3_.getY() + 1D;
        double d2 = (double) p_180655_3_.getZ() + p_180655_4_.nextDouble() * 2;
        p_180655_2_.addParticle(RParticles.CONTAINER.get(), d0, d1, d2, 1.0D, 2.0D, 0.0D);
    }

    public boolean canBeReplaced(BlockState p_225541_1_, Fluid p_225541_2_) {
        return false;
    }
}