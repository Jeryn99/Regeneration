package mc.craig.software.regen.fabric;

import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.fabric.handlers.ClientEvents;
import mc.craig.software.regen.util.Platform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class RegenerationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();
        RModels.init();
        ActingForwarder.init(true);
        //TODO Ask Lucas lol ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new SoundReverbListener());
    }
}
