package me.suff.mc.regen.common.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.Optional;
import java.util.stream.Stream;

/* Created by Craig on 09/03/2021 */
public class BlockShapes {

    static Optional< VoxelShape > JAR = Stream.of(
            Block.box(3.5, 3, 3.5, 12.5, 14, 12.5),
            Block.box(2.5, 0, 6, 3.5, 16, 10),
            Block.box(13.5, 0, 6.5, 14.5, 12, 9.5),
            Block.box(-0.5, 7, 7, 2.5, 14, 9),
            Block.box(12.5, 0, 6, 13.5, 16, 10),
            Block.box(3, 15, 3, 13, 16, 13),
            Block.box(12, 14, 3, 13, 15, 13),
            Block.box(3, 14, 12, 13, 15, 13),
            Block.box(3, 14, 3, 4, 15, 13),
            Block.box(3, 14, 3, 13, 15, 4),
            Block.box(3, 1.5, 3, 13, 3, 4),
            Block.box(12, 1.5, 3, 13, 3, 13),
            Block.box(3, 1.5, 3, 4, 3, 13),
            Block.box(3, 0, 3, 13, 1.5, 13),
            Block.box(3, 1.5, 12, 13, 3, 13)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR));

    static Optional< VoxelShape > JAR_OPEN = Stream.of(
            Block.box(12.503, 14.003, 12.503, 3.497, 2.997, 3.497),
            Block.box(3.5, 3, 3.5, 12.5, 14, 12.5),
            Block.box(2.5, 0, 6, 3.5, 16, 10),
            Block.box(12.5, 0, 6, 13.5, 16, 10),
            Block.box(13.5, 0, 6.5, 14.5, 12, 9.5),
            Block.box(-0.5, 7, 7, 2.5, 14, 9),
            Block.box(3, 1.5, 3, 13, 3, 4),
            Block.box(12, 1.5, 3, 13, 3, 13),
            Block.box(3, 1.5, 3, 4, 3, 13),
            Block.box(3, 0, 3, 13, 1.5, 13),
            Block.box(3, 1.5, 12, 13, 3, 13)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR));

}
