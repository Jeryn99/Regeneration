package me.swirtzly.regen.client.rendering.model;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

public class TimelordGuardModel extends PlayerModel<TimelordEntity> {
    private final ModelRenderer head;
    private final ModelRenderer helment;
    private final ModelRenderer curves2;
    private final ModelRenderer curv;
    private final ModelRenderer body;
    private final ModelRenderer body_skirt;
    private final ModelRenderer belt;
    private final ModelRenderer chest;
    private final ModelRenderer right_arm;
    private final ModelRenderer gloves;
    private final ModelRenderer shoulderpad;
    private final ModelRenderer left_arm;
    private final ModelRenderer shoulderpad2;
    private final ModelRenderer gloves2;
    private final ModelRenderer right_leg;
    private final ModelRenderer shoes_knee_pads;
    private final ModelRenderer skirt;
    private final ModelRenderer left_leg;
    private final ModelRenderer shoes_knee_pads2;
    private final ModelRenderer skirt2;

    public TimelordGuardModel() {
        super(1, false);
        textureWidth = 80;
        textureHeight = 80;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.setTextureOffset(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);

        helment = new ModelRenderer(this);
        helment.setRotationPoint(0.0F, 24.0F, 0.0F);
        head.addChild(helment);
        helment.setTextureOffset(58, 32).addBox(-4.45F, -29.75F, -0.65F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        helment.setTextureOffset(62, 22).addBox(3.45F, -29.75F, -0.65F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        helment.setTextureOffset(66, 41).addBox(1.0F, -30.0F, -4.35F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(24, 24).addBox(-4.0F, -32.25F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);
        helment.setTextureOffset(40, 33).addBox(-4.0F, -32.0F, 3.25F, 8.0F, 6.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(48, 28).addBox(-4.0F, -26.75F, 3.25F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(14, 48).addBox(3.25F, -32.0F, -4.0F, 1.0F, 2.0F, 8.0F, 0.0F, false);
        helment.setTextureOffset(40, 8).addBox(-4.25F, -32.0F, -4.0F, 1.0F, 2.0F, 8.0F, 0.0F, false);
        helment.setTextureOffset(48, 0).addBox(3.25F, -30.0F, -3.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        helment.setTextureOffset(24, 16).addBox(-4.25F, -30.0F, -3.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        helment.setTextureOffset(32, 50).addBox(3.25F, -29.0F, -2.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);
        helment.setTextureOffset(48, 18).addBox(-4.25F, -29.0F, -2.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);
        helment.setTextureOffset(49, 51).addBox(3.25F, -27.0F, -1.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        helment.setTextureOffset(32, 65).addBox(-4.25F, -27.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        helment.setTextureOffset(65, 22).addBox(3.25F, -26.75F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        helment.setTextureOffset(24, 4).addBox(-4.25F, -26.75F, 1.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        helment.setTextureOffset(33, 18).addBox(-4.0F, -32.0F, -4.25F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(42, 68).addBox(-4.0F, -30.0F, -4.35F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(14, 58).addBox(-4.35F, -30.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(54, 44).addBox(3.35F, -30.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(40, 40).addBox(-4.35F, -29.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(36, 33).addBox(3.35F, -29.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(68, 46).addBox(-4.35F, -27.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        helment.setTextureOffset(50, 41).addBox(3.35F, -27.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        helment.setTextureOffset(57, 0).addBox(-4.35F, -27.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(56, 52).addBox(3.35F, -27.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(68, 43).addBox(-4.35F, -26.25F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        helment.setTextureOffset(68, 35).addBox(3.35F, -26.25F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        helment.setTextureOffset(64, 57).addBox(-4.35F, -25.75F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        helment.setTextureOffset(64, 52).addBox(3.35F, -25.75F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        helment.setTextureOffset(48, 30).addBox(-4.0F, -25.75F, 3.35F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(57, 2).addBox(-4.35F, -29.5F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(56, 50).addBox(3.35F, -29.5F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(21, 71).addBox(-1.0F, -32.75F, -4.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(69, 3).addBox(-0.5F, -32.0F, -4.6F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        helment.setTextureOffset(68, 3).addBox(-0.5F, -32.9F, -4.6F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        helment.setTextureOffset(21, 71).addBox(-1.0F, -32.75F, -3.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        curves2 = new ModelRenderer(this);
        curves2.setRotationPoint(3.0F, -32.75F, 2.5F);
        helment.addChild(curves2);
        setRotationAngle(curves2, -0.0873F, 0.0F, 0.0F);
        curves2.setTextureOffset(21, 71).addBox(-4.0F, 0.4395F, -1.9146F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        curv = new ModelRenderer(this);
        curv.setRotationPoint(3.0F, -32.75F, 2.5F);
        helment.addChild(curv);
        setRotationAngle(curv, -0.1745F, 0.0F, 0.0F);
        curv.setTextureOffset(21, 71).addBox(-4.0F, 0.5209F, -2.9544F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        curv.setTextureOffset(68, 2).addBox(-3.5F, 0.3906F, -3.079F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

        body_skirt = new ModelRenderer(this);
        body_skirt.setRotationPoint(0.1F, 26.0F, 0.0F);
        body.addChild(body_skirt);
        body_skirt.setTextureOffset(16, 64).addBox(0.9F, -15.0F, -2.15F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        body_skirt.setTextureOffset(36, 58).addBox(-4.1F, -15.0F, -2.15F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        body_skirt.setTextureOffset(48, 63).addBox(0.9F, -15.0F, 1.15F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        body_skirt.setTextureOffset(56, 22).addBox(-4.1F, -15.0F, 1.15F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        body_skirt.setTextureOffset(60, 39).addBox(3.05F, -15.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        body_skirt.setTextureOffset(24, 58).addBox(-4.25F, -15.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        belt = new ModelRenderer(this);
        belt.setRotationPoint(0.0F, 24.0F, -0.25F);
        body.addChild(belt);
        belt.setTextureOffset(62, 17).addBox(3.25F, -14.0F, -1.75F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        belt.setTextureOffset(33, 21).addBox(-4.0F, -14.0F, 1.5F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        belt.setTextureOffset(62, 11).addBox(-4.25F, -14.0F, -1.75F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        belt.setTextureOffset(48, 26).addBox(-4.0F, -14.0F, -2.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

        chest = new ModelRenderer(this);
        chest.setRotationPoint(0.25F, 23.0F, -0.45F);
        body.addChild(chest);
        chest.setTextureOffset(23, 68).addBox(-1.5F, -21.0F, -1.7F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(67, 0).addBox(-0.75F, -20.0F, -1.6F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(19, 68).addBox(0.0F, -21.0F, -1.7F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(21, 71).addBox(0.0F, -22.0F, -1.85F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(67, 3).addBox(-0.75F, -21.25F, -1.75F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(21, 71).addBox(-1.5F, -22.0F, -1.85F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(19, 68).addBox(-1.5F, -23.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(67, 3).addBox(-0.75F, -22.25F, -1.9F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(67, 0).addBox(-0.75F, -23.0F, -1.9F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        chest.setTextureOffset(19, 68).addBox(0.0F, -23.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        right_arm.setTextureOffset(0, 48).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);

        gloves = new ModelRenderer(this);
        gloves.setRotationPoint(4.9F, 21.75F, 3.0F);
        right_arm.addChild(gloves);
        gloves.setTextureOffset(20, 58).addBox(-6.9F, -16.0F, -5.1F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        gloves.setTextureOffset(57, 0).addBox(-4.8F, -16.0F, -5.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        gloves.setTextureOffset(48, 57).addBox(-6.9F, -16.0F, -1.9F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        gloves.setTextureOffset(14, 58).addBox(-7.0F, -16.0F, -5.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        shoulderpad = new ModelRenderer(this);
        shoulderpad.setRotationPoint(5.0F, 24.25F, 0.05F);
        right_arm.addChild(shoulderpad);
        shoulderpad.setTextureOffset(0, 20).addBox(-7.0F, -26.25F, -2.3F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        shoulderpad.setTextureOffset(68, 16).addBox(-6.5F, -25.75F, -2.35F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        shoulderpad.setTextureOffset(0, 16).addBox(-7.0F, -26.25F, 1.2F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        shoulderpad.setTextureOffset(56, 14).addBox(-7.25F, -26.25F, -2.05F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(42, 56).addBox(-4.75F, -26.25F, -2.05F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(50, 8).addBox(-7.0F, -26.5F, -2.05F, 3.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(64, 64).addBox(-7.15F, -23.5F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(10, 64).addBox(-4.85F, -23.5F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(26, 64).addBox(-7.05F, -23.0F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(0, 64).addBox(-4.95F, -23.0F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad.setTextureOffset(66, 39).addBox(-7.0F, -23.5F, -2.2F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        shoulderpad.setTextureOffset(66, 31).addBox(-7.0F, -23.5F, 1.1F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        shoulderpad.setTextureOffset(66, 33).addBox(-7.0F, -23.0F, -2.1F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        shoulderpad.setTextureOffset(6, 66).addBox(-7.0F, -23.0F, 1.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
        left_arm.setTextureOffset(40, 40).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);

        shoulderpad2 = new ModelRenderer(this);
        shoulderpad2.setRotationPoint(-5.0F, 24.25F, 0.05F);
        left_arm.addChild(shoulderpad2);
        shoulderpad2.setTextureOffset(0, 4).addBox(4.0F, -26.25F, -2.3F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(68, 11).addBox(4.5F, -25.75F, -2.35F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(0, 0).addBox(4.0F, -26.25F, 1.2F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(54, 43).addBox(6.25F, -26.25F, -2.05F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(54, 36).addBox(3.75F, -26.25F, -2.05F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(24, 49).addBox(4.0F, -26.5F, -2.05F, 3.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(52, 63).addBox(6.15F, -23.5F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(42, 63).addBox(3.85F, -23.5F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(20, 63).addBox(6.05F, -23.0F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(62, 46).addBox(3.95F, -23.0F, -2.05F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(65, 29).addBox(4.0F, -23.5F, -2.2F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(65, 27).addBox(4.0F, -23.5F, 1.1F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(48, 65).addBox(4.0F, -23.0F, -2.1F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        shoulderpad2.setTextureOffset(38, 65).addBox(4.0F, -23.0F, 1.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        gloves2 = new ModelRenderer(this);
        gloves2.setRotationPoint(-4.9F, 21.75F, 3.0F);
        left_arm.addChild(gloves2);
        gloves2.setTextureOffset(14, 52).addBox(3.9F, -16.0F, -5.1F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        gloves2.setTextureOffset(52, 57).addBox(3.8F, -16.0F, -5.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        gloves2.setTextureOffset(20, 33).addBox(3.9F, -16.0F, -1.9F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        gloves2.setTextureOffset(56, 50).addBox(6.0F, -16.0F, -5.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        right_leg.setTextureOffset(24, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        shoes_knee_pads = new ModelRenderer(this);
        shoes_knee_pads.setRotationPoint(1.9F, 12.0F, -0.75F);
        right_leg.addChild(shoes_knee_pads);
        shoes_knee_pads.setTextureOffset(50, 22).addBox(-2.4F, -6.0F, -1.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads.setTextureOffset(24, 51).addBox(-2.15F, -6.5F, -1.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads.setTextureOffset(34, 51).addBox(-2.65F, -6.5F, -1.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads.setTextureOffset(62, 51).addBox(-2.9F, -8.0F, -1.45F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        shoes_knee_pads.setTextureOffset(50, 13).addBox(-3.9F, -1.5F, -1.75F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads.setTextureOffset(24, 54).addBox(-3.9F, -1.0F, -2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        skirt = new ModelRenderer(this);
        skirt.setRotationPoint(1.9F, 13.0F, -0.15F);
        right_leg.addChild(skirt);
        skirt.setTextureOffset(60, 44).addBox(-4.0F, -13.0F, 1.3F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        skirt.setTextureOffset(36, 60).addBox(-4.15F, -13.0F, -1.85F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        skirt.setTextureOffset(33, 16).addBox(-3.9F, -12.25F, 1.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        skirt.setTextureOffset(30, 59).addBox(-4.0F, -12.25F, -1.85F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        skirt.setTextureOffset(58, 56).addBox(-3.95F, -11.5F, -1.85F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        skirt.setTextureOffset(24, 49).addBox(-3.9F, -11.5F, 1.2F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        skirt.setTextureOffset(0, 48).addBox(-3.9F, -11.5F, -1.9F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        skirt.setTextureOffset(24, 21).addBox(-3.9F, -12.25F, -1.95F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        skirt.setTextureOffset(60, 37).addBox(-4.0F, -13.0F, -2.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
        left_leg.setTextureOffset(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        shoes_knee_pads2 = new ModelRenderer(this);
        shoes_knee_pads2.setRotationPoint(-1.9F, 12.0F, -0.75F);
        left_leg.addChild(shoes_knee_pads2);
        shoes_knee_pads2.setTextureOffset(50, 20).addBox(1.4F, -6.0F, -1.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads2.setTextureOffset(50, 10).addBox(1.15F, -6.5F, -1.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads2.setTextureOffset(50, 8).addBox(1.65F, -6.5F, -1.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads2.setTextureOffset(48, 4).addBox(0.9F, -8.0F, -1.45F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        shoes_knee_pads2.setTextureOffset(10, 50).addBox(-0.1F, -1.5F, -1.75F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        shoes_knee_pads2.setTextureOffset(10, 48).addBox(-0.1F, -1.0F, -2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        skirt2 = new ModelRenderer(this);
        skirt2.setRotationPoint(-1.9F, 13.0F, -0.15F);
        left_leg.addChild(skirt2);
        skirt2.setTextureOffset(6, 64).addBox(1.0F, -13.0F, 1.3F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        skirt2.setTextureOffset(58, 61).addBox(3.15F, -13.0F, -1.85F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        skirt2.setTextureOffset(62, 66).addBox(1.9F, -12.25F, 1.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        skirt2.setTextureOffset(60, 6).addBox(3.0F, -12.25F, -1.85F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        skirt2.setTextureOffset(60, 32).addBox(2.95F, -11.5F, -1.85F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        skirt2.setTextureOffset(34, 49).addBox(2.9F, -11.5F, 1.2F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        skirt2.setTextureOffset(0, 50).addBox(2.9F, -11.5F, -1.9F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        skirt2.setTextureOffset(68, 19).addBox(1.9F, -12.25F, -1.95F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        skirt2.setTextureOffset(64, 62).addBox(1.0F, -13.0F, -2.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(TimelordEntity timelordEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        IRegen regenData = RegenCap.get(timelordEntity).orElseGet(null);
/*

        if (timelordEntity.getHeldItemMainhand().getItem() instanceof GunItem && regenData.getState() != PlayerUtil.RegenState.REGENERATING) {
            rightArmPose = ArmPose.BOW_AND_ARROW;
        }
*/

        if (regenData.getCurrentState() == REGENERATING) {
            rightArmPose = ArmPose.EMPTY;
        }

        super.setRotationAngles(timelordEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        head.copyModelAngles(bipedHead);
        body.copyModelAngles(bipedBody);
        left_arm.copyModelAngles(bipedLeftArm);
        right_arm.copyModelAngles(bipedRightArm);
        right_leg.copyModelAngles(bipedRightLeg);
        left_leg.copyModelAngles(bipedLeftLeg);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        //head.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        right_arm.render(matrixStack, buffer, packedLight, packedOverlay);
        left_arm.render(matrixStack, buffer, packedLight, packedOverlay);
        right_leg.render(matrixStack, buffer, packedLight, packedOverlay);
        left_leg.render(matrixStack, buffer, packedLight, packedOverlay);
        head.showModel = false;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}