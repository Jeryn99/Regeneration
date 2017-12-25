package com.afg.regeneration.sounds;

import java.util.ArrayList;
import java.util.List;

import com.afg.regeneration.Regeneration;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class SoundReg {

	public static List<SoundEvent> SOUNDS = new ArrayList<>();
	
	public static SoundEvent Reg_1 = createSoundEvent("regen_1");
	public static SoundEvent Reg_2 = createSoundEvent("regen_2");
	
	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(SOUNDS.toArray(new SoundEvent[SOUNDS.size()]));	
	}
	
	public static SoundEvent createSoundEvent(String soundName) {
		ResourceLocation SoundResource = new ResourceLocation(Regeneration.MODID, soundName);
		SoundEvent e = new SoundEvent(SoundResource).setRegistryName(soundName);
		SOUNDS.add(e);
		return e;
	}
	
}
