package mc.craig.software.regen.common.entities;

import mc.craig.software.regen.util.RegenDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

/* Created by Craig on 01/03/2021 */
public class Laser extends ThrowableProjectile {

    private static final EntityDataAccessor<Float> RED = SynchedEntityData.defineId(Laser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> GREEN = SynchedEntityData.defineId(Laser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> BLUE = SynchedEntityData.defineId(Laser.class, EntityDataSerializers.FLOAT);
    private float damage = 3;
    private DamageSource damageSrc = new DamageSource(level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RegenDamageTypes.REGEN_DMG_RIFLE));

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
        this.entityData.define(RED, 1F);
        this.entityData.define(GREEN, 0F);
        this.entityData.define(BLUE, 0F);
    }

    public Vector3d getColor() {
        return new Vector3d(getEntityData().get(RED), getEntityData().get(GREEN), getEntityData().get(BLUE));
    }

    public void setColors(float red, float green, float blue) {
        getEntityData().set(RED, red);
        getEntityData().set(GREEN, green);
        getEntityData().set(BLUE, blue);
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
        if (!this.level().isClientSide && (this.tickCount > 600 || speed < 0.01D)) {
            this.remove(RemovalReason.DISCARDED);
        }
        if (isAlive()) {
            super.tick();
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.SMOKE, true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        Entity entity = entityRayTraceResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.hurt(damageSrc, damage);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("r", (float) getColor().x);
        compound.putFloat("g", (float) getColor().y);
        compound.putFloat("b", (float) getColor().z);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setColors(compound.getFloat("r"), compound.getFloat("g"), compound.getFloat("b"));

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    protected float getGravity() {
        return 0F;
    }
}
