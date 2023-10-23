package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/* Created by Craig on 17/03/2021 */
public class RegenItemTags extends ItemTagsProvider {

    public RegenItemTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, lookupProvider, blockTagsProvider, RConstants.MODID, existingFileHelper);
    }

    public void add(TagKey<Item> branch, Item item) {
        this.tag(branch).add(item);
    }

    public void add(TagKey<Item> branch, Item... item) {
        this.tag(branch).add(item);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        add(RegenUtil.TIMELORD_CURRENCY, Items.GOLD_INGOT, Items.BONE, Items.EMERALD, RItems.ZINC.get(), Items.IRON_INGOT);
        add(RegenUtil.ZINC_INGOT, RItems.ZINC.get());
    }
}