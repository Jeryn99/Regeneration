package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.block.ROreBlock;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
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
    public static final RegistryObject< Block > ZINC_ORE = BLOCKS.register("zinc_ore", () -> new ROreBlock(AbstractBlock.Properties.create(Material.ROCK).setLightLevel(getLightValueLit(9)).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));

    private static ToIntFunction< BlockState > getLightValueLit(int lightValue) {
        return (state) -> state.get(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static void genBlockItems(Block... blocks) {
        for (Block block : blocks) {
            BLOCK_ITEMS.register(block.getRegistryName().getPath(), () -> new BlockItem(block, new Item.Properties().group(MAIN)));
        }
    }

    @SubscribeEvent
    public static void regBlockItems(RegistryEvent.Register< Item > e) {
        genBlockItems(ZINC_ORE.get(), BIO_CONTAINER.get());
    }

}
