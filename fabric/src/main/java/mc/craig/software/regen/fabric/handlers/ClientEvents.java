package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.client.VisualManipulator;
import mc.craig.software.regen.util.SkinRetriever;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;


public class ClientEvents {

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> VisualManipulator.tickCache());

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> SkinRetriever.threadedSetup(true));

    }

}
