package me.craig.software.regen.common.world.gen;


import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import me.craig.software.regen.common.objects.REntities;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

public class GallifreyanHuts extends Structure<ProbabilityConfig> {

    private static final List<MobSpawnInfo.Spawners> STRUCTURE_CREATURES = ImmutableList.of(
            new MobSpawnInfo.Spawners(REntities.TIMELORD.get(), 30, 10, 15),
            new MobSpawnInfo.Spawners(EntityType.CAT, 100, 1, 2)
    );

    public GallifreyanHuts(Codec<ProbabilityConfig> codec) {
        super(codec);
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_CREATURES;
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultCreatureSpawnList() {
        return STRUCTURE_CREATURES;
    }

    @Override
    public IStartFactory<ProbabilityConfig> getStartFactory() {
        return GallifreyanHuts.Start::new;
    }

    @Override
    public Decoration step() {
        return Decoration.SURFACE_STRUCTURES;
    }


    public static class Start extends StructureStart<ProbabilityConfig> {

        public Start(Structure<ProbabilityConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, ProbabilityConfig config) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);
            HutPieces.start(templateManagerIn, blockpos, rotation, this.pieces, this.random);
            this.calculateBoundingBox();
        }

    }

}