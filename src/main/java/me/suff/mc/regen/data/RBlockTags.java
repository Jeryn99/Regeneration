package me.suff.mc.regen.data;

import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/* Created by Craig on 08/03/2021 */
public class RBlockTags extends BlockTagsProvider {

    public RBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        add(RegenUtil.BANNED_BLOCKS, Blocks.MAGMA_BLOCK, Blocks.GLOWSTONE, Blocks.SEA_LANTERN);
    }

    public void add(ITag.INamedTag< Block > branch, Block block) {
        this.getOrCreateBuilder(branch).add(block);
    }

    public void add(ITag.INamedTag< Block > branch, Block... block) {
        this.getOrCreateBuilder(branch).add(block);
    }
}
