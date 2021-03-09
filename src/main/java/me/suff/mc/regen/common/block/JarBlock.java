package me.suff.mc.regen.common.block;

import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.tiles.JarTile;
import me.suff.mc.regen.util.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends DirectionalBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty IS_OPEN = BooleanProperty.create("is_open");

    public static final VoxelShape NORTH = VoxelShapeUtils.rotate(BlockShapes.JAR.get(), Rotation.CLOCKWISE_180);
    public static final VoxelShape EAST = VoxelShapeUtils.rotate(BlockShapes.JAR.get(), Rotation.COUNTERCLOCKWISE_90);
    public static final VoxelShape SOUTH = VoxelShapeUtils.rotate(BlockShapes.JAR.get(), Rotation.NONE);
    public static final VoxelShape WEST = VoxelShapeUtils.rotate(BlockShapes.JAR.get(), Rotation.CLOCKWISE_90);

    public static final VoxelShape NORTH_OPEN = VoxelShapeUtils.rotate(BlockShapes.JAR_OPEN.get(), Rotation.CLOCKWISE_180);
    public static final VoxelShape EAST_OPEN = VoxelShapeUtils.rotate(BlockShapes.JAR_OPEN.get(), Rotation.COUNTERCLOCKWISE_90);
    public static final VoxelShape SOUTH_OPEN = VoxelShapeUtils.rotate(BlockShapes.JAR_OPEN.get(), Rotation.NONE);
    public static final VoxelShape WEST_OPEN = VoxelShapeUtils.rotate(BlockShapes.JAR_OPEN.get(), Rotation.CLOCKWISE_90);


    public JarBlock() {
        super(Properties.create(Material.IRON).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(IS_OPEN)) {
            switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
                case EAST:
                    return EAST_OPEN;
                case SOUTH:
                    return SOUTH_OPEN;
                case WEST:
                    return WEST_OPEN;
                default:
                    return NORTH_OPEN;
            }
        }
        switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            default:
                return NORTH;
        }
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new JarTile();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY()).with(IS_OPEN, true);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder< Block, BlockState > builder) {
        builder.add(FACING, IS_OPEN);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote()) {
            JarTile jarTile = (JarTile) worldIn.getTileEntity(pos);
            if (handIn == Hand.MAIN_HAND) {
                if (!player.isSneaking()) {
                    if (player.getHeldItemMainhand().getItem() == RItems.HAND.get()) {
                        jarTile.dropHandIfPresent(player);
                        jarTile.setHand(player.getHeldItemMainhand().copy());
                        jarTile.setUpdateSkin(true);
                        player.getHeldItemMainhand().shrink(1);
                        jarTile.sendUpdates();
                    } else {
                        jarTile.dropHandIfPresent(player);
                        jarTile.sendUpdates();
                    }
                } else {
                    if (jarTile.getHand().getItem() == RItems.HAND.get() && jarTile.isValid(JarTile.Action.CREATE)) {
                        RegenCap.get(player).ifPresent(iRegen -> {
                            if (iRegen.getCurrentState() == RegenStates.ALIVE) {
                                iRegen.addRegens(1);
                                iRegen.setNextTrait(HandItem.getTrait(jarTile.getHand()));
                                iRegen.setNextSkin(HandItem.getSkin(jarTile.getHand()));
                                iRegen.setAlexSkin(HandItem.isAlex(jarTile.getHand()));
                                iRegen.syncToClients(null);
                                iRegen.regen();
                                player.playSound(RSounds.HAND_GLOW.get(), 1, 1);
                                jarTile.setHand(ItemStack.EMPTY);
                                jarTile.sendUpdates();
                            }
                        });
                    }
                }
            }
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.matchesBlock(this)) {
            if (!worldIn.isRemote()) {
                JarTile jarTile = (JarTile) worldIn.getTileEntity(pos);
                jarTile.dropHandIfPresent(null);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (!world.isRemote()) {
            JarTile jarTile = (JarTile) world.getTileEntity(pos);
            jarTile.dropHandIfPresent(player);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }


}
