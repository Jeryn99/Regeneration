package me.suff.mc.regen.common.objects;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/* Created by Craig on 03/03/2021 */
public interface SoundScheme {
    SoundEvent getHurtSound();

    SoundEvent getTradeDeclineSound();

    SoundEvent getTradeAcceptSound();

    SoundEvent getDeathSound();

    SoundEvent getScreamSound();

    ResourceLocation identify();
}
