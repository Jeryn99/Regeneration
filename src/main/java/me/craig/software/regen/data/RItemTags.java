package me.craig.software.regen.data;

import me.craig.software.regen.common.objects.RItems;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.util.RegenUtil;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/* Created by Craig on 17/03/2021 */
public class RItemTags extends ItemTagsProvider {

    public RItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        add(RegenUtil.TIMELORD_CURRENCY, Items.GOLD_INGOT, Items.BONE, Items.EMERALD, RItems.ZINC.get(), Items.IRON_INGOT);
    }

    public void add(ITag.INamedTag<Item> branch, Item item) {
        this.tag(branch).add(item);
    }

    public void add(ITag.INamedTag<Item> branch, Item... item) {
        this.tag(branch).add(item);
    }
}