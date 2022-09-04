package mc.craig.software.regen;

import com.mojang.logging.LogUtils;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.network.RegenNetwork;
import mc.craig.software.regen.util.Platform;
import org.slf4j.Logger;

public class Regeneration {

    public static final String MOD_ID = "regeneration";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        RegenNetwork.init();
        ActingForwarder.init(Platform.isClient());
    }
}
