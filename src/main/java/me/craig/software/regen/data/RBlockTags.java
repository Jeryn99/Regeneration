package me.craig.software.regen.data;

import me.craig.software.regen.common.objects.RBlocks;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.util.RegenUtil;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

/* Created by Craig on 08/03/2021 */
public class RBlockTags extends BlockTagsProvider {

    public RBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        add(Tags.Blocks.ORES, RBlocks.ZINC_ORE.get());
        add(RegenUtil.FORGE_ZINC, RBlocks.ZINC_ORE.get());
        add(RegenUtil.BANNED_BLOCKS, RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.BIO_CONTAINER.get(), RBlocks.ZINC_ORE.get());
        add(RegenUtil.ARS, RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.BIO_CONTAINER.get());
    }

    public void add(ITag.INamedTag<Block> branch, Block block) {
        this.tag(branch).add(block);
    }

    public void add(ITag.INamedTag<Block> branch, Block... block) {
        this.tag(branch).add(block);
    }
}
