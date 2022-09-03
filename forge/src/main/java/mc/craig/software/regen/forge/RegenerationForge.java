package mc.craig.software.regen.forge;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.forge.util.client.ClientUtil;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Regeneration.MOD_ID)
public class RegenerationForge {
    public RegenerationForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Regeneration.init();
        modBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientUtil.setup();
    }
}
