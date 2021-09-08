package me.suff.mc.regen.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;
import java.util.stream.Stream;

/* Created by Craig on 09/03/2021 */
public class BlockShapes {

    public static VoxelShape makeClosedJar(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.09375, 0.1875, 0.8125, 0.1875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.09375, 0.1875, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.09375, 0.1875, 0.25, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.09375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.09375, 0.75, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.9375, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.875, 0.1875, 0.8125, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.75, 0.8125, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.25, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.8125, 0.9375, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.15625, 0, 0.375, 0.21875, 1, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.78125, 0, 0.375, 0.84375, 1, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.84375, 0, 0.40625, 0.90625, 0.75, 0.59375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.03125, 0.4375, 0.4375, 0.15625, 0.875, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0.1875, 0.21875, 0.78125, 0.875, 0.78125), BooleanOp.OR);

        return shape;
    }
    
    public static VoxelShape makeOpenJar(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.09375, 0.1875, 0.8125, 0.1875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.09375, 0.1875, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.09375, 0.1875, 0.25, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.09375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.09375, 0.75, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.9375, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.875, 0.1875, 0.8125, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.75, 0.8125, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.25, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.8125, 0.9375, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.15625, 0, 0.375, 0.21875, 1, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.78125, 0, 0.375, 0.84375, 1, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.84375, 0, 0.40625, 0.90625, 0.75, 0.59375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.03125, 0.4375, 0.4375, 0.15625, 0.875, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0.1875, 0.21875, 0.78125, 0.875, 0.78125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.7814375, 0.8751875, 0.7814375, 0.2185625, 0.1873125, 0.2185625), BooleanOp.OR);

        return shape;
    }

}
