package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.traits.TraitRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public abstract class TraitBase {

    public abstract int getPotionColor();

    public abstract void onAdded(LivingEntity livingEntity, IRegen data);

    public abstract void onRemoved(LivingEntity livingEntity, IRegen data);

    public abstract void tick(LivingEntity livingEntity, IRegen data);

    public Component getTitle() {
        return Component.translatable("trait." + TraitRegistry.TRAITS_REGISTRY.getKey(this).getPath() + ".title");
    }

    public Component getDescription() {
        return Component.translatable("trait." + TraitRegistry.TRAITS_REGISTRY.getKey(this).getPath() + ".description");
    }
}
