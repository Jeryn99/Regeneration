package me.suff.regeneration.common.types;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.client.rendering.TypeFieryRenderer;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class TypeFiery implements IRegenType<TypeFieryRenderer> {
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {
		
		player.extinguish();
		
		if (!player.world.isRemote) {
			PlayerUtil.setPerspective((EntityPlayerMP) player, true, false);
		}
		
		if (player.world.isRemote)
			return;
		
		if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
			player.world.setBlockToAir(player.getPosition());
		
		double x = player.posX + player.getRNG().nextGaussian() * 2;
		double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
		double z = player.posZ + player.getRNG().nextGaussian() * 2;
		player.world.newExplosion(player, x, y, z, 0.1F, RegenConfig.fieryRegen, false);
		
		for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east())) {
			if (player.world.getBlockState(bs).getBlock() instanceof BlockFire) {
				player.world.setBlockToAir(bs);
			}
		}
	}
	
	@Override
	public void onFinishRegeneration(EntityPlayer player, IRegeneration capability) {
		PlayerUtil.setPerspective((EntityPlayerMP) player, false, true);
		capability.setAnimationTicks(0);
	}
	
	@Override
	public int getAnimationLength() {
		return 14 * 20; // 14 seconds of 20 ticks
	}
	
	@Override
	public double getAnimationProgress(IRegeneration cap) {
		return Math.min(1, cap.getAnimationTicks() / (double) getAnimationLength());
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = IRegenType.super.serializeNBT();
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		IRegenType.super.deserializeNBT(nbt);
	}
	
	@Override
	public TypeFieryRenderer getRenderer() {
		return TypeFieryRenderer.INSTANCE;
	}
	
}
