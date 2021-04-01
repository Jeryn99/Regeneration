package me.suff.mc.regen.client.rendering.model;

import me.suff.mc.regen.common.entities.OmegaEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class OmegaModel extends BipedModel< OmegaEntity > {
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer LeftLeg;
    private final ModelRenderer Body;
    private final ModelRenderer Cape;
    private final ModelRenderer Head;
    private final ModelRenderer Mask;
    private final ModelRenderer TuskRight_r1;
    private final ModelRenderer TuskLeft_r1;
    private final ModelRenderer CheekRight_r1;
    private final ModelRenderer CheekLeft_r1;
    private final ModelRenderer Horn_r1;
    private final ModelRenderer RightLeg;


    public OmegaModel() {
        super(0);
        texHeight = 128;
        texWidth = 128;

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, 2.5F, 0.0F);
        RightArm.texOffs(55, 31).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        RightArm.texOffs(28, 55).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.2F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, 2.5F, 0.0F);
        LeftArm.texOffs(13, 55).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        LeftArm.texOffs(51, 51).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.2F, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.texOffs(38, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        LeftLeg.texOffs(21, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.texOffs(25, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        Body.texOffs(0, 25).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);

        Cape = new ModelRenderer(this);
        Cape.setPos(0.0F, 2.75F, 2.25F);
        Body.addChild(Cape);
        setRotationAngle(Cape, 0.0873F, 0.0F, 0.0F);
        Cape.texOffs(0, 0).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 21.0F, 0.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        Head.texOffs(29, 0).addBox(-3.5F, -7.0F, -2.5F, 7.0F, 7.0F, 6.0F, 0.0F, false);

        Mask = new ModelRenderer(this);
        Mask.setPos(0.0F, -7.0F, -2.5F);
        Head.addChild(Mask);
        Mask.texOffs(56, 0).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 7.0F, 1.0F, 0.0F, false);
        Mask.texOffs(29, 14).addBox(-2.5F, -4.0F, -1.0F, 5.0F, 4.0F, 0.0F, 0.0F, false);

        TuskRight_r1 = new ModelRenderer(this);
        TuskRight_r1.setPos(-2.5F, 4.0F, -1.0F);
        Mask.addChild(TuskRight_r1);
        setRotationAngle(TuskRight_r1, 0.0F, -0.1745F, 0.0F);
        TuskRight_r1.texOffs(0, 59).addBox(-3.0F, -12.0F, 0.0F, 4.0F, 12.0F, 0.0F, 0.0F, false);

        TuskLeft_r1 = new ModelRenderer(this);
        TuskLeft_r1.setPos(2.5F, 4.0F, -1.0F);
        Mask.addChild(TuskLeft_r1);
        setRotationAngle(TuskLeft_r1, 0.0F, 0.1745F, 0.0F);
        TuskLeft_r1.texOffs(66, 48).addBox(-1.0F, -12.0F, 0.0F, 4.0F, 12.0F, 0.0F, 0.0F, false);

        CheekRight_r1 = new ModelRenderer(this);
        CheekRight_r1.setPos(-1.5F, 7.0F, -1.0F);
        Mask.addChild(CheekRight_r1);
        setRotationAngle(CheekRight_r1, 0.0F, -0.2618F, 0.0F);
        CheekRight_r1.texOffs(40, 14).addBox(-3.0F, -3.0F, 0.0F, 3.0F, 4.0F, 0.0F, 0.0F, false);

        CheekLeft_r1 = new ModelRenderer(this);
        CheekLeft_r1.setPos(1.5F, 7.0F, -1.0F);
        Mask.addChild(CheekLeft_r1);
        setRotationAngle(CheekLeft_r1, 0.0F, 0.2618F, 0.0F);
        CheekLeft_r1.texOffs(56, 9).addBox(0.0F, -3.0F, 0.0F, 3.0F, 4.0F, 0.0F, 0.0F, false);

        Horn_r1 = new ModelRenderer(this);
        Horn_r1.setPos(0.0F, 3.0F, -1.0F);
        Mask.addChild(Horn_r1);
        setRotationAngle(Horn_r1, 0.0873F, 0.0F, 0.0F);
        Horn_r1.texOffs(43, 55).addBox(-0.5F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        RightLeg.texOffs(50, 14).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightLeg.texOffs(0, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);

        rightArm = RightArm;
        leftArm = LeftArm;
        leftLeg = LeftLeg;
        body = Body;
        head = Head;
        rightLeg = RightLeg;

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}