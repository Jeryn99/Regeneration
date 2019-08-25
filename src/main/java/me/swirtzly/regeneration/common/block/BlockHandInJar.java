package me.swirtzly.regeneration.common.block;

import afu.org.checkerframework.checker.nullness.qual.Nullable;
import me.swirtzly.regeneration.common.capability.RegenCap;
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
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Created by Swirtzly
 * on 21/08/2019 @ 17:31
 */
public class BlockHandInJar extends Block {

    public static final VoxelShape SHAPE = createShape();

    public BlockHandInJar() {
        super(Block.Properties.create(Material.PISTON));
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
    }

    public static VoxelShape createShape() {
        VoxelShape shape = VoxelShapes.create(0.125, 0.0625, 0.25, 0.234375, 0.84375, 0.75);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.765625, 0.0625, 0.25, 0.875, 0.84375, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.875, 0.109375, 0.4375, 1.03125, 0.609375, 0.59375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.6659975, 0.08270125, 0.4375, 0.8222475, 0.30145125, 0.59375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.18058125, 0.304145, 0.46875, 0.30558125, 0.335395, 0.53125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.078125, 0.75, 0.4765625, 0.140625, 0.78125, 0.5234375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.230795625, 0.431589375, 0.4765625, 0.262045625, 0.564401875, 0.5234375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.0273, 0.5335475, 0.46875, 0.05855, 0.6585475, 0.53125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.125, 0.0, 0.25, 0.875, 0.0625, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.26863625, 0.0, 0.09710875, 0.67488625, 0.0625, 0.15960875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3084625, 0.0, 0.180820625, 0.7147125, 0.0625, 0.243320625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25423375, 0.0, 0.787798125, 0.66048375, 0.0625, 0.850298125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3371375, 0.0, 0.775839375, 0.7433875, 0.0625, 0.838339375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25, 0.0, 0.203125, 0.75, 0.0625, 0.796875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.40625, 0.0, 0.15625, 0.59375, 0.0625, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.125, 0.84375, 0.25, 0.875, 0.90625, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.40625, 0.84375, 0.15625, 0.59375, 0.90625, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25, 0.84375, 0.203125, 0.75, 0.90625, 0.796875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3371375, 0.84375, 0.775839375, 0.7433875, 0.90625, 0.838339375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25423375, 0.84375, 0.787798125, 0.66048375, 0.90625, 0.850298125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3084625, 0.84375, 0.180820625, 0.7147125, 0.90625, 0.243320625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.26863625, 0.84375, 0.09710875, 0.67488625, 0.90625, 0.15960875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        return shape;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, entity)), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
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
                    //return true;
                }
                // return false;
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
        return SHAPE;
    }
}
