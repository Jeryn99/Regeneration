package me.swirtzly.regeneration.common.block;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
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

            if (CapabilityRegeneration.getForPlayer(playerIn).getState() != PlayerUtil.RegenState.ALIVE) {
                PlayerUtil.sendMessage(playerIn, new TextComponentTranslation("regeneration.messages.cannot_use"), true);
                return false;
            }

            if (playerIn.isSneaking() && playerIn.getHeldItemMainhand().isEmpty()) {
                if (!jar.isInUse && jar.getLindosAmont() >= 100) {
                    IRegeneration data = CapabilityRegeneration.getForPlayer(playerIn);
                    data.receiveRegenerations(1);
                    jar.setLindosAmont(0);
                    jar.setInUse(true);
                    data.setSyncingFromJar(true);
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, 1.0F, 0.7F);
                    data.synchronise();
                    PlayerUtil.sendMessage(playerIn, new TextComponentTranslation("regeneration.messages.jar"), true);
                } else if (jar.isInUse) {
                    PlayerUtil.sendMessage(playerIn, new TextComponentTranslation("regeneration.messages.jar_inuse"), true);
                } else if (jar.getLindosAmont() < 100) {
                    PlayerUtil.sendMessage(playerIn, new TextComponentTranslation("regeneration.messages.jar_not_enough"), true);
                }
            } else {
                if (playerIn.getHeldItemMainhand().isEmpty()) {
                    PlayerUtil.sendMessage(playerIn, new TextComponentTranslation("regeneration.messages.jar_amount", jar.getLindosAmont()), true);
                }
            }
            return true;
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
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     *
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (world.getTileEntity(pos) instanceof TileEntityHandInJar) {
            TileEntityHandInJar jar = (TileEntityHandInJar) world.getTileEntity(pos);
            if (jar != null && jar.getLindosAmont() > 0) {
                PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.jar_no_break", jar.getLindosAmont()), true);
                return false;
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.MISC;
    }
}
