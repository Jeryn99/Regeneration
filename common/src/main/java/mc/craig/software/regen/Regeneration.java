package mc.craig.software.regen;

import com.mojang.logging.LogUtils;
import mc.craig.software.regen.common.objects.*;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.common.world.RFeatures;
import mc.craig.software.regen.network.RegenNetwork;
import mc.craig.software.regen.util.DownloadSkinsThread;
import mc.craig.software.regen.util.Platform;
import net.minecraft.sounds.SoundSource;
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
        TransitionTypes.init();
        DownloadSkinsThread.setup();
    }
}
