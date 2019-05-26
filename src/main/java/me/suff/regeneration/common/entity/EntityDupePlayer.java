package me.suff.regeneration.common.entity;

import me.suff.regeneration.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityDupePlayer extends Entity {
    public long lastTickUpdated;

    public EntityDupePlayer(World world) {
        super(world);
        this.ignoreFrustumCheck = true;
        this.setSize(0, 2);
        this.lastTickUpdated = world.getTotalWorldTime();
    }

    public void onUpdate() {
        if (ClientUtil.dummy != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            ClientUtil.dummy.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
            this.lastTickUpdated = world.getTotalWorldTime();
        }
    }

    protected void entityInit() {
    }

    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }
}

