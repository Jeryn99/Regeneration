package me.suff.mc.regen.common.block;

import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.tiles.JarTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends DirectionalBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public JarBlock() {
        super(Properties.create(Material.IRON).notSolid());
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
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
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
        builder.add(FACING);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote()) {
            JarTile jarTile = (JarTile) worldIn.getTileEntity(pos);
            if (handIn == Hand.MAIN_HAND) {
                if (!player.isSneaking()) {
                    if (player.getHeldItemMainhand().getItem() == RItems.HAND.get()) {
                        jarTile.dropHandIfPresent();
                        jarTile.setHand(player.getHeldItemMainhand().copy());
                        jarTile.setUpdateSkin(true);
                        player.getHeldItemMainhand().shrink(1);
                    } else {
                        jarTile.dropHandIfPresent();
                    }
                } else {
                    if (jarTile.getHand().getItem() == RItems.HAND.get() && jarTile.isValid(JarTile.Action.CREATE)) {
                        RegenCap.get(player).ifPresent(iRegen -> {
                            if(iRegen.getCurrentState() == RegenStates.ALIVE) {
                                iRegen.addRegens(1);
                                iRegen.setNextTrait(HandItem.getTrait(jarTile.getHand()));
                                iRegen.setNextSkin(HandItem.getSkin(jarTile.getHand()));
                                iRegen.setAlexSkin(HandItem.isAlex(jarTile.getHand()));
                                iRegen.syncToClients(null);
                                iRegen.regen();
                                jarTile.setHand(ItemStack.EMPTY);
                            }
                        });
                    }
                }
                jarTile.sendUpdates();
            }
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!worldIn.isRemote()) {
            JarTile jarTile = (JarTile) worldIn.getTileEntity(pos);
            jarTile.dropHandIfPresent();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (!world.isRemote()) {
            JarTile jarTile = (JarTile) world.getTileEntity(pos);
            jarTile.dropHandIfPresent();
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }
}
