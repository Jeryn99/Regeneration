package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.*;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 11:41
 */
public class GallifreyanMountainsBiome extends Biome {

    public GallifreyanMountainsBiome() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.MOUNTAIN, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG).precipitation(Biome.RainType.RAIN).category(Biome.Category.EXTREME_HILLS).depth(1.0F).scale(0.5F).temperature(0.2F).downfall(0.3F).waterColor(RED_WATER_COLOR).waterFogColor(RED_WATER_COLOR).parent(null));
        this.addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL));
        this.addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSedimentDisks(this);
        DefaultBiomeFeatures.func_222343_A(this);
        DefaultBiomeFeatures.addDefaultFlowers(this);
        DefaultBiomeFeatures.func_222348_W(this);
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addExtraEmeraldOre(this);
        DefaultBiomeFeatures.addInfestedStone(this);
        DefaultBiomeFeatures.addFreezeTopLayer(this);
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
