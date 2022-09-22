package mc.craig.software.regen.common.objects;

import com.google.common.base.Supplier;
import mc.craig.software.regen.common.entities.Laser;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.entities.Watcher;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;


public class REntities {

    public static final DeferredRegistry<EntityType<?>> ENTITY_TYPES = DeferredRegistry.create(RConstants.MODID, Registry.ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<EntityType<Timelord>> TIMELORD = ENTITY_TYPES.register("timelord", () -> EntityType.Builder.of((EntityType.EntityFactory<Timelord>) (entityType, level) -> new Timelord(level), MobCategory.CREATURE).sized(0.6F, 1.95F).build(RConstants.MODID + ":timelord"));
    public static final RegistrySupplier<EntityType<Watcher>> WATCHER = ENTITY_TYPES.register("watcher", () -> EntityType.Builder.of((EntityType.EntityFactory<Watcher>) (entityType, level) -> new Watcher(level), MobCategory.MISC).sized(0.6F, 1.95F).updateInterval(10).clientTrackingRange(4).build(RConstants.MODID + ":watcher"));

    public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITY_TYPES.register(id, () -> builderSupplier.get().build(RConstants.MODID + ":" + id));
    }

    public static final RegistrySupplier<EntityType<Laser>> LASER = ENTITY_TYPES.register("laser", () -> EntityType.Builder.of((EntityType.EntityFactory<Laser>) (entityType, level) -> new Laser(REntities.LASER.get(), level), MobCategory.MISC).sized(0.25F, 0.25F).updateInterval(10).clientTrackingRange(4).build(RConstants.MODID + ":laser"));


}
