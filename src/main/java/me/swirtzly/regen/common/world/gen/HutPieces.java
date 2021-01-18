package me.swirtzly.regen.common.world.gen;

import com.google.common.collect.ImmutableMap;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.util.RConstants;
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
import java.util.Map;
import java.util.Random;

public class HutPieces {

    private static final ResourceLocation HUT = new ResourceLocation(RConstants.MODID, "gallifrey_shack");

    private static final ResourceLocation[] ALL_STRUCTURES = new ResourceLocation[]{HUT};

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.of(HUT, BlockPos.ZERO);

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
        int x = pos.getX();
        int z = pos.getZ();
        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new HutPieces.Piece(templateManager, new ResourceLocation(RConstants.MODID, "gallifrey_shack"), blockpos, rotation));
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(RStructures.Structures.HUT_PIECE, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = HutPieces.OFFSET.get(resourceLocation);
            this.templatePosition = pos.add(blockpos.getX(), blockpos.getY(), blockpos.getZ());
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
            Template template = templateManager.getTemplateDefaulted(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {

            if ("timelord".equals(function)) {
                TimelordEntity timelordEntity = new TimelordEntity(worldIn.getWorld());
                IRegen cap = RegenCap.get(timelordEntity).orElseGet(null);
                timelordEntity.setTimelordType(TimelordEntity.TimelordType.COUNCIL);
                timelordEntity.initSkin(cap);
                cap.setRegens(rand.nextInt(12));
                timelordEntity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 90, 90);
                worldIn.addEntity(timelordEntity);
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }

            if ("chest_stone".equals(function)) {
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.up(), LootTables.CHESTS_STRONGHOLD_LIBRARY);
                worldIn.setBlockState(pos, Blocks.STONE.getDefaultState(), 2);
            }

        }
    }

}