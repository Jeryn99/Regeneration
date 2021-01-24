package me.swirtzly.data;

import me.swirtzly.regeneration.common.block.RegenTags;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;

public class BlockTagCreation extends BlockTagsProvider {
    public BlockTagCreation(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        add(RegenTags.TARDIS_ARS, RegenObjects.Blocks.HAND_JAR.get());
        add(RegenTags.TARDIS_ARS, RegenObjects.Blocks.ZERO_ROOM_TWO.get());
        add(RegenTags.TARDIS_ARS, RegenObjects.Blocks.ZERO_ROOM.get());
    }

    public void add(Tag< Block > branch, Block block) {
        this.getBuilder(branch).add(block);
    }
}
