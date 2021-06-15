package me.swirtzly.regeneration.common.dimension.features;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class FeatureSpikeyBoys extends Feature<NoFeatureConfig> {

    public FeatureSpikeyBoys(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51493_1_) {
        super(p_i51493_1_);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        while (worldIn.isEmptyBlock(pos) && pos.getY() > 2) {
            pos = pos.below();
        }

        if (!worldIn.getBlockState(pos).getBlock().getRegistryName().toString().contains("sand")) {
            return false;
        } else {
            pos = pos.above(rand.nextInt(4));
            int i = rand.nextInt(4) + 7;
            int j = i / 4 + rand.nextInt(2);
            if (j > 1 && rand.nextInt(60) == 0) {
                pos = pos.above(10 + rand.nextInt(30));
            }

            for (int k = 0; k < i; ++k) {
                float f = (1.0F - (float) k / (float) i) * (float) j;
                int l = MathHelper.ceil(f);

                for (int i1 = -l; i1 <= l; ++i1) {
                    float f1 = (float) MathHelper.abs(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1) {
                        float f2 = (float) MathHelper.abs(j1) - 0.25F;
                        if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > f * f)) && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(rand.nextFloat() > 0.75F))) {
                            BlockState blockstate = worldIn.getBlockState(pos.offset(i1, k, j1));
                            Block block = blockstate.getBlock();
                            if (blockstate.isAir(worldIn, pos.offset(i1, k, j1)) || Block.equalsDirt(block) || block == Blocks.SNOW_BLOCK || block == Blocks.ICE || block.getRegistryName().toString().contains("sand")) {
                                this.setBlock(worldIn, pos.offset(i1, k, j1), Blocks.BLACK_CONCRETE.defaultBlockState());
                            }

                            if (k != 0 && l > 1) {
                                blockstate = worldIn.getBlockState(pos.offset(i1, -k, j1));
                                block = blockstate.getBlock();
                                if (blockstate.isAir(worldIn, pos.offset(i1, -k, j1)) || Block.equalsDirt(block) || block == Blocks.GRASS_BLOCK || block == Blocks.ICE || block.getRegistryName().toString().contains("sand")) {
                                    this.setBlock(worldIn, pos.offset(i1, -k, j1), Blocks.BLACK_CONCRETE.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }

            int k1 = j - 1;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 > 1) {
                k1 = 1;
            }

            for (int l1 = -k1; l1 <= k1; ++l1) {
                for (int i2 = -k1; i2 <= k1; ++i2) {
                    BlockPos blockpos = pos.offset(l1, -1, i2);
                    int j2 = 50;
                    if (Math.abs(l1) == 1 && Math.abs(i2) == 1) {
                        j2 = rand.nextInt(5);
                    }

                    while (blockpos.getY() > 50) {
                        BlockState blockstate1 = worldIn.getBlockState(blockpos);
                        Block block1 = blockstate1.getBlock();
                        if (!blockstate1.isAir(worldIn, blockpos) && !Block.equalsDirt(block1) && block1 != Blocks.SNOW_BLOCK && block1 != Blocks.ICE && block1 != Blocks.BLACK_CONCRETE) {
                            break;
                        }

                        this.setBlock(worldIn, blockpos, Blocks.BLACK_CONCRETE.defaultBlockState());
                        blockpos = blockpos.below();
                        --j2;
                        if (j2 <= 0) {
                            blockpos = blockpos.below(rand.nextInt(5) + 1);
                            j2 = rand.nextInt(5);
                        }
                    }
                }
            }

            return true;
        }
    }
}