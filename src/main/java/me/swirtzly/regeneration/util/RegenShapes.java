package me.swirtzly.regeneration.util;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

/**
 * Created by Swirtzly
 * on 01/04/2020 @ 11:23
 */
public class RegenShapes {

    public static VoxelShape getJarShape() {
        VoxelShape shape = VoxelShapes.create(0.09375, 0.0, 0.25, 0.90625, 0.0546875, 0.75);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.247039375, 2.44375E-4, 0.02060125, 0.686736875, 0.054443125, 0.17685125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.313263125, 4.88125E-4, 0.02060125, 0.752960625, 0.0546875, 0.17685125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.247039375, 4.88125E-4, 0.82314875, 0.686736875, 0.0546875, 0.97939875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.313263125, 2.44375E-4, 0.82314875, 0.752960625, 0.054443125, 0.97939875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.09375, 0.8671875, 0.25, 0.90625, 0.921875, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.247039375, 0.867431875, 0.02060125, 0.686736875, 0.921386875, 0.17685125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.313263125, 0.867431875, 0.02060125, 0.752960625, 0.921630625, 0.17685125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.247039375, 0.867431875, 0.82314875, 0.686736875, 0.921630625, 0.97939875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.313263125, 0.867431875, 0.82314875, 0.752960625, 0.921386875, 0.97939875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.296875, 0.8984375, 0.34375, 0.703125, 0.9296875, 0.65625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.625, 0.9296875, 0.4375, 0.6875, 0.9453125, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.0859375, 0.0078125, 0.2421875, 0.1328125, 0.9140625, 0.7578125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.089820625, 0.00782375, 0.231981875, 0.133765625, 0.91407375, 0.282763125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.089820625, 0.00782375, 0.717236875, 0.133765625, 0.91407375, 0.768018125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.169168125, 0.0276275, 0.43683375, 0.247293125, 0.0901275, 0.56183375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(-0.068519375, 0.45736375, 0.4375, -0.006328125, 0.557983125, 0.562978125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.025085625, 0.71187125, 0.4375075, 0.103210625, 0.77437125, 0.5625075), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(-0.01036625, 0.349148125, 0.4375, 0.051645625, 0.567898125, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(-0.106038125, 0.428460625, 0.4375, -0.044026875, 0.647210625, 0.5625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.078125, 0.21875, 0.421875, 0.109375, 0.3125, 0.578125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.078125, 0.703125, 0.421875, 0.109375, 0.796875, 0.578125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.8671875, 0.0078125, 0.2421875, 0.9140625, 0.9140625, 0.7578125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.866234375, 0.00782375, 0.231981875, 0.910179375, 0.91407375, 0.282763125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.866234375, 0.00782375, 0.717236875, 0.910179375, 0.91407375, 0.768018125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.270894375, 0.0469875, 0.27365875, 0.708394375, 0.8751125, 0.27365875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.079260625, 0.04628, 0.2050025, 0.516760625, 0.874405, 0.2050025), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.270894375, 0.0469875, 0.72634125, 0.708394375, 0.8751125, 0.72634125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.079260625, 0.04628, 0.7949975, 0.516760625, 0.874405, 0.7949975), IBooleanFunction.OR);
        return shape;
    }

}
