package com.lcm.regeneration.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IRegeneration
{
    void update();

    NBTTagCompound writeNBT();
    void readNBT(NBTTagCompound nbt);
    void syncToAll();
    boolean isTimelord();
    int getRegenTicks();
    int getRegensLeft();
    int getTimesRegenerated();
    CapabilityRegeneration.RegenerationState getState();
    EntityPlayer getPlayer();
    NBTTagCompound getStyle();
    void setTimelord(boolean timelord);
    void setRegenTicks(int ticks);
    void setRegensLeft(int left);
    void setTimesRegenerated(int times);
    void changeState(CapabilityRegeneration.RegenerationState state);
    void setStyle(NBTTagCompound nbtTagCompound);
}

