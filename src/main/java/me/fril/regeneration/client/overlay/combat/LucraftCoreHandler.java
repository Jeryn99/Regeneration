package me.fril.regeneration.client.overlay.combat;

import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarHandler;

public class LucraftCoreHandler {

    public static void registerEntry() {
        AbilityBarHandler.registerProvider(new LCCoreBarEntry());
    }

}
