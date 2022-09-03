package mc.craig.software.regen;

import com.mojang.logging.LogUtils;
import mc.craig.software.regen.client.visual.SkinRetriever;
import org.slf4j.Logger;

public class Regeneration {
    public static final String MOD_ID = "regen";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static void init() {
        SkinRetriever.threadedSetup(false);
    }
}
