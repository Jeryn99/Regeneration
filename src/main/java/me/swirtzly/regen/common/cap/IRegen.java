package me.swirtzly.regen.common.cap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRegen extends INBTSerializable<CompoundNBT> {

    void setRegens(int regens);
    int getRegens();

    LivingEntity getLiving();
}
