package com.lcm.regeneration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class RegenerationSounds {
	private static List<SoundEvent> SOUNDS = new ArrayList<>();
	
	public static final SoundEvent REGENERATION = createSoundEvent("regeneration");
	public static final SoundEvent TIMEY_WIMEY = createSoundEvent("timey_wimey");
	
	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(RegenerationSounds.SOUNDS.toArray(new SoundEvent[RegenerationSounds.SOUNDS.size()]));
	}
	
	private static SoundEvent createSoundEvent(String soundName) {
		ResourceLocation soundResource = new ResourceLocation(RegenerationMod.MODID, soundName);
		SoundEvent e = new SoundEvent(soundResource).setRegistryName(soundName);
		SOUNDS.add(e);
		return e;
	}
	
}
