package me.suff.mc.regen.client.animation;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.animateme.AnimationManager;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.item.FobWatchItem;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;

public class GeneralAnimations implements AnimationManager.IAnimate {

    public static void copyAnglesToWear(BipedModel modelBiped) {
        // if (modelBiped instanceof PlayerModel) {
        // PlayerModel playerModel = (PlayerModel) modelBiped;
        // ClientUtil.copyAnglesToWear(playerModel);
        // }
    }

    public static void makeZombieArms(BipedModel modelBiped) {
        modelBiped.rightArm.yRot = -0.1F + modelBiped.head.yRot - 0.4F;
        modelBiped.leftArm.yRot = 0.1F + modelBiped.head.yRot;
        modelBiped.rightArm.xRot = -((float) Math.PI / 2F) + modelBiped.head.xRot;
        modelBiped.leftArm.xRot = -((float) Math.PI / 2F) + modelBiped.head.xRot;
    }

    @Override
    public void preRenderCallback(LivingRenderer renderer, LivingEntity entity) {
        RegenCap.get(entity).ifPresent((data) -> {
            if (!(renderer.getModel() instanceof BipedModel)) return;
            BipedModel modelPlayer = (BipedModel) renderer.getModel();
            if (data.hasDroppedHand() && data.getState() == PlayerUtil.RegenState.POST) {
                modelPlayer.rightArm.neverRender = data.getCutoffHand() == HandSide.RIGHT;
                modelPlayer.leftArm.neverRender = data.getCutoffHand() == HandSide.LEFT;
            }

            if (modelPlayer instanceof PlayerModel) {
                PlayerModel playerModel = (PlayerModel) modelPlayer;
                playerModel.leftSleeve.neverRender = playerModel.rightSleeve.neverRender = playerModel.jacket.neverRender = hideBodyWear(entity.getItemBySlot(EquipmentSlotType.CHEST));
                playerModel.rightLeg.neverRender = playerModel.leftLeg.neverRender = playerModel.rightPants.neverRender = playerModel.leftPants.neverRender = !showLegWear(entity.getItemBySlot(EquipmentSlotType.LEGS));
            }

            if (data.getState() == PlayerUtil.RegenState.POST && PlayerUtil.isAboveZeroGrid(entity)) {
                GlStateManager.rotatef(15, 1, 0, 0);
            }
        });
    }

    private boolean showLegWear(ItemStack stack) {
        Item[] items = new Item[]{RegenObjects.Items.ROBES_LEGS.get(), RegenObjects.Items.GUARD_LEGGINGS.get()};
        for (Item item : items) {
            if (stack.getItem() == item) {
                return false;
            }
        }
        return true;
    }

    public boolean hideBodyWear(ItemStack stack) {
        Item[] items = new Item[]{RegenObjects.Items.ROBES_CHEST.get(), RegenObjects.Items.GUARD_CHEST.get()};
        for (Item item : items) {
            if (stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void animateEntity(BipedModel modelBiped, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getMainHandItem();
        ItemStack offStack = entity.getOffhandItem();

        // ==============FOB WATCH & JAR START==============
        boolean isOpen;

        // MAINHAND
        if (stack.getItem() instanceof FobWatchItem) {
            isOpen = FobWatchItem.getOpen(stack) == 1;
            if (isOpen) {
                makeZombieArms(modelBiped);
                copyAnglesToWear(modelBiped);
            }
        }

        // OFFHAND
        if (offStack.getItem() instanceof FobWatchItem) {
            isOpen = FobWatchItem.getOpen(stack) == 1;
            if (isOpen) {
                makeZombieArms(modelBiped);
                copyAnglesToWear(modelBiped);
            }
        }
        // ==============FOB WATCH END==============

        RegenCap.get(entity).ifPresent((data) -> {
            // JAR SYNCING
            if (data.isSyncingToJar()) {
                makeZombieArms(modelBiped);
                modelBiped.head.xRot = (float) Math.toRadians(45);
                copyAnglesToWear(modelBiped);
            }

            if (data.getState() == PlayerUtil.RegenState.POST && PlayerUtil.isAboveZeroGrid(entity)) {
                modelBiped.head.xRot = (float) Math.toRadians(0);
                modelBiped.head.yRot = (float) Math.toRadians(0);
                modelBiped.head.zRot = (float) Math.toRadians(0);
            }
        });
    }

}
