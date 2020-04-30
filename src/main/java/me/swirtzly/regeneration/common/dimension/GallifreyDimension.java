package me.swirtzly.regeneration.common.dimension;

import me.swirtzly.regeneration.common.dimension.biomes.GalBiomeProvider;
import me.swirtzly.regeneration.common.dimension.util.GallifreyanSkyRenderer;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IRenderHandler;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 10:41
 */
public class GallifreyDimension extends Dimension {

    public GallifreyDimension(World worldIn, DimensionType typeIn) {
        super(worldIn, typeIn);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        OverworldGenSettings gensettings = new OverworldGenSettings();
        GalBiomeProvider biomes = new GalBiomeProvider(BiomeProviderType.VANILLA_LAYERED.createSettings().setWorldInfo(this.world.getWorldInfo()).setGeneratorSettings(gensettings));
        return RegenObjects.ChunkGeneratorTypes.GALLIFREY_CHUNKS.get().create(this.world, biomes, gensettings);
    }

    @Override
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
        for (int i = chunkPosIn.getXStart(); i <= chunkPosIn.getXEnd(); ++i) {
            for (int j = chunkPosIn.getZStart(); j <= chunkPosIn.getZEnd(); ++j) {
                BlockPos blockpos = this.findSpawn(i, j, checkValid);
                if (blockpos != null) {
                    return blockpos;
                }
            }
        }

        return null;
    }

    @Override
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(posX, 0, posZ);
        Biome biome = this.world.getBiome(blockpos$mutableblockpos);
        BlockState blockstate = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
            int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (i < 0) {
                return null;
            } else if (chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15) > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, posX & 15, posZ & 15)) {
                return null;
            } else {
                for (int j = i + 1; j >= 0; --j) {
                    blockpos$mutableblockpos.setPos(posX, j, posZ);
                    BlockState blockstate1 = this.world.getBlockState(blockpos$mutableblockpos);
                    if (!blockstate1.getFluidState().isEmpty()) {
                        break;
                    }

                    if (blockstate1.equals(blockstate)) {
                        return blockpos$mutableblockpos.up().toImmutable();
                    }
                }

                return null;
            }
        }
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return new Vec3d(0.7, 0.4, 0.2);
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isSkyColored() {
        return true;
    }


    @Override
    public Vec3d getSkyColor(BlockPos cameraPos, float partialTicks) {

        if (world.isDaytime()) {
            return new Vec3d(0.7, 0.4, 0.2);
        }

        float f = world.getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int i = net.minecraftforge.client.ForgeHooksClient.getSkyBlendColour(world, cameraPos);
        float f3 = (float) (i >> 16 & 255) / 255.0F;
        float f4 = (float) (i >> 8 & 255) / 255.0F;
        float f5 = (float) (i & 255) / 255.0F;
        f3 = f3 * f1;
        f4 = f4 * f1;
        f5 = f5 * f1;
        float f6 = world.getRainStrength(partialTicks);
        if (f6 > 0.0F) {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f8 = 1.0F - f6 * 0.75F;
            f3 = f3 * f8 + f7 * (1.0F - f8);
            f4 = f4 * f8 + f7 * (1.0F - f8);
            f5 = f5 * f8 + f7 * (1.0F - f8);
        }

        float f10 = world.getThunderStrength(partialTicks);
        if (f10 > 0.0F) {
            float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f9 = 1.0F - f10 * 0.75F;
            f3 = f3 * f9 + f11 * (1.0F - f9);
            f4 = f4 * f9 + f11 * (1.0F - f9);
            f5 = f5 * f9 + f11 * (1.0F - f9);
        }

        if (world.getLastLightningBolt() > 0) {
            float f12 = (float) world.getLastLightningBolt() - partialTicks;
            if (f12 > 1.0F) {
                f12 = 1.0F;
            }

            f12 = f12 * 0.45F;
            f3 = f3 * (1.0F - f12) + 0.8F * f12;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 1.0F * f12;
        }

        return new Vec3d((double) f3, (double) f4, (double) f5);
    }

    @Nullable
    @Override
    public IRenderHandler getSkyRenderer() {
        return GallifreyanSkyRenderer.getInstance();
    }

    @Nullable
    @Override
    public IRenderHandler getCloudRenderer() {
        return null;
    }

    @Override
    public float getCloudHeight() {
        return 128F;
    }

    @Override
    public Vec3d getCloudColor(float partialTicks) {
        return super.getCloudColor(partialTicks);
    }
}
