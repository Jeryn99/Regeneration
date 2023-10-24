package mc.craig.software.regen.client.animation;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import mc.craig.software.regen.util.PlayerUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;

import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationHandler {


    public static Item[] LEG_ITEMS = new Item[]{RItems.F_ROBES_LEGS.get(), RItems.M_ROBES_LEGS.get(), RItems.GUARD_LEGS.get(), RItems.ROBES_FEET.get()};
    public static Item[] BODY_ITEMS = new Item[]{RItems.F_ROBES_CHEST.get(), RItems.GUARD_CHEST.get(), RItems.M_ROBES_CHEST.get()};

    public static void setRotationAnglesCallback(HumanoidModel<?> bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Get the regeneration data for the living entity
        RegenerationData.get(livingEntity).ifPresent(iRegen -> {
            // Get the transition type
            TransitionType type = iRegen.transitionType();

            // Animate the model based on the transition type
            TransitionTypeRenderers.get(type).animate(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // If the living entity is not regenerating and is a player, handle the armor
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
            // Hide the jacket model part if the player is wearing one of the specified items in the BODY_ITEMS array
            playerModel.jacket.visible = hideModelPartIf(livingEntity, BODY_ITEMS, PlayerModelPart.JACKET, EquipmentSlot.CHEST);
            playerModel.leftSleeve.visible = playerModel.rightSleeve.visible = livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() != RItems.GUARD_CHEST.get();

            // Hide the left pants and right pants model parts if the player is wearing one of the specified items in the LEG_ITEMS array
            playerModel.leftPants.visible = hideModelPartIf(livingEntity, LEG_ITEMS, PlayerModelPart.LEFT_PANTS_LEG, EquipmentSlot.LEGS);
            playerModel.rightPants.visible = hideModelPartIf(livingEntity, LEG_ITEMS, PlayerModelPart.RIGHT_PANTS_LEG, EquipmentSlot.LEGS);
        }
    }

    public static boolean showArms(LivingEntity livingEntity) {
        AtomicBoolean show = new AtomicBoolean(true);
        RegenerationData.get(livingEntity).ifPresent(iRegen -> show.set(iRegen.handState() != IRegen.Hand.NOT_CUT));
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

    public static boolean hideModelPartIf(LivingEntity livingEntity, Item[] items, PlayerModelPart playerModelPart, EquipmentSlot equipmentSlot) {
        // Check if the player is wearing one of the specified items
        for (Item item : items) {
            if (item == livingEntity.getItemBySlot(equipmentSlot).getItem()) {
                // If the player is wearing the item, hide the model part
                return false;
            }
        }

        // If the player is not wearing the item, show the model part
        return true;
    }


    public interface Animation {
        void animate(HumanoidModel<?> bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
    }

}
