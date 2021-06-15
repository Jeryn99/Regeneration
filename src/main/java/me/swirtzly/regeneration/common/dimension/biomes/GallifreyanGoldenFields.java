package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.FoliageColors;

public class GallifreyanGoldenFields extends GallifreyanRedLands {

    @Override
    public int getGrassColor(BlockPos pos) {
        return -8448;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        double d0 = MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
        double d1 = MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
        return FoliageColors.get(d0, d1);
    }


}
