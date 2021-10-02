package me.suff.mc.regen.data;

import me.suff.mc.regen.common.block.RegenTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsTagCreation extends ItemTagsProvider {
    public ItemsTagCreation(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void addTags() {
        for (Item value : ForgeRegistries.ITEMS.getValues()) {
            if (value instanceof ToolItem || value instanceof SwordItem || value instanceof ShearsItem) {
                add(RegenTags.SHARP_ITEMS, value);
            }
        }
    }

    public void add(Tag<Item> branch, Item block) {
        this.tag(branch).add(block);
    }
}