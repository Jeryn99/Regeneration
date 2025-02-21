package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.common.block.ROreBlock;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class RBlocks {

    public static final DeferredRegistry<Block> BLOCKS = DeferredRegistry.create(RConstants.MODID, Registries.BLOCK);

    public static final RegistrySupplier<Block> ZERO_ROUNDEL = register("zero_roundel_half", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistrySupplier<Block> ZERO_ROOM_FULL = register("zero_roundel_full", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistrySupplier<Block> AZBANTIUM = register("azbantium", () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.STONE).strength(50.0F, 1200.0F)));

    /**
     * Registers a Block without a BlockItem
     * <br> Use when you need a special BlockItem. The BlockItem should be registered in RItems with the same registry name as the block
     */
    private static <T extends Block> RegistrySupplier<T> registerBlockOnly(String id, Supplier<T> blockSupplier) {
        return BLOCKS.register(id, blockSupplier);
    }

    /**
     * Registers a Block and BlockItem into the Main ItemGroup
     */
    private static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> blockSupplier) {
        RegistrySupplier<T> RegistrySupplier = BLOCKS.register(id, blockSupplier);
        RItems.ITEMS.register(id, () -> new BlockItem(RegistrySupplier.get(), new Item.Properties()));
        return RegistrySupplier;
    }


}
