package com.lcm.regeneration.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExplosionUtil {
	
	public static void regenerationExplosion(EntityPlayer player) {
		explodeKill(player, player.world, player.getPosition(), 4); //TODO configurable
		explodeKnockback(player, player.world, player.getPosition(), 2.5F, 7); //TODO configurable
	}
	
	public static void explodeKnockback(Entity exploder, World world, BlockPos pos, float knockback, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
			if (!(entity instanceof EntityCreature) || exploder.isDead) return;
			EntityCreature victim = (EntityCreature)entity;
			float densMod = world.getBlockDensity(new Vec3d(pos), entity.getEntityBoundingBox());
			
			int xr, zr;
			xr = (int) -(victim.posX - exploder.posX);
			zr = (int) -(victim.posZ - exploder.posZ);
			
			victim.knockBack(exploder, knockback*densMod, xr, zr);
		});
	}
	
	public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
			if (!(entity instanceof EntityCreature)) return;
			((EntityCreature)entity).attackEntityFrom(RegenerativeDamageSource.INSTANCE, Float.MAX_VALUE);
		});
	}
	
	public static AxisAlignedBB getReach(BlockPos pos, int range) {
		return new AxisAlignedBB(pos.up(range).north(range).west(range), pos.down(range).south(range).east(range));
	}
	
	
	public static class RegenerativeDamageSource extends DamageSource { //useful for future extension / add-on hooking
		public static final DamageSource INSTANCE;
		static { INSTANCE = new RegenerativeDamageSource(); }
		public RegenerativeDamageSource() { super("regeneration"); }
	}
	
}
