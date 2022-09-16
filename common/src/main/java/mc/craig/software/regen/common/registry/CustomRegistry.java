package mc.craig.software.regen.common.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Set;

public abstract class CustomRegistry<T> {

    @ExpectPlatform
    public static <T> CustomRegistry<T> create(Class<T> clazz, ResourceLocation id) {
        throw new AssertionError();
    }

    public abstract T get(ResourceLocation key);

    public abstract ResourceLocation getKey(T object);

    public abstract Set<ResourceLocation> getKeys();

    public abstract Collection<T> getValues();

}