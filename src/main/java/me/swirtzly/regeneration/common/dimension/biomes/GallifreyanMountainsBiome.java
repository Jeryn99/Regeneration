package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 11:41
 */
public class GallifreyanMountainsBiome extends Biome {
    public GallifreyanMountainsBiome() {
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState()))).precipitation(RainType.RAIN).category(Category.PLAINS).downfall(0.3F).depth(0.6F).temperature(6F).waterColor(0xEB623D).waterFogColor(0xEB623D).scale(0.4F).parent(null));
        DefaultBiomeFeatures.func_222339_L(this);
    }

    public static void generateGallifreyTrees(World world, BlockPos pos, ResourceLocation location) {
        if (!world.isRemote) {
            Template treeTemp = ((ServerWorld) world).getStructureTemplateManager().getTemplate(location);
            BlockPos treePos = pos.add(-treeTemp.getSize().getX() / 2, 0, -treeTemp.getSize().getZ() / 2);
            //if (world.getBlockState(treePos).hasSolidSide(world, treePos, Direction.UP))
            treeTemp.addBlocksToWorld(world, treePos, new PlacementSettings());
        }
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
    public void decorate(GenerationStage.Decoration stage, ChunkGenerator<? extends GenerationSettings> chunkGenerator, IWorld worldIn, long seed, SharedSeedRandom random, BlockPos pos) {
        super.decorate(stage, chunkGenerator, worldIn, seed, random, pos);
        /*     int maxTrees = random.nextInt(3);
        for (int trees = 0; trees < maxTrees; ++trees) {
            BlockPos treePos = worldIn.getTopSolidOrLiquidBlock(pos.add(rand.nextInt(14) + 8, 0, rand.nextInt(14)+ 8));
            if (worldIn.getBlockState(treePos.down()).getBlock() == Blocks.GRASS) {
                generateGallifreyTrees(worldIn, worldIn.getTopSolidOrLiquidBlock(treePos), treeList[rand.nextInt(treeList.length)]);
            }
        }*/
    }


}
