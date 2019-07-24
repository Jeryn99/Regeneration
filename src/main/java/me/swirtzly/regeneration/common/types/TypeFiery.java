package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.client.rendering.types.TypeFieryRenderer;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class TypeFiery implements IRegenType<TypeFieryRenderer> {
	
	@Override
	public void onUpdateMidRegen(PlayerEntity player, IRegeneration capability) {
		
		player.extinguish();
		
		if (!player.world.isRemote) {
			PlayerUtil.setPerspective((ServerPlayerEntity) player, true, false);
		}
		
		if (player.world.isRemote)
			return;
		
		if (player.world.getBlockState(player.getPosition()).getBlock() instanceof FireBlock)
			player.world.removeBlock(player.getPosition(), false);
		
		double x = player.posX + player.getRNG().nextGaussian() * 2;
		double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
		double z = player.posZ + player.getRNG().nextGaussian() * 2;
		player.world.createExplosion(player, x, y, z, 0.1F, RegenConfig.COMMON.fieryRegen.get(), Explosion.Mode.NONE);
		
		for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east())) {
			if (player.world.getBlockState(bs).getBlock() instanceof FireBlock) {
				player.world.removeBlock(bs, false);
			}
		}
	}
	
	@Override
	public void onFinishRegeneration(PlayerEntity player, IRegeneration capability) {
		PlayerUtil.setPerspective((ServerPlayerEntity) player, false, true);
		capability.setAnimationTicks(0);
	}
	
	@Override
	public int getAnimationLength() {
        return 280; // 14 seconds of 20 ticks
	}
	
	@Override
	public double getAnimationProgress(IRegeneration cap) {
		return Math.min(1, cap.getAnimationTicks() / (double) getAnimationLength());
	}

	@Override
    public TypeHandler.RegenType getTypeID() {
        return TypeHandler.RegenType.FIERY;
    }

	@Override
	public TypeFieryRenderer getRenderer() {
		return TypeFieryRenderer.INSTANCE;
	}
	
}
