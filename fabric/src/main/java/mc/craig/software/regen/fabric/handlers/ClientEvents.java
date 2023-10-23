package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.client.screen.overlay.RegenerationOverlay;
import mc.craig.software.regen.common.item.ChaliceItem;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.ClientUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;


public class ClientEvents {

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> ClientUtil.tickClient());
        ClientUtil.doClientStuff();
        ColorProviderRegistry.ITEM.register((itemStack, i) -> {
            if (i == 0) {
                return ChaliceItem.getTrait(itemStack).getPotionColor();
            }
            return -1;
        }, RItems.GAUNTLET.get());

        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> RegenerationOverlay.renderAll(guiGraphics));

    }

}
