package me.suff.mc.regen.common.objects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/* Created by Craig on 03/03/2021 */
public record SoundScheme(SoundEvent hurtSound, SoundEvent tradeFail, SoundEvent tradeSuccess, SoundEvent deathSound,
                          SoundEvent screamSound, ResourceLocation location) {

    public SoundEvent getTradeDeclineSound() {
        return tradeFail;
    }

    public SoundEvent getTradeAcceptSound() {
        return tradeSuccess;
    }

    public ResourceLocation identify() {
        return location;
    }
}
