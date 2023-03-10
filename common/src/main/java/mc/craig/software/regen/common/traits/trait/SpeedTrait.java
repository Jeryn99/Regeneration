package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Objects;
import java.util.UUID;

public class SpeedTrait extends TraitBase {

    public static final UUID SPRINT_UUID = UUID.fromString("7dec4b0e-a904-46a9-bc03-b35697cdafdc");

    @Override
    public int getPotionColor() {
        return 8171462;
    }

    @Override
    public void onAdded(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void onRemoved(LivingEntity livingEntity, IRegen data) {
        Objects.requireNonNull(livingEntity.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(SPRINT_UUID);
    }

    @Override
    public void tick(LivingEntity livingEntity, IRegen data) {
        LivingEntity living = data.getLiving();
        Objects.requireNonNull(living.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(SPRINT_UUID);
        if (living.isSprinting()) {
            Objects.requireNonNull(living.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(new AttributeModifier(SPRINT_UUID, "Sprint modifier", 1, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }
}
