package me.suff.regeneration.common.capability;

import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RegenerationStorage implements Capability.IStorage<IRegeneration> {
	
	@Nullable
	@Override
	public INBTBase writeNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side) {
		return instance.serializeNBT();
	}
	
	@Override
	public void readNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side, INBTBase nbt) {
		instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
	}
	
	
}
