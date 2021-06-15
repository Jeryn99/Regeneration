package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class LaserEntity extends ThrowableEntity {
    public float damage;
    public float scale;
    public Vec3d color;
    private DamageSource source;

    public LaserEntity(EntityType type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
        this.damage = 10.0F;
        this.scale = 0.5F;
        this.color = new Vec3d(0.0D, 1.0D, 1.0D);
        this.source = DamageSource.GENERIC;
    }

    public LaserEntity(EntityType type, LivingEntity livingEntityIn, World worldIn) {
        super(type, livingEntityIn, worldIn);
        this.damage = 10.0F;
        this.scale = 0.5F;
        this.color = new Vec3d(0.0D, 1.0D, 1.0D);
        this.source = DamageSource.GENERIC;
    }

    public LaserEntity(double x, double y, double z, World worldIn, LivingEntity livingEntityIn, float damage, DamageSource source, Vec3d color) {
        super(RegenObjects.EntityEntries.LASER.get(), x, y, z, worldIn);
        this.damage = 10.0F;
        this.scale = 0.5F;
        this.color = new Vec3d(0.0D, 1.0D, 1.0D);
        this.source = DamageSource.GENERIC;
        this.damage = damage;
        this.color = color;
        this.source = source;
    }

    public LaserEntity(World world) {
        this(RegenObjects.EntityEntries.LASER.get(), world);
    }

    public <T extends Entity> LaserEntity(EntityType tEntityType, World world) {
        super(tEntityType, world);
        this.damage = 10.0F;
        this.scale = 0.5F;
        this.color = new Vec3d(0.0D, 1.0D, 1.0D);
        this.source = DamageSource.GENERIC;
    }


    @Override
    public void tick() {
        double speed = (new Vec3d(this.x, this.y, this.z)).distanceTo(new Vec3d(this.xo, this.yo, this.zo));
        if (!this.level.isClientSide && (this.tickCount > 600 || speed < 0.01D)) {
            this.remove();
        }


        if (isAlive()) {
            super.tick();
        }

    }

    @Override
    protected void onHit(RayTraceResult result) {
        if (result != null && this.isAlive()) {
            if (result.getType() == Type.ENTITY) {
                EntityRayTraceResult entityHitResult = (EntityRayTraceResult) result;
                if (entityHitResult.getEntity() == this.getOwner() || entityHitResult == null) {
                    return;
                }

                Entity hitEntity = entityHitResult.getEntity();
                hitEntity.hurt(this.source, getDamage());
            } else if (result.getType() == Type.BLOCK) {
                BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
                BlockState block = this.level.getBlockState(blockResult.getBlockPos());
                if (block.getBlock() instanceof TNTBlock) {
                    BlockPos pos = blockResult.getBlockPos();
                    this.level.removeBlock(pos, true);
                    TNTEntity tntEntity = new TNTEntity(this.level, (float) pos.getX() + 0.5F, pos.getY(), (float) pos.getZ() + 0.5F, this.getOwner());
                    tntEntity.setFuse(0);
                    this.level.addFreshEntity(tntEntity);
                    this.level.playSound(null, tntEntity.x, tntEntity.y, tntEntity.z, SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (this.level.isClientSide()) {
                        this.level.addParticle(ParticleTypes.SMOKE, true, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
                    }
                }
            }

            if (!this.level.isClientSide) {
                this.remove();
            }

        }
    }

    public Vec3d getColor() {
        return this.color;
    }

    public void setColor(Vec3d color) {
        this.color = color;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public void setSource(DamageSource source) {
        this.source = source;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        this.damage = compoundNBT.getFloat("damage");
        this.color = new Vec3d(compoundNBT.getDouble("red"), compoundNBT.getDouble("green"), compoundNBT.getDouble("blue"));
        this.scale = compoundNBT.getFloat("scale");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putFloat("damage", this.damage);
        compoundNBT.putDouble("red", this.color.x);
        compoundNBT.putDouble("green", this.color.y);
        compoundNBT.putDouble("blue", this.color.z);
        compoundNBT.putFloat("scale", this.scale);
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravity() {
        return 0.00001F;
    }
}
