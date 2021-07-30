package me.suff.mc.regen.common.block;

import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.objects.RTiles;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.tiles.JarTile;
import me.suff.mc.regen.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class JarBlock extends DirectionalBlock implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
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
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_OPEN);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide()) {
            JarTile jarTile = (JarTile) worldIn.getBlockEntity(pos);
            if (handIn == InteractionHand.MAIN_HAND) {
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
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(this)) {
            if (!worldIn.isClientSide()) {
                JarTile jarTile = (JarTile) worldIn.getBlockEntity(pos);
                jarTile.dropHandIfPresent(null);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!world.isClientSide()) {
            JarTile jarTile = (JarTile) world.getBlockEntity(pos);
            jarTile.dropHandIfPresent(player);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new JarTile(RTiles.HAND_JAR.get(), blockPos, blockState);
    }

    @Nullable
    @Override //TODO
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return EntityBlock.super.getTicker(p_153212_, p_153213_, p_153214_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(Level p_153210_, T p_153211_) {
        return EntityBlock.super.getListener(p_153210_, p_153211_);
    }
}
