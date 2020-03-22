package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.common.capability.IRegeneration;

public interface IActingHandler {

    /**
     * Called for every tick a Player is regenerating WARNING: Server only!
     */
    void onRegenTick(IRegeneration cap);

    /**
     * Called just after the player has been killed It is only called ONCE, once the player enters a grace period
     */
    void onEnterGrace(IRegeneration cap);

    /**
     * Called ONCE when the players hands begin to glow
     */
    void onHandsStartGlowing(IRegeneration cap);

    /**
     * Called when the player enters the critical stage of a grace period
     */
    void onGoCritical(IRegeneration cap);

    /**
     * Called on the first tick of a players Regeneration
     */
    void onRegenTrigger(IRegeneration cap);

    /**
     * Called on the last tick of a players Regeneration
     */
    void onRegenFinish(IRegeneration cap);

    void onStartPost(IRegeneration cap);

    void onProcessDone(IRegeneration cap);
}
