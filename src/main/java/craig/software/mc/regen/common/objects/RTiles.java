package craig.software.mc.regen.common.objects;

import craig.software.mc.regen.common.blockentity.BioContainerBlockEntity;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RTiles {

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RConstants.MODID);

    private static <T extends BlockEntity> BlockEntityType<T> registerTiles(BlockEntityType.BlockEntitySupplier<T> tile, Block validBlock) {
        return BlockEntityType.Builder.of(tile, validBlock).build(null);
    }

    public static final RegistryObject<BlockEntityType<BioContainerBlockEntity>> HAND_JAR = TILES.register("hand_jar", () -> registerTiles(BioContainerBlockEntity::new, RBlocks.BIO_CONTAINER.get()));


}
