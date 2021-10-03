package me.suff.mc.regen.common.block;

import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.tiles.BioContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;


public class JarBlock extends Block implements EntityBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public JarBlock() {
        super(Properties.of(Material.METAL).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(CampfireBlock.WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION, Mth.floor((double) (context.getRotation() * 16.0F / 360.0F) + 0.5D) & 15);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(ROTATION, mirrorIn.mirror(state.getValue(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
        builder.add(BlockStateProperties.WATERLOGGED);
    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide()) {
            BioContainerBlockEntity jarTile = (BioContainerBlockEntity) worldIn.getBlockEntity(pos);
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
                    if (jarTile.getHand().getItem() == RItems.HAND.get() && jarTile.isValid(BioContainerBlockEntity.Action.CREATE)) {
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
                BioContainerBlockEntity jarTile = (BioContainerBlockEntity) worldIn.getBlockEntity(pos);
                jarTile.dropHandIfPresent(null);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!world.isClientSide()) {
            BioContainerBlockEntity jarTile = (BioContainerBlockEntity) world.getBlockEntity(pos);
            jarTile.dropHandIfPresent(player);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BioContainerBlockEntity(blockPos, blockState);
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