package me.swirtzly.regeneration.client.animation;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.item.FobWatchItem;
import me.swirtzly.regeneration.util.ClientUtil;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.item.ItemStack;

public class AnimationManager {

    public static boolean animate(AnimationContext animationContext) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) animationContext.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        ItemStack offStack = player.getHeldItemOffhand();
        BipedModel modelBiped = animationContext.getModelBiped();
        IRegeneration data = CapabilityRegeneration.getForPlayer(player).orElse(null);


        //==============FOB WATCH & JAR START==============
        boolean isOpen;

        //MAINHAND
        if (stack.getItem() instanceof FobWatchItem) {
            isOpen = FobWatchItem.getOpen(stack) == 1;
            if (isOpen) {
                makeZombieArms(modelBiped);
                return copyAndReturn(modelBiped, true);
            }
        }

        //OFFHAND
        if (offStack.getItem() instanceof FobWatchItem) {
            isOpen = FobWatchItem.getOpen(stack) == 1;
            if (isOpen) {
                makeZombieArms(modelBiped);
                return copyAndReturn(modelBiped, true);
            }
        }
        //==============FOB WATCH END==============

        //JAR SYNCING
        if (data.isSyncingToJar()) {
            makeZombieArms(modelBiped);
            modelBiped.bipedHead.rotateAngleX = (float) Math.toRadians(45);
            return copyAndReturn(modelBiped, true);
        }

        /**    if(CapabilityRegeneration.getForPlayer(player).getState() == PlayerUtil.RegenState.GRACE_CRIT){

         float limbSwingCrawl = animationContext.getLimbSwingAmount();

         if(limbSwingCrawl > 0.25) {
         limbSwingCrawl = 0.25f;
         }
         float movement = MathHelper.cos(animationContext.getLimbSwing() * 0.8f + (float)Math.PI) * limbSwingCrawl;

         modelBiped.bipedLeftArm.rotateAngleX = 180 / (180F / (float)Math.PI) - movement * 0.25f;
         modelBiped.bipedLeftArm.rotateAngleY = movement * -0.46f;
         modelBiped.bipedLeftArm.rotateAngleZ = movement * -0.2f;
         //	modelBiped.bipedLeftArm.rotationPointY = 2 - movement * 9.0F;

         modelBiped.bipedRightArm.rotateAngleX = 180 / (180F / (float)Math.PI) + movement * 0.25f;
         modelBiped.bipedRightArm.rotateAngleY = movement * -0.4f;
         modelBiped.bipedRightArm.rotateAngleZ = movement * -0.2f;
         //	modelBiped.bipedRightArm.rotationPointY = 2 + movement * 9.0F;

         modelBiped.bipedBody.rotateAngleY = movement * 0.1f;
         modelBiped.bipedBody.rotateAngleX = 0;
         modelBiped.bipedBody.rotateAngleZ = movement * 0.1f;

         modelBiped.bipedLeftLeg.rotateAngleX = movement * 0.1f;
         modelBiped.bipedLeftLeg.rotateAngleY = movement * 0.1f;
         modelBiped.bipedLeftLeg.rotateAngleZ = -7 / (180F / (float)Math.PI) - movement * 0.25f;
         //	modelBiped.bipedLeftLeg.rotationPointY = 10.4f + movement * 9.0F;
         //	modelBiped.bipedLeftLeg.rotationPointZ = movement * 0.6f;

         modelBiped.bipedRightLeg.rotateAngleX = movement * -0.1f;
         modelBiped.bipedRightLeg.rotateAngleY = movement * 0.1f;
         modelBiped.bipedRightLeg.rotateAngleZ = 7 / (180F / (float)Math.PI) - movement * 0.25f;
         //	modelBiped.bipedRightLeg.rotationPointY = 10.4f - movement * 9.0F;
         //		modelBiped.bipedRightLeg.rotationPointZ = movement * -0.6f;

         return copyAndReturn(modelBiped, true);
         }
         */

        return copyAndReturn(modelBiped, false);
    }

    public static boolean copyAndReturn(BipedModel modelBiped, boolean cancel) {
        if (modelBiped instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) modelBiped;
            ClientUtil.copyAnglesToWear(playerModel);
        }
        return cancel;
    }

    public static void makeZombieArms(BipedModel modelBiped) {
        modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY - 0.4F;
        modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY;
        modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
        modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
    }


}
