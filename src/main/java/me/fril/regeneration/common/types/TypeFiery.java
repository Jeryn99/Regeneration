package me.fril.regeneration.common.types;

import java.util.Random;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.client.rendering.TypeFieryRenderer;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
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
	private long animationTicks;
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {
		animationTicks++;
		
		player.extinguish();
		
		if (!player.world.isRemote) {
			PlayerUtil.setPerspective((EntityPlayerMP) player, true, false);
		}
		
		Random rand = player.world.rand;
		player.rotationPitch += (rand.nextInt(10) - 5) * 0.2;
		player.rotationYaw += (rand.nextInt(10) - 5) * 0.2;
		
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
		if (!player.world.isRemote) { // NOTE redundant, only called on server side (TODO document)
			PlayerUtil.setPerspective((EntityPlayerMP) player, false, true);
		}
		
		animationTicks = 0;
	}
	
	@Override
	public int getAnimationLength() { // TODO shorten to be in line with the music (don't forget to update 'p' and 'r'!)
		return 14 * 20; // 14 seconds of 20 ticks
	}
	
	/**
	 * @deprecated No idea why you'd want to use this outside of this class, so think carefully before you do because it's probably wrong. Keep in mind that animations can change length, so <b>never</b> use this in a non-relative way
	 */
	@Deprecated
	public long getAnimationTicks() {
		return animationTicks;
	}
	
	public double getAnimationProgress() {
		return Math.min(1, animationTicks / (double) getAnimationLength());
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = IRegenType.super.serializeNBT();
		nbt.setLong("animationTicks", animationTicks);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		IRegenType.super.deserializeNBT(nbt);
		
		long nbtTicks = nbt.getLong("animationTicks");
		if (nbtTicks > animationTicks)
			animationTicks = nbtTicks;
	}
	
	@Override
	public TypeFieryRenderer getRenderer() {
		return TypeFieryRenderer.INSTANCE;
	}
	
}
