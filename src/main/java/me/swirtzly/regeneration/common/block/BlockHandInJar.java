package me.swirtzly.regeneration.common.block;


import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.tiles.RegenShapes;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.EnumMap;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 21/08/2019 @ 17:31
 */
public class BlockHandInJar extends Block {

    protected final EnumMap<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);


    public BlockHandInJar() {
        super(Block.Properties.create(Material.PISTON));

        for (Direction value : Direction.values()) {
            shapes.put(value, VoxelShapes.func_216387_a(RegenShapes.HAND_JAR, value));
        }
    }


    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            world.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, getFacingFromEntity(pos, entity)), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) return false;

        if (worldIn.getTileEntity(pos) instanceof TileEntityHandInJar) {
            TileEntityHandInJar jar = (TileEntityHandInJar) worldIn.getTileEntity(pos);

            RegenCap.get(player).ifPresent((data) -> {

                if (jar.getLindosAmont() >= 100 && data.getState() == PlayerUtil.RegenState.ALIVE && player.isSneaking() && jar.hasHand()) {
                    PlayerUtil.lookAt(jar.getPos().getX(), jar.getPos().getY(), jar.getPos().getZ(), player);
                    jar.setLindosAmont(jar.getLindosAmont() - 100);
                    data.receiveRegenerations(1);
                    data.setSyncingFromJar(true);
                    jar.destroyHand();
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, 1.0F, 0.7F);
                    data.synchronise();
                    jar.sendUpdates();
                    //return true;
                }

                if (data.getState() != PlayerUtil.RegenState.REGENERATING && !player.isSneaking()) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, jar, jar.getPos());
                    //       return true;
                }
                //   return false;
            });

        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityHandInJar();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isVariableOpacity() {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        //return shapes.get(state.get(BlockStateProperties.HORIZONTAL_FACING));
        return super.getShape(state, worldIn, pos, context);
    }
}
