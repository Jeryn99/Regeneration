package me.swirtzly.regen.common.traits;

import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TraitFireResistant extends Traits.ITrait {
    @Override
    public void apply(IRegen data) {

    }

    @Override
    public void reset(IRegen data) {

    }

    @Override
    public void tick(IRegen data) {
        LivingEntity living = data.getLiving();
        if (living.isBurning()) {
            living.extinguish();
        }
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "fire_resistant");
    }

    @Override
    public int getColor() {
        return 14981690;
    }
}
