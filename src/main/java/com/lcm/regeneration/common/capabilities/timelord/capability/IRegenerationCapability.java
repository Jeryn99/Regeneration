package com.lcm.regeneration.common.capabilities.timelord.capability;

import com.lcm.regeneration.common.trait.ITrait;
import com.lcm.regeneration.common.trait.TraitHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Nictogen on 3/16/18.
 */
public interface IRegenerationCapability {
    void update();

    NBTTagCompound writeNBT();

    void readNBT(NBTTagCompound nbt);

    void syncToAll();

    boolean isTimelord();

    void setTimelord(boolean timelord);

    int getRegenTicks();

    void setRegenTicks(int ticks);

    int getRegensLeft();

    void setRegensLeft(int left);

    int getTimesRegenerated();

    void setTimesRegenerated(int times);

    CapabilityRegeneration.RegenerationState getState();

    EntityPlayer getPlayer();

    NBTTagCompound getStyle();

    void setStyle(NBTTagCompound nbtTagCompound);

    void changeState(CapabilityRegeneration.RegenerationState state);

    ITrait getTrait();

    void setTrait(TraitHandler.Trait trait);
}
