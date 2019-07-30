package me.swirtzly.regeneration.client.animation;

import me.swirtzly.animateme.AnimationManager;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.item.FobWatchItem;
import me.swirtzly.regeneration.util.ClientUtil;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class GeneralAnimations implements AnimationManager.IAnimate {

    public static void copyAnglesToWear(BipedModel modelBiped) {
        if (modelBiped instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) modelBiped;
            ClientUtil.copyAnglesToWear(playerModel);
        }
    }

    public static void makeZombieArms(BipedModel modelBiped) {
        modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY - 0.4F;
        modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY;
        modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
        modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
    }


    @Override
    public void preAnimation(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void postAnimation(BipedModel modelBiped, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;

            ItemStack stack = player.getHeldItemMainhand();
            ItemStack offStack = player.getHeldItemOffhand();
            IRegeneration data = CapabilityRegeneration.getForPlayer(player).orElse(null);


            //==============FOB WATCH & JAR START==============
            boolean isOpen;

            //MAINHAND
            if (stack.getItem() instanceof FobWatchItem) {
                isOpen = FobWatchItem.getOpen(stack) == 1;
                if (isOpen) {
                    makeZombieArms(modelBiped);
                    copyAnglesToWear(modelBiped);
                }
            }

            //OFFHAND
            if (offStack.getItem() instanceof FobWatchItem) {
                isOpen = FobWatchItem.getOpen(stack) == 1;
                if (isOpen) {
                    makeZombieArms(modelBiped);
                    copyAnglesToWear(modelBiped);
                }
            }
            //==============FOB WATCH END==============

            //JAR SYNCING
            if (data.isSyncingToJar()) {
                makeZombieArms(modelBiped);
                modelBiped.bipedHead.rotateAngleX = (float) Math.toRadians(45);
                copyAnglesToWear(modelBiped);
            }
        }
    }

    @Override
    public boolean useVanilla() {
        return false;
    }
}
