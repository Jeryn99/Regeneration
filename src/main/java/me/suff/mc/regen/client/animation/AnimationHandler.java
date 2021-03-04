package me.suff.mc.regen.client.animation;

import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AnimationHandler {

    public static void setRotationAnglesCallback(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            TransitionType< ? > type = iRegen.getTransitionType().get();
            type.getRenderer().animate(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            correctPlayerModel(bipedModel);
            if (iRegen.getCurrentState() != RegenStates.REGENERATING) {
                handleArmor(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }
        });
    }

    public static void handleArmor(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (bipedModel instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) bipedModel;
            playerModel.bipedBodyWear.showModel = hideBodyWear(livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST));
            playerModel.bipedLeftArmwear.showModel = playerModel.bipedRightArmwear.showModel = livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() != RItems.GUARD_CHEST.get();
            playerModel.bipedLeftLegwear.showModel = playerModel.bipedRightLegwear.showModel = hideLegWear(livingEntity.getItemStackFromSlot(EquipmentSlotType.LEGS));
        }
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
            playerModel.bipedHeadwear.copyModelAngles(playerModel.bipedHead);
            playerModel.bipedLeftArmwear.copyModelAngles(playerModel.bipedLeftArm);
            playerModel.bipedRightArmwear.copyModelAngles(playerModel.bipedRightArm);
            playerModel.bipedLeftLegwear.copyModelAngles(playerModel.bipedLeftLeg);
            playerModel.bipedRightLegwear.copyModelAngles(playerModel.bipedRightLeg);
        }
    }

    public interface Animation {
        void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
    }

}
