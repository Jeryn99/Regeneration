package me.swirtzly.regeneration.common.dimension.features;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;
import java.util.function.Function;

public class SkullFeature extends Feature<ProbabilityConfig> {

    public SkullFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ProbabilityConfig config) {
        BlockPos skullPos = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, pos);
        if (worldIn.getBlockState(skullPos.below()).getFluidState().isSource()) {
            return false;
        } else if (worldIn.getBlockState(skullPos).isAir(worldIn, skullPos) && worldIn.getBlockState(skullPos).isAir(worldIn, skullPos.above())) {
            if (worldIn.getRandom().nextInt() / 100 < 500 && worldIn.getRandom().nextInt() > 0) {
                BlockState skullToPlace = Blocks.SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, worldIn.getRandom().nextInt(14));
                worldIn.setBlock(skullPos, skullToPlace, 7);
            }
        }
        return true;
    }

}
