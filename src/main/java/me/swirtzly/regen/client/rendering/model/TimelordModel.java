package me.swirtzly.regen.client.rendering.model;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

public class TimelordModel extends PlayerModel<TimelordEntity> {
    private final ModelRenderer head;
    private final ModelRenderer timelord_hat;
    private final ModelRenderer body;
    private final ModelRenderer timelordcape;
    private final ModelRenderer timelord_body_armor;
    private final ModelRenderer head_part_armor;
    private final ModelRenderer right_arm;
    private final ModelRenderer rist2;
    private final ModelRenderer timelord_shoulder_right;
    private final ModelRenderer left_arm;
    private final ModelRenderer rist;
    private final ModelRenderer timelord_shoulder_left;
    private final ModelRenderer right_leg;
    private final ModelRenderer shoes2;
    private final ModelRenderer robes;
    private final ModelRenderer left_leg;
    private final ModelRenderer shoes;
    private final ModelRenderer robs;

    public TimelordModel() {
        super(1, false);
        textureWidth = 80;
        textureHeight = 80;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.setTextureOffset(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);

        timelord_hat = new ModelRenderer(this);
        timelord_hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(timelord_hat);
        timelord_hat.setTextureOffset(62, 69).addBox(3.25F, -8.0F, -4.0F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.25F, -8.0F, -4.0F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(3.25F, -5.0F, -3.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.25F, -5.0F, -3.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(3.25F, -4.0F, 1.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.25F, -4.0F, 1.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.0F, -8.0F, 3.25F, 8.0F, 6.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(3.25F, -4.0F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.25F, -4.0F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(3.0F, -8.0F, -4.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.0F, -8.0F, -4.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-2.5F, -7.5F, -4.25F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-1.5F, -7.0F, -4.25F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-1.0F, -6.5F, -4.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(62, 69).addBox(-4.0F, -8.0F, -4.25F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_hat.setTextureOffset(13, 58).addBox(-4.0F, -8.25F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(32, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

        timelordcape = new ModelRenderer(this);
        timelordcape.setRotationPoint(0.0F, 4.0F, 2.5F);
        body.addChild(timelordcape);
        timelordcape.setTextureOffset(0, 32).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 19.0F, 0.0F, 0.0F, false);

        timelord_body_armor = new ModelRenderer(this);
        timelord_body_armor.setRotationPoint(0.0F, 4.0F, 2.0F);
        body.addChild(timelord_body_armor);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-4.0F, -3.5F, 1.5F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-4.0F, -3.25F, 1.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(3.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-4.0F, 0.0F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(2.0F, 0.0F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(1.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-2.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(32, 36).addBox(-1.0F, -2.5F, -4.75F, 2.0F, 1.0F, 0.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-4.0F, -4.0F, -5.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(2.0F, -4.0F, -5.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(62, 62).addBox(-4.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(64, 68).addBox(1.0F, -4.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(64, 68).addBox(1.0F, -4.5F, 2.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(30, 53).addBox(-1.0F, -4.0F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(22, 53).addBox(-1.0F, -4.5F, 2.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(64, 68).addBox(-2.0F, -4.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(64, 68).addBox(-2.0F, -4.5F, 2.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(-3.0F, -1.0F, 0.25F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(-4.0F, -2.0F, 0.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(-4.0F, -1.0F, -5.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(-2.0F, -4.0F, -5.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(3.0F, -2.0F, 0.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(2.0F, -1.0F, -5.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_body_armor.setTextureOffset(16, 71).addBox(1.0F, -4.0F, -5.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        head_part_armor = new ModelRenderer(this);
        head_part_armor.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(head_part_armor);
        head_part_armor.setTextureOffset(64, 68).addBox(1.0F, -1.0F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(0, 0).addBox(-1.0F, -7.0F, 4.5F, 2.0F, 7.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(64, 68).addBox(-2.0F, -1.0F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-5.0F, -3.0F, 4.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(31, 21).addBox(1.0F, -4.0F, 4.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(31, 21).addBox(-5.0F, -4.0F, 4.5F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        head_part_armor.setTextureOffset(30, 21).addBox(1.0F, -6.0F, 4.5F, 5.0F, 2.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(30, 21).addBox(-6.0F, -6.0F, 4.5F, 5.0F, 2.0F, 1.0F, 0.0F, true);
        head_part_armor.setTextureOffset(68, 70).addBox(-7.0F, -4.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-7.0F, -6.0F, 4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(1.0F, -3.0F, 4.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(5.0F, -4.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(6.0F, -6.0F, 4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(52, 43).addBox(1.0F, -8.0F, 4.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(52, 43).addBox(-5.0F, -8.0F, 4.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);
        head_part_armor.setTextureOffset(24, 2).addBox(2.0F, -9.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(24, 2).addBox(-4.0F, -9.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        head_part_armor.setTextureOffset(68, 70).addBox(4.0F, -9.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(5.0F, -9.0F, 4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(1.0F, -10.0F, 4.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(1.0F, -10.0F, 4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-1.0F, -8.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-2.0F, -10.0F, 4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-4.0F, -10.0F, 4.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-6.0F, -9.0F, 4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head_part_armor.setTextureOffset(68, 70).addBox(-6.0F, -9.0F, 4.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        right_arm.setTextureOffset(48, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);

        rist2 = new ModelRenderer(this);
        rist2.setRotationPoint(5.0F, 21.75F, 0.25F);
        right_arm.addChild(rist2);
        rist2.setTextureOffset(49, 73).addBox(-7.25F, -17.0F, -2.25F, 1.0F, 3.0F, 4.0F, 0.0F, true);
        rist2.setTextureOffset(49, 73).addBox(-7.0F, -17.0F, -2.5F, 3.0F, 3.0F, 1.0F, 0.0F, true);
        rist2.setTextureOffset(49, 73).addBox(-4.75F, -17.0F, -2.25F, 1.0F, 3.0F, 4.0F, 0.0F, true);
        rist2.setTextureOffset(49, 73).addBox(-7.0F, -17.0F, 1.0F, 3.0F, 3.0F, 1.0F, 0.0F, true);

        timelord_shoulder_right = new ModelRenderer(this);
        timelord_shoulder_right.setRotationPoint(0.0F, 0.5F, 0.0F);
        right_arm.addChild(timelord_shoulder_right);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.0F, -0.5F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.0F, -0.5F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.0F, -3.5F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.0F, -3.5F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(0.0F, -2.5F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(0.0F, -2.5F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(0.0F, -3.5F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-1.0F, 0.5F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-1.0F, 0.5F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(16, 71).addBox(0.0F, -0.5F, -3.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(16, 71).addBox(-1.0F, -3.5F, -3.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(16, 71).addBox(-1.0F, -3.75F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(16, 71).addBox(-1.0F, -3.5F, 2.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(16, 71).addBox(0.0F, -0.5F, 2.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.25F, -3.5F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.25F, -3.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        timelord_shoulder_right.setTextureOffset(62, 62).addBox(-2.25F, -3.5F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
        left_arm.setTextureOffset(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);

        rist = new ModelRenderer(this);
        rist.setRotationPoint(-5.0F, 21.75F, 0.25F);
        left_arm.addChild(rist);
        rist.setTextureOffset(49, 73).addBox(6.25F, -17.0F, -2.25F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        rist.setTextureOffset(49, 73).addBox(4.0F, -17.0F, -2.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        rist.setTextureOffset(49, 73).addBox(3.75F, -17.0F, -2.25F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        rist.setTextureOffset(49, 73).addBox(4.0F, -17.0F, 1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        timelord_shoulder_left = new ModelRenderer(this);
        timelord_shoulder_left.setRotationPoint(0.0F, 0.5F, 0.0F);
        left_arm.addChild(timelord_shoulder_left);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(0.0F, -0.5F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(0.0F, -0.5F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(1.0F, -3.5F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(1.0F, -3.5F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(-1.0F, -2.5F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(-1.0F, -2.5F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(-1.0F, -3.5F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(1.0F, -3.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(-1.0F, 0.5F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(-1.0F, 0.5F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(1.25F, -3.5F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(1.25F, -3.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(62, 62).addBox(1.25F, -3.5F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(16, 71).addBox(-1.0F, -0.5F, -3.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(16, 71).addBox(0.0F, -3.5F, -3.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(16, 71).addBox(0.0F, -3.75F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(16, 71).addBox(0.0F, -3.5F, 2.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        timelord_shoulder_left.setTextureOffset(16, 71).addBox(-1.0F, -0.5F, 2.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        right_leg.setTextureOffset(36, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        shoes2 = new ModelRenderer(this);
        shoes2.setRotationPoint(1.9F, 11.5F, 0.5F);
        right_leg.addChild(shoes2);
        shoes2.setTextureOffset(0, 73).addBox(-3.9F, -0.5F, -3.25F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        shoes2.setTextureOffset(0, 73).addBox(-3.9F, -1.0F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);

        robes = new ModelRenderer(this);
        robes.setRotationPoint(-1.0F, 10.0F, -2.25F);
        right_leg.addChild(robes);
        robes.setTextureOffset(34, 73).addBox(2.25F, -4.0F, 2.25F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        robes.setTextureOffset(50, 71).addBox(2.25F, -3.0F, 3.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        robes.setTextureOffset(34, 73).addBox(2.25F, -1.0F, 2.25F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        robes.setTextureOffset(35, 73).addBox(-1.0F, -1.0F, 3.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        robes.setTextureOffset(50, 73).addBox(-1.0F, -3.0F, 3.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        robes.setTextureOffset(50, 71).addBox(-1.25F, -4.0F, 0.25F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        robes.setTextureOffset(34, 73).addBox(-1.25F, -1.0F, 0.25F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        robes.setTextureOffset(34, 73).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        robes.setTextureOffset(52, 73).addBox(-1.0F, -5.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        robes.setTextureOffset(35, 73).addBox(0.0F, -6.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
        left_leg.setTextureOffset(20, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        shoes = new ModelRenderer(this);
        shoes.setRotationPoint(-1.9F, 12.0F, 0.25F);
        left_leg.addChild(shoes);
        shoes.setTextureOffset(0, 73).addBox(-0.1F, -1.5F, -2.75F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        shoes.setTextureOffset(0, 73).addBox(-0.1F, -1.0F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        robs = new ModelRenderer(this);
        robs.setRotationPoint(1.0F, 10.0F, -1.75F);
        left_leg.addChild(robs);
        robs.setTextureOffset(35, 73).addBox(-3.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        robs.setTextureOffset(34, 73).addBox(-0.75F, -1.0F, -0.25F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        robs.setTextureOffset(34, 73).addBox(-3.25F, -1.0F, 1.75F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        robs.setTextureOffset(34, 73).addBox(-3.25F, -4.0F, 1.75F, 1.0F, 3.0F, 1.0F, 0.0F, true);
        robs.setTextureOffset(34, 73).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        robs.setTextureOffset(35, 73).addBox(-1.0F, -6.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
        robs.setTextureOffset(52, 73).addBox(0.0F, -5.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
        robs.setTextureOffset(50, 71).addBox(0.25F, -4.0F, -0.25F, 1.0F, 3.0F, 4.0F, 0.0F, true);
        robs.setTextureOffset(50, 71).addBox(-3.25F, -3.0F, 2.75F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        robs.setTextureOffset(50, 73).addBox(-3.0F, -3.0F, 3.0F, 4.0F, 2.0F, 1.0F, 0.0F, true);

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        left_leg.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        right_leg.render(matrixStack, buffer, packedLight, packedOverlay);
        left_leg.render(matrixStack, buffer, packedLight, packedOverlay);
        right_arm.render(matrixStack, buffer, packedLight, packedOverlay);
        left_arm.render(matrixStack, buffer, packedLight, packedOverlay);
        head.showModel = false;
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
}