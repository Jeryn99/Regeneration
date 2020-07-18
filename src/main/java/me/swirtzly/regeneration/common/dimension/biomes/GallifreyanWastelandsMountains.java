package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.common.dimension.features.BiomeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.awt.*;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.GREY_LEAVES_COLOR;
import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.WASTELAND_WATER;

/**
 * Created by Swirtzly
 * on 05/05/2020 @ 14:56
 */
public class GallifreyanWastelandsMountains extends GallifrayanWastelands {

    public GallifreyanWastelandsMountains(){
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.SANDSTONE.getDefaultState()))).precipitation(RainType.RAIN).category(Category.PLAINS).downfall(0.3F).depth(3.6F).temperature(6F).waterColor(WASTELAND_WATER).waterFogColor(WASTELAND_WATER).scale(3.6F).parent(null));
        this.addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL));
        this.addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSedimentDisks(this);
        DefaultBiomeFeatures.addDefaultFlowers(this);
        DefaultBiomeFeatures.func_222348_W(this); //Grass
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addExtraEmeraldOre(this);
        DefaultBiomeFeatures.addInfestedStone(this);
        DefaultBiomeFeatures.addFreezeTopLayer(this);
        BiomeHelper.addGallifreyTress(this);
        BiomeHelper.addHut(this);

    }
    
    @Override
    public int getGrassColor(BlockPos pos) {
        return new Color(156 / 255F, 108 / 255F, 61 / 255F).getRGB();
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return GREY_LEAVES_COLOR;
    }

}
