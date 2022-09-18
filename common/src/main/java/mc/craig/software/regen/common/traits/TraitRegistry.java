package mc.craig.software.regen.common.traits;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.registry.CustomRegistry;
import mc.craig.software.regen.common.traits.trait.*;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Optional;

public class TraitRegistry {

    public static CustomRegistry<TraitBase> TRAITS_REGISTRY = CustomRegistry.create(TraitBase.class, new ResourceLocation(RConstants.MODID, "traits"));
    public static final DeferredRegistry<TraitBase> TRAITS = DeferredRegistry.create(Regeneration.MOD_ID, TRAITS_REGISTRY);

    public static RegistrySupplier<TraitBase> HUMAN = TRAITS.register("human", HumanTrait::new);
    public static RegistrySupplier<TraitBase> FIRE_RESISTANCE = TRAITS.register("fire_resistance", FireTrait::new);
    public static RegistrySupplier<TraitBase> ARROW_DODGE = TRAITS.register("arrow_dodge", HumanTrait::new);
    public static RegistrySupplier<TraitBase> STRENGTH = TRAITS.register("strength", StrengthTrait::new);
    public static RegistrySupplier<TraitBase> WATER_BREATHING = TRAITS.register("water_breathing", WaterBreathingTrait::new);
    public static RegistrySupplier<TraitBase> SPEED = TRAITS.register("speed", SpeedTrait::new);

    public static Optional<TraitBase> getRandomTrait(){
        Collection<TraitBase> values = TRAITS_REGISTRY.getValues();
        return values.stream().skip((int) (values.size() * Math.random())).findFirst();
    }


}
