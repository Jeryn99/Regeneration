package mc.craig.software.regen.fabric;

import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.fabric.handlers.ClientEvents;
import mc.craig.software.regen.util.Platform;
import net.fabricmc.api.ClientModInitializer;

public class RegenerationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();
    }
}
