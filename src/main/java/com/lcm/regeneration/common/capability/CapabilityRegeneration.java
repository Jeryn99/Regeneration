package com.lcm.regeneration.common.capability;

import com.lcm.regeneration.RegenConfig;
import com.lcm.regeneration.Regeneration;
import com.lcm.regeneration.network.MessageChangeState;
import com.lcm.regeneration.network.MessageSyncData;
import com.lcm.regeneration.regeneration_events.RegenerationEvent;
import com.lcm.regeneration.regeneration_events.RegenerationFinishEvent;
import com.lcm.regeneration.regeneration_events.RegenerationStartEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityRegeneration implements IRegeneration {

    public int regenerationsLeft = RegenConfig.regenCapacity, timesRegenerated, regenTicks;
    private boolean isTimelord;

    public RegenerationState state = RegenerationState.NONE;

    private EntityPlayer player;

    private NBTTagCompound styleTag = defaultStyle();

    private boolean dirty = true;

    @CapabilityInject(IRegeneration.class) public static final Capability<IRegeneration> TIMELORD_CAP = null;

    public CapabilityRegeneration(EntityPlayer player) {
        this.player = player;
    }

    @Override public void update() {

        if (!player.world.isRemote) {
            if (dirty) {
                dirty = false;
                syncToAll();
            }
            RegenerationState nextState = determineState();
            if (this.state != nextState) {
                Regeneration.INSTANCE.sendToAll(new MessageChangeState(player, nextState));
                changeState(nextState);
            }
        }

        switch (state) {
            case NONE:
                break;
            case REGENERATING:
                MinecraftForge.EVENT_BUS.post(new RegenerationEvent(player, this));
                break;
            case EXPLODING:
                MinecraftForge.EVENT_BUS.post(new RegenerationEvent.RegenerationExplosionEvent(player, this));
                break;
        }
    }

    @Override public void changeState(RegenerationState state) {
        this.state = state;
        switch (state) {
            case NONE:
                MinecraftForge.EVENT_BUS.post(new RegenerationFinishEvent(player, this));
                break;
            case REGENERATING:
                MinecraftForge.EVENT_BUS.post(new RegenerationStartEvent(player, this));
                break;
            case EXPLODING:
                break;
        }
    }

    private NBTTagCompound defaultStyle() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("PrimaryRed", 1.0f);
        nbt.setFloat("PrimaryGreen", 0.78f);
        nbt.setFloat("PrimaryBlue", 0.0f);
        nbt.setFloat("SecondaryRed", 1.0f);
        nbt.setFloat("SecondaryGreen", 0.47f);
        nbt.setFloat("SecondaryBlue", 0.0f);
        nbt.setBoolean("textured", false);
        return nbt;
    }

    public enum RegenerationState {
        NONE, REGENERATING, EXPLODING
    }

    private RegenerationState determineState() {
        if (regenTicks <= 0)
            return RegenerationState.NONE;
        if (++regenTicks > 0 && regenTicks < 100)
            return RegenerationState.REGENERATING;
        if (regenTicks >= 100 && regenTicks < 200)
            return RegenerationState.EXPLODING;
        regenTicks = 0;
        return RegenerationState.NONE;
    }
    
    @Override public NBTTagCompound writeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("regenerationsLeft", regenerationsLeft);
        compound.setInteger("timesRegenerated", timesRegenerated);
        compound.setBoolean("isTimelord", isTimelord);
        return compound;
    }

    @Override public void readNBT(NBTTagCompound compound) {
        regenerationsLeft = compound.getInteger("regenerationsLeft");
        timesRegenerated = compound.getInteger("timesRegenerated");
        isTimelord = compound.getBoolean("isTimelord");
    }

    @Override public void syncToAll() {
        Regeneration.INSTANCE.sendToAll(new MessageSyncData(player, writeNBT()));
    }

    @Override public boolean isTimelord() {
        return isTimelord;
    }

    @Override public int getRegenTicks() {
        return regenTicks;
    }

    @Override public int getRegensLeft() {
        return regenerationsLeft;
    }

    @Override public int getTimesRegenerated() {
        return timesRegenerated;
    }

    @Override public RegenerationState getState() {
        return state;
    }

    @Override public EntityPlayer getPlayer() {
        return player;
    }

    @Override public NBTTagCompound getStyle() {
        return styleTag;
    }

    @Override public void setStyle(NBTTagCompound nbtTagCompound) {
        styleTag = nbtTagCompound;
    }

    @Override public void setTimelord(boolean timelord) {
        isTimelord = timelord;
    }

    @Override public void setRegenTicks(int ticks) {
        regenTicks = ticks;
    }

    @Override public void setRegensLeft(int left) {
        regenerationsLeft = left;
    }

    @Override public void setTimesRegenerated(int times) {
        timesRegenerated = times;
    }

    public static class CapabilityTimelordProvider implements ICapabilitySerializable<NBTTagCompound> {

        private IRegeneration capability;

        public CapabilityTimelordProvider(IRegeneration capability) {
            this.capability = capability;
        }

        @Override public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return TIMELORD_CAP != null && capability == TIMELORD_CAP;
        }

        @Nullable @Override public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == TIMELORD_CAP ? TIMELORD_CAP.cast(this.capability) : null;
        }

        @Override public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) TIMELORD_CAP.getStorage().writeNBT(TIMELORD_CAP, this.capability, null);
        }

        @Override public void deserializeNBT(NBTTagCompound nbt) {
            TIMELORD_CAP.getStorage().readNBT(TIMELORD_CAP, this.capability, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<IRegeneration> {

        @Nullable @Override public NBTBase writeNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side) {
            return instance.writeNBT();
        }

        @Override public void readNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side, NBTBase nbt) {
            instance.readNBT((NBTTagCompound) nbt);
        }
    }
}

