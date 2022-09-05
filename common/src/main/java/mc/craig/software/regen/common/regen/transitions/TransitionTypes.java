package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static mc.craig.software.regen.util.RConstants.MODID;

/**
 * Created by Craig
 * on 06/05/2020 @ 14:09
 */
public class TransitionTypes {

    
    public static final HashMap<ResourceLocation, TransitionType> TRANSITION_TYPES = new HashMap<>();
    public static final TransitionType FIERY = register("fiery", (FieryTransition::new));
    public static final TransitionType TROUGHTON = register("troughton", (TroughtonTransition::new));

    private static TransitionType register(String id, Supplier<TransitionType> transitionType) {
        TRANSITION_TYPES.put(new ResourceLocation(Regeneration.MOD_ID, id), transitionType.get());
        return transitionType.get();
    }

    public static final TransitionType WATCHER = register("watcher", (WatcherTransition::new));
    public static final TransitionType SPARKLE = register("sparkle", (SparkleTransition::new));
    public static final TransitionType ENDER_DRAGON = register("ender_dragon", (EnderDragonTransition::new));
    public static final TransitionType BLAZE = register("blaze", (BlazeTranstion::new));
    public static final TransitionType TRISTIS_IGNIS = register("tristis_ignis", (SadFieryTransition::new));
    public static final TransitionType SNEEZE = register("sneeze", (SneezeTransition::new));
    public static TransitionType[] TYPES = new TransitionType[]{};
    public static int getPosition(TransitionType rrRegenType) {
        if (TYPES.length <= 0) {
            TYPES = TRANSITION_TYPES.values().toArray(new TransitionType[0]);
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
            TYPES = TRANSITION_TYPES.values().toArray(new TransitionType[0]);
        }
        return TYPES[(int) (System.currentTimeMillis() % TYPES.length)];
    }

    public static TransitionType getRandomTimelordType() {
        TransitionType[] timelordTypes = new TransitionType[]{FIERY, TRISTIS_IGNIS, SNEEZE};
        return timelordTypes[(int) (System.currentTimeMillis() % timelordTypes.length)];
    }

    public static ResourceLocation getTransitionId(TransitionType transitionType){
        for (Map.Entry<ResourceLocation, TransitionType> typeEntry : TRANSITION_TYPES.entrySet()) {
            if(transitionType == typeEntry.getValue()){
                return typeEntry.getKey();
            }
        }
        return null;
    }


}
