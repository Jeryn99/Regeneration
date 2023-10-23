package mc.craig.software.regen;

import com.mojang.logging.LogUtils;
import mc.craig.software.regen.client.skin.DownloadSkinsThread;
import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.objects.*;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.world.RFeatures;
//import mc.craig.software.regen.compat.TardisRefinedCompat;
import mc.craig.software.regen.network.RegenNetwork;
import mc.craig.software.regen.util.Platform;
import org.slf4j.Logger;

public class Regeneration {

    public static final String MOD_ID = "regen";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {

        RegenNetwork.init();
        ActingForwarder.init(Platform.isClient());
        RItems.ITEMS.register();
        RSounds.SOUNDS.register();
        RBlocks.BLOCKS.register();
        REntities.ENTITY_TYPES.register();
        RTiles.TILES.register();
        RFeatures.CONFIGURED_FEATURES.register();
        RFeatures.PLACED_FEATURES.register();
        RFeatures.DEFERRED_REGISTRY_STRUCTURE.register();
        RParticles.TYPES.register();
        TraitRegistry.TRAITS.register();
        RMotives.TYPES.register();
        TransitionTypes.init();
        DownloadSkinsThread skinsThread = new DownloadSkinsThread(Platform.isClient());
        skinsThread.setName("Skins Downloader");
        skinsThread.start();
        TriggerManager.init();

        if (Platform.isModLoaded("tardis_refined")) {
            //TardisRefinedCompat.init();
        }
    }
}
