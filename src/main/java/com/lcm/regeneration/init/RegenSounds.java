package com.lcm.regeneration.init;

import java.util.ArrayList;
import java.util.List;

import com.lcm.regeneration.Regeneration;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class RegenSounds {
	private static List<SoundEvent> SOUNDS = new ArrayList<>();
	
	public static final SoundEvent REGENERATION = createSoundEvent("regeneration");
	public static final SoundEvent TIMEY_WIMEY = createSoundEvent("timey_wimey");
	public static final SoundEvent FOB_WATCH = createSoundEvent("fob_watch");
	
	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(RegenSounds.SOUNDS.toArray(new SoundEvent[RegenSounds.SOUNDS.size()]));
	}
	
	private static SoundEvent createSoundEvent(String soundName) {
		ResourceLocation soundResource = new ResourceLocation(Regeneration.MODID, soundName);
		SoundEvent e = new SoundEvent(soundResource).setRegistryName(soundName);
		SOUNDS.add(e);
		return e;
	}
	
}
