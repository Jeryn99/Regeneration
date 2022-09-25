package me.craig.software.regen.common.objects;

import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.common.block.JarBlock;
import me.craig.software.regen.common.block.ROreBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static me.craig.software.regen.common.objects.RItems.MAIN;

public class RBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RConstants.MODID);

    public static final RegistryObject<Block> BIO_CONTAINER = register("bio_container", JarBlock::new, MAIN);
    public static final RegistryObject<Block> ZINC_ORE = register("zinc_ore", () -> new ROreBlock(AbstractBlock.Properties.of(Material.STONE).lightLevel(getLightValueLit(9)).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> ZERO_ROUNDEL = register("zero_roundel_half", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> ZERO_ROOM_FULL = register("zero_roundel_full", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> AZBANTIUM = register("azbantium", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static void genBlockItems(Block... blocks) {
        for (Block block : blocks) {
            RItems.ITEMS.register(block.getRegistryName().getPath(), () -> new BlockItem(block, new Item.Properties().tab(MAIN)));
        }
    }

    /**
     * Registers a Block and BlockItem to the ItemGroup of your choice
     *
     * @param <T>
     * @param id
     * @param blockSupplier
     * @param itemGroup
     * @return
     */
    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, ItemGroup itemGroup) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        RItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(itemGroup)));
        return registryObject;
    }

    /**
     * Registers a Block without a BlockItem
     * <br> Use when you need a special BlockItem. The BlockItem should be registered in RItems with the same registry name as the block
     *
     * @param <T>
     * @param id
     * @param blockSupplier
     * @return
     */
    private static <T extends Block> RegistryObject<T> registerBlockOnly(String id, Supplier<T> blockSupplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        return registryObject;
    }

    /**
     * Registers a Block and BlockItem into the Main ItemGroup
     *
     * @param <T>
     * @param id
     * @param blockSupplier
     * @return
     */
    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        RItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(MAIN)));
        return registryObject;
    }


}
