package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.*;

public class GallifreyanSnowFields extends Biome {

    public GallifreyanSnowFields() {
        super((new Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.CONFIG_GRASS).precipitation(RainType.SNOW).biomeCategory(Category.TAIGA).depth(0.2F).scale(0.2F).temperature(-0.5F).downfall(0.4F).waterColor(RED_WATER_COLOR).waterFogColor(RED_WATER_COLOR).parent((String) null));
        DefaultBiomeFeatures.addDefaultCarvers(this);
        DefaultBiomeFeatures.addStructureFeaturePlacement(this);
        DefaultBiomeFeatures.addDefaultLakes(this);
        DefaultBiomeFeatures.addDefaultMonsterRoom(this);
        DefaultBiomeFeatures.addFerns(this);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(this);
        DefaultBiomeFeatures.addDefaultOres(this);
        DefaultBiomeFeatures.addDefaultSoftDisks(this);
        DefaultBiomeFeatures.addTaigaTrees(this);
        DefaultBiomeFeatures.addDefaultFlowers(this);
        DefaultBiomeFeatures.addTaigaGrass(this);
        DefaultBiomeFeatures.addDefaultMushrooms(this);
        DefaultBiomeFeatures.addDefaultExtraVegetation(this);
        DefaultBiomeFeatures.addDefaultSprings(this);
        DefaultBiomeFeatures.addBerryBushes(this);
        DefaultBiomeFeatures.addSurfaceFreezing(this);
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
