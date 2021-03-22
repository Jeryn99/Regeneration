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

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
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
        super(Properties.of(Material.METAL).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.getValue(IS_OPEN)) {
            switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
        switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise()).setValue(IS_OPEN, true);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder< Block, BlockState > builder) {
        builder.add(FACING, IS_OPEN);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide()) {
            JarTile jarTile = (JarTile) worldIn.getBlockEntity(pos);
            if (handIn == Hand.MAIN_HAND) {
                if (!player.isShiftKeyDown()) {
                    if (player.getMainHandItem().getItem() == RItems.HAND.get()) {
                        jarTile.dropHandIfPresent(player);
                        jarTile.setHand(player.getMainHandItem().copy());
                        jarTile.setUpdateSkin(true);
                        player.getMainHandItem().shrink(1);
                        jarTile.sendUpdates();
                    } else {
                        jarTile.dropHandIfPresent(player);
                        jarTile.sendUpdates();
                    }
                } else {
                    if (jarTile.getHand().getItem() == RItems.HAND.get() && jarTile.isValid(JarTile.Action.CREATE)) {
                        RegenCap.get(player).ifPresent(iRegen -> {
                            if (iRegen.regenState() == RegenStates.ALIVE) {
                                iRegen.addRegens(1);
                                iRegen.setNextTrait(HandItem.getTrait(jarTile.getHand()));
                                iRegen.setNextSkin(HandItem.getSkin(jarTile.getHand()));
                                iRegen.setAlexSkin(HandItem.isAlex(jarTile.getHand()));
                                iRegen.syncToClients(null);
                                iRegen.forceRegeneration();
                                player.playSound(RSounds.HAND_GLOW.get(), 1, 1);
                                jarTile.setHand(ItemStack.EMPTY);
                                jarTile.sendUpdates();
                            }
                        });
                    }
                }
            }
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(this)) {
            if (!worldIn.isClientSide()) {
                JarTile jarTile = (JarTile) worldIn.getBlockEntity(pos);
                jarTile.dropHandIfPresent(null);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (!world.isClientSide()) {
            JarTile jarTile = (JarTile) world.getBlockEntity(pos);
            jarTile.dropHandIfPresent(player);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }


}
