package me.fril.regeneration.api;

import me.fril.regeneration.common.capability.IRegeneration;

public interface IActingHandler {
    /**
     * NOT FORWARDED TO THE CLIENT! Having a packet sent every tick probably is not something we want, and it creates issues with half-loaded players
     */
    void onRegenTick(IRegeneration cap);

    void onEnterGrace(IRegeneration cap);

    void onRegenFinish(IRegeneration cap);

    void onRegenTrigger(IRegeneration cap);

    void onGoCritical(IRegeneration cap);
}
