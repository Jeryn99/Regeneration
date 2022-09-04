package mc.craig.software.regen.fabric;

import mc.craig.software.regen.RegenConfig;
import mc.craig.software.regen.Regeneration;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class RegenerationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Regeneration.init();
        ModLoadingContext.registerConfig(Regeneration.MOD_ID, ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.registerConfig(Regeneration.MOD_ID, ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);

    }
}
