package me.swirtzly.regeneration.common.item.arch.capability;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.item.arch.IArch;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;

/**
 * Created by Swirtzly on 01/03/2020 @ 11:25
 */
public class CapabilityArch implements IArch {

    @CapabilityInject(IArch.class)
    public static final Capability<IArch> CAPABILITY = null;
    public static final ResourceLocation ARCH_ID = new ResourceLocation(RegenerationMod.MODID, "arch");
    private final ItemStack itemStack;

    private ArchStatus archStatus = ArchStatus.NORMAL_ITEM;
    private int regenAmount = 0;
    private ResourceLocation savedTrait = DnaHandler.DNA_BORING.getRegistryName();

    public CapabilityArch() {
        itemStack = null;
    }

    public CapabilityArch(ItemStack stack) {
        this.itemStack = stack;
    }

    @Nonnull
    public static IArch getForStack(ItemStack stack) {
        if (stack.hasCapability(CAPABILITY, null)) {
            return stack.getCapability(CAPABILITY, null);
        }
        throw new IllegalStateException("Missing Arch capability: " + stack + " , please report this to the issue tracker");
    }

    public int getRegenAmount() {
        return regenAmount;
    }

    public void setRegenAmount(int regenAmount) {
        this.regenAmount = regenAmount;
    }

    public ResourceLocation getSavedTrait() {
        return savedTrait;
    }

    public void setSavedTrait(ResourceLocation savedTrait) {
        this.savedTrait = savedTrait;
    }


    @Override
    public ArchStatus getArchStatus() {
        return archStatus;
    }

    @Override
    public void setArchStatus(ArchStatus status) {
        archStatus = status;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("regenAmount", regenAmount);
        nbt.setString("trait", savedTrait.toString());
        nbt.setString("arch_status", archStatus.name());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        regenAmount = nbt.getInteger("regenAmount");
        savedTrait = new ResourceLocation(nbt.getString("trait"));
        archStatus = ArchStatus.valueOf(nbt.getString("arch_status"));
    }

}
