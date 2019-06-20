package me.swirtzly.regeneration.common.block;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHandInJar extends Block {
	
	public BlockHandInJar() {
		super(Material.GOURD);
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
			
			if (jar.getLindosAmont() == 100) {
				CapabilityRegeneration.getForPlayer(playerIn).receiveRegenerations(1);
				jar.setLindosAmont(jar.lindosAmont - 100);
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
}
