package me.suff.mc.regen.common.dimension;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

import java.util.function.BiFunction;

public class DimSingle extends ModDimension {
    DimSingle.DimFactory<?> fact;

    public DimSingle(DimSingle.DimFactory<?> fact) {
        this.fact = fact;
    }

    public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
        return (t, u) -> {
            return this.fact.create(t, u);
        };
    }

    public interface DimFactory<T extends Dimension> {
        T create(World var1, DimensionType var2);
    }
}
