package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class AbstractTrait extends ForgeRegistryEntry< AbstractTrait > {

    public abstract void apply(IRegen data);

    public abstract void remove(IRegen data);

    public abstract void tick(IRegen data);

    public TranslationTextComponent translation() {
        ResourceLocation regName = getRegistryName();
        return new TranslationTextComponent("trait." + regName.getNamespace() + "." + regName.getPath());
    }

    public TranslationTextComponent description() {
        ResourceLocation regName = getRegistryName();
        return new TranslationTextComponent("trait." + regName.getNamespace() + "." + regName.getPath() + ".description");
    }

    public abstract boolean isPlayerOnly();

    public int color() {
        return 2293580;
    }

}