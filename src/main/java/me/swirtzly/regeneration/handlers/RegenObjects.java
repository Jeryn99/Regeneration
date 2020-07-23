package me.swirtzly.regeneration.handlers;


import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.gui.BioContainerContainer;
import me.swirtzly.regeneration.common.block.ArchBlock;
import me.swirtzly.regeneration.common.block.BlockHandInJar;
import me.swirtzly.regeneration.common.block.ZeroRoomBlock;
import me.swirtzly.regeneration.common.dimension.DimSingle;
import me.swirtzly.regeneration.common.dimension.GallifreyChunkGenerator;
import me.swirtzly.regeneration.common.dimension.GallifreyDimension;
import me.swirtzly.regeneration.common.dimension.biomes.*;
import me.swirtzly.regeneration.common.dimension.features.FeatureSpikeyBoys;
import me.swirtzly.regeneration.common.dimension.features.GallifreyanHuts;
import me.swirtzly.regeneration.common.dimension.features.GallifreyanTreeFeature;
import me.swirtzly.regeneration.common.dimension.features.SkullFeature;
import me.swirtzly.regeneration.common.entity.LaserEntity;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.item.*;
import me.swirtzly.regeneration.common.tiles.ArchTile;
import me.swirtzly.regeneration.common.tiles.HandInJarTile;
import me.swirtzly.regeneration.util.common.ICompatObject;
import me.swirtzly.regeneration.util.common.RegenDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.function.Supplier;

import static me.swirtzly.regeneration.Regeneration.MODID;

/**
 * Created by Sub on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegenObjects {

	public static DimensionType GALLIFREY_TYPE;

	private static ItemGroup itemGroup = ItemGroups.REGEN_TAB;

    public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenDamageSource("regen_energy"), REGEN_DMG_HEALING = new RegenDamageSource("regen_heal").setDamageAllowedInCreativeMode(), // The irony lmao
			REGEN_DMG_CRITICAL = new RegenDamageSource("regen_crit").setDamageAllowedInCreativeMode(), REGEN_DMG_KILLED = new RegenDamageSource("regen_killed"), REGEN_DMG_FORCED = new RegenDamageSource("forced").setDamageAllowedInCreativeMode();
    
 	//Container Creation
 	private static <T extends Container> ContainerType<T> registerContainer(IContainerFactory<T> fact, String name){
		ContainerType<T> type = new ContainerType<T>(fact);
		type.setRegistryName(new ResourceLocation(Regeneration.MODID, name));
		return type;
	}

 	private static <T extends Container> ContainerType<T> registerContainerSpecial(IContainerFactory<T> fact, int windowId, PlayerInventory playerInv, PlayerEntity player, PacketBuffer buf ,TileEntity te, String name){
		ContainerType<T> type = new ContainerType<T>(fact);
		type.setRegistryName(new ResourceLocation(Regeneration.MODID, name));
		return type;
	}
	
 	// Entity Creation
	private static <T extends Entity> EntityType<T> registerNoSpawnerBase(EntityType.IFactory<T> factory, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
		ResourceLocation loc = new ResourceLocation(Regeneration.MODID, name);
 		EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
 		builder.setShouldReceiveVelocityUpdates(sendUpdate);
 		builder.setTrackingRange(trackingRange);
 		builder.setUpdateInterval(updateFreq);
 		builder.size(width, height);
		return builder.build(loc.toString());
 	}

 	private static <T extends Entity> EntityType<T> registerBase(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
		ResourceLocation loc = new ResourceLocation(Regeneration.MODID, name);
 		EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
 		builder.setShouldReceiveVelocityUpdates(sendUpdate);
 		builder.setTrackingRange(trackingRange);
 		builder.setUpdateInterval(updateFreq);
 		builder.size(width, height);
 		builder.setCustomClientFactory((spawnEntity, world) -> client.spawn(world));
		return builder.build(loc.toString());
 	}

 	// Fire Resistant Entity Creation
 	private static <T extends Entity> EntityType<T> registerFireImmuneBase(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
		ResourceLocation loc = new ResourceLocation(Regeneration.MODID, name);
 		EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
 		builder.setShouldReceiveVelocityUpdates(sendUpdate);
 		builder.setTrackingRange(trackingRange);
 		builder.setUpdateInterval(updateFreq);
 		builder.immuneToFire();
 		builder.size(width, height);
 		builder.setCustomClientFactory((spawnEntity, world) -> client.spawn(world));
 		EntityType<T> type = builder.build(loc.toString());
 		return type;
 	}

	private static <C extends IFeatureConfig, F extends Feature<C>> F registerFeatures(F value) {
		return value;
	}

	//Registry Methods
	private static Item setUpItem(Item item) {
		return item;
	}

	@SubscribeEvent
	public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(WorldGenEntries.TREES.setRegistryName(MODID, "trees"));
		event.getRegistry().register(WorldGenEntries.SPIKEYS.setRegistryName(MODID, "spikeys"));
		event.getRegistry().register(WorldGenEntries.SKULLS.setRegistryName(MODID, "skulls"));
		event.getRegistry().register(WorldGenEntries.HUT.setRegistryName(MODID, "huts"));
	}

	public static class Sounds {
		public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, Regeneration.MODID);

		public static final RegistryObject<SoundEvent> FOB_WATCH = SOUNDS.register("fob_watch", () -> setUpSound("fob_watch"));
		public static final RegistryObject<SoundEvent> FOB_WATCH_DIALOGUE = SOUNDS.register("fob_watch_dialogue", () -> setUpSound("fob_watch_dialogue"));
		public static final RegistryObject<SoundEvent> CRITICAL_STAGE = SOUNDS.register("critical_stage", () -> setUpSound("critical_stage"));
		public static final RegistryObject<SoundEvent> HEART_BEAT = SOUNDS.register("heart_beat", () -> setUpSound("heart_beat"));
		public static final RegistryObject<SoundEvent> HAND_GLOW = SOUNDS.register("hand_glow", () -> setUpSound("hand_glow"));
		public static final RegistryObject<SoundEvent> GRACE_HUM = SOUNDS.register("grace_hum", () -> setUpSound("grace_hum"));
		public static final RegistryObject<SoundEvent> REGEN_BREATH = SOUNDS.register("regen_breath", () -> setUpSound("regen_breath"));
		public static final RegistryObject<SoundEvent> ALARM = SOUNDS.register("alarm", () -> setUpSound("alarm"));
		public static final RegistryObject<SoundEvent> JAR_BUBBLES = SOUNDS.register("jar_bubbles", () -> setUpSound("jar_bubbles"));

        public static final RegistryObject<SoundEvent> REGENERATION_0 = SOUNDS.register("regeneration_0", () -> setUpSound("regeneration_0"));
		public static final RegistryObject<SoundEvent> REGENERATION_1 = SOUNDS.register("regeneration_1", () -> setUpSound("regeneration_1"));
		public static final RegistryObject<SoundEvent> REGENERATION_2 = SOUNDS.register("regeneration_2", () -> setUpSound("regeneration_2"));
		public static final RegistryObject<SoundEvent> REGENERATION_3 = SOUNDS.register("regeneration_3", () -> setUpSound("regeneration_3"));
		public static final RegistryObject<SoundEvent> REGENERATION_4 = SOUNDS.register("regeneration_4", () -> setUpSound("regeneration_4"));
		public static final RegistryObject<SoundEvent> REGENERATION_5 = SOUNDS.register("regeneration_5", () -> setUpSound("regeneration_5"));
		public static final RegistryObject<SoundEvent> REGENERATION_6 = SOUNDS.register("regeneration_6", () -> setUpSound("regeneration_6"));
		public static final RegistryObject<SoundEvent> REGENERATION_7 = SOUNDS.register("regeneration_7", () -> setUpSound("regeneration_7"));
		public static final RegistryObject<SoundEvent> STASER = SOUNDS.register("staser", () -> setUpSound("staser"));
		public static final RegistryObject<SoundEvent> RIFLE = SOUNDS.register("rifle", () -> setUpSound("rifle"));
	}

	private static void genBlockItems(Collection<RegistryObject<Block>> collection) {
		for (RegistryObject<Block> block : collection) {
			if (block.get() instanceof ICompatObject && !ModList.get().isLoaded("tardis")) {
				itemGroup = null;
			} else {
				itemGroup = ItemGroups.REGEN_TAB;
				Blocks.BLOCK_ITEMS.register(block.get().getRegistryName().getPath(), () -> setUpItem(new BlockItem(block.get(), new Item.Properties().group(itemGroup))));
			}
		}
	}
	
	@SubscribeEvent
	public static void regBlockItems(RegistryEvent.Register<Item> e) {
		genBlockItems(Blocks.BLOCKS.getEntries());
	}

    public static class Blocks {
        public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Regeneration.MODID);
        public static final DeferredRegister<Item> BLOCK_ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Regeneration.MODID);
        public static final RegistryObject<Block> HAND_JAR = BLOCKS.register("hand_jar", () -> setUpBlock(new BlockHandInJar()));
        public static final RegistryObject<Block> ARCH = BLOCKS.register("arch", () -> setUpBlock(new ArchBlock(Block.Properties.create(Material.PISTON).hardnessAndResistance(1.25F, 10))));
        public static final RegistryObject<Block> GAL_ORE = BLOCKS.register("gal_ore", () -> setUpBlock(new OreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F))));
        public static final RegistryObject<Block> ZERO_ROOM = BLOCKS.register("zero_roundel_one", () -> setUpBlock(new ZeroRoomBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F))));
        public static final RegistryObject<Block> ZERO_ROOM_TWO = BLOCKS.register("zero_roundel_two", () -> setUpBlock(new ZeroRoomBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F))));
	}
	
	private static Block setUpBlock(Block block) {
		return block;
	}

	@SubscribeEvent
	public static void addSpawns(FMLLoadCompleteEvent e) {
		RegenObjects.GallifreyBiomes.GALLIFREY_MOUNTAINS.get().getSpawns(EntityClassification.AMBIENT).add(new Biome.SpawnListEntry(RegenObjects.EntityEntries.TIMELORD.get(), 5, 1, 1));
		RegenObjects.GallifreyBiomes.REDLANDS_FOREST.get().getSpawns(EntityClassification.AMBIENT).add(new Biome.SpawnListEntry(RegenObjects.EntityEntries.TIMELORD.get(), 5, 1, 1));
		RegenObjects.GallifreyBiomes.REDLANDS.get().getSpawns(EntityClassification.AMBIENT).add(new Biome.SpawnListEntry(RegenObjects.EntityEntries.TIMELORD.get(), 5, 1, 1));
	}
    
    // Tile Creation
 	private static <T extends TileEntity> TileEntityType<T> registerTiles(Supplier<T> tile, Block... validBlock) {
 		return TileEntityType.Builder.create(tile, validBlock).build(null);
 	}
 	
	public static class Tiles {
		public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Regeneration.MODID);

		public static final RegistryObject<TileEntityType<HandInJarTile>> HAND_JAR = TILES.register("hand_jar", () -> registerTiles(HandInJarTile::new, Blocks.HAND_JAR.get()));
        public static final RegistryObject<TileEntityType<ArchTile>> ARCH = TILES.register("arch", () -> registerTiles(ArchTile::new, Blocks.ARCH.get()));
	}
	
	public static class Containers {
		public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Regeneration.MODID);

		//Sorry if this is hacky, needed a way to register your custom parameters, so doing this for now until I can make a helper method
		public static final RegistryObject<ContainerType<BioContainerContainer>> BIO_CONTAINER = CONTAINERS.register("bio_container",
				() -> IForgeContainerType.create((windowId, inv, data) -> {
					BlockPos pos = data.readBlockPos();
					return new BioContainerContainer(windowId, inv, Regeneration.proxy.getClientPlayer(), (HandInJarTile) Minecraft.getInstance().world.getTileEntity(pos));
				}));
	}
	
	private static SoundEvent setUpSound(String soundName) {
		return new SoundEvent(new ResourceLocation(MODID, soundName));
	}

	public static class EntityEntries {
		public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Regeneration.MODID);

		public static RegistryObject<EntityType<OverrideEntity>> ITEM_OVERRIDE_ENTITY_TYPE = ENTITIES.register("item_override", () -> registerNoSpawnerBase(OverrideEntity::new, EntityClassification.MISC, 0.5F, 0.2F, 128, 1, true, "item_override"));
		public static RegistryObject<EntityType<TimelordEntity>> TIMELORD = ENTITIES.register("timelord", () -> registerNoSpawnerBase(TimelordEntity::new, EntityClassification.AMBIENT, 0.6F, 1.95F, 128, 1, true, "timelord"));
		public static RegistryObject<EntityType<LaserEntity>> LASER = ENTITIES.register("laser", () -> registerMob(LaserEntity::new, LaserEntity::new, EntityClassification.MISC, 0.5F, 0.5F, "laser", true));
	}

	public static class GallifreyBiomes {
		public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, Regeneration.MODID);

		public static final RegistryObject<Biome> GALLIFREY_MOUNTAINS = BIOMES.register("gallifreyan_mountains", GallifreyanMountainsBiome::new);
		public static final RegistryObject<Biome> GALLIFREYAN_RIVER = BIOMES.register("gallifreyan_river", GallifreyanRiver::new);
		public static final RegistryObject<Biome> GALLIFREYAN_OCEAN = BIOMES.register("gallifreyan_ocean", GallifreyanOcean::new);

		public static final RegistryObject<Biome> REDLANDS = BIOMES.register("redlands", GallifreyanRedLands::new);
		public static final RegistryObject<Biome> WASTELANDS = BIOMES.register("wastelands", GallifrayanWastelands::new);
		public static final RegistryObject<Biome> REDLANDS_FOREST = BIOMES.register("redlands_forest", GallifreyanRedlandsForest::new);
		public static final RegistryObject<Biome> WASTELANDS_MOUNTAINS = BIOMES.register("wasteland_mountains", GallifreyanWastelandsMountains::new);
		public static final RegistryObject<Biome> GOLDEN_FIELDS = BIOMES.register("golden_fields", GallifreyanGoldenFields::new);

		public static Biome[] getBiomes() {
			return new Biome[]{
					GALLIFREY_MOUNTAINS.get(),
					GALLIFREYAN_RIVER.get(),
					GALLIFREYAN_OCEAN.get(),
					REDLANDS.get(),
					WASTELANDS.get(),
					REDLANDS_FOREST.get(),
					WASTELANDS_MOUNTAINS.get(),
					GOLDEN_FIELDS.get()
			};
		}

		public static void registerBiomeTypes() {
			addBiomeTypes(GALLIFREY_MOUNTAINS, BiomeDictionary.Type.MOUNTAIN);
			addBiomeTypes(GALLIFREYAN_RIVER, BiomeDictionary.Type.RIVER);
			addBiomeTypes(GALLIFREYAN_OCEAN, BiomeDictionary.Type.OCEAN);
			addBiomeTypes(REDLANDS, BiomeDictionary.Type.PLAINS);
			addBiomeTypes(WASTELANDS, BiomeDictionary.Type.SANDY);
			addBiomeTypes(REDLANDS_FOREST, BiomeDictionary.Type.PLAINS);
		}

		public static void addBiomeTypes(RegistryObject<Biome> biome, BiomeDictionary.Type biomeType) {
	 		BiomeDictionary.addTypes(biome.get(), biomeType);
	 	}
	}
	
	public static class ChunkGeneratorTypes{
		public static final DeferredRegister<ChunkGeneratorType<?, ?>> CHUNK_GENERATOR_TYPES = new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, Regeneration.MODID);

		public static IChunkGeneratorFactory<OverworldGenSettings, GallifreyChunkGenerator> factory = GallifreyChunkGenerator::new;

		public static final RegistryObject<ChunkGeneratorType<OverworldGenSettings, GallifreyChunkGenerator>> GALLIFREY_CHUNKS =
				CHUNK_GENERATOR_TYPES.register("gallifrey_chunks", () ->
						registerChunkGeneratorType(factory, OverworldGenSettings::new, false));
	}
 	
	public static class Dimensions{
		public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Regeneration.MODID);

		public static final RegistryObject<ModDimension> GALLIFREY = DIMENSIONS.register("gallifrey", () -> registerDimensions(new DimSingle(GallifreyDimension::new)));
	}

	public static class WorldGenEntries {

		public static final GallifreyanTreeFeature TREES = new GallifreyanTreeFeature(NoFeatureConfig::deserialize);
		public static final FeatureSpikeyBoys SPIKEYS = new FeatureSpikeyBoys(NoFeatureConfig::deserialize);
		public static final SkullFeature SKULLS = new SkullFeature(ProbabilityConfig::deserialize);
		public static final GallifreyanHuts HUT = new GallifreyanHuts(NoFeatureConfig::deserialize);

	}


	public static class Items {
		public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Regeneration.MODID);

		public static final RegistryObject<Item> FOB_WATCH = ITEMS.register("fob_watch", FobWatchItem::new);
		public static final RegistryObject<Item> HAND = ITEMS.register("hand", HandItem::new);
		public static final RegistryObject<Item> ARCH_PART = ITEMS.register("arch_part", ComponentItem::new);
		public static final RegistryObject<Item> GAL_INGOT = ITEMS.register("gal_ingot", IngotItem::new);
		public static final RegistryObject<Item> SEAL = ITEMS.register("rassilon_seal", SealItem::new);
		public static final RegistryObject<Item> DIAL = ITEMS.register("confession_dial", ConfessionDialItem::new);
		public static final RegistryObject<Item> RIFLE = ITEMS.register("time_lord_rifle", () -> new GunItem(30, 10, 5));
		public static final RegistryObject<Item> PISTOL = ITEMS.register("time_lord_gun", () -> new GunItem(18, 5, 2));
		public static final RegistryObject<Item> ROBES_CHEST = ITEMS.register("robes", () -> new DyeableClothingItem(EquipmentSlotType.CHEST));
		public static final RegistryObject<Item> ROBES_HEAD = ITEMS.register("robes_hat", () -> new DyeableClothingItem(EquipmentSlotType.HEAD));

		public static final RegistryObject<Item> GUARD_HEAD = ITEMS.register("guard_head", () -> new ClothingItem(EquipmentSlotType.HEAD));
		public static final RegistryObject<Item> GUARD_CHEST = ITEMS.register("guard_chest", () -> new ClothingItem(EquipmentSlotType.CHEST));
		public static final RegistryObject<Item> GUARD_LEGGINGS = ITEMS.register("guard_leggings", () -> new ClothingItem(EquipmentSlotType.LEGS));
		public static final RegistryObject<Item> GUARD_FEET = ITEMS.register("guard_feet", () -> new ClothingItem(EquipmentSlotType.FEET));

	}

	private static <T extends Entity> EntityType<T> registerFireResistMob(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, String name, boolean velocity) {
		return registerFireImmuneBase(factory, client, classification, width, height, 80, 3, velocity, name);
	}

	private static <T extends Entity> EntityType<T> registerStatic(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, String name) {
		return registerBase(factory, client, classification, width, height, 64, 40, false, name);
	}

	private static <T extends Entity> EntityType<T> registerMob(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, String name, boolean velocity) {
 		return registerBase(factory, client, classification, width, height, 80, 3, velocity, name);
 	}
 	
	private static <T extends Entity> EntityType<T> registerNonSpawner(EntityType.IFactory<T> factory, EntityClassification classification, float width, float height, boolean velocity, String name) {
 		return registerNoSpawnerBase(factory, classification, width, height, 64, 40, velocity, name);
 	}
 	
 	public interface IClientSpawner<T> {
		T spawn(World world);
	}
 	
	//Chunk Generator Type creation
 	private static  <C extends GenerationSettings, T extends ChunkGenerator<C>> 
	ChunkGeneratorType<C, T> 
	registerChunkGeneratorType(IChunkGeneratorFactory<C, T> factoryIn, Supplier<C> settingsIn, boolean canUseForBuffet) {
		ChunkGeneratorType<C, T> type = new ChunkGeneratorType<C, T>(factoryIn, canUseForBuffet, settingsIn);
		return type;
	}
	
 	private static ModDimension registerDimensions(ModDimension type) {
		return type;
	}
}
