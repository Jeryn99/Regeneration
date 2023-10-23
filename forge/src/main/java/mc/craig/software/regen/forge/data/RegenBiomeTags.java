package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RegenBiomeTags extends BiomeTagsProvider {
    public RegenBiomeTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(RegenUtil.IS_OVERWORLD).addTags(BiomeTags.IS_OVERWORLD, Tags.Biomes.IS_UNDERGROUND);
        this.tag(RegenUtil.TIMELORD_SETTLEMENT).addTags(BiomeTags.IS_FOREST, BiomeTags.HAS_WOODLAND_MANSION, BiomeTags.IS_TAIGA, BiomeTags.IS_HILL);
    }
}
