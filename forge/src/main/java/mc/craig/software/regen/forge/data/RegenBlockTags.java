package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/* Created by Craig on 08/03/2021 */
public class RegenBlockTags extends BlockTagsProvider {

    public RegenBlockTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, RConstants.MODID, existingFileHelper);
    }

    public void add(TagKey<Block> branch, Block block) {
        this.tag(branch).add(block);
    }

    public void add(TagKey<Block> branch, Block... block) {
        this.tag(branch).add(block);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        add(Tags.Blocks.ORES, RBlocks.ZINC_ORE.get(), RBlocks.ZINC_ORE_DEEPSLATE.get());
        add(RegenUtil.ZINC, RBlocks.ZINC_ORE.get(), RBlocks.ZINC_ORE_DEEPSLATE.get());
        add(RegenUtil.BANNED_BLOCKS, RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.BIO_CONTAINER.get(), RBlocks.ZINC_ORE.get());
        add(RegenUtil.ARS, RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.BIO_CONTAINER.get());
        add(BlockTags.MINEABLE_WITH_PICKAXE, RBlocks.ZINC_ORE.get(), RBlocks.ZINC_ORE_DEEPSLATE.get(), RBlocks.BIO_CONTAINER.get(), RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.AZBANTIUM.get());
    }
}
