package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.client.VisualManipulator;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;


public class ClientEvents {

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            VisualManipulator.tickCache();
            VisualManipulator.addSkin(Minecraft.getInstance().getUser().getProfileId(), DefaultPlayerSkin.getDefaultSkin());
        });
    }

}
