package me.swirtzly.regeneration.common.tiles;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

/**
 * Created by Swirtzly
 * on 16/09/2019 @ 15:09
 */
public class RegenShapes {

    public static VoxelShape HAND_JAR = createJarShape();


    private static VoxelShape createJarShape() {
        VoxelShape shape = VoxelShapes.create(0.125, 0.0625, 0.25, 0.234375, 0.84375, 0.75);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.765625, 0.0625, 0.25, 0.875, 0.84375, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.875, 0.109375, 0.4375, 1.03125, 0.609375, 0.59375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.6659975, 0.08270125, 0.4375, 0.8222475, 0.30145125, 0.59375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.18058125, 0.304145, 0.46875, 0.30558125, 0.335395, 0.53125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.078125, 0.75, 0.4765625, 0.140625, 0.78125, 0.5234375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.230795625, 0.431589375, 0.4765625, 0.262045625, 0.564401875, 0.5234375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.0273, 0.5335475, 0.46875, 0.05855, 0.6585475, 0.53125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.125, 0.0, 0.25, 0.875, 0.0625, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.26863625, 0.0, 0.09710875, 0.67488625, 0.0625, 0.15960875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3084625, 0.0, 0.180820625, 0.7147125, 0.0625, 0.243320625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25423375, 0.0, 0.787798125, 0.66048375, 0.0625, 0.850298125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3371375, 0.0, 0.775839375, 0.7433875, 0.0625, 0.838339375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25, 0.0, 0.203125, 0.75, 0.0625, 0.796875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.40625, 0.0, 0.15625, 0.59375, 0.0625, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.125, 0.84375, 0.25, 0.875, 0.90625, 0.75), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.40625, 0.84375, 0.15625, 0.59375, 0.90625, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25, 0.84375, 0.203125, 0.75, 0.90625, 0.796875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3371375, 0.84375, 0.775839375, 0.7433875, 0.90625, 0.838339375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.25423375, 0.84375, 0.787798125, 0.66048375, 0.90625, 0.850298125), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.3084625, 0.84375, 0.180820625, 0.7147125, 0.90625, 0.243320625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.26863625, 0.84375, 0.09710875, 0.67488625, 0.90625, 0.15960875), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.15625, 0.56837625, 0.84375, 0.15625), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);
        shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.create(0.43162375, 0.0625, 0.84375, 0.56837625, 0.84375, 0.84375), IBooleanFunction.OR);

        return shape;
    }

}
