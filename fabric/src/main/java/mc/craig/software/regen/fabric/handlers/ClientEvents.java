package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.client.visual.VisualManipulator;
import mc.craig.software.regen.util.ClientUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;


public class ClientEvents {

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> VisualManipulator.tickCache());
        ClientUtil.doClientStuff();
    }

}
