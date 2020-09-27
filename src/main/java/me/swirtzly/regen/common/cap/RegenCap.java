package me.swirtzly.regen.common.cap;

import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;


public class RegenCap implements IRegen {

    //Injection
    @CapabilityInject(IRegen.class)
    public static final Capability<IRegen> CAPABILITY = null;

    //Data
    private final LivingEntity livingEntity;
    private int regensLeft = 0;

    public RegenCap() {
        this.livingEntity = null;
      //  this.stateManager = null;
    }

    public RegenCap(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public void setRegens(int regens) {
        this.regensLeft = regens;
    }

    @Override
    public int getRegens() {
        return regensLeft;
    }

    @Override
    public LivingEntity getLiving() {
        return livingEntity;
    }


    @Nonnull
    public static LazyOptional<IRegen> get(LivingEntity player) {
        return player.getCapability(RegenCap.CAPABILITY, null);
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt(RConstants.REGENS_LEFT, regensLeft);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        regensLeft = nbt.getInt(RConstants.REGENS_LEFT);
    }
}
