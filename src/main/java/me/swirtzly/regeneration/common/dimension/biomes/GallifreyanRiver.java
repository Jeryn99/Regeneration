package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.RiverBiome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SeaGrassConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class GallifreyanRiver extends Biome {


    public GallifreyanRiver() {
            super((new Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG).precipitation(RainType.RAIN).category(Category.RIVER).depth(-0.5F).scale(0.0F).temperature(6f).downfall(0.5F).waterColor(0xEB623D).waterFogColor(0xEB623D).parent((String)null));
            DefaultBiomeFeatures.addCarvers(this);
            DefaultBiomeFeatures.addStructures(this);
            DefaultBiomeFeatures.addLakes(this);
            DefaultBiomeFeatures.addMonsterRooms(this);
            DefaultBiomeFeatures.addStoneVariants(this);
            DefaultBiomeFeatures.addOres(this);
            DefaultBiomeFeatures.addSedimentDisks(this);
            DefaultBiomeFeatures.func_222296_u(this);
            DefaultBiomeFeatures.addDefaultFlowers(this);
            DefaultBiomeFeatures.func_222348_W(this);
            DefaultBiomeFeatures.addMushrooms(this);
            DefaultBiomeFeatures.addReedsAndPumpkins(this);
            DefaultBiomeFeatures.addSprings(this);
            this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.SEAGRASS, new SeaGrassConfig(48, 0.4D), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NO_PLACEMENT_CONFIG));
            DefaultBiomeFeatures.addFreezeTopLayer(this);
    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return 0xE74C3C;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return 0xEAEDED;
    }
}
