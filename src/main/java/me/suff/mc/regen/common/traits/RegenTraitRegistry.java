package me.suff.mc.regen.common.traits;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Supplier;

import com.google.common.collect.Iterables;

import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class RegenTraitRegistry {
	
	public static final DeferredRegister<AbstractTrait> TRAITS = DeferredRegister.create(AbstractTrait.class, RConstants.MODID);
    
	public static Supplier<IForgeRegistry<AbstractTrait>> TRAIT_REGISTRY = TRAITS.makeRegistry("trait", () -> new RegistryBuilder<AbstractTrait>().setMaxID(Integer.MAX_VALUE - 1));

    public static final RegistryObject<AbstractTrait> QUICK = TRAITS.register("quick", (TraitQuick::new));
    public static final RegistryObject<AbstractTrait> BORING = TRAITS.register("boring", () -> new TraitBase(3484199));
    public static final RegistryObject<AbstractTrait> SMART = TRAITS.register("smart", () -> new TraitBase(3381504));
    public static final RegistryObject<AbstractTrait> FAST_MINE = TRAITS.register("fast_mine", () -> new TraitBase(16773073));
    public static final RegistryObject<AbstractTrait> KNOCKBACK = TRAITS.register("knockback", () -> new TraitBase(1950417));
    public static final RegistryObject<AbstractTrait> LEAP = TRAITS.register("leap", () -> new TraitBase( 2293580));
    public static final RegistryObject<AbstractTrait> LONG_ARMS = TRAITS.register("long_arms", (TraitLongArms::new));
    public static final RegistryObject<AbstractTrait> STRONG = TRAITS.register("strong", (TraitStrong::new));
    public static final RegistryObject<AbstractTrait> SWIM_SPEED = TRAITS.register("swim_quick", (TraitSwimSpeed::new));
    public static final RegistryObject<AbstractTrait> FISH = TRAITS.register("fish", (TraitFish::new));
    public static final RegistryObject<AbstractTrait> FIRE = TRAITS.register("fire_resistant", (TraitFireResistant::new));
    public static final RegistryObject<AbstractTrait> ENDER_HURT = TRAITS.register("ender_hurt", () -> new TraitBase(Color.MAGENTA.getRGB()));
    public static final RegistryObject<AbstractTrait> WATER_STRIDE = TRAITS.register("water_stride", () -> new TraitBase(Color.WHITE.getRGB()));
    public static final RegistryObject<AbstractTrait> PHOTOSYNTHETIC = TRAITS.register("photosynthetic", () -> new TraitBase(Color.green.getRGB()));


    //Create Registry

    public static AbstractTrait fromID(String location) {
        ResourceLocation resourceLocation = new ResourceLocation(location);
        AbstractTrait value = TRAIT_REGISTRY.get().getValue(resourceLocation);
        if (value != null) {
            return value;
        }
        return RegenTraitRegistry.BORING.get();
    }
    
    public static AbstractTrait fromID(ResourceLocation location) {
        AbstractTrait value = TRAIT_REGISTRY.get().getValue(location);
        if (value != null) {
            return value;
        }
        return RegenTraitRegistry.BORING.get();
    }


    public static AbstractTrait getRandomTrait(Random random, boolean isMob) {
        Collection<AbstractTrait> value = TRAIT_REGISTRY.get().getValues();
        ArrayList<AbstractTrait> traits = new ArrayList<>(value);
        traits.removeIf(trait -> trait.isPlayerOnly() && isMob || trait.getRegistryName().equals(RegenTraitRegistry.BORING.get().getRegistryName()));
        for (AbstractTrait trait : traits) {
            for (String s : RegenConfig.COMMON.disabledTraits.get()) {
                if(trait.getRegistryName().toString().contains(s)) {
                    traits.remove(trait);
                }
            }
        }
        int i = random.nextInt(value.size());
        return Iterables.get(value, i);
    }
}
