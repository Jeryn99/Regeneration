package me.swirtzly.regeneration.common.dimension;

import me.swirtzly.regeneration.common.dimension.util.GallifreyBiomeProviderNew;
import me.swirtzly.regeneration.common.dimension.util.GallifreyanSkyRenderer;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.BlockState;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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
    public ChunkGenerator<?> createRandomLevelGenerator() {
        OverworldGenSettings gensettings = new OverworldGenSettings();
        GallifreyBiomeProviderNew biomes = new GallifreyBiomeProviderNew(level, RegenObjects.GallifreyBiomes.getBiomes());
        return RegenObjects.ChunkGeneratorTypes.GALLIFREY_CHUNKS.get().create(this.level, biomes, gensettings);
    }

    @Override
    public void resetRainAndThunder() {
        level.getLevelData().setRainTime(0);
        level.getLevelData().setRaining(false);
        level.getLevelData().setThunderTime(0);
        level.getLevelData().setThundering(false);
    }

    @Override
    public void updateWeather(Runnable defaultLogic) {
        super.updateWeather(defaultLogic);
    }

    @Override
    public BlockPos getSpawnPosInChunk(ChunkPos chunkPosIn, boolean checkValid) {
        for (int i = chunkPosIn.getMinBlockX(); i <= chunkPosIn.getMaxBlockX(); ++i) {
            for (int j = chunkPosIn.getMinBlockZ(); j <= chunkPosIn.getMaxBlockZ(); ++j) {
                BlockPos blockpos = this.getValidSpawnPosition(i, j, checkValid);
                if (blockpos != null) {
                    return blockpos;
                }
            }
        }

        return null;
    }

    @Override
    public BlockPos getValidSpawnPosition(int posX, int posZ, boolean checkValid) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(posX, 0, posZ);
        Biome biome = this.level.getBiome(blockpos$mutableblockpos);
        BlockState blockstate = biome.getSurfaceBuilderConfig().getTopMaterial();
        if (checkValid && !blockstate.getBlock().is(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = this.level.getChunk(posX >> 4, posZ >> 4);
            int i = chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (i < 0) {
                return null;
            } else if (chunk.getHeight(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15) > chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, posX & 15, posZ & 15)) {
                return null;
            } else {
                for (int j = i + 1; j >= 0; --j) {
                    blockpos$mutableblockpos.set(posX, j, posZ);
                    BlockState blockstate1 = this.level.getBlockState(blockpos$mutableblockpos);
                    if (!blockstate1.getFluidState().isEmpty()) {
                        break;
                    }

                    if (blockstate1.equals(blockstate)) {
                        return blockpos$mutableblockpos.above().immutable();
                    }
                }

                return null;
            }
        }
    }

    @Override
    public float getTimeOfDay(long worldTime, float partialTicks) {
        double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

    @Override
    public boolean isNaturalDimension() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float f = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7F;
        float f2 = 0.4F;
        float f3 = 0.2F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(f1, f2, f3);
        // return new Vec3d(0.7, 0.4, 0.2);
    }

    @Override
    public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) {
        return SleepResult.ALLOW;
    }

    @Override
    public boolean mayRespawn() {
        return false;
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean hasGround() {
        return true;
    }


    @Override
    public Vec3d getSkyColor(BlockPos cameraPos, float partialTicks) {
        float f = level.getTimeOfDay(partialTicks);
        float f1 = MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int i = net.minecraftforge.client.ForgeHooksClient.getSkyBlendColour(level, cameraPos);
        float f3 = 0.7F;
        float f4 = 0.4F;
        float f5 = 0.2F;
        f3 = f3 * f1;
        f4 = f4 * f1;
        f5 = f5 * f1;
        float f6 = level.getRainLevel(partialTicks);
        if (f6 > 0.0F) {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f8 = 1.0F - f6 * 0.75F;
            f3 = f3 * f8 + f7 * (1.0F - f8);
            f4 = f4 * f8 + f7 * (1.0F - f8);
            f5 = f5 * f8 + f7 * (1.0F - f8);
        }

        float f10 = level.getThunderLevel(partialTicks);
        if (f10 > 0.0F) {
            float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f9 = 1.0F - f10 * 0.75F;
            f3 = f3 * f9 + f11 * (1.0F - f9);
            f4 = f4 * f9 + f11 * (1.0F - f9);
            f5 = f5 * f9 + f11 * (1.0F - f9);
        }

        if (level.getSkyFlashTime() > 0) {
            float f12 = (float) level.getSkyFlashTime() - partialTicks;
            if (f12 > 1.0F) {
                f12 = 1.0F;
            }

            f12 = f12 * 0.45F;
            f3 = f3 * (1.0F - f12) + 0.8F * f12;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 1.0F * f12;
        }

        return new Vec3d(f3, f4, f5);
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
        return 150F;
    }

    @Override
    public Vec3d getCloudColor(float partialTicks) {
        return new Vec3d(0.9, 0.3, 0.1);
    }

    @Override
    public boolean isHasSkyLight() {
        return true;
    }

    @Override
    public long getSeed() {
        return super.getSeed();
    }

    @Nullable
    @Override
    public MusicTicker.MusicType getMusicType() {
        return null;
    }
}
