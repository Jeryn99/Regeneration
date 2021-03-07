package me.swirtzly.regeneration.client.rendering.model;// Made with Blockbench 3.5.4
// Exported for Minecraft version 1.14
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class GuardModel extends BipedModel {
    private final RendererModel head;
    private final RendererModel helment;
    private final RendererModel curves2;
    private final RendererModel curv;
    private final RendererModel body;
    private final RendererModel body_skirt;
    private final RendererModel belt;
    private final RendererModel chest;
    private final RendererModel right_arm;
    private final RendererModel gloves;
    private final RendererModel shoulderpad;
    private final RendererModel left_arm;
    private final RendererModel shoulderpad2;
    private final RendererModel gloves2;
    private final RendererModel right_leg;
    private final RendererModel shoes_knee_pads;
    private final RendererModel skirt;
    private final RendererModel left_leg;
    private final RendererModel skirt2;
    private final RendererModel right_boot;
    private final RendererModel left_boot;

    EquipmentSlotType type = EquipmentSlotType.CHEST;

    public GuardModel(EquipmentSlotType type) {
        this.type = type;
        textureWidth = 80;
        textureHeight = 80;

        head = new RendererModel(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);


        helment = new RendererModel(this);
        helment.setRotationPoint(0.0F, 24.0F, 0.0F);
        head.addChild(helment);
        helment.cubeList.add(new ModelBox(helment, 58, 32, -4.45F, -29.75F, -0.65F, 1, 2, 2, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 62, 22, 3.45F, -29.75F, -0.65F, 1, 2, 2, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 66, 41, 1.0F, -30.0F, -4.35F, 3, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 24, 24, -4.0F, -32.25F, -4.0F, 8, 1, 8, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 40, 33, -4.0F, -32.0F, 3.25F, 8, 6, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 48, 28, -4.0F, -26.75F, 3.25F, 8, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 14, 48, 3.25F, -32.0F, -4.0F, 1, 2, 8, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 40, 8, -4.25F, -32.0F, -4.0F, 1, 2, 8, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 48, 0, 3.25F, -30.0F, -3.0F, 1, 1, 7, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 24, 16, -4.25F, -30.0F, -3.0F, 1, 1, 7, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 32, 50, 3.25F, -29.0F, -2.0F, 1, 2, 6, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 48, 18, -4.25F, -29.0F, -2.0F, 1, 2, 6, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 49, 51, 3.25F, -27.0F, -1.0F, 1, 1, 5, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 32, 65, -4.25F, -27.0F, 0.0F, 1, 1, 4, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 65, 22, 3.25F, -26.75F, 0.0F, 1, 1, 4, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 24, 4, -4.25F, -26.75F, 1.0F, 1, 1, 3, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 33, 18, -4.0F, -32.0F, -4.25F, 8, 2, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 42, 68, -4.0F, -30.0F, -4.35F, 3, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 14, 58, -4.35F, -30.0F, -4.0F, 1, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 54, 44, 3.35F, -30.0F, -4.0F, 1, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 40, 40, -4.35F, -29.0F, -3.0F, 1, 2, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 36, 33, 3.35F, -29.0F, -3.0F, 1, 2, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 68, 46, -4.35F, -27.0F, -2.0F, 1, 1, 2, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 50, 41, 3.35F, -27.0F, -2.0F, 1, 1, 2, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 57, 0, -4.35F, -27.5F, -2.5F, 1, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 56, 52, 3.35F, -27.5F, -2.5F, 1, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 68, 43, -4.35F, -26.25F, -1.0F, 1, 1, 2, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 68, 35, 3.35F, -26.25F, -1.0F, 1, 1, 2, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 64, 57, -4.35F, -25.75F, 0.0F, 1, 1, 4, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 64, 52, 3.35F, -25.75F, 0.0F, 1, 1, 4, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 48, 30, -4.0F, -25.75F, 3.35F, 8, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 57, 2, -4.35F, -29.5F, -3.5F, 1, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 56, 50, 3.35F, -29.5F, -3.5F, 1, 1, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 21, 71, -1.0F, -32.75F, -4.5F, 2, 4, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 69, 3, -0.5F, -32.0F, -4.6F, 1, 3, 1, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 68, 3, -0.5F, -32.9F, -4.6F, 1, 1, 4, 0.0F, false));
        helment.cubeList.add(new ModelBox(helment, 21, 71, -1.0F, -32.75F, -3.5F, 2, 1, 3, 0.0F, false));

        curves2 = new RendererModel(this);
        curves2.setRotationPoint(3.0F, -32.75F, 2.5F);
        helment.addChild(curves2);
        setRotationAngle(curves2, -0.0873F, 0.0F, 0.0F);
        curves2.cubeList.add(new ModelBox(curves2, 21, 71, -4.0F, 0.4395F, -1.9146F, 2, 1, 3, 0.0F, false));

        curv = new RendererModel(this);
        curv.setRotationPoint(3.0F, -32.75F, 2.5F);
        helment.addChild(curv);
        setRotationAngle(curv, -0.1745F, 0.0F, 0.0F);
        curv.cubeList.add(new ModelBox(curv, 21, 71, -4.0F, 0.5209F, -2.9544F, 2, 1, 3, 0.0F, false));
        curv.cubeList.add(new ModelBox(curv, 68, 2, -3.5F, 0.3906F, -3.079F, 1, 1, 4, 0.0F, false));

        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 0, 32, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

        body_skirt = new RendererModel(this);
        body_skirt.setRotationPoint(0.1F, 26.0F, 0.0F);
        body.addChild(body_skirt);
        body_skirt.cubeList.add(new ModelBox(body_skirt, 16, 64, 0.9F, -15.0F, -2.15F, 3, 1, 1, 0.0F, false));
        body_skirt.cubeList.add(new ModelBox(body_skirt, 36, 58, -4.1F, -15.0F, -2.15F, 3, 1, 1, 0.0F, false));
        body_skirt.cubeList.add(new ModelBox(body_skirt, 48, 63, 0.9F, -15.0F, 1.15F, 3, 1, 1, 0.0F, false));
        body_skirt.cubeList.add(new ModelBox(body_skirt, 56, 22, -4.1F, -15.0F, 1.15F, 3, 1, 1, 0.0F, false));
        body_skirt.cubeList.add(new ModelBox(body_skirt, 60, 39, 3.05F, -15.0F, -2.0F, 1, 1, 4, 0.0F, false));
        body_skirt.cubeList.add(new ModelBox(body_skirt, 24, 58, -4.25F, -15.0F, -2.0F, 1, 1, 4, 0.0F, false));

        belt = new RendererModel(this);
        belt.setRotationPoint(0.0F, 24.0F, -0.25F);
        body.addChild(belt);
        belt.cubeList.add(new ModelBox(belt, 62, 17, 3.25F, -14.0F, -1.75F, 1, 1, 4, 0.0F, false));
        belt.cubeList.add(new ModelBox(belt, 33, 21, -4.0F, -14.0F, 1.5F, 8, 1, 1, 0.0F, false));
        belt.cubeList.add(new ModelBox(belt, 62, 11, -4.25F, -14.0F, -1.75F, 1, 1, 4, 0.0F, false));
        belt.cubeList.add(new ModelBox(belt, 48, 26, -4.0F, -14.0F, -2.0F, 8, 1, 1, 0.0F, false));

        chest = new RendererModel(this);
        chest.setRotationPoint(0.25F, 23.0F, -0.45F);
        body.addChild(chest);
        chest.cubeList.add(new ModelBox(chest, 23, 68, -1.5F, -21.0F, -1.7F, 1, 8, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 67, 0, -0.75F, -20.0F, -1.6F, 1, 7, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 19, 68, 0.0F, -21.0F, -1.7F, 1, 8, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 21, 71, 0.0F, -22.0F, -1.85F, 1, 3, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 67, 3, -0.75F, -21.25F, -1.75F, 1, 2, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 21, 71, -1.5F, -22.0F, -1.85F, 1, 3, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 19, 68, -1.5F, -23.0F, -2.0F, 1, 2, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 67, 3, -0.75F, -22.25F, -1.9F, 1, 1, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 67, 0, -0.75F, -23.0F, -1.9F, 1, 1, 1, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 19, 68, 0.0F, -23.0F, -2.0F, 1, 2, 1, 0.0F, false));

        right_arm = new RendererModel(this);
        right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        right_arm.cubeList.add(new ModelBox(right_arm, 0, 48, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        gloves = new RendererModel(this);
        gloves.setRotationPoint(4.9F, 21.75F, 3.0F);
        right_arm.addChild(gloves);
        gloves.cubeList.add(new ModelBox(gloves, 20, 58, -6.9F, -16.0F, -5.1F, 3, 2, 1, 0.25F, false));
        gloves.cubeList.add(new ModelBox(gloves, 57, 0, -4.8F, -16.0F, -5.0F, 1, 2, 4, 0.25F, false));
        gloves.cubeList.add(new ModelBox(gloves, 48, 57, -6.9F, -16.0F, -1.9F, 3, 2, 1, 0.25F, false));
        gloves.cubeList.add(new ModelBox(gloves, 14, 58, -7.0F, -16.0F, -5.0F, 1, 2, 4, 0.25F, false));

        shoulderpad = new RendererModel(this);
        shoulderpad.setRotationPoint(5.0F, 24.25F, 0.05F);
        right_arm.addChild(shoulderpad);
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 0, 20, -7.0F, -26.25F, -2.3F, 3, 3, 1, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 68, 16, -6.5F, -25.75F, -2.35F, 2, 2, 1, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 0, 16, -7.0F, -26.25F, 1.2F, 3, 3, 1, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 56, 14, -7.25F, -26.25F, -2.05F, 1, 3, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 42, 56, -4.75F, -26.25F, -2.05F, 1, 3, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 50, 8, -7.0F, -26.5F, -2.05F, 3, 1, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 64, 64, -7.15F, -23.5F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 10, 64, -4.85F, -23.5F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 26, 64, -7.05F, -23.0F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 0, 64, -4.95F, -23.0F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 66, 39, -7.0F, -23.5F, -2.2F, 3, 1, 1, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 66, 31, -7.0F, -23.5F, 1.1F, 3, 1, 1, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 66, 33, -7.0F, -23.0F, -2.1F, 3, 1, 1, 0.25F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 6, 66, -7.0F, -23.0F, 1.0F, 3, 1, 1, 0.25F, false));

        left_arm = new RendererModel(this);
        left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
        left_arm.cubeList.add(new ModelBox(left_arm, 40, 40, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        shoulderpad2 = new RendererModel(this);
        shoulderpad2.setRotationPoint(-5.0F, 24.25F, 0.05F);
        left_arm.addChild(shoulderpad2);
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 0, 4, 4.0F, -26.25F, -2.3F, 3, 3, 1, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 68, 11, 4.5F, -25.75F, -2.35F, 2, 2, 1, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 0, 0, 4.0F, -26.25F, 1.2F, 3, 3, 1, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 54, 43, 6.25F, -26.25F, -2.05F, 1, 3, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 54, 36, 3.75F, -26.25F, -2.05F, 1, 3, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 24, 49, 4.0F, -26.5F, -2.05F, 3, 1, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 52, 63, 6.15F, -23.5F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 42, 63, 3.85F, -23.5F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 20, 63, 6.05F, -23.0F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 62, 46, 3.95F, -23.0F, -2.05F, 1, 1, 4, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 65, 29, 4.0F, -23.5F, -2.2F, 3, 1, 1, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 65, 27, 4.0F, -23.5F, 1.1F, 3, 1, 1, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 48, 65, 4.0F, -23.0F, -2.1F, 3, 1, 1, 0.25F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 38, 65, 4.0F, -23.0F, 1.0F, 3, 1, 1, 0.25F, false));

        gloves2 = new RendererModel(this);
        gloves2.setRotationPoint(-4.9F, 21.75F, 3.0F);
        left_arm.addChild(gloves2);
        gloves2.cubeList.add(new ModelBox(gloves2, 14, 52, 3.9F, -16.0F, -5.1F, 3, 2, 1, 0.25F, false));
        gloves2.cubeList.add(new ModelBox(gloves2, 52, 57, 3.8F, -16.0F, -5.0F, 1, 2, 4, 0.25F, false));
        gloves2.cubeList.add(new ModelBox(gloves2, 20, 33, 3.9F, -16.0F, -1.9F, 3, 2, 1, 0.25F, false));
        gloves2.cubeList.add(new ModelBox(gloves2, 56, 50, 6.0F, -16.0F, -5.0F, 1, 2, 4, 0.25F, false));

        right_leg = new RendererModel(this);
        right_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        right_leg.cubeList.add(new ModelBox(right_leg, 24, 33, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.1F, false));

        shoes_knee_pads = new RendererModel(this);
        shoes_knee_pads.setRotationPoint(1.9F, 12.0F, -0.75F);
        right_leg.addChild(shoes_knee_pads);


        skirt = new RendererModel(this);
        skirt.setRotationPoint(1.9F, 13.0F, -0.15F);
        right_leg.addChild(skirt);
        skirt.cubeList.add(new ModelBox(skirt, 60, 44, -4.0F, -13.0F, 1.3F, 3, 1, 1, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 36, 60, -4.15F, -13.0F, -1.85F, 1, 1, 4, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 33, 16, -3.9F, -12.25F, 1.25F, 2, 1, 1, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 30, 59, -4.0F, -12.25F, -1.85F, 1, 1, 4, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 58, 56, -3.95F, -11.5F, -1.85F, 1, 1, 4, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 24, 49, -3.9F, -11.5F, 1.2F, 1, 1, 1, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 0, 48, -3.9F, -11.5F, -1.9F, 1, 1, 1, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 24, 21, -3.9F, -12.25F, -1.95F, 2, 1, 1, 0.1F, false));
        skirt.cubeList.add(new ModelBox(skirt, 60, 37, -4.0F, -13.0F, -2.0F, 3, 1, 1, 0.1F, false));

        right_boot = new RendererModel(this);
        right_boot.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_leg.addChild(right_boot);
        right_boot.cubeList.add(new ModelBox(right_boot, 51, 70, -2.0F, 6.0F, -2.0F, 4, 6, 4, 0.25F, false));
        right_boot.cubeList.add(new ModelBox(right_boot, 50, 22, -0.5F, 6.0F, -2.3F, 1, 1, 1, 0.1F, false));
        right_boot.cubeList.add(new ModelBox(right_boot, 34, 51, -0.75F, 5.5F, -2.4F, 1, 1, 1, 0.1F, false));
        right_boot.cubeList.add(new ModelBox(right_boot, 24, 51, -0.25F, 5.5F, -2.4F, 1, 1, 1, 0.1F, false));
        right_boot.cubeList.add(new ModelBox(right_boot, 62, 51, -1.0F, 4.0F, -2.45F, 2, 2, 1, 0.1F, false));
        right_boot.cubeList.add(new ModelBox(right_boot, 50, 13, -2.0F, 10.5F, -2.5F, 4, 1, 1, 0.25F, false));
        right_boot.cubeList.add(new ModelBox(right_boot, 24, 54, -2.0F, 11.0F, -2.75F, 4, 1, 1, 0.25F, false));

        left_leg = new RendererModel(this);
        left_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
        left_leg.cubeList.add(new ModelBox(left_leg, 32, 0, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.1F, false));

        skirt2 = new RendererModel(this);
        skirt2.setRotationPoint(-1.9F, 13.0F, -0.15F);
        left_leg.addChild(skirt2);
        skirt2.cubeList.add(new ModelBox(skirt2, 6, 64, 1.0F, -13.0F, 1.3F, 3, 1, 1, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 58, 61, 3.15F, -13.0F, -1.85F, 1, 1, 4, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 62, 66, 1.9F, -12.25F, 1.25F, 2, 1, 1, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 60, 6, 3.0F, -12.25F, -1.85F, 1, 1, 4, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 60, 32, 2.95F, -11.5F, -1.85F, 1, 1, 4, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 34, 49, 2.9F, -11.5F, 1.2F, 1, 1, 1, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 0, 50, 2.9F, -11.5F, -1.9F, 1, 1, 1, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 68, 19, 1.9F, -12.25F, -1.95F, 2, 1, 1, 0.1F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 64, 62, 1.0F, -13.0F, -2.0F, 3, 1, 1, 0.1F, false));

        left_boot = new RendererModel(this);
        left_boot.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_leg.addChild(left_boot);
        left_boot.cubeList.add(new ModelBox(left_boot, 51, 70, -2.0F, 6.0F, -2.0F, 4, 6, 4, 0.25F, false));
        left_boot.cubeList.add(new ModelBox(left_boot, 50, 20, -0.5F, 6.0F, -2.3F, 1, 1, 1, 0.1F, false));
        left_boot.cubeList.add(new ModelBox(left_boot, 50, 10, -0.75F, 5.5F, -2.4F, 1, 1, 1, 0.1F, false));
        left_boot.cubeList.add(new ModelBox(left_boot, 50, 8, -0.25F, 5.5F, -2.4F, 1, 1, 1, 0.1F, false));
        left_boot.cubeList.add(new ModelBox(left_boot, 48, 4, -1.0F, 4.0F, -2.45F, 2, 2, 1, 0.1F, false));
        left_boot.cubeList.add(new ModelBox(left_boot, 10, 50, -2.0F, 10.5F, -2.5F, 4, 1, 1, 0.25F, false));
        left_boot.cubeList.add(new ModelBox(left_boot, 10, 48, -2.0F, 11.0F, -2.75F, 4, 1, 1, 0.25F, false));
    }

    @Override
    public void render(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        boolean isWearingHelmet = entityIn.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegenObjects.Items.GUARD_HEAD.get() && type == EquipmentSlotType.HEAD;
        boolean isWearingChest = entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RegenObjects.Items.GUARD_CHEST.get() && type == EquipmentSlotType.CHEST;
        boolean isWearingLeggings = entityIn.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == RegenObjects.Items.GUARD_LEGGINGS.get() && type == EquipmentSlotType.LEGS;
        boolean isWearingBoots = entityIn.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == RegenObjects.Items.GUARD_FEET.get() && type == EquipmentSlotType.FEET;


        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();
        if (entityIn.shouldRenderSneaking()) {
            GlStateManager.translatef(0.0F, 0.2F, 0.0F);
        }

        if (isWearingHelmet) {
            GlStateManager.pushMatrix();
            if (entityIn instanceof ArmorStandEntity) {
                GlStateManager.rotatef(-90, 0F, 1, 0F);
            }
            this.head.render(scale);
            GlStateManager.popMatrix();
        }
        if (isWearingChest) {
            GlStateManager.pushMatrix();
            bipedBody = body;
            bipedBody.render(scale);
            GlStateManager.popMatrix();
            this.right_arm.render(scale);
            this.left_arm.render(scale);
        }

        if (isWearingBoots) {
            GlStateManager.pushMatrix();
            right_leg.postRender(scale);
            right_boot.render(scale);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            left_leg.postRender(scale);
            left_boot.render(scale);
            GlStateManager.popMatrix();
        }

        if (isWearingLeggings) {
            this.right_leg.render(scale);
            this.left_leg.render(scale);
            left_boot.isHidden = true;
            right_boot.isHidden = true;
        }

        GlStateManager.popMatrix();

    }


    @Override
    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        RegenCap.get(entityIn).ifPresent(iRegen -> {
            iRegen.getRegenType().create().getRenderer().animateEntity(this, entityIn, limbSwing, limbSwingAmount, Minecraft.getInstance().getRenderPartialTicks(), ageInTicks, netHeadYaw, headPitch);
        });
        this.head.copyModelAngles(this.bipedHead);
        this.left_arm.copyModelAngles(this.bipedLeftArm);
        this.right_arm.copyModelAngles(this.bipedRightArm);
        this.left_leg.copyModelAngles(this.bipedLeftLeg);
        this.right_leg.copyModelAngles(this.bipedRightLeg);
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}