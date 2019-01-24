package me.fril.regeneration.util;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.handlers.RegenObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class RegenUtil {
	
	public static float randFloat(float min, float max) {
		Random rand = new Random();
		float result = rand.nextFloat() * (max - min) + min;
		return result;
	}
	
	public static void genCrater(World world, BlockPos pos, int radius) {
		for (int x = pos.getX() - radius; x < pos.getX() + radius; ++x) {
			for (int y = pos.getY() - radius; y < pos.getY() + radius; ++y) {
				for (int z = pos.getZ() - radius; z < pos.getZ() + radius; ++z) {
					double squareDistance = Math.pow(x - pos.getX(), 2) + Math.pow(y - pos.getY(), 2) + Math.pow(z - pos.getZ(), 2);
					if (squareDistance <= Math.pow(radius, 2)) {
						world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}
	
	public static void regenerationExplosion(EntityPlayer player) {
		explodeKnockback(player, player.world, player.getPosition(), RegenConfig.onRegen.regenerativeKnockback, RegenConfig.onRegen.regenerativeKnockbackRange);
		explodeKill(player, player.world, player.getPosition(), RegenConfig.onRegen.regenerativeKillRange);
	}
	
	public static void explodeKnockback(Entity exploder, World world, BlockPos pos, float knockback, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
			if (entity instanceof EntityLivingBase && !exploder.isDead) {
				EntityLivingBase victim = (EntityLivingBase) entity;
				
				if (entity instanceof EntityPlayer && !RegenConfig.onRegen.regenerationKnocksbackPlayers)
					return;
				
				float densMod = world.getBlockDensity(new Vec3d(pos), entity.getEntityBoundingBox());
				
				int xr, zr;
				xr = (int) -(victim.posX - exploder.posX);
				zr = (int) -(victim.posZ - exploder.posZ);
				
				victim.knockBack(exploder, knockback * densMod, xr, zr);
			}
		});
	}
	
	public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
		world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
			if ((entity instanceof EntityCreature && entity.isNonBoss()) || (entity instanceof EntityPlayer && RegenConfig.onRegen.regenerationKillsPlayers))
				entity.attackEntityFrom(RegenObjects.REGEN_DMG_ENERGY_EXPLOSION, Float.MAX_VALUE);
		});
	}
	
	public static AxisAlignedBB getReach(BlockPos pos, int range) {
		return new AxisAlignedBB(pos.up(range).north(range).west(range), pos.down(range).south(range).east(range));
	}
	
}
