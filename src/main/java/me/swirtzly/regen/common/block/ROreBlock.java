package me.swirtzly.regen.common.block;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

/* Created by Craig on 04/03/2021 */
public class ROreBlock extends OreBlock {
    public ROreBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected int getExperience(Random rand) {
        return MathHelper.nextInt(rand, 3, 7);
    }
}
