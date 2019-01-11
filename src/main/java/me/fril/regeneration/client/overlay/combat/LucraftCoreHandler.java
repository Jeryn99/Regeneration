package me.fril.regeneration.client.overlay.combat;

import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarHandler;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.ActingForwarder;

public class LucraftCoreHandler implements ActingForwarder.IActingHandler {

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
