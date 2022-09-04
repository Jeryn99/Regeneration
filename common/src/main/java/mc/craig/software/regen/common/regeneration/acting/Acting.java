package mc.craig.software.regen.common.regeneration.acting;


import mc.craig.software.regen.common.regeneration.RegenerationData;

public interface Acting {

    /**
     * Called for every tick a Player is regenerating WARNING: Server only!
     */
    void onRegenTick(RegenerationData cap);

    /**
     * Called just after the player has been killed It is only called ONCE, once the player enters a grace period
     */
    void onEnterGrace(RegenerationData cap);

    /**
     * Called ONCE when the players hands begin to glow
     */
    void onHandsStartGlowing(RegenerationData cap);

    /**
     * Called when the player enters the critical stage of a grace period
     */
    void onGoCritical(RegenerationData cap);

    /**
     * Called on the first tick of a players Regeneration
     */
    void onRegenTrigger(RegenerationData cap);

    /**
     * Called on the last tick of a players Regeneration
     */
    void onRegenFinish(RegenerationData cap);

    void onPerformingPost(RegenerationData cap);

}
