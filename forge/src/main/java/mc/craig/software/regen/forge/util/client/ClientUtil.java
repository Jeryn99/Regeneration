package mc.craig.software.regen.forge.util.client;

import mc.craig.software.regen.util.SkinRetriever;

public class ClientUtil {
    public static void setup() {
        SkinRetriever.threadedSetup(true);
    }
}
