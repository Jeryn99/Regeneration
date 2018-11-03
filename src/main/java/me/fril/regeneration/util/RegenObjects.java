package me.fril.regeneration.util;

import java.util.ArrayList;
import java.util.List;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.items.ItemFobWatch;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class RegenObjects {
	
	public static List<Item> ITEMS = new ArrayList<>();
	
	@SubscribeEvent
	public static void addItems(RegistryEvent.Register<Item> e) {
		IForgeRegistry<Item> reg = e.getRegistry();
		reg.registerAll(setUpItem(new ItemFobWatch(), "fob_watch"));
	}
	
	private static Item setUpItem(Item item, String name) {
		item.setRegistryName(RegenerationMod.MODID, name);
		item.setTranslationKey(name);
		ITEMS.add(item);
		return item;
	}
	
	
	
	@SubscribeEvent
	public static void addSounds(RegistryEvent.Register<SoundEvent> e) {
		e.getRegistry().registerAll(setUpSound("regeneration"),
									setUpSound("fob_watch"),
									setUpSound("critical_stage"),
									setUpSound("heart_beat"),
									setUpSound("hand_glow"));
	}
	
	private static SoundEvent setUpSound(String soundName) {
		return new SoundEvent(new ResourceLocation(RegenerationMod.MODID, soundName)).setRegistryName(soundName);
	}
	
	
	
	@GameRegistry.ObjectHolder(RegenerationMod.MODID)
	public static class Items {
		public static final Item FOB_WATCH = null;
	}
	
	@GameRegistry.ObjectHolder(RegenerationMod.MODID)
	public static class Sounds {
		public static final SoundEvent FOB_WATCH = null;
		public static final SoundEvent REGENERATION = null;
		public static final SoundEvent CRITICAL_STAGE = null;
		public static final SoundEvent HEART_BEAT = null;
		public static final SoundEvent HAND_GLOW = null;
	}
}
