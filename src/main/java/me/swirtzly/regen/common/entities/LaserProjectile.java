package me.swirtzly.regen.common.entities;

import me.swirtzly.regen.util.RegenSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/* Created by Craig on 01/03/2021 */
public class LaserProjectile extends ThrowableEntity {

    private float damage = 3;
    private DamageSource damageSrc = RegenSources.REGEN_DMG_RIFLE;

    public LaserProjectile(EntityType< ? extends ThrowableEntity > type, World worldIn) {
        super(type, worldIn);
    }

    public LaserProjectile(EntityType< ? extends ThrowableEntity > type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public LaserProjectile(EntityType< ? extends ThrowableEntity > type, LivingEntity livingEntityIn, World worldIn) {
        super(type, livingEntityIn, worldIn);
    }

    @Override
    protected void registerData() {

    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setDamageSource(DamageSource damage) {
        this.damageSrc = damage;
    }

    @Override
    public void tick() {
        super.tick();
        double speed = (new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ())).distanceTo(new Vector3d(this.prevPosX, this.prevPosY, this.prevPosZ));
        if (!this.world.isRemote && (this.ticksExisted > 600 || speed < 0.01D)) {
            this.remove();
        }
        if (isAlive()) {
            super.tick();
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult entityRayTraceResult) {
        Entity entity = entityRayTraceResult.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.attackEntityFrom(damageSrc, damage);
        }
    }

    @Override
    public IPacket< ? > createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravityVelocity() {
        return 0F;
    }
}
