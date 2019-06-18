package me.swirtzly.regeneration.client;

import me.swirtzly.regeneration.client.rendering.AnimationContext;
import me.swirtzly.regeneration.common.item.ItemFobWatch;
import me.swirtzly.regeneration.util.ClientUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AnimationHandler {

    public static boolean animate(AnimationContext animationContext) {
        EntityPlayer player = animationContext.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        ItemStack offStack = player.getHeldItemOffhand();
        ModelBiped modelBiped = animationContext.getModelBiped();

        //==============FOB WATCH START==============
        boolean isOpen;
        if (stack.getItem() instanceof ItemFobWatch) {

            isOpen = ItemFobWatch.getOpen(stack) == 1;

            if (isOpen) {
                modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY;
                modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY + 0.4F;
                modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                return copyAndReturn(modelBiped, true);
            }
        } else if (offStack.getItem() instanceof ItemFobWatch) {
            isOpen = ItemFobWatch.getOpen(stack) == 1;
            if (isOpen) {
                modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY - 0.4F;
                modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY;
                modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;

                //==============MAKE SURE ANGLES COPY OVER==============
                if (modelBiped instanceof ModelPlayer) {
                    ModelPlayer playerModel = (ModelPlayer) modelBiped;
                    ClientUtil.copyAnglesToWear(playerModel);
                }
                return copyAndReturn(modelBiped, true);
            }
        }
        //==============FOB WATCH END==============


        return copyAndReturn(modelBiped, false);
    }

    public static boolean copyAndReturn(ModelBiped modelBiped, boolean cancel) {
        if (modelBiped instanceof ModelPlayer) {
            ModelPlayer playerModel = (ModelPlayer) modelBiped;
            ClientUtil.copyAnglesToWear(playerModel);
        }
        return cancel;
    }

}
