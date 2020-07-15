package me.swirtzly.regeneration.common.dimension.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.IslandLayer;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.LongFunction;

public class GallifreyBiomeProviderNew extends BiomeProvider {
    private final Layer genBiomes;
    private final Layer biomeFactoryLayer;
    private final Biome[] biomes;

    public GallifreyBiomeProviderNew(World world, Biome[] dimensionBiomes) {
        Layer[] aLayer = makeTheWorld(world.getSeed());
        this.genBiomes = aLayer[0];
        this.biomeFactoryLayer = aLayer[1];
        this.biomes = dimensionBiomes;
        for (Biome biome : this.biomes) {
            Regeneration.LOG.debug(biome.getRegistryName());
            biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(new CaveWorldCarver(ProbabilityConfig::deserialize, 256) {
                {
                    carvableBlocks = ImmutableSet.of(Blocks.DIRT.getDefaultState().getBlock(),
                            biome.getSurfaceBuilder().getConfig().getTop().getBlock(),
                            biome.getSurfaceBuilder().getConfig().getUnder().getBlock());
                }
            }, new ProbabilityConfig(0.14285715f)));
        }
    }

    private Layer[] makeTheWorld(long seed) {
        LongFunction<IExtendedNoiseRandom<LazyArea>> contextFactory = l -> new LazyAreaLayerContext(25, seed, l);
        IAreaFactory<LazyArea> parentLayer = IslandLayer.INSTANCE.apply(contextFactory.apply(1));
        IAreaFactory<LazyArea> biomeLayer = (new BiomeLayerCustom(RegenObjects.Biomes.getBiomes())).apply(contextFactory.apply(200), parentLayer);
        biomeLayer = ZoomLayer.NORMAL.apply(contextFactory.apply(1000), biomeLayer);
        biomeLayer = ZoomLayer.NORMAL.apply(contextFactory.apply(1001), biomeLayer);
        biomeLayer = ZoomLayer.NORMAL.apply(contextFactory.apply(1002), biomeLayer);
        biomeLayer = ZoomLayer.NORMAL.apply(contextFactory.apply(1003), biomeLayer);
        biomeLayer = ZoomLayer.NORMAL.apply(contextFactory.apply(1004), biomeLayer);
        biomeLayer = ZoomLayer.NORMAL.apply(contextFactory.apply(1005), biomeLayer);
        IAreaFactory<LazyArea> voronoizoom = VoroniZoomLayer.INSTANCE.apply(contextFactory.apply(10), biomeLayer);
        return new Layer[]{new Layer(biomeLayer), new Layer(voronoizoom)};
    }

    @Override
    /**
     * Gets the biome from the provided coordinates
     */
    public Biome getBiome(int x, int y) {
        return this.biomeFactoryLayer.func_215738_a(x, y);
    }

    @Override
    public Biome func_222366_b(int p_222366_1_, int p_222366_2_) {
        return this.genBiomes.func_215738_a(p_222366_1_, p_222366_2_);
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        return this.biomeFactoryLayer.generateBiomes(x, z, width, length);
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        int i = centerX - sideLength >> 2;
        int j = centerZ - sideLength >> 2;
        int k = centerX + sideLength >> 2;
        int l = centerZ + sideLength >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, this.genBiomes.generateBiomes(i, j, i1, j1));
        return set;
    }

    @Override
    @Nullable
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Biome[] abiome = this.genBiomes.generateBiomes(i, j, i1, j1);
        BlockPos blockpos = null;
        int k1 = 0;
        for (int l1 = 0; l1 < i1 * j1; ++l1) {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            if (biomes.contains(abiome[l1])) {
                if (blockpos == null || random.nextInt(k1 + 1) == 0) {
                    blockpos = new BlockPos(i2, 0, j2);
                }
                ++k1;
            }
        }
        return blockpos;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn) {
        return this.hasStructureCache.computeIfAbsent(structureIn, (p_205006_1_) -> {
            for (Biome biome : this.biomes) {
                if (biome.hasStructure(p_205006_1_)) {
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        if (this.topBlocksCache.isEmpty()) {
            for (Biome biome : this.biomes) {
                this.topBlocksCache.add(biome.getSurfaceBuilderConfig().getTop());
            }
        }
        return this.topBlocksCache;
    }

    public static class BiomeLayerCustom implements IC0Transformer {

        private final Biome[] biomes;

        public BiomeLayerCustom(Biome[] biomes) {
            this.biomes = biomes;
        }

        @Override
        public int apply(INoiseRandom context, int value) {
            return Registry.BIOME.getId(biomes[context.random(biomes.length)]);
        }
    }
}



