package me.suff.regeneration.client;

import me.suff.regeneration.common.item.ItemFobWatch;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AnimationHandler {

    public static boolean animatePlayer(EntityPlayer player, ModelBiped modelBiped) {

        ItemStack stack = player.getHeldItemMainhand();
        ItemStack offStack = player.getHeldItemOffhand();


        //==============FOB WATCH START==============
        boolean isOpen = false;
        if (stack.getItem() instanceof ItemFobWatch) {

            isOpen = ItemFobWatch.getOpen(stack) == 1;

            if (isOpen) {
                modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY;
                modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY + 0.4F;
                modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                return true;
            }
        } else if (offStack.getItem() instanceof ItemFobWatch) {
            isOpen = ItemFobWatch.getOpen(stack) == 1;
            if (isOpen) {
                modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY - 0.4F;
                modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY;
                modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
                return true;
            }
        }
        //==============FOB WATCH END==============


        //==============MAKE SURE ANGLES COPY OVER==============
        if (modelBiped instanceof ModelPlayer) {
            ModelPlayer playerModel = (ModelPlayer) modelBiped;
            ModelBase.copyModelAngles(modelBiped.bipedRightArm, playerModel.bipedRightArmwear);
            ModelBase.copyModelAngles(modelBiped.bipedLeftArm, playerModel.bipedLeftArmwear);
        }

        return false;
    }

}
