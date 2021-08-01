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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationHandler {

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
        if (bipedModel instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) bipedModel;
            playerModel.jacket.visible = hideBodyWear(livingEntity.getItemBySlot(EquipmentSlot.CHEST));
            playerModel.leftSleeve.visible = playerModel.rightSleeve.visible = livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() != RItems.GUARD_CHEST.get();
            playerModel.leftPants.visible = playerModel.rightPants.visible = hideLegWear(livingEntity.getItemBySlot(EquipmentSlot.LEGS));
            playerModel.leftSleeve.visible = playerModel.leftArm.visible = showArms(livingEntity);
        }
    }

    public static boolean showArms(LivingEntity livingEntity) {
        AtomicBoolean show = new AtomicBoolean(true);
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            show.set(iRegen.handState() == IRegen.Hand.LEFT_GONE);
        });
        return !show.get();
    }

    public static boolean hideLegWear(ItemStack stack) {
        Item[] items = new Item[]{RItems.F_ROBES_LEGS.get(), RItems.M_ROBES_LEGS.get(), RItems.GUARD_LEGS.get(), RItems.ROBES_FEET.get()};
        for (Item item : items) {
            if (item == stack.getItem()) {
                return false;
            }
        }
        return true;
    }

    public static boolean hideBodyWear(ItemStack stack) {
        Item[] items = new Item[]{RItems.F_ROBES_CHEST.get(), RItems.GUARD_CHEST.get(), RItems.M_ROBES_CHEST.get()};
        for (Item item : items) {
            if (item == stack.getItem()) {
                return false;
            }
        }
        return true;
    }


    public static void correctPlayerModel(HumanoidModel bipedModel) {
        if (bipedModel instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) bipedModel;
            playerModel.hat.copyFrom(playerModel.head);
            playerModel.leftSleeve.copyFrom(playerModel.leftArm);
            playerModel.rightSleeve.copyFrom(playerModel.rightArm);
            playerModel.leftPants.copyFrom(playerModel.leftLeg);
            playerModel.rightPants.copyFrom(playerModel.rightLeg);
        }
    }


    public interface Animation {
        void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
    }

}
