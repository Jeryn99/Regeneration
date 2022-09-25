package me.craig.software.regen.client.sound;

import me.craig.software.regen.Regeneration;
import me.craig.software.regen.common.regen.IRegen;
import me.craig.software.regen.common.regen.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

public class SoundReverb {
    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean available;
    private static boolean setup;

    private static int auxEffectSlot;

    public static void addReloader() {
        ((IReloadableResourceManager) MC.getResourceManager()).registerReloadListener((ISelectiveResourceReloadListener) (manager, predicate) -> {
            setup = false;
        });
    }

    public static void setSelfPosition(int soundId) {
        if (!setup) {
            setupEffects();
            setup = true;
        }

        if (available && shouldEcho()) {
            AL11.alSource3i(soundId, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxEffectSlot, 0, EXTEfx.AL_FILTER_NULL);
        }
    }

    private static void setupEffects() {
        available = AL.getCapabilities().ALC_EXT_EFX;
        if (!available) {
            Regeneration.LOG.warn("Unable to setup reverb effects, AL EFX not supported!");
            return;
        }

        auxEffectSlot = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxEffectSlot, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);

        int reverbEffectSlot = EXTEfx.alGenEffects();

        EXTEfx.alEffecti(reverbEffectSlot, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);
        EXTEfx.alEffectf(reverbEffectSlot, EXTEfx.AL_EAXREVERB_DECAY_TIME, 9F);

        EXTEfx.alAuxiliaryEffectSloti(auxEffectSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverbEffectSlot);
    }

    private static boolean shouldEcho() {
        if (Minecraft.getInstance().level == null) return false;
        IRegen data = RegenCap.get(Minecraft.getInstance().player).orElse(null);
        if (data != null) {
            return data.regenState().isGraceful();
        }
        return false;
    }

}
