package me.swirtzly.regen.common.traits;

import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TraitStrong extends Traits.ITrait {

    public static final UUID STRONG_UUID = UUID.fromString("687d360b-c983-415e-80bb-f34dc2c0b77b");

    @Override
    public void apply(IRegen data) {
        LivingEntity living = data.getLiving();
        ModifiableAttributeInstance reach = living.getAttribute(Attributes.ATTACK_DAMAGE);
        reach.applyPersistentModifier(new AttributeModifier(STRONG_UUID, "Strong modifier", 5, AttributeModifier.Operation.MULTIPLY_BASE));
    }

    @Override
    public void reset(IRegen data) {
        LivingEntity living = data.getLiving();
        ModifiableAttributeInstance reach = living.getAttribute(Attributes.ATTACK_DAMAGE);
        reach.removeModifier(STRONG_UUID);
    }

    @Override
    public void tick(IRegen data) {

    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "strong");
    }
}
