package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TraitLongArms extends Traits.ITrait {

    public static final UUID REACH_UUID = UUID.fromString("4a204916-e836-4b7c-b133-0da469f8b9ec");
    private static final ResourceLocation LOCATION = new ResourceLocation(RConstants.MODID, "long_arms");

    @Override
    public void apply(IRegen data) {
        LivingEntity living = data.getLiving();
        ModifiableAttributeInstance reach = living.getAttribute(ForgeMod.REACH_DISTANCE.get());
        reach.applyPersistentModifier(new AttributeModifier(REACH_UUID, "Reach modifier", 15, AttributeModifier.Operation.MULTIPLY_BASE));
    }

    @Override
    public void reset(IRegen data) {
        LivingEntity living = data.getLiving();
        ModifiableAttributeInstance reach = living.getAttribute(ForgeMod.REACH_DISTANCE.get());
        if (reach != null) {
            reach.removeModifier(REACH_UUID);
        }
    }

    @Override
    public void tick(IRegen data) {

    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }


    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return LOCATION;
    }

    @Override
    public int getColor() {
        return 9740385;
    }
}
