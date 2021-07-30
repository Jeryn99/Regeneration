package me.suff.mc.regen.common.regen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class RegenStorage implements Capability.IStorage<IRegen> {

    @Override
    public Tag writeNBT(Capability<IRegen> capability, IRegen instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IRegen> capability, IRegen instance, Direction side, Tag nbt) {
        instance.deserializeNBT((CompoundTag) nbt);
    }
}
