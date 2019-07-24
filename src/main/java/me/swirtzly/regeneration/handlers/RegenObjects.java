package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.block.BlockHandInJar;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.common.entity.EntityLindos;
import me.swirtzly.regeneration.common.item.ItemFobWatch;
import me.swirtzly.regeneration.common.item.ItemLindos;
import me.swirtzly.regeneration.util.RegenDamageSource;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

import static me.swirtzly.regeneration.RegenerationMod.MODID;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class RegenObjects {
	
	public static List<Item> ITEMS = new ArrayList<>();
	
	public static List<Item> ITEM_BLOCKS = new ArrayList<>();
	
	public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenDamageSource("regen_energy"),
			REGEN_DMG_HEALING = new RegenDamageSource("regen_heal").setDamageAllowedInCreativeMode(), // The irony lmao
			REGEN_DMG_CRITICAL = new RegenDamageSource("regen_crit").setDamageAllowedInCreativeMode(), REGEN_DMG_KILLED = new RegenDamageSource("regen_killed"), REGEN_DMG_LINDOS = new RegenDamageSource("lindos").setDamageAllowedInCreativeMode();
	
	@SubscribeEvent
	public static void addItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(
				setUpItem(new ItemFobWatch(), "fob_watch"),
				setUpItem(new ItemLindos(), "lindos_vial")
		);
		e.getRegistry().registerAll(ITEM_BLOCKS.toArray(new Item[ITEM_BLOCKS.size()]));
	}
	
	private static Item setUpItem(Item item, String name) {
		item.setRegistryName(MODID, name);
		item.setTranslationKey(name);
		ITEMS.add(item);
		return item;
	}
	
	
	private static Block setUpBlock(Block block, String name) {
		block.setRegistryName(MODID, name);
		block.setTranslationKey(MODID + "." + name);
		return block;
	}
	
	private static void registerBlocks(IForgeRegistry<Block> reg, Block... blocks) {
		reg.registerAll(blocks);
		for (Block block : blocks) {
			ITEM_BLOCKS.add(new BlockItem(block).setRegistryName(block.getRegistryName()).setTranslationKey(block.getTranslationKey()));
		}
	}
	
	@SubscribeEvent
	public static void addEntities(RegistryEvent.Register<EntityEntry> e) {
		e.getRegistry().registerAll(EntityEntries.ENTITY_ITEM, EntityEntries.ENTITY_LINDOS);
	}
	
	
	@SubscribeEvent
	public static void addBlocks(RegistryEvent.Register<Block> e) {
		registerBlocks(e.getRegistry(), setUpBlock(new BlockHandInJar(), "hand_jar"));
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
				setUpSound("regen_breath"),
				setUpSound("alarm"),
				setUpSound("jar_bubbles")
		);
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
		public static final SoundEvent ALARM = null;
		public static final SoundEvent JAR_BUBBLES = null;
	}

	public static class EntityEntries {
		public static EntityType ITEM_OVERRIDE_ENTITY_TYPE = EntityType.register(MODID + ":item_override", EntityType.Builder.create(EntityItemOverride.class, EntityItemOverride::new).tracker(256, 20, false));
		public static EntityType ITEM_LINDOS_TYPE = EntityType.register(MODID + ":lindos", EntityType.Builder.create(EntityLindos.class, EntityLindos::new).tracker(256, 20, false));
	}
	
	@GameRegistry.ObjectHolder(MODID)
	public static class Blocks {
		public static final Block HAND_JAR = null;
	}
}
