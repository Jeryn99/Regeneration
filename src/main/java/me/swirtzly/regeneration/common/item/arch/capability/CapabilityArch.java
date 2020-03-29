package me.swirtzly.regeneration.common.item.arch.capability;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
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
    private String encoded = "NONE";
    private SkinInfo.SkinType skinType = SkinInfo.SkinType.STEVE;

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
    public void setSkinType(SkinInfo.SkinType skinType) {
        this.skinType = skinType;
    }

    @Override
    public SkinInfo.SkinType getSkinType() {
        return skinType;
    }

    @Override
    public void setSkin(String encoded) {
        this.encoded = encoded;
    }

    @Override
    public String getSkin() {
        return encoded;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("regenAmount", regenAmount);

        if (savedTrait != null) {
            nbt.setString("trait", savedTrait.toString());
        }
        if (archStatus == null) {
            archStatus = ArchStatus.NORMAL_ITEM;
        } else {
            nbt.setString("arch_status", archStatus.name());
        }
        if (skinType == null) {
            skinType = SkinInfo.SkinType.STEVE;
        } else {
            nbt.setString("skinType", skinType.name());
        }
        if (encoded == null) {
            encoded = "NONE";
        } else {
            nbt.setString("skin", encoded);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        regenAmount = nbt.getInteger("regenAmount");

        savedTrait = new ResourceLocation(nbt.getString("trait"));

        if (nbt.hasKey("arch_status")) {
            archStatus = ArchStatus.valueOf(nbt.getString("arch_status"));
        } else {
            archStatus = ArchStatus.NORMAL_ITEM;
        }
        if (nbt.hasKey("skinType")) {
            skinType = SkinInfo.SkinType.valueOf(nbt.getString("skinType"));
        } else {
            skinType = SkinInfo.SkinType.ALEX;
        }
        if (nbt.hasKey("skin")) {
            encoded = nbt.getString("skin");
        } else {
            encoded = "NONE";
        }
    }

}
