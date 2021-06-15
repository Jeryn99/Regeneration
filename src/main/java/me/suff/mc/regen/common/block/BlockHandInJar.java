package me.suff.mc.regen.common.block;

import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.tiles.HandInJarTile;
import me.suff.mc.regen.network.messages.InvalidatePlayerDataMessage;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.util.common.PlayerUtil;
import me.suff.mc.regen.util.common.RegenShapes;
import me.suff.mc.regen.util.common.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly on 21/08/2019 @ 17:31
 */
public class BlockHandInJar extends DirectionalBlock {

    public static final VoxelShape NORTH = RegenShapes.getJarShape();
    public static final VoxelShape EAST = VoxelShapeUtils.rotate(RegenShapes.getJarShape(), Rotation.CLOCKWISE_90);
    public static final VoxelShape SOUTH = VoxelShapeUtils.rotate(RegenShapes.getJarShape(), Rotation.CLOCKWISE_180);
    public static final VoxelShape WEST = VoxelShapeUtils.rotate(RegenShapes.getJarShape(), Rotation.COUNTERCLOCKWISE_90);

    public static final DirectionProperty CFACING = DirectionProperty.create("facing", (facing) -> facing != Direction.UP && facing != Direction.DOWN);

    public BlockHandInJar() {
        super(Block.Properties.of(Material.PISTON).strength(1.25F, 10));
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        Direction direction = Direction.getNearest((float) (entity.x - clickedBlock.getX()), (float) (entity.y - clickedBlock.getY()), (float) (entity.z - clickedBlock.getZ()));
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return Direction.NORTH;
        }
        return direction;
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            world.setBlock(pos, state.setValue(CFACING, getFacingFromEntity(pos, entity)), 2);
        }
    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(CFACING)) {
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CFACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .setValue(CFACING, context.getPlayer().getDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) return false;

        if (worldIn.getBlockEntity(pos) instanceof HandInJarTile) {
            HandInJarTile jar = (HandInJarTile) worldIn.getBlockEntity(pos);

            boolean dataThere = RegenCap.get(player).isPresent();

            if (dataThere) {
                IRegen data = RegenCap.get(player).orElseGet(null);
                if (jar.getLindosAmont() >= 100 && data.getState() == PlayerUtil.RegenState.ALIVE && player.isSneaking() && jar.hasHand()) {
                    PlayerUtil.lookAt(jar.getBlockPos().getX(), jar.getBlockPos().getY(), jar.getBlockPos().getZ(), player);
                    jar.setLindosAmont(jar.getLindosAmont() - 100);
                    data.receiveRegenerations(1);
                    data.setSyncingFromJar(true);
                    data.setTrait(new ResourceLocation(HandItem.getTrait(jar.getHand())));
                    data.setEncodedSkin(HandItem.getTextureString(jar.getHand()));
                    data.setSkinType(HandItem.getSkinType(jar.getHand()));
                    data.synchronise();
                    NetworkDispatcher.sendPacketToAll(new InvalidatePlayerDataMessage(data.getLivingEntity().getUUID()));
                    jar.destroyHand();
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), RegenObjects.Sounds.HAND_GLOW.get(), SoundCategory.PLAYERS, 1.0F, 0.7F);
                    data.synchronise();
                    jar.sendUpdates();
                    return true;
                }

                if (data.getState() != PlayerUtil.RegenState.REGENERATING && !player.isSneaking()) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, jar, jar.getBlockPos());
                    return true;
                }
                return false;
            }
        }

        return true; //This means you can't accidentally place another block when you click this
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HandInJarTile();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        HandInJarTile te = (HandInJarTile) worldIn.getBlockEntity(pos);
        if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
//			InventoryHelper.dropInventoryItems(worldIn, pos, (HandInJarTile)te);
            //TODO: Make TE implement IInventory so we can make it drop its items
            worldIn.removeBlockEntity(pos);
        }
    }


}
