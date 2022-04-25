package me.suff.mc.regen.client.animation;

import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypeRenderers;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationHandler {


    public static Item[] LEG_ITEMS = new Item[]{RItems.F_ROBES_LEGS.get(), RItems.M_ROBES_LEGS.get(), RItems.GUARD_LEGS.get(), RItems.ROBES_FEET.get()};
    public static Item[] BODY_ITEMS = new Item[]{RItems.F_ROBES_CHEST.get(), RItems.GUARD_CHEST.get(), RItems.M_ROBES_CHEST.get()};

    public static void setRotationAnglesCallback(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            TransitionType type = iRegen.transitionType();

            TransitionTypeRenderers.get(type).animate(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            if (iRegen.regenState() != RegenStates.REGENERATING && livingEntity.getType() == EntityType.PLAYER) {
                handleArmor(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }

            if (livingEntity.getType() == EntityType.PLAYER) {
                if (PlayerUtil.isPlayerAboveZeroGrid(livingEntity) && iRegen.regenState() == RegenStates.POST) {
                    bipedModel.head.xRot = (float) Math.toRadians(20);
                    bipedModel.head.yRot = (float) Math.toRadians(0);
                    bipedModel.head.zRot = (float) Math.toRadians(0);
                    bipedModel.leftLeg.zRot = (float) Math.toRadians(-2);
                    bipedModel.rightLeg.zRot = (float) Math.toRadians(2);
                }
            }

        });
        correctPlayerModel(bipedModel);
    }

    public static void handleArmor(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (bipedModel instanceof PlayerModel playerModel) {
            playerModel.jacket.visible = hideModelPartIf(livingEntity, BODY_ITEMS, PlayerModelPart.JACKET, EquipmentSlot.CHEST);
            playerModel.leftSleeve.visible = playerModel.rightSleeve.visible = livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() != RItems.GUARD_CHEST.get();
            playerModel.leftPants.visible = hideModelPartIf(livingEntity, LEG_ITEMS, PlayerModelPart.LEFT_PANTS_LEG, EquipmentSlot.LEGS);
            playerModel.rightPants.visible = hideModelPartIf(livingEntity, LEG_ITEMS, PlayerModelPart.RIGHT_PANTS_LEG, EquipmentSlot.LEGS);
            playerModel.leftSleeve.visible = playerModel.leftArm.visible = showArms(livingEntity); // Cut off Arm
        }
    }

    public static boolean showArms(LivingEntity livingEntity) {
        AtomicBoolean show = new AtomicBoolean(true);
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            show.set(iRegen.handState() == IRegen.Hand.LEFT_GONE);
        });
        return !show.get();
    }

    public static void correctPlayerModel(HumanoidModel bipedModel) {
        if (bipedModel instanceof PlayerModel playerModel) {
            playerModel.hat.copyFrom(playerModel.head);
            playerModel.leftSleeve.copyFrom(playerModel.leftArm);
            playerModel.rightSleeve.copyFrom(playerModel.rightArm);
            playerModel.leftPants.copyFrom(playerModel.leftLeg);
            playerModel.rightPants.copyFrom(playerModel.rightLeg);
        }
    }

    /* Return false to hide, true to show */
    public static boolean hideModelPartIf(LivingEntity livingEntity, Item[] items, PlayerModelPart part, EquipmentSlot slot) {
        for (Item item : items) {
            if(livingEntity.getItemBySlot(slot).getItem() == item){
                return false;
            }
        }

        if (livingEntity instanceof AbstractClientPlayer player) {
            return player.isModelPartShown(part);
        }

        return true;
    }


    public interface Animation {
        void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
    }

}
