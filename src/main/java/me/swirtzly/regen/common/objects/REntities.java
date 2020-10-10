package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.common.entities.OverrideEntity;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.entities.WatcherEntity;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class REntities {


    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RConstants.MODID);
    public static RegistryObject<EntityType<OverrideEntity>> ITEM_OVERRIDE_ENTITY_TYPE = ENTITIES.register("item_override", () -> registerNoSpawnerBase(OverrideEntity::new, EntityClassification.MISC, 0.5F, 0.2F, 128, 1, true, "item_override"));
    public static RegistryObject<EntityType<TimelordEntity>> TIMELORD = ENTITIES.register("timelord", () -> registerNoSpawnerBase(TimelordEntity::new, EntityClassification.MISC, 0.6F, 1.95F, 128, 1, true, "timelord"));
    public static RegistryObject<EntityType<WatcherEntity>> WATCHER = ENTITIES.register("watcher", () -> registerNoSpawnerBase(WatcherEntity::new, EntityClassification.MISC, 0.6F, 1.95F, 128, 1, true, "watcher"));


    // Entity Creation
    private static <T extends Entity> EntityType<T> registerNoSpawnerBase(EntityType.IFactory<T> factory, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
        ResourceLocation loc = new ResourceLocation(RConstants.MODID, name);
        EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
        builder.setShouldReceiveVelocityUpdates(sendUpdate);
        builder.setTrackingRange(trackingRange);
        builder.setUpdateInterval(updateFreq);
        builder.size(width, height);
        return builder.build(loc.toString());
    }

    private static <T extends Entity> EntityType<T> registerBase(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
        ResourceLocation loc = new ResourceLocation(RConstants.MODID, name);
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
        ResourceLocation loc = new ResourceLocation(RConstants.MODID, name);
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

}
