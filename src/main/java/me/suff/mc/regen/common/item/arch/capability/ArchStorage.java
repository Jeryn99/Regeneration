package me.suff.mc.regen.common.item.arch.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly on 01/03/2020 @ 11:42
 */
public class ArchStorage implements Capability.IStorage<IArch> {

	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IArch> capability, IArch instance, EnumFacing side) {
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(Capability<IArch> capability, IArch instance, EnumFacing side, NBTBase nbt) {
		instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
	}
}
