package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.util.RConstants;
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
@Mod.EventBusSubscriber(modid = RConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TransitionTypes extends ForgeRegistryEntry<TransitionTypes> {

    public static final TransitionTypes FIERY = new TransitionTypes(FieryTransition::new);
    public static final TransitionTypes TROUGHTON = new TransitionTypes(TroughtonTransition::new);
    public static IForgeRegistry<TransitionTypes> REGISTRY;
    public static TransitionTypes[] TYPES = new TransitionTypes[]{FIERY, TROUGHTON};
    //==================================
    private Supplier<TransitionType> supplier;

    public TransitionTypes(Supplier<TransitionType> supplier) {
        this.supplier = supplier;
        this.setRegistryName(supplier.get().getRegistryName());
    }

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<TransitionTypes>().setName(new ResourceLocation(RConstants.MODID, "regeneration_types")).setType(TransitionTypes.class).setIDRange(0, 2048).create();
    }

    public static int getPosition(TransitionTypes rrRegenType) {
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i] == rrRegenType) {
                return i;
            }
        }
        return 0;
    }

    @SubscribeEvent
    public static void onRegisterTypes(RegistryEvent.Register<TransitionTypes> e) {
        e.getRegistry().registerAll(FIERY, TROUGHTON);
    }

    public TransitionType create() {
        return this.supplier.get();
    }


}
