package mc.craig.software.regen.client.event;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.client.RKeybinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;


@Mod.EventBusSubscriber(modid = Regeneration.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void keyMapping(RegisterKeyMappingsEvent event) {
        event.register(RKeybinds.FORCE_REGEN);
        event.register(RKeybinds.REGEN_GUI);
        event.register(RKeybinds.TOGGLE_TRAIT);
    }
}
