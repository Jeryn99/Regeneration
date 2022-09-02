package mc.craig.software.regen.fabric;

import mc.craig.software.regen.fabric.handlers.ClientEvents;
import mc.craig.software.regen.util.SkinRetriever;
import net.fabricmc.api.ClientModInitializer;

import java.io.IOException;

public class RegenerationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();


        try {
            SkinRetriever.folderSetup(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
