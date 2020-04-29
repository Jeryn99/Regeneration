package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.dimension.biomes.features.GallifreyanBiomeFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.extensions.IForgeWorldServer;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 12:45
 */
public class GallifrayanWastelands extends Biome {

    protected static final BlockState GRASS = Blocks.TALL_GRASS.getDefaultState();
    protected static final BlockState SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    protected static final BlockState DIRT = Blocks.DIRT.getDefaultState();

    private ServerWorld world;

    private static final ResourceLocation[] treeList = {
            new ResourceLocation(RegenerationMod.MODID, "gallifrey/trees/gal_tree_test")

    };

    public GallifrayanWastelands() {
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.SAND.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState()))).precipitation(RainType.RAIN).category(Category.PLAINS).downfall(0.3F).depth(0.6F).temperature(6F).waterColor(0xEB623D).waterFogColor(0xEB623D).scale(0.5F).parent(null));
        DefaultBiomeFeatures.addSwampClayDisks(this);
        DefaultBiomeFeatures.addSwampVegetation(this);
      //  GallifreyanBiomeFeatures.addWoodlandTrees(this);

    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return 0xAFA469;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return 0xEAEDED;
    }

    @Override
    public void decorate(GenerationStage.Decoration stage, ChunkGenerator<? extends GenerationSettings> chunkGenerator, IWorld worldIn, long seed, SharedSeedRandom random, BlockPos pos) {
        super.decorate(stage, chunkGenerator, worldIn, seed, random, pos);

        int maxSandStone = 100;
        for (int sandstone = 0; sandstone < maxSandStone; ++sandstone) {
            BlockPos sstonePos = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (pos.add(random.nextInt(16), 0, random.nextInt(16))));
            if (worldIn.getBlockState(sstonePos.down()).getBlock() == Blocks.SAND) {
                worldIn.setBlockState(sstonePos.down(), SANDSTONE, 7);


            }
        }

        int maxDirt = 25;
        for (int dirt = 0; dirt < maxDirt; ++dirt) {
            BlockPos dirtPos = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (pos.add(random.nextInt(16), 0, random.nextInt(16))));
            if (worldIn.getBlockState(dirtPos.down()).getBlock() == Blocks.SAND) {
                worldIn.setBlockState(dirtPos.down(), DIRT, 7);
            }
        }

        int maxGrass = 16;
        for (int grass = 0; grass < maxGrass; ++grass) {
            BlockPos grassPos = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (pos.add(random.nextInt(14), 0, random.nextInt(14))));
            if (worldIn.getBlockState(grassPos.down()).getBlock() == Blocks.DIRT) {
                worldIn.setBlockState(grassPos, GRASS, 7);
            }
        }


        int maxSkull = 1;
        for (int skull = 0; skull < maxSkull; ++skull) {

            int percentageSpawn = random.nextInt(100);

            if (percentageSpawn == 1) {

                BlockPos skullPos = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (pos.add(random.nextInt(16), 0, random.nextInt(16))));
                worldIn.setBlockState(skullPos, Blocks.SKELETON_SKULL.getDefaultState(), 7);
            }
        }


    }
}
