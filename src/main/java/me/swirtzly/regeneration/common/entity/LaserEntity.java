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

    public < T extends Entity > LaserEntity(EntityType tEntityType, World world) {
        super(tEntityType, world);
        this.damage = 10.0F;
        this.scale = 0.5F;
        this.color = new Vec3d(0.0D, 1.0D, 1.0D);
        this.source = DamageSource.GENERIC;
    }


    @Override
    public void tick() {
        double speed = (new Vec3d(this.posX, this.posY, this.posZ)).distanceTo(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ));
        if (!this.world.isRemote && (this.ticksExisted > 600 || speed < 0.01D)) {
            this.remove();
        }


        if (isAlive()) {
            super.tick();
        }

    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result != null && this.isAlive()) {
            if (result.getType() == Type.ENTITY) {
                EntityRayTraceResult entityHitResult = (EntityRayTraceResult) result;
                if (entityHitResult.getEntity() == this.getThrower() || entityHitResult == null) {
                    return;
                }

                Entity hitEntity = entityHitResult.getEntity();
                hitEntity.attackEntityFrom(this.source, getDamage());
            } else if (result.getType() == Type.BLOCK) {
                BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
                BlockState block = this.world.getBlockState(blockResult.getPos());
                if (block.getBlock() instanceof TNTBlock) {
                    BlockPos pos = blockResult.getPos();
                    this.world.removeBlock(pos, true);
                    TNTEntity tntEntity = new TNTEntity(this.world, (float) pos.getX() + 0.5F, pos.getY(), (float) pos.getZ() + 0.5F, this.getThrower());
                    tntEntity.setFuse(0);
                    this.world.addEntity(tntEntity);
                    this.world.playSound(null, tntEntity.posX, tntEntity.posY, tntEntity.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (this.world.isRemote()) {
                        this.world.addParticle(ParticleTypes.SMOKE, true, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                    }
                }
            }

            if (!this.world.isRemote) {
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
    protected void registerData() {
    }

    @Override
    public void readAdditional(CompoundNBT compoundNBT) {
        super.readAdditional(compoundNBT);
        this.damage = compoundNBT.getFloat("damage");
        this.color = new Vec3d(compoundNBT.getDouble("red"), compoundNBT.getDouble("green"), compoundNBT.getDouble("blue"));
        this.scale = compoundNBT.getFloat("scale");
    }

    @Override
    public void writeAdditional(CompoundNBT compoundNBT) {
        super.writeAdditional(compoundNBT);
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
    public IPacket< ? > createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.00001F;
    }
}
