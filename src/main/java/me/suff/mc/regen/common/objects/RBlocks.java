package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.block.ROreBlock;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

import static me.suff.mc.regen.common.objects.RItems.MAIN;

@Mod.EventBusSubscriber(modid = RConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBlocks {

    public static final DeferredRegister< Block > BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RConstants.MODID);
    public static final DeferredRegister< Item > BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);

    public static final RegistryObject< Block > BIO_CONTAINER = BLOCKS.register("bio_container", JarBlock::new);
    public static final RegistryObject< Block > ZINC_ORE = BLOCKS.register("zinc_ore", () -> new ROreBlock(AbstractBlock.Properties.of(Material.STONE).lightLevel(getLightValueLit(9)).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject< Block > ZERO_ROUNDEL = BLOCKS.register("zero_roundel_half", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject< Block > ZERO_ROOM_FULL = BLOCKS.register("zero_roundel_full", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject< Block > AZBANTIUM = BLOCKS.register("azbantium", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    private static ToIntFunction< BlockState > getLightValueLit(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static void genBlockItems(Block... blocks) {
        for (Block block : blocks) {
            BLOCK_ITEMS.register(block.getRegistryName().getPath(), () -> new BlockItem(block, new Item.Properties().tab(MAIN)));
        }
    }

    @SubscribeEvent
    public static void regBlockItems(RegistryEvent.Register< Item > e) {
        genBlockItems(ZINC_ORE.get(), BIO_CONTAINER.get(), ZERO_ROOM_FULL.get(), ZERO_ROUNDEL.get(), AZBANTIUM.get());
    }

}
