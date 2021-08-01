package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.block.ROreBlock;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static me.suff.mc.regen.common.objects.RItems.MAIN;

public class RBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RConstants.MODID);

    public static final RegistryObject<Block> BIO_CONTAINER = register("bio_container", JarBlock::new, MAIN);
    public static final RegistryObject<Block> ZINC_ORE = register("zinc_ore", () -> new ROreBlock(BlockBehaviour.Properties.of(Material.STONE).lightLevel(getLightValueLit(9)).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> ZERO_ROUNDEL = register("zero_roundel_half", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> ZERO_ROOM_FULL = register("zero_roundel_full", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> AZBANTIUM = register("azbantium", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

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
     */
    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, CreativeModeTab itemGroup) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        RItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(itemGroup)));
        return registryObject;
    }

    /**
     * Registers a Block without a BlockItem
     * <br> Use when you need a special BlockItem. The BlockItem should be registered in RItems with the same registry name as the block
     */
    private static <T extends Block> RegistryObject<T> registerBlockOnly(String id, Supplier<T> blockSupplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        return registryObject;
    }

    /**
     * Registers a Block and BlockItem into the Main ItemGroup
     */
    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        RItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(MAIN)));
        return registryObject;
    }


}
