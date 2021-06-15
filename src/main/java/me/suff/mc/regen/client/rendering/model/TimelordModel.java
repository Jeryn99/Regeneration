package me.suff.mc.regen.client.rendering.model;

import me.suff.mc.regen.common.capability.RegenCap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;

public class TimelordModel extends BipedModel {

    private final RendererModel Head;
    private final RendererModel Body;
    private final RendererModel RightArm;
    private final RendererModel LeftArm;
    private final RendererModel RightLeg;
    private final RendererModel LeftLeg;
    private final RendererModel bb_main;

    public TimelordModel() {
        texWidth = 100;
        texHeight = 100;

        Head = new RendererModel(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        Head.cubes.add(new ModelBox(Head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        Head.cubes.add(new ModelBox(Head, 32, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F, false));

        Body = new RendererModel(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.cubes.add(new ModelBox(Body, 54, 16, -5.0F, -0.25F, 3.0F, 10, 23, 0, 0.0F, false));
        Body.cubes.add(new ModelBox(Body, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));
        Body.cubes.add(new ModelBox(Body, 16, 32, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F, false));

        RightArm = new RendererModel(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);
        RightArm.cubes.add(new ModelBox(RightArm, 40, 16, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F, false));
        RightArm.cubes.add(new ModelBox(RightArm, 40, 32, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        LeftArm = new RendererModel(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);
        LeftArm.cubes.add(new ModelBox(LeftArm, 32, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F, false));
        LeftArm.cubes.add(new ModelBox(LeftArm, 48, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        RightLeg = new RendererModel(this);
        RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        RightLeg.cubes.add(new ModelBox(RightLeg, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        RightLeg.cubes.add(new ModelBox(RightLeg, 0, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        LeftLeg = new RendererModel(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.cubes.add(new ModelBox(LeftLeg, 16, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        LeftLeg.cubes.add(new ModelBox(LeftLeg, 0, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        bb_main = new RendererModel(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.cubes.add(new ModelBox(bb_main, 0, 84, -7.5F, -24.275F, -2.5F, 15, 3, 5, 0.0F, false));
        bb_main.cubes.add(new ModelBox(bb_main, 0, 64, -7.5F, -36.275F, -2.5F, 15, 12, 8, 0.0F, false));

        head = Head;
        body = Body;
        leftArm = LeftArm;
        rightArm = RightArm;
        leftLeg = LeftLeg;
        rightLeg = RightLeg;
    }

    @Override
    public void render(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        bb_main.render(scale);
        head.visible = false;
        hat.visible = false;
    }

    @Override
    public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        RegenCap.get(entityIn).ifPresent((data) -> data.getRegenType().create().getRenderer().animateEntity(this, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor));
    }

    @Override
    protected RendererModel getArm(HandSide side) {
        return side == HandSide.LEFT ? this.leftArm : this.rightArm;
    }
}