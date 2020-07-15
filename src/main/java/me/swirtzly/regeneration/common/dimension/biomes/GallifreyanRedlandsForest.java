package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.common.dimension.features.BiomeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.*;

public final class GallifreyanRedlandsForest extends Biome {

    public GallifreyanRedlandsForest() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG).precipitation(Biome.RainType.RAIN).category(Biome.Category.TAIGA).depth(0.2F).scale(0.2F).temperature(0.25F).downfall(0.8F).waterColor(RED_WATER_COLOR).waterFogColor(RED_WATER_COLOR).parent(null));
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSedimentDisks(this);
        DefaultBiomeFeatures.addDefaultFlowers(this);
        DefaultBiomeFeatures.func_222319_X(this); //Grass
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addBerryBushes(this);
        DefaultBiomeFeatures.addFreezeTopLayer(this);
        BiomeHelper.addGallifreyTress(this);
        BiomeHelper.addGallifreyOres(this);
        BiomeHelper.addHut(this);

    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return RED_GRASS_COLOR;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return GREY_LEAVES_COLOR;
    }
}