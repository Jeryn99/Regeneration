package me.suff.mc.regen.data;

import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class RBiomes extends BiomeTagsProvider {
    public RBiomes(DataGenerator p_211094_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_211094_, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(RegenUtil.TIMELORD_SETTLEMENT).addTags(BiomeTags.IS_FOREST, BiomeTags.HAS_WOODLAND_MANSION, BiomeTags.IS_TAIGA, BiomeTags.IS_HILL);
    }
}
