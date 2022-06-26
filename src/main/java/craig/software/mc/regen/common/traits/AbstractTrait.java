package craig.software.mc.regen.common.traits;

import craig.software.mc.regen.common.regen.IRegen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTrait {

    public abstract void apply(IRegen data);

    public abstract void remove(IRegen data);

    public abstract void tick(IRegen data);

    public MutableComponent translation() {
        ResourceLocation regName = RegenTraitRegistry.TRAIT_REGISTRY.get().getKey(this);
        return Component.translatable("trait." + regName.getNamespace() + "." + regName.getPath());
    }

    public String translationKey() {
        ResourceLocation regName = RegenTraitRegistry.TRAIT_REGISTRY.get().getKey(this);
        return "trait." + regName.getNamespace() + "." + regName.getPath();
    }

    public String descriptionKey() {
        ResourceLocation regName = RegenTraitRegistry.TRAIT_REGISTRY.get().getKey(this);
        return "trait." + regName.getNamespace() + "." + regName.getPath() + ".description";
    }

    public MutableComponent description() {
        ResourceLocation regName = RegenTraitRegistry.TRAIT_REGISTRY.get().getKey(this);
        return Component.translatable("trait." + regName.getNamespace() + "." + regName.getPath() + ".description");
    }

    public abstract boolean isPlayerOnly();

    public int color() {
        return 2293580;
    }

}