package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.common.dimension.features.BiomeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;


/**
 * Created by Swirtzly
 * on 28/04/2020 @ 12:42
 */
public class GallifreyanRedLands extends Biome {

    public GallifreyanRedLands() {
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState()))).precipitation(RainType.RAIN).category(Category.PLAINS).downfall(0.3F).depth(0.0F).temperature(6).waterColor(0xEB623D).waterFogColor(0xEB623D).scale(0.0F).parent(null));
        DefaultBiomeFeatures.func_222339_L(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.func_222319_X(this);
        DefaultBiomeFeatures.addExtraDefaultFlowers(this);
        DefaultBiomeFeatures.addTaigaRocks(this);
        BiomeHelper.restoreVanillaStuff(this);
        BiomeHelper.addGallifreyTress(this);

    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return 0xE74C3C;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return 0xEAEDED;
    }

    @Override
    public <C extends IFeatureConfig> boolean hasStructure(Structure<C> structureIn) {
        return super.hasStructure(structureIn);
    }
}
