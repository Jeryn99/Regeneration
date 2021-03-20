package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TraitBase extends Traits.ITrait {
    private ResourceLocation LOCATION;
    private int color;

    public TraitBase(ResourceLocation resourceLocation, int color) {
        LOCATION = resourceLocation;
        this.color = color;
    }

    @Override
    public void apply(IRegen data) {

    }

    @Override
    public void remove(IRegen data) {

    }

    @Override
    public void tick(IRegen data) {

    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public Traits.ITrait setRegistryName(ResourceLocation name) {
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return LOCATION;
    }

    @Override
    public Class< Traits.ITrait > getRegistryType() {
        return Traits.ITrait.class;
    }

    @Override
    public int color() {
        return color;
    }
}
