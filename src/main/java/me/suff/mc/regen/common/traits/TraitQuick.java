package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.UUID;

public class TraitQuick extends Traits.ITrait {

    public static final UUID SPRINT_UUID = UUID.fromString("7dec4b0e-a904-46a9-bc03-b35697cdafdc");
    private ResourceLocation LOCATION = new ResourceLocation(RConstants.MODID, "quick");

    @Override
    public void apply(IRegen data) {

    }

    @Override
    public void remove(IRegen data) {

    }

    @Override
    public void tick(IRegen data) {
        LivingEntity living = data.getLiving();
        Objects.requireNonNull(living.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(SPRINT_UUID);
        if (living.isSprinting()) {
            Objects.requireNonNull(living.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(new AttributeModifier(SPRINT_UUID, "Sprint modifier", 1, AttributeModifier.Operation.MULTIPLY_BASE));
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
    public Class< Traits.ITrait > getRegistryType() {
        return Traits.ITrait.class;
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public int color() {
        return 8171462;
    }

}
