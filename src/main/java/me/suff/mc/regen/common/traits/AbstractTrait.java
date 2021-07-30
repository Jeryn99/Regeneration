package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class AbstractTrait extends ForgeRegistryEntry<AbstractTrait> {

    public abstract void apply(IRegen data);

    public abstract void remove(IRegen data);

    public abstract void tick(IRegen data);

    public TranslatableComponent translation() {
        ResourceLocation regName = getRegistryName();
        return new TranslatableComponent("trait." + regName.getNamespace() + "." + regName.getPath());
    }

    public TranslatableComponent description() {
        ResourceLocation regName = getRegistryName();
        return new TranslatableComponent("trait." + regName.getNamespace() + "." + regName.getPath() + ".description");
    }

    public abstract boolean isPlayerOnly();

    public int color() {
        return 2293580;
    }

}