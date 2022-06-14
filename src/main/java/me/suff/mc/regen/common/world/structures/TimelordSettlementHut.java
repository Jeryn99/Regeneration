package me.suff.mc.regen.common.world.structures;

import com.mojang.serialization.Codec;
import me.suff.mc.regen.common.entities.Timelord;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.world.structures.pieces.RPieces;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public class TimelordSettlementHut extends Structure {


    public static final Codec<TimelordSettlementHut> CODEC = simpleCodec(TimelordSettlementHut::new);

    public TimelordSettlementHut(StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    public @NotNull Optional<GenerationStub> findGenerationPoint(Structure.@NotNull GenerationContext p_227595_) {
        return onTopOfChunkCenter(p_227595_, Heightmap.Types.WORLD_SURFACE_WG, (p_227598_) -> {
            this.generatePieces(p_227598_, p_227595_);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
        ChunkPos chunkpos = generationContext.chunkPos();
        WorldgenRandom worldgenrandom = generationContext.random();
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 90, chunkpos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        TimelordSettlementHut.addPiece(generationContext.structureTemplateManager(), blockpos, rotation, structurePiecesBuilder, worldgenrandom);
    }

    private static void addPiece(StructureTemplateManager structureManager, BlockPos blockPos, Rotation rotation, StructurePiecesBuilder structurePieceAccessor, WorldgenRandom random) {
        ResourceLocation piece = HutPiece.HUTS[random.nextInt(HutPiece.HUTS.length)];
        structurePieceAccessor.addPiece(new HutPiece(0, structureManager, piece, piece.toString(), HutPiece.makeSettings(rotation), blockPos));
    }


    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public @NotNull StructureType<?> type() {
        return StructureType.BURIED_TREASURE;
    }

    public static class HutPiece extends TemplateStructurePiece {
        private static final ResourceLocation HUT = new ResourceLocation(RConstants.MODID, "gallifrey_shack");
        private static final ResourceLocation HUT_D = new ResourceLocation(RConstants.MODID, "gallifrey_barn_m");
        private static final ResourceLocation HUT_C = new ResourceLocation(RConstants.MODID, "gallifrey_barn_d");
        private static final ResourceLocation[] HUTS = new ResourceLocation[]{HUT, HUT_C, HUT_D};

        public HutPiece(int p_163661_, StructureTemplateManager p_163662_, ResourceLocation p_163663_, String p_163664_, StructurePlaceSettings p_163665_, BlockPos p_163666_) {
            super(RPieces.TIMELORD_HUT, p_163661_, p_163662_, p_163663_, p_163664_, p_163665_, p_163666_);
        }

        public HutPiece(CompoundTag p_192678_, StructureTemplateManager p_192679_, Function<ResourceLocation, StructurePlaceSettings> p_192680_) {
            super(RPieces.TIMELORD_HUT, p_192678_, p_192679_, p_192680_);
        }

        public HutPiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
            super(RPieces.TIMELORD_HUT, tag, structurePieceSerializationContext.structureTemplateManager(), (p_162451_) -> makeSettings(Rotation.NONE));
        }


        private static StructurePlaceSettings makeSettings(Rotation rot) {
            return (new StructurePlaceSettings()).setRotation(rot).setMirror(Mirror.NONE).setRotationPivot(BlockPos.ZERO).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }


        @Override
        protected void handleDataMarker(@NotNull String function, @NotNull BlockPos pos, @NotNull ServerLevelAccessor worldIn, @NotNull RandomSource rand, @NotNull BoundingBox p_73687_) {
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