package me.swirtzly.regeneration.common.dimension.features;

import com.mojang.datafixers.Dynamic;
import me.swirtzly.regeneration.Regeneration;
import net.minecraft.block.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;

public class GallifreyanHuts extends Feature<NoFeatureConfig> {

    public GallifreyanHuts(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld iworld, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        int ci = pos.getX();
        int ck = pos.getZ();
        int count = random.nextInt(1) + 1;
        if (Math.random() < 0.2) {
            for (int a = 0; a < count; a++) {
                int i = ci + random.nextInt(16) + 8;
                int k = ck + random.nextInt(16) + 8;
                int j = iworld.getHeight(Heightmap.Type.WORLD_SURFACE_WG, i, k);
                j -= 1;
                Template template = ((ServerWorld) iworld.getWorld()).getSaveHandler().getStructureTemplateManager().getTemplateDefaulted(new ResourceLocation(Regeneration.MODID, "regeneration/structures/gallifrey_shack"));
                if (template == null)
                    return false;
                Rotation rotation = Rotation.values()[random.nextInt(3)];
                Mirror mirror = Mirror.values()[random.nextInt(2)];
                BlockPos spawnTo = new BlockPos(i, j, k).up();
                if (iworld.getBlockState(pos).getBlock() == Blocks.WATER) {
                    return false;
                }
                template.addBlocksToWorldChunk(iworld, spawnTo, new PlacementSettings().setRotation(rotation).setRandom(random).setMirror(mirror).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK).setChunk(null).setIgnoreEntities(false));

            }
        }
        return true;
    }

}
