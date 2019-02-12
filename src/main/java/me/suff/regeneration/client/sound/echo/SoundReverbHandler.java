package me.suff.regeneration.client.sound.echo;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.client.Minecraft;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EFX10;

//CREDIT: https://raw.githubusercontent.com/Cryptic-Mushroom/The-Midnight/master/src/main/java/com/mushroom/midnight/client/SoundReverbHandler.java
public class SoundReverbHandler {
	private static final Minecraft MC = Minecraft.getInstance();
	
	private static boolean available;
	private static boolean setup;
	
	private static int auxEffectSlot;
	private static int reverbEffectSlot;
	
	private static boolean reverbApplied;
	
	public static void onPlaySound(int soundId) {
		if (!setup) {
			setupEffects();
			setup = true;
		}
		
		if (available) {
			applyEffect(soundId);
		}
	}
	
	private static void setupEffects() {
		available = ALC10.alcIsExtensionPresent(AL.getDevice(), EFX10.ALC_EXT_EFX_NAME);
		if (!available) {
			RegenerationMod.LOG.warn("Unable to setup reverb effects, AL EFX not supported!");
			return;
		}
		
		auxEffectSlot = EFX10.alGenAuxiliaryEffectSlots();
		EFX10.alAuxiliaryEffectSloti(auxEffectSlot, EFX10.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		
		reverbEffectSlot = EFX10.alGenEffects();
	}
	
	private static void applyEffect(int soundId) {
		if (shouldEcho()) {
			applyReverbEffect();
		} else {
			applyDefaultEffect();
		}
		
		AL11.alSource3i(soundId, EFX10.AL_AUXILIARY_SEND_FILTER, auxEffectSlot, 0, EFX10.AL_FILTER_NULL);
	}
	
	private static void applyReverbEffect() {
		if (reverbApplied) {
			return;
		}
		
		reverbApplied = true;
		
		EFX10.alEffecti(reverbEffectSlot, EFX10.AL_EFFECT_TYPE, EFX10.AL_EFFECT_EAXREVERB);
		
		EFX10.alEffectf(reverbEffectSlot, EFX10.AL_EAXREVERB_DECAY_TIME, 6.0F);
		
		EFX10.alAuxiliaryEffectSloti(auxEffectSlot, EFX10.AL_EFFECTSLOT_EFFECT, reverbEffectSlot);
	}
	
	private static void applyDefaultEffect() {
		if (!reverbApplied) {
			return;
		}
		
		reverbApplied = false;
		
		EFX10.alEffecti(reverbEffectSlot, EFX10.AL_EFFECT_TYPE, EFX10.AL_EFFECT_NULL);
		EFX10.alAuxiliaryEffectSloti(auxEffectSlot, EFX10.AL_EFFECTSLOT_EFFECT, reverbEffectSlot);
	}
	
	private static boolean shouldEcho() {
		if (MC.player == null)
			return false;
		return CapabilityRegeneration.getForPlayer(MC.player).getState().isGraceful();
	}
}