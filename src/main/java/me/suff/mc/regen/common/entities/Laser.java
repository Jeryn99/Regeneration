package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.util.RegenSources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/* Created by Craig on 01/03/2021 */
public class Laser extends ThrowableProjectile {

    private float damage = 3;
    private DamageSource damageSrc = RegenSources.REGEN_DMG_RIFLE;

    public Laser(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public Laser(EntityType<? extends ThrowableProjectile> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    public Laser(EntityType<? extends ThrowableProjectile> type, LivingEntity livingEntityIn, Level worldIn) {
        super(type, livingEntityIn, worldIn);
    }

    @Override
    protected void defineSynchedData() {

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
        double speed = (new Vec3(this.getX(), this.getY(), this.getZ())).distanceTo(new Vec3(this.xo, this.yo, this.zo));
        if (!this.level.isClientSide && (this.tickCount > 600 || speed < 0.01D)) {
            this.remove(RemovalReason.DISCARDED);
        }
        if (isAlive()) {
            super.tick();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level.isClientSide()) {
            this.level.addParticle(ParticleTypes.SMOKE, true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        Entity entity = entityRayTraceResult.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.hurt(damageSrc, damage);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravity() {
        return 0F;
    }
}
