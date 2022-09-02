package mc.craig.software.regen.forge;

import mc.craig.software.regen.Regeneration;
import net.minecraftforge.fml.common.Mod;

@Mod(Regeneration.MOD_ID)
public class RegenerationForge {
    public RegenerationForge() {
        Regeneration.init();
    }
}
