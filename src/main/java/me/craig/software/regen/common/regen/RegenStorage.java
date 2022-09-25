package me.craig.software.regen.common.regen;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class RegenStorage implements Capability.IStorage<IRegen> {

    @Override
    public INBT writeNBT(Capability<IRegen> capability, IRegen instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IRegen> capability, IRegen instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }
}
