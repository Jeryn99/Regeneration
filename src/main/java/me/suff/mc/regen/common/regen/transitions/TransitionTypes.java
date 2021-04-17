package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.util.RConstants;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
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

    public static final DeferredRegister< TransitionType > TRANSITION_TYPES = DeferredRegister.create(TransitionType.class, RConstants.MODID);
    public static final RegistryObject< TransitionType > FIERY = TRANSITION_TYPES.register("fiery", (FieryTransition::new));
    public static final RegistryObject< TransitionType > TROUGHTON = TRANSITION_TYPES.register("troughton", (TroughtonTransition::new));
    public static final RegistryObject< TransitionType > WATCHER = TRANSITION_TYPES.register("watcher", (WatcherTransition::new));
    public static final RegistryObject< TransitionType > SPARKLE = TRANSITION_TYPES.register("sparkle", (SparkleTransition::new));
    public static final RegistryObject< TransitionType > ENDER_DRAGON = TRANSITION_TYPES.register("ender_dragon", (EnderDragonTransition::new));
    public static final RegistryObject< TransitionType > BLAZE = TRANSITION_TYPES.register("blaze", (BlazeTranstion::new));
    public static TransitionType[] TYPES = new TransitionType[]{};
    public static Supplier< IForgeRegistry< TransitionType > > TRANSITION_TYPES_REGISTRY = TRANSITION_TYPES.makeRegistry("transition_types", () -> new RegistryBuilder< TransitionType >().setMaxID(Integer.MAX_VALUE - 1));

    public static int getPosition(TransitionType rrRegenType) {
        if (TYPES.length <= 0) {
            TYPES = TRANSITION_TYPES_REGISTRY.get().getValues().toArray(new TransitionType[0]);
        }
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i] == rrRegenType) {
                return i;
            }
        }
        return 0;
    }

    public static TransitionType getRandomType() {
        if (TYPES.length <= 0) {
            TYPES = TRANSITION_TYPES_REGISTRY.get().getValues().toArray(new TransitionType[0]);
        }
        return TYPES[(int) (System.currentTimeMillis() % TYPES.length)];
    }

    public static TransitionType getRandomTimelordType() {
        TransitionType[] timelordTypes = new TransitionType[]{FIERY.get(), TROUGHTON.get(), ENDER_DRAGON.get(), BLAZE.get()};
        return timelordTypes[(int) (System.currentTimeMillis() % timelordTypes.length)];
    }


}
