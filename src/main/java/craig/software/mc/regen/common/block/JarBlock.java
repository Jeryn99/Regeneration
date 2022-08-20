package craig.software.mc.regen.common.block;

import craig.software.mc.regen.common.item.HandItem;
import craig.software.mc.regen.common.objects.RItems;
import craig.software.mc.regen.common.objects.RSounds;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.common.blockentity.BioContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(ROTATION, mirrorIn.mirror(state.getValue(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
        builder.add(BlockStateProperties.WATERLOGGED);
    }


    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!worldIn.isClientSide()) {
            BioContainerBlockEntity jarTile = (BioContainerBlockEntity) worldIn.getBlockEntity(pos);

            if (handIn != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

            if (!player.isShiftKeyDown()) {
                if (player.getMainHandItem().getItem() == RItems.HAND.get()) {
                    if (jarTile != null) {
                        jarTile.dropHandIfPresent(player);
                        jarTile.setHand(player.getMainHandItem().copy());
                        jarTile.setUpdateSkin(true);
                        player.getMainHandItem().shrink(1);
                        jarTile.sendUpdates();
                        return InteractionResult.SUCCESS;
                    }
                }
                jarTile.dropHandIfPresent(player);
                jarTile.sendUpdates();
            }

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

        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(this)) {
            if (!worldIn.isClientSide()) {
                BioContainerBlockEntity jarTile = (BioContainerBlockEntity) worldIn.getBlockEntity(pos);
                jarTile.dropHandIfPresent(null);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!world.isClientSide()) {
            BioContainerBlockEntity jarTile = (BioContainerBlockEntity) world.getBlockEntity(pos);
            if (jarTile != null) {
                jarTile.dropHandIfPresent(player);
            }
        }
        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new BioContainerBlockEntity(blockPos, blockState);
    }

    @Override
    public void tick(BlockState p_222945_, ServerLevel p_222946_, BlockPos p_222947_, RandomSource p_222948_) {
        super.tick(p_222945_, p_222946_, p_222947_, p_222948_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level currentLevel, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return (level1, blockPos, block, t) -> {
            if (t instanceof BioContainerBlockEntity coffinBlockEntity) {
                coffinBlockEntity.tick(currentLevel, blockPos, blockState, coffinBlockEntity);
            }
        };
    }
}