package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.block.ROreBlock;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static me.suff.mc.regen.common.objects.RItems.MAIN;

public class RBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RConstants.MODID);

    public static final RegistryObject<Block> BIO_CONTAINER = register("bio_container", JarBlock::new, MAIN);
    public static final RegistryObject<Block> ZERO_ROUNDEL = register("zero_roundel_half", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> ZERO_ROOM_FULL = register("zero_roundel_full", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> AZBANTIUM = register("azbantium", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    static BlockBehaviour.Properties PROP = BlockBehaviour.Properties.of(Material.STONE).lightLevel(getLightValueLit(9)).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
    public static final RegistryObject<Block> ZINC_ORE = register("zinc_ore", () -> new ROreBlock(PROP));
    public static final RegistryObject<Block> ZINC_ORE_DEEPSLATE = register("deepslate_zinc_ore", () -> new ROreBlock(PROP.strength(4.5F, 3F).sound(SoundType.DEEPSLATE).color(MaterialColor.DEEPSLATE)));

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
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
        return BLOCKS.register(id, blockSupplier);
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
