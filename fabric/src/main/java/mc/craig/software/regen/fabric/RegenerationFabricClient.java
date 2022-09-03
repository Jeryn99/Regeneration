package mc.craig.software.regen.fabric;

import mc.craig.software.regen.fabric.handlers.ClientEvents;
import mc.craig.software.regen.util.SkinRetriever;
import net.fabricmc.api.ClientModInitializer;

public class RegenerationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();
    }
}
