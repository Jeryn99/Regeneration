package mc.craig.software.regen.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.common.registry.CustomRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.function.Supplier;

public abstract class DeferredRegistry<T> {

    public static <T> DeferredRegistry<T> create(String modid, CustomRegistry<T> registry) {
        return create(modid, registry.getRegistryKey());
    }

    @ExpectPlatform
    public static <T> DeferredRegistry<T> create(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
        throw new AssertionError();
    }

    public abstract void register();

    public abstract <R extends T> RegistrySupplier<R> register(String id, Supplier<R> supplier);

    public abstract Collection<RegistrySupplier<T>> getEntries();

}
