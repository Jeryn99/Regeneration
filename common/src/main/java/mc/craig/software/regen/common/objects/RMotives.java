package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class RMotives {

    public static final DeferredRegistry<PaintingVariant> TYPES = DeferredRegistry.create(RConstants.MODID, Registry.PAINTING_VARIANT_REGISTRY);

    public static final RegistrySupplier<PaintingVariant> RASSILON = TYPES.register("rassilon", () -> new PaintingVariant(32, 32));
    public static final RegistrySupplier<PaintingVariant> RASSILON_LARGE = TYPES.register("rassilon_large", () -> new PaintingVariant(48, 48));


}
