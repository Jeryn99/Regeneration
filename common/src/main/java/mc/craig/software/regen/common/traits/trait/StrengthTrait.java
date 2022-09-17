package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class StrengthTrait extends TraitBase{

    public static final UUID STRONG_UUID = UUID.fromString("687d360b-c983-415e-80bb-f34dc2c0b77b");

    @Override
    public int getPotionColor() {
        return 9643043;
    }

    @Override
    public void onAdded(LivingEntity livingEntity, IRegen data) {
        LivingEntity living = data.getLiving();
        AttributeInstance reach = living.getAttribute(Attributes.ATTACK_DAMAGE);
        reach.addPermanentModifier(new AttributeModifier(STRONG_UUID, "Strong modifier", 5, AttributeModifier.Operation.MULTIPLY_BASE));
    }

    @Override
    public void onRemoved(LivingEntity livingEntity, IRegen data) {
        LivingEntity living = data.getLiving();
        AttributeInstance reach = living.getAttribute(Attributes.ATTACK_DAMAGE);
        reach.removeModifier(STRONG_UUID);
    }

    @Override
    public void tick(LivingEntity livingEntity, IRegen data) {

    }
}
