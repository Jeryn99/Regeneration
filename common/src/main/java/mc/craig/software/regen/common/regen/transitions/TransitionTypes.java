package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.Regeneration;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Created by Craig
 * on 06/05/2020 @ 14:09
 */
public class TransitionTypes {


    public static final HashMap<ResourceLocation, TransitionType> TRANSITION_TYPES = new HashMap<>();
    public static TransitionType TROUGHTON, FIERY, SNEEZE, TRISTIS_IGNIS, WATCHER, ENDER_DRAGON, BLAZE, SPARKLE, DRINK;
    public static TransitionType[] TYPES = new TransitionType[]{};

    private static TransitionType register(String id, Supplier<TransitionType> transitionType) {
        TransitionType regTransition = transitionType.get().setLocation(new ResourceLocation(Regeneration.MOD_ID, id));
        TRANSITION_TYPES.put(new ResourceLocation(Regeneration.MOD_ID, id), regTransition);
        return regTransition;
    }

    public static void init() {
        WATCHER = register("watcher", (WatcherTransition::new));
        SPARKLE = register("sparkle", (SparkleTransition::new));
        ENDER_DRAGON = register("ender_dragon", (EnderDragonTransition::new));
        BLAZE = register("blaze", (BlazeTranstion::new));
        TRISTIS_IGNIS = register("tristis_ignis", (SadFieryTransition::new));
        SNEEZE = register("sneeze", (SneezeTransition::new));
        FIERY = register("fiery", (FieryTransition::new));
        TROUGHTON = register("troughton", (TroughtonTransition::new));
        DRINK = register("drink", (DrinkTransition::new));
    }

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

    public static ResourceLocation getTransitionId(TransitionType transitionType) {
        return transitionType.getLocation();
    }


}
