package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.entities.Cyberman;
import me.suff.mc.regen.common.entities.Laser;
import me.suff.mc.regen.common.entities.Timelord;
import me.suff.mc.regen.common.entities.Watcher;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class REntities {


    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RConstants.MODID);
    public static RegistryObject<EntityType<Timelord>> TIMELORD = ENTITIES.register("timelord", () -> registerNoSpawnerBase(Timelord::new, MobCategory.MISC, 0.6F, 1.95F, 128, 1, true, "timelord"));
    public static RegistryObject<EntityType<Watcher>> WATCHER = ENTITIES.register("watcher", () -> registerNoSpawnerBase(Watcher::new, MobCategory.MISC, 0.6F, 1.95F, 128, 1, true, "watcher"));
    public static RegistryObject<EntityType<Laser>> LASER = ENTITIES.register("laser", () -> registerNoSpawnerBase(Laser::new, MobCategory.MISC, 0.25F, 0.25F, 128, 1, true, "laser"));
    public static RegistryObject<EntityType<Cyberman>> CYBERLORD = ENTITIES.register("cyberman", () -> registerNoSpawnerBase(Cyberman::new, MobCategory.MISC, 0.6F, 1.95F, 128, 1, true, "cyberman"));

    // Entity Creation
    private static <T extends Entity> EntityType<T> registerNoSpawnerBase(EntityType.EntityFactory<T> factory, MobCategory classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
        ResourceLocation loc = new ResourceLocation(RConstants.MODID, name);
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, classification);
        builder.setShouldReceiveVelocityUpdates(sendUpdate);
        builder.setTrackingRange(trackingRange);
        builder.setUpdateInterval(updateFreq);

        builder.sized(width, height);
        return builder.build(loc.toString());
    }

    private static <T extends Entity> EntityType<T> registerBase(EntityType.EntityFactory<T> factory, IClientSpawner<T> client, MobCategory classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
        ResourceLocation loc = new ResourceLocation(RConstants.MODID, name);
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, classification);
        builder.setShouldReceiveVelocityUpdates(sendUpdate);
        builder.setTrackingRange(trackingRange);
        builder.setUpdateInterval(updateFreq);
        builder.sized(width, height);
        builder.setCustomClientFactory((spawnEntity, world) -> client.spawn(world));
        return builder.build(loc.toString());
    }

    // Fire Resistant Entity Creation
    private static <T extends Entity> EntityType<T> registerFireImmuneBase(EntityType.EntityFactory<T> factory, IClientSpawner<T> client, MobCategory classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
        ResourceLocation loc = new ResourceLocation(RConstants.MODID, name);
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, classification);
        builder.setShouldReceiveVelocityUpdates(sendUpdate);
        builder.setTrackingRange(trackingRange);
        builder.setUpdateInterval(updateFreq);
        builder.fireImmune();
        builder.sized(width, height);
        builder.setCustomClientFactory((spawnEntity, world) -> client.spawn(world));
        EntityType<T> type = builder.build(loc.toString());
        return type;
    }

    private static <T extends Entity> EntityType<T> registerFireResistMob(EntityType.EntityFactory<T> factory, IClientSpawner<T> client, MobCategory classification, float width, float height, String name, boolean velocity) {
        return registerFireImmuneBase(factory, client, classification, width, height, 80, 3, velocity, name);
    }

    private static <T extends Entity> EntityType<T> registerStatic(EntityType.EntityFactory<T> factory, IClientSpawner<T> client, MobCategory classification, float width, float height, String name) {
        return registerBase(factory, client, classification, width, height, 64, 40, false, name);
    }

    private static <T extends Entity> EntityType<T> registerMob(EntityType.EntityFactory<T> factory, IClientSpawner<T> client, MobCategory classification, float width, float height, String name, boolean velocity) {
        return registerBase(factory, client, classification, width, height, 80, 3, velocity, name);
    }

    private static <T extends Entity> EntityType<T> registerNonSpawner(EntityType.EntityFactory<T> factory, MobCategory classification, float width, float height, boolean velocity, String name) {
        return registerNoSpawnerBase(factory, classification, width, height, 64, 40, velocity, name);
    }

    public interface IClientSpawner<T> {
        T spawn(Level world);
    }

}
