package me.fril.regeneration.handlers;

import me.fril.regeneration.Regeneration;
import me.fril.regeneration.common.items.ItemFobWatch;
import me.fril.regeneration.util.RegenDamageSource;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class RegenObjects {
	
	public static List<Item> ITEMS = new ArrayList<>();
	
	public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenDamageSource("regen_energy"),
	                           REGEN_DMG_HEALING = new RegenDamageSource("regen_heal"), //The irony lmao
	                           REGEN_DMG_CRITICAL = new RegenDamageSource("regen_crit");
	
	@SubscribeEvent
	public static void addItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(setUpItem(new ItemFobWatch(), "fob_watch"));
	}
	
	private static Item setUpItem(Item item, String name) {
		item.setRegistryName(Regeneration.MODID, name);
		item.setTranslationKey(name);
		ITEMS.add(item);
		return item;
	}
	
	
	
	@SubscribeEvent
	public static void addSounds(RegistryEvent.Register<SoundEvent> e) {
		e.getRegistry().registerAll(
			setUpSound("regeneration"),
			setUpSound("fob_watch"),
			setUpSound("critical_stage"),
			setUpSound("heart_beat"),
			setUpSound("hand_glow")
		);
	}
	
	private static SoundEvent setUpSound(String soundName) {
		return new SoundEvent(new ResourceLocation(Regeneration.MODID, soundName)).setRegistryName(soundName);
	}


	@GameRegistry.ObjectHolder(Regeneration.MODID)
	public static class Items {
		public static final Item FOB_WATCH = null;
	}

	@GameRegistry.ObjectHolder(Regeneration.MODID)
	public static class Sounds {
		public static final SoundEvent FOB_WATCH = null;
		public static final SoundEvent REGENERATION = null;
		public static final SoundEvent CRITICAL_STAGE = null;
		public static final SoundEvent HEART_BEAT = null;
		public static final SoundEvent HAND_GLOW = null;
	}
}
