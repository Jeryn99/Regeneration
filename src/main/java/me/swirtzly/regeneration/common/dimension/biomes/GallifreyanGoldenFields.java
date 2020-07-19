package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.FoliageColors;

import java.awt.*;

public class GallifreyanGoldenFields extends GallifreyanRedLands {

    @Override
    public int getGrassColor(BlockPos pos) {
        Color color = new Color(255, 223, 0);
        return color.getRGB();
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        double d0 = MathHelper.clamp(this.func_225486_c(pos), 0.0F, 1.0F);
        double d1 = MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
        return FoliageColors.get(d0, d1);
    }


}
