package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class TraitStrong extends AbstractTrait {

    public static final UUID STRONG_UUID = UUID.fromString("687d360b-c983-415e-80bb-f34dc2c0b77b");

    @Override
    public void apply(IRegen data) {
        LivingEntity living = data.getLiving();
        AttributeInstance reach = living.getAttribute(Attributes.ATTACK_DAMAGE);
        reach.addPermanentModifier(new AttributeModifier(STRONG_UUID, "Strong modifier", 5, AttributeModifier.Operation.MULTIPLY_BASE));
    }

    @Override
    public void remove(IRegen data) {
        LivingEntity living = data.getLiving();
        AttributeInstance reach = living.getAttribute(Attributes.ATTACK_DAMAGE);
        reach.removeModifier(STRONG_UUID);
    }

    @Override
    public void tick(IRegen data) {

    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public int color() {
        return 9643043;
    }
}
