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
	public static List<SoundEvent> SOUNDS = new ArrayList<>();
	
	public static SoundEvent SHORT = createSoundEvent("regen_1");
	public static SoundEvent LONG = createSoundEvent("regen_2");
	
	public static SoundEvent GET = createSoundEvent("regen_get");
	
	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(RegenerationSounds.SOUNDS.toArray(new SoundEvent[RegenerationSounds.SOUNDS.size()]));
	}
	
	public static SoundEvent createSoundEvent(String soundName) {
		ResourceLocation SoundResource = new ResourceLocation(RegenerationMod.MODID, soundName);
		SoundEvent e = new SoundEvent(SoundResource).setRegistryName(soundName);
		RegenerationSounds.SOUNDS.add(e);
		return e;
	}
	
}
