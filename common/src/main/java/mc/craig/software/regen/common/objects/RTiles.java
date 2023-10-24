package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.common.blockentity.BioContainerBlockEntity;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RTiles {

    public static final DeferredRegistry<BlockEntityType<?>> TILES = DeferredRegistry.create(RConstants.MODID, Registries.BLOCK_ENTITY_TYPE);

    private static <T extends BlockEntity> BlockEntityType<T> registerTiles(BlockEntityType.BlockEntitySupplier<T> tile, Block validBlock) {
        return BlockEntityType.Builder.of(tile, validBlock).build(null);
    }

    public static final RegistrySupplier<BlockEntityType<BioContainerBlockEntity>> HAND_JAR = TILES.register("hand_jar", () -> registerTiles(BioContainerBlockEntity::new, RBlocks.BIO_CONTAINER.get()));


}
