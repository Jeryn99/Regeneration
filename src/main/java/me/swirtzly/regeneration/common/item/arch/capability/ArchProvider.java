package me.swirtzly.regeneration.common.item.arch.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Swirtzly on 01/03/2020 @ 11:31
 */
public class ArchProvider implements ICapabilitySerializable<NBTTagCompound> {

    private IArch capability;

    public ArchProvider(IArch capability) {
        this.capability = capability;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return CapabilityArch.CAPABILITY != null && capability == CapabilityArch.CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityArch.CAPABILITY ? CapabilityArch.CAPABILITY.cast(this.capability) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) CapabilityArch.CAPABILITY.getStorage().writeNBT(CapabilityArch.CAPABILITY, capability, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        CapabilityArch.CAPABILITY.getStorage().readNBT(CapabilityArch.CAPABILITY, capability, null, nbt);
    }
}
