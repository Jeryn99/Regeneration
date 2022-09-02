package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.client.VisualManipulator;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        VisualManipulator.tickCache();
    }

}
