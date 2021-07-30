package me.suff.mc.regen.common.world.gen;


import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.objects.REntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

public class GallifreyanHuts extends StructureFeature<ProbabilityFeatureConfiguration> {

    private static final List<MobSpawnSettings.SpawnerData> STRUCTURE_CREATURES = ImmutableList.of(
            new MobSpawnSettings.SpawnerData(REntities.TIMELORD.get(), 30, 10, 15),
            new MobSpawnSettings.SpawnerData(EntityType.CAT, 100, 1, 2)
    );

    public GallifreyanHuts(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
        return STRUCTURE_CREATURES;
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultCreatureSpawnList() {
        return STRUCTURE_CREATURES;
    }

    //Required, sets the Structure Start settings
    @Override
    public StructureStartFactory<ProbabilityFeatureConfiguration> getStartFactory() {
        return GallifreyanHuts.Start::new;
    }

    //Required, otherwise will cause NPE Crash
    @Override
    public Decoration step() {
        return Decoration.SURFACE_STRUCTURES;
    }


    public static class Start extends StructureStart<ProbabilityFeatureConfiguration> {

        public Start(StructureFeature<ProbabilityFeatureConfiguration> structureIn, int chunkX, int chunkZ, BoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, ProbabilityFeatureConfiguration config) {

            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);
            HutPieces.start(templateManagerIn, blockpos, rotation, this.pieces, this.random);
            this.calculateBoundingBox();
            Regeneration.LOG.info("Hut at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }

    }

}