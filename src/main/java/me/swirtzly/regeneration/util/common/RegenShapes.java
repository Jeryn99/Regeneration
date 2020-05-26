package me.swirtzly.regeneration.util.common;

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
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.09375, 0.8671875, 0.25, 0.90625, 0.921875, 0.75), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.296875, 0.8984375, 0.34375, 0.703125, 0.9296875, 0.65625), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.625, 0.9296875, 0.4375, 0.6875, 0.9453125, 0.5625), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.0859375, 0.0078125, 0.2421875, 0.1328125, 0.9140625, 0.7578125), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.025921875, 0.247774375, 0.43683375, 0.104046875, 0.310274375, 0.56183375), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(-0.046910625, 0.255534375, 0.436355625, 0.046839375, 0.755534375, 0.56183375), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.025085625, 0.71187125, 0.4375075, 0.103210625, 0.77437125, 0.5625075), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.078125, 0.21875, 0.421875, 0.109375, 0.3125, 0.578125), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.078125, 0.703125, 0.421875, 0.109375, 0.796875, 0.578125), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.8671875, 0.0078125, 0.2421875, 0.9140625, 0.9140625, 0.7578125), IBooleanFunction.OR);
    	shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.125, 0.0469875, 0.27365875, 0.875, 0.8751125, 0.74240875), IBooleanFunction.OR);
        return shape;
    }

}
