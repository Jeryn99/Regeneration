package me.suff.regeneration.common.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RegenerationProvider implements ICapabilitySerializable<NBTTagCompound> {
	
	private final LazyOptional<IRegeneration> lazyOptional;
	private IRegeneration capability;
	
	public RegenerationProvider(IRegeneration capability, LazyOptional<IRegeneration> lazyOptional) {
		this.capability = capability;
		this.lazyOptional = lazyOptional;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) CapabilityRegeneration.CAPABILITY.getStorage().writeNBT(CapabilityRegeneration.CAPABILITY, capability, null);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		CapabilityRegeneration.CAPABILITY.getStorage().readNBT(CapabilityRegeneration.CAPABILITY, capability, null, nbt);
	}
	
	
	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return CapabilityRegeneration.CAPABILITY.orEmpty(capability, lazyOptional);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		return getCapability(cap, null);
	}
}
