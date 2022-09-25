package me.craig.software.regen.common.regen.acting;


import me.craig.software.regen.common.regen.IRegen;

public interface Acting {

    /**
     * Called for every tick a Player is regenerating WARNING: Server only!
     */
    void onRegenTick(IRegen cap);

    /**
     * Called just after the player has been killed It is only called ONCE, once the player enters a grace period
     */
    void onEnterGrace(IRegen cap);

    /**
     * Called ONCE when the players hands begin to glow
     */
    void onHandsStartGlowing(IRegen cap);

    /**
     * Called when the player enters the critical stage of a grace period
     */
    void onGoCritical(IRegen cap);

    /**
     * Called on the first tick of a players Regeneration
     */
    void onRegenTrigger(IRegen cap);

    /**
     * Called on the last tick of a players Regeneration
     */
    void onRegenFinish(IRegen cap);

    void onPerformingPost(IRegen cap);

}
