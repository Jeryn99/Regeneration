package me.swirtzly.regeneration.client.rendering.model;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.item.GunItem;
import me.swirtzly.regeneration.util.client.RenderUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.HandSide;

/**
 * Created by Swirtzly
 * on 08/05/2020 @ 11:28
 */
public class TimelordGuardModel extends BipedModel< TimelordEntity > {
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
    private final RendererModel shoes_knee_pads2;
    private final RendererModel skirt2;

    public TimelordGuardModel() {
        textureWidth = 80;
        textureHeight = 80;

        head = new RendererModel(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 16, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F, false));

        helment = new RendererModel(this);
        helment.setRotationPoint(0.0F, 24.0F, 0.0F);
        bipedHead.addChild(helment);
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
        right_arm.setRotationPoint(-5.0F, 2.5F, 0.0F);
        right_arm.cubeList.add(new ModelBox(right_arm, 0, 48, -2.0F, -2.5F, -2.0F, 3, 12, 4, 0.0F, false));

        gloves = new RendererModel(this);
        gloves.setRotationPoint(4.9F, 21.25F, 3.0F);
        right_arm.addChild(gloves);
        gloves.cubeList.add(new ModelBox(gloves, 20, 58, -6.9F, -16.0F, -5.1F, 3, 2, 1, 0.0F, false));
        gloves.cubeList.add(new ModelBox(gloves, 57, 0, -4.8F, -16.0F, -5.0F, 1, 2, 4, 0.0F, false));
        gloves.cubeList.add(new ModelBox(gloves, 48, 57, -6.9F, -16.0F, -1.9F, 3, 2, 1, 0.0F, false));
        gloves.cubeList.add(new ModelBox(gloves, 14, 58, -7.0F, -16.0F, -5.0F, 1, 2, 4, 0.0F, false));

        shoulderpad = new RendererModel(this);
        shoulderpad.setRotationPoint(5.0F, 23.75F, 0.05F);
        right_arm.addChild(shoulderpad);
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 0, 20, -7.0F, -26.25F, -2.3F, 3, 3, 1, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 68, 16, -6.5F, -25.75F, -2.35F, 2, 2, 1, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 0, 16, -7.0F, -26.25F, 1.2F, 3, 3, 1, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 56, 14, -7.25F, -26.25F, -2.05F, 1, 3, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 42, 56, -4.75F, -26.25F, -2.05F, 1, 3, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 50, 8, -7.0F, -26.5F, -2.05F, 3, 1, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 64, 64, -7.15F, -23.5F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 10, 64, -4.85F, -23.5F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 26, 64, -7.05F, -23.0F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 0, 64, -4.95F, -23.0F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 66, 39, -7.0F, -23.5F, -2.2F, 3, 1, 1, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 66, 31, -7.0F, -23.5F, 1.1F, 3, 1, 1, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 66, 33, -7.0F, -23.0F, -2.1F, 3, 1, 1, 0.0F, false));
        shoulderpad.cubeList.add(new ModelBox(shoulderpad, 6, 66, -7.0F, -23.0F, 1.0F, 3, 1, 1, 0.0F, false));

        left_arm = new RendererModel(this);
        left_arm.setRotationPoint(5.0F, 2.5F, 0.0F);
        left_arm.cubeList.add(new ModelBox(left_arm, 40, 40, -1.0F, -2.5F, -2.0F, 3, 12, 4, 0.0F, false));

        shoulderpad2 = new RendererModel(this);
        shoulderpad2.setRotationPoint(-5.0F, 23.75F, 0.05F);
        left_arm.addChild(shoulderpad2);
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 0, 4, 4.0F, -26.25F, -2.3F, 3, 3, 1, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 68, 11, 4.5F, -25.75F, -2.35F, 2, 2, 1, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 0, 0, 4.0F, -26.25F, 1.2F, 3, 3, 1, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 54, 43, 6.25F, -26.25F, -2.05F, 1, 3, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 54, 36, 3.75F, -26.25F, -2.05F, 1, 3, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 24, 49, 4.0F, -26.5F, -2.05F, 3, 1, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 52, 63, 6.15F, -23.5F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 42, 63, 3.85F, -23.5F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 20, 63, 6.05F, -23.0F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 62, 46, 3.95F, -23.0F, -2.05F, 1, 1, 4, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 65, 29, 4.0F, -23.5F, -2.2F, 3, 1, 1, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 65, 27, 4.0F, -23.5F, 1.1F, 3, 1, 1, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 48, 65, 4.0F, -23.0F, -2.1F, 3, 1, 1, 0.0F, false));
        shoulderpad2.cubeList.add(new ModelBox(shoulderpad2, 38, 65, 4.0F, -23.0F, 1.0F, 3, 1, 1, 0.0F, false));

        gloves2 = new RendererModel(this);
        gloves2.setRotationPoint(-4.9F, 21.25F, 3.0F);
        left_arm.addChild(gloves2);
        gloves2.cubeList.add(new ModelBox(gloves2, 14, 52, 3.9F, -16.0F, -5.1F, 3, 2, 1, 0.0F, false));
        gloves2.cubeList.add(new ModelBox(gloves2, 52, 57, 3.8F, -16.0F, -5.0F, 1, 2, 4, 0.0F, false));
        gloves2.cubeList.add(new ModelBox(gloves2, 20, 33, 3.9F, -16.0F, -1.9F, 3, 2, 1, 0.0F, false));
        gloves2.cubeList.add(new ModelBox(gloves2, 56, 50, 6.0F, -16.0F, -5.0F, 1, 2, 4, 0.0F, false));

        right_leg = new RendererModel(this);
        right_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        right_leg.cubeList.add(new ModelBox(right_leg, 24, 33, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

        shoes_knee_pads = new RendererModel(this);
        shoes_knee_pads.setRotationPoint(1.9F, 12.0F, -0.75F);
        right_leg.addChild(shoes_knee_pads);
        shoes_knee_pads.cubeList.add(new ModelBox(shoes_knee_pads, 50, 22, -2.4F, -6.0F, -1.3F, 1, 1, 1, 0.0F, false));
        shoes_knee_pads.cubeList.add(new ModelBox(shoes_knee_pads, 24, 51, -2.15F, -6.5F, -1.4F, 1, 1, 1, 0.0F, false));
        shoes_knee_pads.cubeList.add(new ModelBox(shoes_knee_pads, 34, 51, -2.65F, -6.5F, -1.4F, 1, 1, 1, 0.0F, false));
        shoes_knee_pads.cubeList.add(new ModelBox(shoes_knee_pads, 62, 51, -2.9F, -8.0F, -1.45F, 2, 2, 1, 0.0F, false));
        shoes_knee_pads.cubeList.add(new ModelBox(shoes_knee_pads, 50, 13, -3.9F, -1.5F, -1.75F, 4, 1, 1, 0.0F, false));
        shoes_knee_pads.cubeList.add(new ModelBox(shoes_knee_pads, 24, 54, -3.9F, -1.0F, -2.0F, 4, 1, 1, 0.0F, false));

        skirt = new RendererModel(this);
        skirt.setRotationPoint(1.9F, 13.0F, -0.15F);
        right_leg.addChild(skirt);
        skirt.cubeList.add(new ModelBox(skirt, 60, 44, -4.0F, -13.0F, 1.3F, 3, 1, 1, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 36, 60, -4.15F, -13.0F, -1.85F, 1, 1, 4, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 33, 16, -3.9F, -12.25F, 1.25F, 2, 1, 1, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 30, 59, -4.0F, -12.25F, -1.85F, 1, 1, 4, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 58, 56, -3.95F, -11.5F, -1.85F, 1, 1, 4, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 24, 49, -3.9F, -11.5F, 1.2F, 1, 1, 1, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 0, 48, -3.9F, -11.5F, -1.9F, 1, 1, 1, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 24, 21, -3.9F, -12.25F, -1.95F, 2, 1, 1, 0.0F, false));
        skirt.cubeList.add(new ModelBox(skirt, 60, 37, -4.0F, -13.0F, -2.0F, 3, 1, 1, 0.0F, false));

        left_leg = new RendererModel(this);
        left_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
        left_leg.cubeList.add(new ModelBox(left_leg, 32, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

        shoes_knee_pads2 = new RendererModel(this);
        shoes_knee_pads2.setRotationPoint(-1.9F, 12.0F, -0.75F);
        left_leg.addChild(shoes_knee_pads2);
        shoes_knee_pads2.cubeList.add(new ModelBox(shoes_knee_pads2, 50, 20, 1.4F, -6.0F, -1.3F, 1, 1, 1, 0.0F, false));
        shoes_knee_pads2.cubeList.add(new ModelBox(shoes_knee_pads2, 50, 10, 1.15F, -6.5F, -1.4F, 1, 1, 1, 0.0F, false));
        shoes_knee_pads2.cubeList.add(new ModelBox(shoes_knee_pads2, 50, 8, 1.65F, -6.5F, -1.4F, 1, 1, 1, 0.0F, false));
        shoes_knee_pads2.cubeList.add(new ModelBox(shoes_knee_pads2, 48, 4, 0.9F, -8.0F, -1.45F, 2, 2, 1, 0.0F, false));
        shoes_knee_pads2.cubeList.add(new ModelBox(shoes_knee_pads2, 10, 50, -0.1F, -1.5F, -1.75F, 4, 1, 1, 0.0F, false));
        shoes_knee_pads2.cubeList.add(new ModelBox(shoes_knee_pads2, 10, 48, -0.1F, -1.0F, -2.0F, 4, 1, 1, 0.0F, false));

        skirt2 = new RendererModel(this);
        skirt2.setRotationPoint(-1.9F, 13.0F, -0.15F);
        left_leg.addChild(skirt2);
        skirt2.cubeList.add(new ModelBox(skirt2, 6, 64, 1.0F, -13.0F, 1.3F, 3, 1, 1, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 58, 61, 3.15F, -13.0F, -1.85F, 1, 1, 4, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 62, 66, 1.9F, -12.25F, 1.25F, 2, 1, 1, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 60, 6, 3.0F, -12.25F, -1.85F, 1, 1, 4, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 60, 32, 2.95F, -11.5F, -1.85F, 1, 1, 4, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 34, 49, 2.9F, -11.5F, 1.2F, 1, 1, 1, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 0, 50, 2.9F, -11.5F, -1.9F, 1, 1, 1, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 68, 19, 1.9F, -12.25F, -1.95F, 2, 1, 1, 0.0F, false));
        skirt2.cubeList.add(new ModelBox(skirt2, 64, 62, 1.0F, -13.0F, -2.0F, 3, 1, 1, 0.0F, false));

    }

    @Override
    public void render(TimelordEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        // head.render(f5);
        bipedHead.render(f5);
        body.render(f5);
        right_arm.render(f5);
        left_arm.render(f5);
        right_leg.render(f5);
        left_leg.render(f5);
        head.isHidden = true;
    }

    @Override
    public void setRotationAngles(TimelordEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

        IRegen regenData = RegenCap.get(entityIn).orElseGet(null);

        if (entityIn.getHeldItemMainhand().getItem() instanceof GunItem && regenData.getState() != PlayerUtil.RegenState.REGENERATING) {
            rightArmPose = ArmPose.BOW_AND_ARROW;
        }

        if (regenData.getState() == PlayerUtil.RegenState.REGENERATING) {
            rightArmPose = ArmPose.EMPTY;
        }

        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        RegenCap.get(entityIn).ifPresent((data) -> data.getRegenType().create().getRenderer().animateEntity(this, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor));
        RenderUtil.copyModelAngles(bipedHead, head);
        RenderUtil.copyModelAngles(bipedBody, body);
        RenderUtil.copyModelAngles(bipedLeftArm, left_arm);
        RenderUtil.copyModelAngles(bipedRightArm, right_arm);
        RenderUtil.copyModelAngles(bipedRightLeg, right_leg);
        RenderUtil.copyModelAngles(bipedLeftLeg, left_leg);
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    protected RendererModel getArmForSide(HandSide side) {
        return side == HandSide.LEFT ? this.left_arm : this.right_arm;
    }

}
