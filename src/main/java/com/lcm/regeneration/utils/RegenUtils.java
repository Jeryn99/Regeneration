package com.lcm.regeneration.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RegenUtils {

    private static AxisAlignedBB getReach(BlockPos pos, int range) {
        return new AxisAlignedBB(pos.up(range).north(range).west(range), pos.down(range).south(range).east(range));
    }
    
    public static void regenerationExplosion(EntityPlayer player) {
        explodeKill(player, player.world, player.getPosition(), RegenConfig.regenerativeKillRange);
        explodeKnockback(player, player.world, player.getPosition(), RegenConfig.regenerativeKnockback, RegenConfig.regenerativeKnockbackRange);
    }

    private static void explodeKnockback(Entity exploder, World world, BlockPos pos, float knockback, int range) {
        world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
            if (!(entity instanceof EntityLiving || entity instanceof EntityPlayer) || exploder.isDead)
                return;
            EntityLivingBase victim = (EntityLivingBase) entity;
            float densMod = world.getBlockDensity(new Vec3d(pos), entity.getEntityBoundingBox());

            int xr, zr;
            xr = (int) -(victim.posX - exploder.posX);
            zr = (int) -(victim.posZ - exploder.posZ);

            victim.knockBack(exploder, knockback * densMod, xr, zr);
        });
    }

    private static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
        world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
            if (!(entity instanceof EntityLiving || entity instanceof EntityPlayer) || !entity.isNonBoss())
                return;
            entity.attackEntityFrom(RegenUtils.RegenerativeDamageSource.INSTANCE, Float.MAX_VALUE);
        });
    }

    public static class RegenerativeDamageSource extends DamageSource { // useful for future extension / add-on hooking
        public static final DamageSource INSTANCE = new RegenerativeDamageSource();

        private RegenerativeDamageSource() {
            super("regeneration");
        }
    }

}
