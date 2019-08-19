package me.swirtzly.regeneration.common.block;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.entity.EntityLindos;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHandInJar extends BlockDirectional {

    public BlockHandInJar() {
        super(Material.GOURD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
        setHardness(5);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return false;

        if (worldIn.getTileEntity(pos) instanceof TileEntityHandInJar) {
            TileEntityHandInJar jar = (TileEntityHandInJar) worldIn.getTileEntity(pos);
            IRegeneration data = CapabilityRegeneration.getForPlayer(playerIn);

            if (jar.getLindosAmont() >= 100 && data.getState() == PlayerUtil.RegenState.ALIVE && playerIn.isSneaking() && jar.hasHand()) {
                jar.setLindosAmont(jar.getLindosAmont() - 100);
                data.receiveRegenerations(1);
                data.setSyncingFromJar(true);
                worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, 1.0F, 0.7F);
                data.synchronise();
                jar.sendUpdates();
                return true;
            }

            if (data.getState() != PlayerUtil.RegenState.REGENERATING && !playerIn.isSneaking()) {
                playerIn.openGui(RegenerationMod.INSTANCE, 77, worldIn, jar.getPos().getX(), jar.getPos().getY(), jar.getPos().getZ());
                return true;
            }

        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityHandInJar();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     *
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     *
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (world.isRemote) return super.removedByPlayer(state, world, pos, player, willHarvest);
        if (world.getTileEntity(pos) instanceof TileEntityHandInJar) {
            TileEntityHandInJar jar = (TileEntityHandInJar) world.getTileEntity(pos);
            if (jar.lindosAmont > 0) {
                EntityLindos lindos = new EntityLindos(player.world);
                lindos.setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, 0, 0);
                lindos.setAmount(jar.lindosAmont);
                player.world.spawnEntity(lindos);
            }
            InventoryHelper.dropInventoryItems(world, pos, jar);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.MISC;
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        EnumFacing entityFacing = entity.getHorizontalFacing();

        if (!world.isRemote) {
            if (entityFacing == EnumFacing.NORTH) {
                entityFacing = EnumFacing.SOUTH;
            } else if (entityFacing == EnumFacing.EAST) {
                entityFacing = EnumFacing.WEST;
            } else if (entityFacing == EnumFacing.SOUTH) {
                entityFacing = EnumFacing.NORTH;
            } else if (entityFacing == EnumFacing.WEST) {
                entityFacing = EnumFacing.EAST;
            }

            world.setBlockState(pos, state.withProperty(FACING, entityFacing), 2);
        }
    }
}
