package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.common.block.JarBlock;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static me.swirtzly.regen.common.objects.RItems.MAIN;

@Mod.EventBusSubscriber(modid = RConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RConstants.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);

    public static final RegistryObject<Block> BIO_CONTAINER = BLOCKS.register("bio_container", JarBlock::new);


    private static void genBlockItems(Block... blocks) {
        for (Block block : blocks) {
            BLOCK_ITEMS.register(block.getRegistryName().getPath(), () -> new BlockItem(block, new Item.Properties().group(MAIN)));
        }
    }

    @SubscribeEvent
    public static void regBlockItems(RegistryEvent.Register<Item> e) {
        //TODO Disabled: genBlockItems(BIO_CONTAINER.get());
    }

}
