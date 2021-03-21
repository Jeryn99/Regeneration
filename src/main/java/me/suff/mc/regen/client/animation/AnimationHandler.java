package me.suff.mc.regen.client.animation;

import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationHandler {

    public static void setRotationAnglesCallback(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            TransitionType< ? > type = iRegen.transitionType().get();

            type.getRenderer().animate(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            if (iRegen.getCurrentState() != RegenStates.REGENERATING && livingEntity.getType() == EntityType.PLAYER) {
                handleArmor(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }

            if (livingEntity.getType() == EntityType.PLAYER) {
                if (PlayerUtil.isPlayerAboveZeroGrid(livingEntity) && iRegen.getCurrentState() == RegenStates.POST) {
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

    public static void handleArmor(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (bipedModel instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) bipedModel;
            playerModel.jacket.visible = hideBodyWear(livingEntity.getItemBySlot(EquipmentSlotType.CHEST));
            playerModel.leftSleeve.visible = playerModel.rightSleeve.visible = livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() != RItems.GUARD_CHEST.get();
            playerModel.leftPants.visible = playerModel.rightPants.visible = hideLegWear(livingEntity.getItemBySlot(EquipmentSlotType.LEGS));
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


    public static void correctPlayerModel(BipedModel bipedModel) {
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
        void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
    }

}
