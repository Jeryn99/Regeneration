package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Created by Swirtzly
 * on 10/05/2020 @ 22:26
 */
public class CrackEntity extends Entity {

    public CrackEntity(World worldIn) {
        super(RegenObjects.EntityEntries.CRACK.get(), worldIn);
    }

    public CrackEntity(EntityType<CrackEntity> crackEntityEntityType, World worldIn) {
        super(crackEntityEntityType, worldIn);
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isRemote) {
            for (LivingEntity livingEntity : world.getEntitiesWithinAABB(LivingEntity.class, getCollisionBoundingBox().grow(5))) {
                world.getServer().enqueue(new TickDelayedTask(0, () -> {
                    livingEntity.changeDimension(RegenObjects.GALLIFREY_TYPE);
                }));
            }
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
