package me.fril.regeneration.combat.lucraft;

import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarHandler;
import me.fril.regeneration.api.IActingHandler;
import me.fril.regeneration.common.capability.IRegeneration;

public class LucraftCoreHandler implements IActingHandler {

    public static void registerEntry() {
        AbilityBarHandler.registerProvider(new LCCoreBarEntry());
    }


    @Override
    public void onRegenTick(IRegeneration cap) {

    }

    @Override
    public void onEnterGrace(IRegeneration cap) {

    }

    @Override
    public void onRegenFinish(IRegeneration cap) {

    }

    @Override
    public void onRegenTrigger(IRegeneration cap) {

    }

    @Override
    public void onGoCritical(IRegeneration cap) {

    }
}
