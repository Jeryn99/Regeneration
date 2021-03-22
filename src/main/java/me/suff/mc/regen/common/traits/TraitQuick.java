package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.Objects;
import java.util.UUID;

public class TraitQuick extends AbstractTrait {

    public static final UUID SPRINT_UUID = UUID.fromString("7dec4b0e-a904-46a9-bc03-b35697cdafdc");

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
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public int color() {
        return 8171462;
    }

}
