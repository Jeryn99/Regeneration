package me.suff.mc.regen.level.structures;

import com.mojang.serialization.Codec;
import me.suff.mc.regen.common.entities.Timelord;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Random;
import java.util.function.Function;

public class RegenHuts extends StructureFeature<NoneFeatureConfiguration> {


    public RegenHuts(Codec<NoneFeatureConfiguration> p_72474_) {
        super(p_72474_, PieceGeneratorSupplier.simple(RegenHuts::checkLocation, RegenHuts::generatePieces));
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> p_197155_) {
        return p_197155_.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG);
    }

    private static void addPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation, StructurePiecesBuilder structurePieceAccessor, Random random, NoneFeatureConfiguration noneFeatureConfiguration) {
        ResourceLocation piece = GraveyardPiece.HUTS[random.nextInt(GraveyardPiece.HUTS.length)];
        structurePieceAccessor.addPiece(new GraveyardPiece(0, structureManager, piece, piece.toString(), GraveyardPiece.makeSettings(rotation), blockPos));
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> configurationContext) {
        int height = configurationContext.chunkGenerator().getFirstFreeHeight(configurationContext.chunkPos().getMinBlockX(), configurationContext.chunkPos().getMinBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, configurationContext.heightAccessor());
        BlockPos blockpos = new BlockPos(configurationContext.chunkPos().getMinBlockX(), height, configurationContext.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(configurationContext.random());
        addPiece(configurationContext.structureManager(), blockpos, rotation, structurePiecesBuilder, configurationContext.random(), configurationContext.config());
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static class GraveyardPiece extends TemplateStructurePiece {

        private static final ResourceLocation HUT = new ResourceLocation(RConstants.MODID, "gallifrey_shack");
        private static final ResourceLocation HUT_D = new ResourceLocation(RConstants.MODID, "gallifrey_barn_m");
        private static final ResourceLocation HUT_C = new ResourceLocation(RConstants.MODID, "gallifrey_barn_d");
        private static final ResourceLocation[] HUTS = new ResourceLocation[]{HUT, HUT_C, HUT_D};

        public GraveyardPiece(int p_163661_, StructureManager p_163662_, ResourceLocation p_163663_, String p_163664_, StructurePlaceSettings p_163665_, BlockPos p_163666_) {
            super(RPiece.TIMELORD_HUT, p_163661_, p_163662_, p_163663_, p_163664_, p_163665_, p_163666_);
        }

        public GraveyardPiece(CompoundTag p_192678_, StructureManager p_192679_, Function<ResourceLocation, StructurePlaceSettings> p_192680_) {
            super(RPiece.TIMELORD_HUT, p_192678_, p_192679_, p_192680_);
        }

        public GraveyardPiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
            super(RPiece.TIMELORD_HUT, tag, structurePieceSerializationContext.structureManager(), (p_162451_) -> makeSettings(Rotation.NONE));
        }


        private static StructurePlaceSettings makeSettings(Rotation rot) {
            return (new StructurePlaceSettings()).setRotation(rot).setMirror(Mirror.NONE).setRotationPivot(BlockPos.ZERO).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox p_73687_) {
            if ("timelord".equals(function)) {
                Timelord timelord = REntities.TIMELORD.get().create(worldIn.getLevel());
                RegenCap.get(timelord).ifPresent(iRegen -> {
                    timelord.initSkin(iRegen);
                    timelord.genName();
                    iRegen.setRegens(rand.nextInt(12));
                    timelord.moveTo(pos.getX(), pos.getY(), pos.getZ(), 90, 90);
                    worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    iRegen.syncToClients(null);
                    worldIn.addFreshEntity(timelord);
                });
            }

            if ("chest_stone".equals(function)) {
                RandomizableContainerBlockEntity.setLootTable(worldIn, rand, pos.above(), BuiltInLootTables.STRONGHOLD_LIBRARY);
                worldIn.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
            }
        }
    }


}