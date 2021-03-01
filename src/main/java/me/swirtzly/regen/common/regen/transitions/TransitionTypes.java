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
public class TransitionTypes extends ForgeRegistryEntry< TransitionTypes > {

    public static final TransitionTypes FIERY = new TransitionTypes(FieryTransition::new);
    public static final TransitionTypes TROUGHTON = new TransitionTypes(TroughtonTransition::new);
    public static final TransitionTypes WATCHER = new TransitionTypes(WatcherTransition::new);
    public static final TransitionTypes SPARKLE = new TransitionTypes(SparkleTransition::new);
    public static final TransitionTypes ENDER_DRAGON = new TransitionTypes(EnderDragonTransition::new);
    public static final TransitionTypes BLAZE = new TransitionTypes(BlazeTranstion::new);

    public static IForgeRegistry< TransitionTypes > REGISTRY;
    public static TransitionTypes[] TYPES = new TransitionTypes[]{FIERY, TROUGHTON};
    //==================================
    private Supplier< TransitionType > supplier;

    public TransitionTypes(Supplier< TransitionType > supplier) {
        this.supplier = supplier;
        this.setRegistryName(supplier.get().getRegistryName());
    }

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder< TransitionTypes >().setName(new ResourceLocation(RConstants.MODID, "regeneration_types")).setType(TransitionTypes.class).setIDRange(0, 2048).create();
    }

    public static int getPosition(TransitionTypes rrRegenType) {
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i] == rrRegenType) {
                return i;
            }
        }
        return 0;
    }

    public static TransitionTypes getRandomType() {
        return TYPES[(int) (System.currentTimeMillis() % TYPES.length)];
    }

    public static TransitionTypes getRandomTimelordType() {
        TransitionTypes[] timelordTypes = new TransitionTypes[]{FIERY, TROUGHTON, ENDER_DRAGON, BLAZE};
        return timelordTypes[(int) (System.currentTimeMillis() % timelordTypes.length)];
    }

    @SubscribeEvent
    public static void onRegisterTypes(RegistryEvent.Register< TransitionTypes > e) {
        e.getRegistry().registerAll(FIERY, TROUGHTON, WATCHER, SPARKLE, ENDER_DRAGON, BLAZE);
        TYPES = e.getRegistry().getValues().toArray(new TransitionTypes[0]);
    }

    public TransitionType get() {
        return this.supplier.get();
    }


}
