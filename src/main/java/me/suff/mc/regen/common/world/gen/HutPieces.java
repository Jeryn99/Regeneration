package me.suff.mc.regen.common.world.gen;

import com.google.common.collect.ImmutableMap;
import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class HutPieces {

    private static final ResourceLocation HUT = new ResourceLocation(RConstants.MODID, "gallifrey_shack");

    private static final ResourceLocation[] ALL_STRUCTURES = new ResourceLocation[]{HUT};

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.of(HUT, BlockPos.ZERO);

    public static void start(StructureManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
        int x = pos.getX();
        int z = pos.getZ();
        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new HutPieces.Piece(templateManager, new ResourceLocation(RConstants.MODID, "gallifrey_shack"), blockpos, rotation, 64));
    }

    public static class Piece extends TemplateStructurePiece {

        public Piece(StructureManager p_71244_, ResourceLocation p_71245_, BlockPos p_71246_, Rotation p_71247_, int p_71248_) {
            super(RStructures.Structures.HUT_PIECE, 0, p_71244_, p_71245_, p_71245_.toString(), makeSettings(p_71247_, p_71245_), makePosition(p_71245_, p_71246_, p_71248_));
        }

        private static BlockPos makePosition(ResourceLocation p_71245_, BlockPos p_71246_, int p_71248_) {
            return p_71246_;
        }

        public Piece(ServerLevel p_162441_, CompoundTag p_162442_) {
            super(RStructures.Structures.HUT_PIECE, p_162442_, p_162441_, (p_162451_) -> makeSettings(Rotation.valueOf(p_162442_.getString("Rot")), p_162451_));

        }

        private static StructurePlaceSettings makeSettings(Rotation p_162447_, ResourceLocation p_162448_) {
            return (new StructurePlaceSettings()).setRotation(p_162447_).setMirror(Mirror.NONE).setRotationPivot(BlockPos.ZERO).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }


        @Override
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {

            if ("timelord".equals(function)) {
                TimelordEntity timelordEntity = REntities.TIMELORD.get().create(worldIn.getLevel());
                RegenCap.get(timelordEntity).ifPresent(iRegen -> {
                    timelordEntity.initSkin(iRegen);
                    timelordEntity.genName();
                    iRegen.setRegens(rand.nextInt(12));
                    timelordEntity.moveTo(pos.getX(), pos.getY(), pos.getZ(), 90, 90);
                    worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    iRegen.syncToClients(null);
                    worldIn.addFreshEntity(timelordEntity);
                });
            }

            if ("chest_stone".equals(function)) {
                RandomizableContainerBlockEntity.setLootTable(worldIn, rand, pos.above(), BuiltInLootTables.STRONGHOLD_LIBRARY);
                worldIn.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
            }

        }
    }

}