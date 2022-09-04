package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistrySupplier;

import java.util.function.Supplier;

import static mc.craig.software.regen.util.RConstants.MODID;

/**
 * Created by Craig
 * on 06/05/2020 @ 14:09
 */
public class TransitionTypes {

    public static final DeferredRegistry<TransitionType> TRANSITION_TYPES = DeferredRegistry.create(new ResourceLocation(MODID, "transition_types"), MODID);
    public static final RegistrySupplier<TransitionType> FIERY = TRANSITION_TYPES.register("fiery", (FieryTransition::new));
    public static final RegistrySupplier<TransitionType> TROUGHTON = TRANSITION_TYPES.register("troughton", (TroughtonTransition::new));
    public static final RegistrySupplier<TransitionType> WATCHER = TRANSITION_TYPES.register("watcher", (WatcherTransition::new));
    public static final RegistrySupplier<TransitionType> SPARKLE = TRANSITION_TYPES.register("sparkle", (SparkleTransition::new));
    public static final RegistrySupplier<TransitionType> ENDER_DRAGON = TRANSITION_TYPES.register("ender_dragon", (EnderDragonTransition::new));
    public static final RegistrySupplier<TransitionType> BLAZE = TRANSITION_TYPES.register("blaze", (BlazeTranstion::new));
    public static final RegistrySupplier<TransitionType> TRISTIS_IGNIS = TRANSITION_TYPES.register("tristis_ignis", (SadFieryTransition::new));
    public static final RegistrySupplier<TransitionType> SNEEZE = TRANSITION_TYPES.register("sneeze", (SneezeTransition::new));
    public static TransitionType[] TYPES = new TransitionType[]{};
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
        TransitionType[] timelordTypes = new TransitionType[]{FIERY.get(), TRISTIS_IGNIS.get(), SNEEZE.get()};
        return timelordTypes[(int) (System.currentTimeMillis() % timelordTypes.length)];
    }

    public static ResourceLocation getTransitionId(TransitionType transitionType){
        return TRANSITION_TYPES_REGISTRY.get().getKey(transitionType);
    }


}
