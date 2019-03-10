package me.suff.regeneration.handlers;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.common.entity.EntityLindos;
import me.suff.regeneration.common.item.ItemFobWatch;
import me.suff.regeneration.common.item.ItemLindos;
import me.suff.regeneration.util.RegenDamageSource;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class RegenObjects {
	
	public static List<Item> ITEMS = new ArrayList<>();
	
	public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenDamageSource("regen_energy"),
			REGEN_DMG_HEALING = new RegenDamageSource("regen_heal"), // The irony lmao
			REGEN_DMG_CRITICAL = new RegenDamageSource("regen_crit"), REGEN_DMG_LINDOS = new RegenDamageSource("lindos").setDamageAllowedInCreativeMode();
	
	@SubscribeEvent
	public static void addItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(
				setUpItem(new ItemFobWatch(), "fob_watch"),
				setUpItem(new ItemLindos(), "lindos_vial").setCreativeTab(CreativeTabs.BREWING)
		);
	}
	
	private static Item setUpItem(Item item, String name) {
		item.setRegistryName(RegenerationMod.MODID, name);
		item.setTranslationKey(name);
		ITEMS.add(item);
		return item;
	}
	
	@SubscribeEvent
	public static void addEntities(RegistryEvent.Register<EntityEntry> e) {
		e.getRegistry().registerAll(EntityEntries.ENTITY_ITEM, EntityEntries.ENTITY_LINDOS);
	}
	
	@SubscribeEvent
	public static void addSounds(RegistryEvent.Register<SoundEvent> e) {
		e.getRegistry().registerAll(
				setUpSound("regeneration"),
				setUpSound("fob_watch"),
				setUpSound("critical_stage"),
				setUpSound("heart_beat"),
				setUpSound("hand_glow"),
				setUpSound("regeneration_2"),
				setUpSound("fob_watch_dialogue"),
				setUpSound("regeneration_3"),
				setUpSound("grace_hum"),
				setUpSound("regen_breath")
		);
	}
	
	private static SoundEvent setUpSound(String soundName) {
		return new SoundEvent(new ResourceLocation(RegenerationMod.MODID, soundName)).setRegistryName(soundName);
	}
	
	@GameRegistry.ObjectHolder(RegenerationMod.MODID)
	public static class Items {
		public static final Item FOB_WATCH = null;
		public static final Item LINDOS_VIAL = null;
	}
	
	@GameRegistry.ObjectHolder(RegenerationMod.MODID)
	public static class Sounds {
		public static final SoundEvent FOB_WATCH = null;
		public static final SoundEvent FOB_WATCH_DIALOGUE = null;
		public static final SoundEvent REGENERATION = null;
		public static final SoundEvent REGENERATION_2 = null;
		public static final SoundEvent CRITICAL_STAGE = null;
		public static final SoundEvent HEART_BEAT = null;
		public static final SoundEvent HAND_GLOW = null;
		public static final SoundEvent REGENERATION_3 = null;
		public static final SoundEvent GRACE_HUM = null;
		public static final SoundEvent REGEN_BREATH = null;
	}
	
	public static class EntityEntries {
		public static final EntityEntry ENTITY_ITEM = EntityEntryBuilder.create().entity(EntityItemOverride.class).id(new ResourceLocation(RegenerationMod.MODID, "fob_watch"), 0).name("fob").tracker(80, 3, false).build();
		public static final EntityEntry ENTITY_LINDOS = EntityEntryBuilder.create().entity(EntityLindos.class).id(new ResourceLocation(RegenerationMod.MODID, "lindos"), 1).name("lindos").tracker(80, 3, false).build();
	}
}
