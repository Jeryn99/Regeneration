package com.lcm.regeneration.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExplosionUtil {
	
	public static void regenerationExplosion(EntityPlayer player) {
		explodeKill(player, player.world, player.getPosition(), 3); //TODO configurable
		explodeKnockback(player, player.world, player.getPosition(), 2.5F, 3, 7); //TODO configurable
	}
	
	public static void explodeKnockback(Entity exploder, World world, BlockPos pos, float knockback, int rangeMin, int rangeMax) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, rangeMax)).forEach(entity -> {
			if (!(entity instanceof EntityCreature) || exploder.getDistance(entity) <= rangeMin) return;
			EntityCreature victim = (EntityCreature)entity;
			float densMod = world.getBlockDensity(new Vec3d(pos), entity.getEntityBoundingBox());
			
			victim.knockBack(exploder, knockback*densMod,
					(double)MathHelper.sin(exploder.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(exploder.rotationYaw * 0.017453292F)));
		});
	}
	
	public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
			if (!(entity instanceof EntityCreature)) return; //TODO predicate?
			((EntityCreature)entity).attackEntityFrom(DamageSource.GENERIC, ((EntityCreature)entity).getHealth());
		});
	}
	
	private static AxisAlignedBB getReach(BlockPos pos, int range) {
		return new AxisAlignedBB(pos.up(range).north(range).west(range), pos.down(range).south(range).east(range));
	}
}
