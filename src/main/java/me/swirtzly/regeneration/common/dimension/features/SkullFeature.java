package me.swirtzly.regeneration.common.dimension.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class SkullFeature extends Feature<ProbabilityConfig>{

	public SkullFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
			BlockPos pos, ProbabilityConfig config) {
		BlockPos skullPos = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);	
		if (worldIn.getBlockState(skullPos.down()).getFluidState().isSource()) {
        	return false;
        }
        else if (worldIn.getBlockState(skullPos).isAir(worldIn, skullPos) && worldIn.getBlockState(skullPos).isAir(worldIn, skullPos.up())){
        	System.out.println(worldIn.getRandom().nextInt());
        	if (worldIn.getRandom().nextInt() / 100 < 500 && worldIn.getRandom().nextInt()  > 0) {
	        	worldIn.setBlockState(skullPos, Blocks.SKELETON_SKULL.getDefaultState(), 7);
        	}
        }
		
		
		return true;
	}

}
