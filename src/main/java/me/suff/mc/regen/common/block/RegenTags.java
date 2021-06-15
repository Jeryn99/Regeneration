package me.suff.mc.regen.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class RegenTags {

    public static final Tag<Item> SHARP_ITEMS = makeItem("regeneration", "sharp_objects");
    public static final Tag<Block> TARDIS_ARS = makeBlock("tardis", "ars");

    public static Tag<Item> makeItem(String name) {
        return new ItemTags.Wrapper(new ResourceLocation(name));
    }

    public static Tag<Item> makeItem(String domain, String path) {
        return new ItemTags.Wrapper(new ResourceLocation(domain, path));
    }


    public static Tag<Block> makeBlock(String name) {
        return new BlockTags.Wrapper(new ResourceLocation(name));
    }

    public static Tag<Block> makeBlock(String domain, String path) {
        return new BlockTags.Wrapper(new ResourceLocation(domain, path));
    }
}
