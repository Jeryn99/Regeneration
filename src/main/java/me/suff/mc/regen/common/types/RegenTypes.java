package me.suff.mc.regen.common.types;

import me.suff.mc.regen.Regeneration;
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
@Mod.EventBusSubscriber(modid = Regeneration.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegenTypes extends ForgeRegistryEntry<RegenTypes> {

    public static final RegenTypes FIERY = new RegenTypes(FieryType::new);
    public static final RegenTypes HARTNELL = new RegenTypes(TypeLayFade::new);
    public static IForgeRegistry<RegenTypes> REGISTRY;
    public static RegenTypes[] TYPES = new RegenTypes[]{FIERY, HARTNELL};
    //==================================
    private Supplier<RegenType> supplier;

    public RegenTypes(Supplier<RegenType> supplier) {
        this.supplier = supplier;
        this.setRegistryName(supplier.get().getRegistryName());
    }

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<RegenTypes>().setName(new ResourceLocation(Regeneration.MODID, "regeneration_types")).setType(RegenTypes.class).setIDRange(0, 2048).create();
    }

    public static int getPosition(RegenTypes rrRegenType) {
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i] == rrRegenType) {
                return i;
            }
        }
        return 0;
    }

    @SubscribeEvent
    public static void onRegisterTypes(RegistryEvent.Register<RegenTypes> e) {
        e.getRegistry().registerAll(FIERY, HARTNELL);
    }

    public RegenType create() {
        return this.supplier.get();
    }


}
