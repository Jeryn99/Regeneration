package craig.software.mc.regen.data;

import craig.software.mc.regen.common.objects.RBlocks;
import craig.software.mc.regen.util.RConstants;
import craig.software.mc.regen.util.RegenUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

/* Created by Craig on 08/03/2021 */
public class RegenBlockTags extends BlockTagsProvider {

    public RegenBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        add(Tags.Blocks.ORES, RBlocks.ZINC_ORE.get(), RBlocks.ZINC_ORE_DEEPSLATE.get());
        add(RegenUtil.ZINC, RBlocks.ZINC_ORE.get(), RBlocks.ZINC_ORE_DEEPSLATE.get());
        add(RegenUtil.BANNED_BLOCKS, RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.BIO_CONTAINER.get(), RBlocks.ZINC_ORE.get());
        add(RegenUtil.ARS, RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.BIO_CONTAINER.get());
        add(BlockTags.MINEABLE_WITH_PICKAXE, RBlocks.ZINC_ORE.get(), RBlocks.BIO_CONTAINER.get(), RBlocks.ZERO_ROOM_FULL.get(), RBlocks.ZERO_ROUNDEL.get(), RBlocks.AZBANTIUM.get());
    }

    public void add(TagKey<Block> branch, Block block) {
        this.tag(branch).add(block);
    }

    public void add(TagKey<Block> branch, Block... block) {
        this.tag(branch).add(block);
    }
}
