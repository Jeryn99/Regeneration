package me.swirtzly.regeneration.client.animation;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.item.ItemFobWatch;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.item.ItemStack;

public class AnimationHandler {

    public static boolean animate(AnimationContext animationContext) {
        AbstractClientPlayer player = (AbstractClientPlayer) animationContext.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        ItemStack offStack = player.getHeldItemOffhand();
        ModelBiped modelBiped = animationContext.getModelBiped();
        IRegeneration data = CapabilityRegeneration.getForPlayer(player);

        //==============FOB WATCH & JAR START==============
        boolean isOpen;

        //MAINHAND
        if (stack.getItem() instanceof ItemFobWatch) {
            isOpen = ItemFobWatch.getOpen(stack) == 1;
            if (isOpen) {
                makeZombieArms(modelBiped);
                return copyAndReturn(modelBiped, true);
            }
        }

        //OFFHAND
        if (offStack.getItem() instanceof ItemFobWatch) {
            isOpen = ItemFobWatch.getOpen(stack) == 1;
            if (isOpen) {
                makeZombieArms(modelBiped);
                return copyAndReturn(modelBiped, true);
            }
        }
        //==============FOB WATCH END==============

        //JAR SYNCING
        if (data.isSyncingToJar()) {

            double animationProgress = data.getAnimationTicks();
            float armRot = (float) animationProgress * 1.5F;

            if (armRot > 90) {
                armRot = 90;
            }

            modelBiped.bipedLeftArm.rotateAngleX = (float) -Math.toRadians(armRot);
            modelBiped.bipedRightArm.rotateAngleX = (float) -Math.toRadians(armRot);

            modelBiped.bipedBody.rotateAngleX = 0;
            modelBiped.bipedBody.rotateAngleY = 0;
            modelBiped.bipedBody.rotateAngleZ = 0;

            //Legs
            modelBiped.bipedLeftLeg.rotateAngleY = 0;
            modelBiped.bipedRightLeg.rotateAngleY = 0;
            modelBiped.bipedLeftLeg.rotateAngleX = 0;
            modelBiped.bipedRightLeg.rotateAngleX = 0;
            modelBiped.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
            modelBiped.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

            return copyAndReturn(modelBiped, true);
        }

        //STRUGGLE IN CRITICAL
        if (CapabilityRegeneration.getForPlayer(player).getState() == PlayerUtil.RegenState.GRACE_CRIT) {
            modelBiped.bipedBody.rotateAngleX = 0.5F;
            modelBiped.bipedRightArm.rotateAngleX = (float) Math.toRadians(-25);
            modelBiped.bipedRightArm.rotateAngleY = (float) Math.toRadians(-55);
            modelBiped.bipedLeftArm.rotateAngleX += 0.4F;
            modelBiped.bipedRightLeg.rotationPointZ = 4.0F;
            modelBiped.bipedLeftLeg.rotationPointZ = 4.0F;
            modelBiped.bipedRightLeg.rotationPointY = 9.0F;
            modelBiped.bipedLeftLeg.rotationPointY = 9.0F;
            modelBiped.bipedHead.rotationPointY = 1.0F;
            modelBiped.bipedHead.rotateAngleX = (float) Math.toRadians(45);
            return copyAndReturn(modelBiped, true);
        }


        return copyAndReturn(modelBiped, false);
    }

    public static boolean copyAndReturn(ModelBiped modelBiped, boolean cancel) {
        if (modelBiped instanceof ModelPlayer) {
            ModelPlayer playerModel = (ModelPlayer) modelBiped;
            ClientUtil.copyAnglesToWear(playerModel);
        }
        return cancel;
    }

    public static boolean makeZombieArms(ModelBiped modelBiped) {
        modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY - 0.4F;
        modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY;
        modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
        modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
        return copyAndReturn(modelBiped, true);
    }


}
