package mc.craig.software.regen.fabric;

import mc.craig.software.regen.Regeneration;
import net.fabricmc.api.ModInitializer;

public class RegenerationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Regeneration.init();
    }
}
