package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.common.entities.Laser;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.entities.Watcher;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;


public class REntities {


    public static final DeferredRegistry<EntityType<?>> ENTITIES = DeferredRegistry.create(RConstants.MODID, Registry.ENTITY_TYPE_REGISTRY);
    public static RegistrySupplier<EntityType<Timelord>> TIMELORD = ENTITIES.register("timelord", () -> registerNoSpawnerBase(Timelord::new, MobCategory.MISC, 0.6F, 1.95F, 128, 1, true, "timelord"));
    public static RegistrySupplier<EntityType<Watcher>> WATCHER = ENTITIES.register("watcher", () -> registerNoSpawnerBase(Watcher::new, MobCategory.MISC, 0.6F, 1.95F, 128, 1, true, "watcher"));
    public static RegistrySupplier<EntityType<Laser>> LASER = ENTITIES.register("laser", () -> registerNoSpawnerBase(Laser::new, MobCategory.MISC, 0.25F, 0.25F, 128, 1, true, "laser"));
    // Entity Creation




}
