package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.Layer;

import java.util.IdentityHashMap;
import java.util.Map;

public class GenLayerBiomeMapping extends Layer {
    private Map<Biome, Biome> biomeMap = new IdentityHashMap<>();

    private Biome defaultBiome;

    private Layer parent;

    public GenLayerBiomeMapping(Biome defaultBiome, Layer parent) {
        super(() -> null);
        this.defaultBiome = defaultBiome;
        this.parent = parent;
    }

    public Biome[] generateBiomes(int startX, int startZ, int xSize, int zSize) {
        Biome[] base = this.parent.generateBiomes(startX, startZ, xSize, zSize);
        for (int i = 0; i < base.length; i++)
            base[i] = this.biomeMap.getOrDefault(base[i], this.defaultBiome);
        return base;
    }

    public void addBiomeOverride(Biome oldB, Biome newB) {
        this.biomeMap.put(oldB, newB);
    }

    public Biome func_215738_a(int p_215738_1_, int p_215738_2_) {
        Biome def = this.parent.func_215738_a(p_215738_1_, p_215738_2_);
        return this.biomeMap.getOrDefault(def, this.defaultBiome);
    }
}
