package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;
import java.util.UUID;

public class TraitSwimSpeed extends Traits.ITrait {

    public static final UUID SWIM_UUID = UUID.fromString("7dec4b0e-a904-46a9-bc03-b35697cdafdc");
    private ResourceLocation LOCATION = new ResourceLocation(RConstants.MODID, "swim_quick");

    @Override
    public void apply(IRegen data) {

    }

    @Override
    public void reset(IRegen data) {

    }

    @Override
    public void tick(IRegen data) {
        LivingEntity living = data.getLiving();
        Objects.requireNonNull(living.getAttribute(ForgeMod.SWIM_SPEED.get())).removeModifier(SWIM_UUID);
        if (living.isSwimming()) {
            Objects.requireNonNull(living.getAttribute(ForgeMod.SWIM_SPEED.get())).addTransientModifier(new AttributeModifier(SWIM_UUID, "SWIM modifier", 5, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public Traits.ITrait setRegistryName(ResourceLocation name) {
        this.LOCATION = name;
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return LOCATION;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public int getColor() {
        return 2039713;
    }
}
