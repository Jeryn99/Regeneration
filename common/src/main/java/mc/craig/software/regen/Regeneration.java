package mc.craig.software.regen;

import com.mojang.logging.LogUtils;
import mc.craig.software.regen.client.visual.SkinRetriever;
import mc.craig.software.regen.common.regeneration.acting.ActingForwarder;
import mc.craig.software.regen.network.RegenNetwork;
import org.slf4j.Logger;

public class Regeneration {

    public static final String MOD_ID = "regeneration";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        SkinRetriever.threadedSetup(false);
        RegenNetwork.init();
        ActingForwarder.init(false);
    }
}
