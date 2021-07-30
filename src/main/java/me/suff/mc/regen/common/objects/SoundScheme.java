package me.suff.mc.regen.common.objects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/* Created by Craig on 03/03/2021 */
public class SoundScheme {

    private final SoundEvent hurtSound, tradeFail, tradeSuccess, deathSound, screamSound;
    private final ResourceLocation location;

    public SoundScheme(SoundEvent hurtSound, SoundEvent tradeFail, SoundEvent tradeSuccess, SoundEvent deathSound, SoundEvent screamSound, ResourceLocation location) {
        this.deathSound = deathSound;
        this.hurtSound = hurtSound;
        this.tradeFail = tradeFail;
        this.tradeSuccess = tradeSuccess;
        this.screamSound = screamSound;
        this.location = location;
    }

    public SoundEvent getHurtSound() {
        return hurtSound;
    }

    public SoundEvent getTradeDeclineSound() {
        return tradeFail;
    }

    public SoundEvent getTradeAcceptSound() {
        return tradeSuccess;
    }

    public SoundEvent getDeathSound() {
        return deathSound;
    }

    public SoundEvent getScreamSound() {
        return screamSound;
    }

    public ResourceLocation identify() {
        return location;
    }
}
