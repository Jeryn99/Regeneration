package me.swirtzly.regeneration.common.block;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHandInJar extends DirectionalBlock {
	
	public BlockHandInJar() {
		super(Material.GOURD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.SOUTH));
		setHardness(5);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return false;
		
		if (worldIn.getTileEntity(pos) instanceof TileEntityHandInJar) {
			TileEntityHandInJar jar = (TileEntityHandInJar) worldIn.getTileEntity(pos);
			
			if (CapabilityRegeneration.getForPlayer(playerIn).getState() != PlayerUtil.RegenState.ALIVE) {
				PlayerUtil.sendMessage(playerIn, new TranslationTextComponent("regeneration.messages.cannot_use"), true);
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
					PlayerUtil.sendMessage(playerIn, new TranslationTextComponent("regeneration.messages.jar"), true);
				} else if (jar.isInUse) {
					PlayerUtil.sendMessage(playerIn, new TranslationTextComponent("regeneration.messages.jar_inuse"), true);
				} else if (jar.getLindosAmont() < 100) {
					PlayerUtil.sendMessage(playerIn, new TranslationTextComponent("regeneration.messages.jar_not_enough"), true);
				}
			} else {
				if (playerIn.getHeldItemMainhand().isEmpty()) {
					PlayerUtil.sendMessage(playerIn, new TranslationTextComponent("regeneration.messages.jar_amount", jar.getLindosAmont()), true);
				}
			}
			return true;
		}
		return false;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, BlockState state) {
		return new TileEntityHandInJar();
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	public int getMetaFromState(BlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 *
	 * @deprecated call via {@link BlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
	 * fine.
	 */
	public BlockState withRotation(BlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 *
	 * @deprecated call via {@link BlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	 */
	public BlockState withMirror(BlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public BlockState getStateFromMeta(int meta) {
		Direction enumfacing = Direction.byIndex(meta);
		
		if (enumfacing.getAxis() == Direction.Axis.Y) {
			enumfacing = Direction.NORTH;
		}
		
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest) {
		if (world.getTileEntity(pos) instanceof TileEntityHandInJar) {
			TileEntityHandInJar jar = (TileEntityHandInJar) world.getTileEntity(pos);
			if (jar != null && jar.getLindosAmont() > 0) {
				PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.jar_no_break", jar.getLindosAmont()), true);
				return false;
			}
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public ItemGroup getCreativeTab() {
		return ItemGroup.MISC;
	}
}
