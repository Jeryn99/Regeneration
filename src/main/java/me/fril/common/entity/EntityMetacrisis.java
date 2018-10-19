package me.fril.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityMetacrisis extends EntityCreature {

    private static final DataParameter<String> PLAYER_UUID = EntityDataManager.createKey(EntityZombie.class, DataSerializers.STRING);


    public EntityMetacrisis(World worldIn) {
        super(worldIn);
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, Entity.class, 6.0F));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(PLAYER_UUID, "bc8b891e-5c25-4c9f-ae61-cdfb270f1cc1");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    public UUID getPlayerUuid() {
        return UUID.fromString(getDataManager().get(PLAYER_UUID));
    }

    public void setPlayerUuid(UUID uuid) {
        getDataManager().set(PLAYER_UUID, uuid.toString());
    }
}
