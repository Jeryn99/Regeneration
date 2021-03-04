package me.suff.mc.regen.common.traits;

import com.google.common.collect.Iterables;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = RConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Traits extends ForgeRegistryEntry< Traits > {


    public static final Traits QUICK = new Traits(TraitQuick::new);
    public static final Traits BORING = new Traits(() -> new TraitBase(new ResourceLocation(RConstants.MODID, "boring"), 3484199));
    public static final Traits SMART = new Traits(() -> new TraitBase(new ResourceLocation(RConstants.MODID, "smart"), 3381504));
    public static final Traits FAST_MINE = new Traits(() -> new TraitBase(new ResourceLocation(RConstants.MODID, "fast_mine"), 16773073));
    public static final Traits KNOCKBACK = new Traits(() -> new TraitBase(new ResourceLocation(RConstants.MODID, "knockback"), 1950417));
    public static final Traits LEAP = new Traits(() -> new TraitBase(new ResourceLocation(RConstants.MODID, "leap"), 2293580));
    public static final Traits LONG_ARMS = new Traits(TraitLongArms::new);
    public static final Traits STRONG = new Traits(TraitStrong::new);
    public static final Traits SWIM_SPEED = new Traits(TraitSwimSpeed::new);
    public static final Traits FISH = new Traits(TraitFish::new);
    public static final Traits FIRE = new Traits(TraitFireResistant::new);


    //Create Registry
    public static IForgeRegistry< ITrait > REGISTRY;
    private Supplier< ITrait > supplier;

    public Traits(Supplier< ITrait > supplier) {
        this.supplier = supplier;
        this.setRegistryName(supplier.get().getRegistryName());
    }

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder< ITrait >().setName(new ResourceLocation(RConstants.MODID, "regeneration_traits")).setType(ITrait.class).setIDRange(0, 2048).create();
    }

    @SubscribeEvent
    public static void onRegisterTypes(RegistryEvent.Register< ITrait > e) {
        e.getRegistry().registerAll(FIRE.get(), LEAP.get(), FISH.get(), QUICK.get(), BORING.get(), SMART.get(), FAST_MINE.get(), LONG_ARMS.get(), STRONG.get(), SWIM_SPEED.get(), KNOCKBACK.get());
    }

    public static ITrait fromID(String location) {
        ResourceLocation resourceLocation = new ResourceLocation(location);
        ITrait value = REGISTRY.getValue(resourceLocation);
        if (value != null) {
            return value;
        }
        return Traits.BORING.get();
    }


    public static ITrait getRandomTrait(Random random, boolean isMob) {
        Collection< ITrait > value = REGISTRY.getValues();
        ArrayList< ITrait > traits = new ArrayList<>(value);
        traits.removeIf(trait -> trait.isPlayerOnly() && isMob || trait.getRegistryName().equals(Traits.BORING.getRegistryName()));
        int i = random.nextInt(value.size());
        return Iterables.get(value, i);
    }


    public ITrait get() {
        return this.supplier.get();
    }


    //Base
    public static abstract class ITrait implements IForgeRegistryEntry< ITrait > {
        public abstract void apply(IRegen data);

        public abstract void reset(IRegen data);

        public abstract void tick(IRegen data);

        public TranslationTextComponent getTranslation() {
            ResourceLocation regName = getRegistryName();
            return new TranslationTextComponent("trait." + regName.getNamespace() + "." + regName.getPath());
        }

        public TranslationTextComponent getDescription() {
            ResourceLocation regName = getRegistryName();
            return new TranslationTextComponent("trait." + regName.getNamespace() + "." + regName.getPath() + ".description");
        }

        public abstract boolean isPlayerOnly();

        public int getColor() {
            return 2293580;
        }

        @Override
        public Traits.ITrait setRegistryName(ResourceLocation name) {
            return this;
        }

        @Override
        public Class< ITrait > getRegistryType() {
            return ITrait.class;
        }
    }

}
