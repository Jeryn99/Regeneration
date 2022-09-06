package mc.craig.software.regen.fabric;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.fabric.handlers.CommonEvents;
import mc.craig.software.regen.util.fabric.PlatformImpl;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class RegenerationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Regeneration.init();
        ModLoadingContext.registerConfig(Regeneration.MOD_ID, ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.registerConfig(Regeneration.MOD_ID, ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
        PlatformImpl.init();
        CommonEvents.init();
    }
}
