package me.swirtzly.regeneration.registries;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.types.FieryType;
import me.swirtzly.regeneration.common.types.RegenType;
import me.swirtzly.regeneration.common.types.TypeLayFade;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Created by Swirtzly
 * on 06/05/2020 @ 14:09
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RRRegenType extends ForgeRegistryEntry<RRRegenType> {

    public static IForgeRegistry<RRRegenType> REGISTRY;

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<RRRegenType>().setName(new ResourceLocation(RegenerationMod.MODID, "regeneration_types")).setType(RRRegenType.class).setIDRange(0, 2048).create();
    }


    public static final RRRegenType FIERY = new RRRegenType(FieryType::new);
    public static final RRRegenType HARTNELL = new RRRegenType(TypeLayFade::new);

    @SubscribeEvent
    public static void onRegisterTypes(RegistryEvent.Register<RRRegenType> e) {
        e.getRegistry().registerAll(FIERY, HARTNELL);
    }


        //==================================
    private Supplier<RegenType> supplier;

    public RRRegenType(Supplier<RegenType> supplier) {
        this.supplier = supplier;
        this.setRegistryName(supplier.get().getRegistryName());
    }

    public RegenType create() {
        return this.supplier.get();
    }


}
