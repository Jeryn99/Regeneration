package me.suff.mc.regen.common.world.gen;

import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class HutPieces {

    private static final ResourceLocation HUT = new ResourceLocation(RConstants.MODID, "gallifrey_shack");
    private static final ResourceLocation HUT_1 = new ResourceLocation(RConstants.MODID, "gallifrey_barn_d");
    private static final ResourceLocation HUT_2 = new ResourceLocation(RConstants.MODID, "gallifrey_barn_m");

    private static final ResourceLocation[] ALL_STRUCTURES = new ResourceLocation[]{HUT, HUT_1, HUT_2};

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
        int x = pos.getX();
        int z = pos.getZ();
        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new HutPieces.Piece(templateManager, ALL_STRUCTURES[random.nextInt(ALL_STRUCTURES.length)], blockpos, rotation));
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(RStructures.Structures.HUT_PIECE, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = BlockPos.ZERO;
            this.templatePosition = pos.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound) {
            super(RStructures.Structures.HUT_PIECE, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        private void setupPiece(TemplateManager templateManager) {
            Template template = templateManager.getOrCreate(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void addAdditionalSaveData(CompoundNBT tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {

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
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.above(), LootTables.STRONGHOLD_LIBRARY);
                worldIn.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
            }

            if ("barrel_down".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(), LootTables.STRONGHOLD_LIBRARY);
                worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }

        }
    }

}