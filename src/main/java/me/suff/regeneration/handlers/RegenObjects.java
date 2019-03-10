package me.suff.regeneration.handlers;

import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.common.entity.EntityLindos;
import me.suff.regeneration.common.item.ItemFobWatch;
import me.suff.regeneration.common.item.ItemLindos;
import me.suff.regeneration.util.RegenDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

import static me.suff.regeneration.RegenerationMod.MODID;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegenObjects {
	
	public static List<Item> ITEMS = new ArrayList<>();
	
	public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenDamageSource("regen_energy"),
			REGEN_DMG_HEALING = new RegenDamageSource("regen_heal"), // The irony lmao
			REGEN_DMG_CRITICAL = new RegenDamageSource("regen_crit"), REGEN_DMG_FORCED = new RegenDamageSource("forced").setDamageAllowedInCreativeMode(), REGEN_DMG_LINDOS = new RegenDamageSource("lindos");
	
	@SubscribeEvent
	public static void addItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(
				setUpItem(new ItemFobWatch(), "fob_watch"),
				setUpItem(new ItemLindos(), "lindos_vial")
		);
	}
	
	private static Item setUpItem(Item item, String name) {
		item.setRegistryName(MODID, name);
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
				setUpSound("hand_glow"),
				setUpSound("regeneration_2"),
				setUpSound("fob_watch_dialogue"),
				setUpSound("regeneration_3"),
				setUpSound("grace_hum"),
				setUpSound("regen_breath")
		);
	}
	
	@SubscribeEvent
	public static void addEntities(final RegistryEvent.Register<EntityType<?>> event) {
		IForgeRegistry<EntityType<?>> reg = event.getRegistry();
		reg.register(EntityEntries.ITEM_OVERRIDE_ENTITY_TYPE);
		reg.register(EntityEntries.ITEM_LINDOS_TYPE);
	}
	
	private static SoundEvent setUpSound(String soundName) {
		return new SoundEvent(new ResourceLocation(MODID, soundName)).setRegistryName(soundName);
	}
	
	@ObjectHolder(MODID)
	public static class Items {
		public static final Item FOB_WATCH = null;
		public static final Item LINDOS_VIAL = null;
	}
	
	@ObjectHolder(MODID)
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
		public static EntityType ITEM_OVERRIDE_ENTITY_TYPE = EntityType.register(MODID + ":item_override", EntityType.Builder.create(EntityItemOverride.class, EntityItemOverride::new).tracker(256, 20, false));
		public static EntityType ITEM_LINDOS_TYPE = EntityType.register(MODID + ":lindos", EntityType.Builder.create(EntityLindos.class, EntityLindos::new).tracker(256, 20, false));
	}
}
