package me.swirtzly.regeneration.client.rendering.model;// Made with Blockbench
// Paste this code into your mod.
// Make sure to generate all required imports

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.Entity;

public class VillagerTimelordModel extends EntityModel {
    private final RendererModel head;
    private final RendererModel timelord_hat;
    private final RendererModel body;
    private final RendererModel timelordcape;
    private final RendererModel timelord_body_armor;
    private final RendererModel head_part_armor;
    private final RendererModel right_arm;
    private final RendererModel rist2;
    private final RendererModel timelord_shoulder_right;
    private final RendererModel left_arm;
    private final RendererModel rist;
    private final RendererModel timelord_shoulder_left;
    private final RendererModel right_leg;
    private final RendererModel shoes2;
    private final RendererModel robes;
    private final RendererModel left_leg;
    private final RendererModel shoes;
    private final RendererModel robs;
    private final RendererModel nose;

    public VillagerTimelordModel() {
        textureWidth = 80;
        textureHeight = 80;

        head = new RendererModel(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 15, -4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F, false));

        timelord_hat = new RendererModel(this);
        timelord_hat.setRotationPoint(0.0F, -2.0F, 0.0F);
        head.addChild(timelord_hat);
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, 3.25F, -8.0F, -4.0F, 1, 3, 8, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.25F, -8.0F, -4.0F, 1, 3, 8, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, 3.25F, -5.0F, -3.0F, 1, 1, 7, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.25F, -5.0F, -3.0F, 1, 1, 7, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, 3.25F, -4.0F, 1.0F, 1, 2, 3, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.25F, -4.0F, 1.0F, 1, 2, 3, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.0F, -8.0F, 3.25F, 8, 6, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, 3.25F, -4.0F, -2.5F, 1, 2, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.25F, -4.0F, -2.5F, 1, 2, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, 3.0F, -8.0F, -4.25F, 1, 3, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.0F, -8.0F, -4.25F, 1, 3, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -2.5F, -7.5F, -4.25F, 5, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -1.5F, -7.0F, -4.25F, 3, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -1.0F, -6.5F, -4.25F, 2, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.0F, -8.0F, -4.25F, 8, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 68, 70, -4.0F, -8.25F, -4.0F, 8, 1, 8, 0.0F, false));

        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 32, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

        timelordcape = new RendererModel(this);
        timelordcape.setRotationPoint(0.0F, 4.0F, 2.5F);
        body.addChild(timelordcape);
        timelordcape.cubeList.add(new ModelBox(timelordcape, 0, 33, -5.0F, 0.0F, 0.0F, 10, 19, 0, 0.0F, false));

        timelord_body_armor = new RendererModel(this);
        timelord_body_armor.setRotationPoint(0.0F, 4.0F, 2.0F);
        body.addChild(timelord_body_armor);
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -3.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -3.0F, -2.0F, 0.0F, 6, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -4.0F, -4.0F, 0.0F, 8, 2, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -4.0F, -3.5F, 1.5F, 8, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -4.0F, -3.25F, 1.0F, 8, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, 3.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -4.0F, 0.0F, -5.0F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, 2.0F, 0.0F, -5.0F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, 1.0F, -1.0F, -5.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -2.0F, -1.0F, -5.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 32, 36, -1.0F, -2.5F, -4.75F, 2, 1, 0, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -4.0F, -4.0F, -5.0F, 2, 3, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, 2.0F, -4.0F, -5.0F, 2, 3, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 68, 70, -4.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 64, 68, 1.0F, -4.0F, 2.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 64, 68, 1.0F, -4.5F, 2.25F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 30, 53, -1.0F, -4.0F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 22, 53, -1.0F, -4.5F, 2.25F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 64, 68, -2.0F, -4.0F, 2.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 64, 68, -2.0F, -4.5F, 2.25F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, -3.0F, -1.0F, 0.25F, 6, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, -4.0F, -2.0F, 0.25F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, -4.0F, -1.0F, -5.25F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, -2.0F, -4.0F, -5.25F, 1, 3, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, 3.0F, -2.0F, 0.25F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, 2.0F, -1.0F, -5.25F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 16, 71, 1.0F, -4.0F, -5.25F, 1, 3, 1, 0.0F, false));

        head_part_armor = new RendererModel(this);
        head_part_armor.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(head_part_armor);
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 64, 68, 1.0F, -1.0F, 4.5F, 1, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 0, 0, -1.0F, -7.0F, 4.5F, 2, 7, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 64, 68, -2.0F, -1.0F, 4.5F, 1, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -5.0F, -3.0F, 4.5F, 4, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 31, 21, 1.0F, -4.0F, 4.5F, 4, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 31, 21, -5.0F, -4.0F, 4.5F, 4, 1, 1, 0.0F, true));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 30, 21, 1.0F, -6.0F, 4.5F, 5, 2, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 30, 21, -6.0F, -6.0F, 4.5F, 5, 2, 1, 0.0F, true));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -7.0F, -4.0F, 4.5F, 2, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -7.0F, -6.0F, 4.5F, 1, 3, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 1.0F, -3.0F, 4.5F, 4, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 5.0F, -4.0F, 4.5F, 2, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 6.0F, -6.0F, 4.5F, 1, 3, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 52, 43, 1.0F, -8.0F, 4.5F, 4, 2, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 52, 43, -5.0F, -8.0F, 4.5F, 4, 2, 1, 0.0F, true));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 24, 2, 2.0F, -9.0F, 4.5F, 2, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 24, 2, -4.0F, -9.0F, 4.5F, 2, 1, 1, 0.0F, true));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 4.0F, -9.0F, 4.5F, 2, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 5.0F, -9.0F, 4.5F, 1, 3, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 1.0F, -10.0F, 4.5F, 3, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, 1.0F, -10.0F, 4.5F, 1, 2, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -1.0F, -8.0F, 4.5F, 2, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -2.0F, -10.0F, 4.5F, 1, 2, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -4.0F, -10.0F, 4.5F, 3, 1, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -6.0F, -9.0F, 4.5F, 1, 3, 1, 0.0F, false));
        head_part_armor.cubeList.add(new ModelBox(head_part_armor, 68, 70, -6.0F, -9.0F, 4.5F, 2, 1, 1, 0.0F, false));

        right_arm = new RendererModel(this);
        right_arm.setRotationPoint(-5.0F, 2.5F, 0.0F);
        right_arm.cubeList.add(new ModelBox(right_arm, 48, 16, -2.0F, -2.5F, -2.0F, 3, 12, 4, 0.0F, false));

        rist2 = new RendererModel(this);
        rist2.setRotationPoint(5.0F, 21.25F, 0.25F);
        right_arm.addChild(rist2);
        rist2.cubeList.add(new ModelBox(rist2, 49, 73, -7.25F, -17.0F, -2.25F, 1, 3, 4, 0.0F, true));
        rist2.cubeList.add(new ModelBox(rist2, 49, 73, -7.0F, -17.0F, -2.5F, 3, 3, 1, 0.0F, true));
        rist2.cubeList.add(new ModelBox(rist2, 49, 73, -4.75F, -17.0F, -2.25F, 1, 3, 4, 0.0F, true));
        rist2.cubeList.add(new ModelBox(rist2, 49, 73, -7.0F, -17.0F, 1.0F, 3, 3, 1, 0.0F, true));

        timelord_shoulder_right = new RendererModel(this);
        timelord_shoulder_right.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_arm.addChild(timelord_shoulder_right);
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.0F, -0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.0F, -0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.0F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.0F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, 0.0F, -2.5F, 2.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, 0.0F, -2.5F, -3.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, 0.0F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.0F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -1.0F, 0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -1.0F, 0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, 0.0F, -0.5F, -3.25F, 1, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, -1.0F, -3.5F, -3.25F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, -1.0F, -3.75F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, -1.0F, -3.5F, 2.25F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, 0.0F, -0.5F, 2.25F, 1, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.25F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.25F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 68, 70, -2.25F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));

        left_arm = new RendererModel(this);
        left_arm.setRotationPoint(5.0F, 2.5F, 0.0F);
        left_arm.cubeList.add(new ModelBox(left_arm, 48, 48, -1.0F, -2.5F, -2.0F, 3, 12, 4, 0.0F, false));

        rist = new RendererModel(this);
        rist.setRotationPoint(-5.0F, 21.25F, 0.25F);
        left_arm.addChild(rist);
        rist.cubeList.add(new ModelBox(rist, 49, 73, 6.25F, -17.0F, -2.25F, 1, 3, 4, 0.0F, false));
        rist.cubeList.add(new ModelBox(rist, 49, 73, 4.0F, -17.0F, -2.5F, 3, 3, 1, 0.0F, false));
        rist.cubeList.add(new ModelBox(rist, 49, 73, 3.75F, -17.0F, -2.25F, 1, 3, 4, 0.0F, false));
        rist.cubeList.add(new ModelBox(rist, 49, 73, 4.0F, -17.0F, 1.0F, 3, 3, 1, 0.0F, false));

        timelord_shoulder_left = new RendererModel(this);
        timelord_shoulder_left.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_arm.addChild(timelord_shoulder_left);
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 0.0F, -0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 0.0F, -0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 1.0F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 1.0F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, -1.0F, -2.5F, 2.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, -1.0F, -2.5F, -3.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, -1.0F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 1.0F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, -1.0F, 0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, -1.0F, 0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 1.25F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 1.25F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 68, 70, 1.25F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 16, 71, -1.0F, -0.5F, -3.25F, 1, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 16, 71, 0.0F, -3.5F, -3.25F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 16, 71, 0.0F, -3.75F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 16, 71, 0.0F, -3.5F, 2.25F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 16, 71, -1.0F, -0.5F, 2.25F, 1, 1, 1, 0.0F, false));

        right_leg = new RendererModel(this);
        right_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        right_leg.cubeList.add(new ModelBox(right_leg, 36, 36, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

        shoes2 = new RendererModel(this);
        shoes2.setRotationPoint(1.9F, 11.5F, 0.5F);
        right_leg.addChild(shoes2);
        shoes2.cubeList.add(new ModelBox(shoes2, 0, 73, -3.9F, -0.5F, -3.25F, 4, 1, 1, 0.0F, true));
        shoes2.cubeList.add(new ModelBox(shoes2, 0, 73, -3.9F, -1.0F, -3.0F, 4, 1, 1, 0.0F, true));

        robes = new RendererModel(this);
        robes.setRotationPoint(-1.0F, 10.0F, -2.25F);
        right_leg.addChild(robes);
        robes.cubeList.add(new ModelBox(robes, 34, 73, 2.25F, -4.0F, 2.25F, 1, 3, 1, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 50, 71, 2.25F, -3.0F, 3.25F, 1, 2, 1, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 34, 73, 2.25F, -1.0F, 2.25F, 1, 1, 2, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 35, 73, -1.0F, -1.0F, 3.5F, 4, 1, 1, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 50, 73, -1.0F, -3.0F, 3.5F, 4, 2, 1, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 50, 71, -1.25F, -4.0F, 0.25F, 1, 3, 4, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 34, 73, -1.25F, -1.0F, 0.25F, 2, 1, 4, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 34, 73, -1.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 52, 73, -1.0F, -5.0F, 0.0F, 1, 4, 1, 0.0F, false));
        robes.cubeList.add(new ModelBox(robes, 35, 73, 0.0F, -6.0F, 0.0F, 1, 5, 1, 0.0F, false));

        left_leg = new RendererModel(this);
        left_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
        left_leg.cubeList.add(new ModelBox(left_leg, 20, 33, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

        shoes = new RendererModel(this);
        shoes.setRotationPoint(-1.9F, 12.0F, 0.25F);
        left_leg.addChild(shoes);
        shoes.cubeList.add(new ModelBox(shoes, 0, 73, -0.1F, -1.5F, -2.75F, 4, 1, 1, 0.0F, false));
        shoes.cubeList.add(new ModelBox(shoes, 0, 73, -0.1F, -1.0F, -3.0F, 4, 1, 1, 0.0F, false));

        robs = new RendererModel(this);
        robs.setRotationPoint(1.0F, 10.0F, -1.75F);
        left_leg.addChild(robs);
        robs.cubeList.add(new ModelBox(robs, 35, 73, -3.0F, -1.0F, 3.0F, 4, 1, 1, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 34, 73, -0.75F, -1.0F, -0.25F, 2, 1, 4, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 34, 73, -3.25F, -1.0F, 1.75F, 1, 1, 2, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 34, 73, -3.25F, -4.0F, 1.75F, 1, 3, 1, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 34, 73, -1.0F, -1.0F, -0.5F, 2, 1, 1, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 35, 73, -1.0F, -6.0F, -0.5F, 1, 5, 1, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 52, 73, 0.0F, -5.0F, -0.5F, 1, 4, 1, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 50, 71, 0.25F, -4.0F, -0.25F, 1, 3, 4, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 50, 71, -3.25F, -3.0F, 2.75F, 1, 2, 1, 0.0F, true));
        robs.cubeList.add(new ModelBox(robs, 50, 73, -3.0F, -3.0F, 3.0F, 4, 2, 1, 0.0F, true));

        nose = new RendererModel(this);
        nose.setRotationPoint(0.0F, 24.0F, 0.0F);
        nose.cubeList.add(new ModelBox(nose, 24, 15, -1.0F, -27.0F, -6.0F, 2, 4, 2, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
        body.render(f5);
        right_arm.render(f5);
        left_arm.render(f5);
        right_leg.render(f5);
        left_leg.render(f5);
        nose.render(f5);
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}