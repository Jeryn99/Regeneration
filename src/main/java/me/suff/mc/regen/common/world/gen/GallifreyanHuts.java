package me.suff.mc.regen.common.world.gen;


import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.objects.REntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;

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


        public Start(StructureFeature<ProbabilityFeatureConfiguration> p_163595_, ChunkPos p_163596_, int p_163597_, long p_163598_) {
            super(p_163595_, p_163596_, p_163597_, p_163598_);
        }

        @Override
        public void generatePieces(RegistryAccess p_163615_, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos p_163618_, Biome p_163619_, ProbabilityFeatureConfiguration p_163620_, LevelHeightAccessor p_163621_) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            int x = (getChunkPos().x << 4) + 7;
            int z = (getChunkPos().z << 4) + 7;
          //TODO Height  int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, 64, z);

            HutPieces.start(structureManager, blockpos, rotation, this.pieces, this.random);
            this.createBoundingBox();
            Regeneration.LOG.info("Hut at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }
    }

}