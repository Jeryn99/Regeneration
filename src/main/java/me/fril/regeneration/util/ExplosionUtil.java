package me.fril.regeneration.util;

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
		explodeKill(player, player.world, player.getPosition(), RegenConfig.regenerativeKillRange);
		explodeKnockback(player, player.world, player.getPosition(), RegenConfig.regenerativeKnockback, RegenConfig.regenerativeKnockbackRange);
	}
	
	
	public static void explodeKnockback(Entity exploder, World world, BlockPos pos, float knockback, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity-> {
			if (!(entity instanceof EntityCreature) || exploder.isDead)
				return;
			EntityCreature victim = (EntityCreature) entity;
			float densMod = world.getBlockDensity(new Vec3d(pos), entity.getEntityBoundingBox());
			
			int xr, zr;
			xr = (int) -(victim.posX - exploder.posX);
			zr = (int) -(victim.posZ - exploder.posZ);
			
			victim.knockBack(exploder, knockback * densMod, xr, zr);
		});
	}
	
	public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity-> {
			if (!(entity instanceof EntityCreature) || !entity.isNonBoss())
				return;
			entity.attackEntityFrom(RegenerativeDamageSource.INSTANCE, Float.MAX_VALUE);
		});
	}
	
	
	public static AxisAlignedBB getReach(BlockPos pos, int range) {
		return new AxisAlignedBB(pos.up(range).north(range).west(range), pos.down(range).south(range).east(range));
	}
	
	
	/** Useful for future extension / add-on hooking */
	public static class RegenerativeDamageSource extends DamageSource {
		public static final DamageSource INSTANCE;
		
		static {
			INSTANCE = new RegenerativeDamageSource();
		}
		
		private RegenerativeDamageSource() {
			super("regeneration");
		}
	}
	
}
