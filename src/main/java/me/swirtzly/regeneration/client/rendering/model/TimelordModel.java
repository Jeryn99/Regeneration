package me.swirtzly.regeneration.client.rendering.model;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.client.RenderUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class TimelordModel extends BipedModel<TimelordEntity> {

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

    public TimelordModel() {
        textureWidth = 80;
        textureHeight = 80;

        head = new RendererModel(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 16, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F, false));

        timelord_hat = new RendererModel(this);
        timelord_hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(timelord_hat);
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, 3.25F, -8.0F, -4.0F, 1, 3, 8, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.25F, -8.0F, -4.0F, 1, 3, 8, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, 3.25F, -5.0F, -3.0F, 1, 1, 7, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.25F, -5.0F, -3.0F, 1, 1, 7, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, 3.25F, -4.0F, 1.0F, 1, 2, 3, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.25F, -4.0F, 1.0F, 1, 2, 3, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.0F, -8.0F, 3.25F, 8, 6, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, 3.25F, -4.0F, -2.5F, 1, 2, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.25F, -4.0F, -2.5F, 1, 2, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, 3.0F, -8.0F, -4.25F, 1, 3, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.0F, -8.0F, -4.25F, 1, 3, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -2.5F, -7.5F, -4.25F, 5, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -1.5F, -7.0F, -4.25F, 3, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -1.0F, -6.5F, -4.25F, 2, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.0F, -8.0F, -4.25F, 8, 1, 1, 0.0F, false));
        timelord_hat.cubeList.add(new ModelBox(timelord_hat, 62, 69, -4.0F, -8.25F, -4.0F, 8, 1, 8, 0.0F, false));

        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 32, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

        timelordcape = new RendererModel(this);
        timelordcape.setRotationPoint(0.0F, 4.0F, 2.5F);
        body.addChild(timelordcape);
        timelordcape.cubeList.add(new ModelBox(timelordcape, 0, 32, -5.0F, 0.0F, 0.0F, 10, 19, 0, 0.0F, false));

        timelord_body_armor = new RendererModel(this);
        timelord_body_armor.setRotationPoint(0.0F, 4.0F, 2.0F);
        body.addChild(timelord_body_armor);
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -3.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -3.0F, -2.0F, 0.0F, 6, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -4.0F, -4.0F, 0.0F, 8, 2, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -4.0F, -3.5F, 1.5F, 8, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -4.0F, -3.25F, 1.0F, 8, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, 3.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -4.0F, 0.0F, -5.0F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, 2.0F, 0.0F, -5.0F, 2, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, 1.0F, -1.0F, -5.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -2.0F, -1.0F, -5.0F, 1, 1, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 32, 36, -1.0F, -2.5F, -4.75F, 2, 1, 0, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -4.0F, -4.0F, -5.0F, 2, 3, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, 2.0F, -4.0F, -5.0F, 2, 3, 1, 0.0F, false));
        timelord_body_armor.cubeList.add(new ModelBox(timelord_body_armor, 62, 62, -4.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
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
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.0F, -0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.0F, -0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.0F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.0F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, 0.0F, -2.5F, 2.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, 0.0F, -2.5F, -3.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, 0.0F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.0F, -3.5F, -2.0F, 1, 1, 4, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -1.0F, 0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -1.0F, 0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, 0.0F, -0.5F, -3.25F, 1, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, -1.0F, -3.5F, -3.25F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, -1.0F, -3.75F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, -1.0F, -3.5F, 2.25F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 16, 71, 0.0F, -0.5F, 2.25F, 1, 1, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.25F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.25F, -3.5F, -2.0F, 1, 1, 4, 0.0F, false));
        timelord_shoulder_right.cubeList.add(new ModelBox(timelord_shoulder_right, 62, 62, -2.25F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));

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
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 0.0F, -0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 0.0F, -0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 1.0F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 1.0F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, -1.0F, -2.5F, 2.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, -1.0F, -2.5F, -3.0F, 1, 2, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, -1.0F, -3.5F, -3.0F, 1, 1, 6, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 1.0F, -3.5F, -2.0F, 1, 1, 4, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, -1.0F, 0.5F, -3.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, -1.0F, 0.5F, 2.0F, 2, 1, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 1.25F, -3.5F, -3.0F, 1, 3, 1, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 1.25F, -3.5F, -2.0F, 1, 1, 4, 0.0F, false));
        timelord_shoulder_left.cubeList.add(new ModelBox(timelord_shoulder_left, 62, 62, 1.25F, -3.5F, 2.0F, 1, 3, 1, 0.0F, false));
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


    }

    @Override
    public void render(TimelordEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GlStateManager.pushMatrix();
        head.render(f5);
        body.render(f5);
        right_arm.render(f5);
        left_arm.render(f5);
        right_leg.render(f5);
        left_leg.render(f5);
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(TimelordEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        RegenCap.get(entityIn).ifPresent((data) -> {
            if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                double animationProgress = data.getAnimationTicks();
                double arm_shake = entityIn.getRNG().nextDouble();
                float armRotY = (float) animationProgress * 1.5F;
                float armRotZ = (float) animationProgress * 1.5F;
                float headRot = (float) animationProgress * 1.5F;

                if (armRotY > 90) {
                    armRotY = 90;
                }

                if (armRotZ > 95) {
                    armRotZ = 95;
                }

                if (headRot > 45) {
                    headRot = 45;
                }

                // ARMS
                bipedLeftArm.rotateAngleY = 0;
                bipedRightArm.rotateAngleY = 0;

                bipedLeftArm.rotateAngleX = 0;
                bipedRightArm.rotateAngleX = 0;

                bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRotZ + arm_shake);
                bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRotZ + arm_shake);
                bipedLeftArm.rotateAngleY = (float) -Math.toRadians(armRotY);
                bipedRightArm.rotateAngleY = (float) Math.toRadians(armRotY);

                // bipedBody
                bipedBody.rotateAngleX = 0;
                bipedBody.rotateAngleY = 0;
                bipedBody.rotateAngleZ = 0;

                // LEGS
                bipedLeftLeg.rotateAngleY = 0;
                bipedRightLeg.rotateAngleY = 0;

                bipedLeftLeg.rotateAngleX = 0;
                bipedRightLeg.rotateAngleX = 0;

                bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
                bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);


                bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);
                bipedHead.rotateAngleY = (float) Math.toRadians(0);
                bipedHead.rotateAngleZ = (float) Math.toRadians(0);
            }
        });

        RenderUtil.copyModelAngles(bipedHead, head);
        RenderUtil.copyModelAngles(bipedBody, body);
        RenderUtil.copyModelAngles(bipedLeftArm, left_arm);
        RenderUtil.copyModelAngles(bipedRightArm, right_arm);
        RenderUtil.copyModelAngles(bipedRightLeg, right_leg);
        RenderUtil.copyModelAngles(bipedLeftLeg, left_leg);
    }

    private float func_203068_a(float p_203068_1_) {
        return -65.0F * p_203068_1_ + p_203068_1_ * p_203068_1_;
    }

    protected float func_205060_a(float p_205060_1_, float p_205060_2_, float p_205060_3_) {
        float f = (p_205060_2_ - p_205060_1_) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI) {
            f += ((float) Math.PI * 2F);
        }

        if (f >= (float) Math.PI) {
            f -= ((float) Math.PI * 2F);
        }

        return p_205060_1_ + p_205060_3_ * f;
    }

    public void setLivingAnimations(TimelordEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.swimAnimation = entityIn.getSwimAnimation(partialTick);
        // this.remainingItemUseTime = (float) entityIn.getItemInUseMaxCount();
        //  super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}