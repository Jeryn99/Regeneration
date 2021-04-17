package me.suff.mc.regen.client.rendering.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

/* Created by Craig on 13/04/2021 */
public class RegenerationConeModel extends BipedModel< LivingEntity > {

    public final ModelRenderer Head;
    public final ModelRenderer RightArm;
    public final ModelRenderer LeftArm;
    private final ModelRenderer Head_r1;
    private final ModelRenderer Head_r2;
    private final ModelRenderer Head_r3;
    private final ModelRenderer Head_r4;
    private final ModelRenderer Head_r5;
    private final ModelRenderer Head_r6;
    private final ModelRenderer Head_r7;
    private final ModelRenderer Head_r8;
    private final ModelRenderer LeftArm_r1;
    private final ModelRenderer LeftArm_r2;
    private final ModelRenderer LeftArm_r3;
    private final ModelRenderer LeftArm_r4;
    private final ModelRenderer LeftArm_r5;
    private final ModelRenderer LeftArm_r6;
    private final ModelRenderer LeftArm_r7;
    private final ModelRenderer LeftArm_r8;
    private final ModelRenderer LeftArm_r9;
    private final ModelRenderer LeftArm_r10;
    private final ModelRenderer LeftArm_r11;
    private final ModelRenderer LeftArm_r12;
    private final ModelRenderer LeftArm_r13;
    private final ModelRenderer LeftArm_r14;
    private final ModelRenderer LeftArm_r15;
    private final ModelRenderer LeftArm_r16;

    public RegenerationConeModel() {
        super(0);
        texWidth = 128;
        texHeight = 128;

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, 0.0F, 0.0F);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(0.0F, 0.0F, 4.0F);
        Head.addChild(Head_r1);
        setRotationAngle(Head_r1, -0.1222F, 0.0F, 0.0F);
        Head_r1.texOffs(38, 17).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, 0.0F, false);

        Head_r2 = new ModelRenderer(this);
        Head_r2.setPos(-4.0F, 0.0F, 0.0F);
        Head.addChild(Head_r2);
        setRotationAngle(Head_r2, 0.0F, 0.0F, -0.1222F);
        Head_r2.texOffs(13, 17).addBox(0.0F, -16.0F, -6.0F, 0.0F, 16.0F, 12.0F, 0.0F, false);

        Head_r3 = new ModelRenderer(this);
        Head_r3.setPos(-4.0F, 0.0F, 0.0F);
        Head.addChild(Head_r3);
        setRotationAngle(Head_r3, 0.0F, 0.0F, -0.1571F);
        Head_r3.texOffs(0, 0).addBox(-2.0F, -16.0F, -6.0F, 0.0F, 16.0F, 12.0F, 0.0F, false);

        Head_r4 = new ModelRenderer(this);
        Head_r4.setPos(4.0F, 0.0F, 0.0F);
        Head.addChild(Head_r4);
        setRotationAngle(Head_r4, 0.0F, 0.0F, 0.1571F);
        Head_r4.texOffs(0, 0).addBox(2.0F, -16.0F, -6.0F, 0.0F, 16.0F, 12.0F, 0.0F, false);

        Head_r5 = new ModelRenderer(this);
        Head_r5.setPos(4.0F, 0.0F, 0.0F);
        Head.addChild(Head_r5);
        setRotationAngle(Head_r5, 0.0F, 0.0F, 0.1222F);
        Head_r5.texOffs(13, 17).addBox(0.0F, -16.0F, -6.0F, 0.0F, 16.0F, 12.0F, 0.0F, false);

        Head_r6 = new ModelRenderer(this);
        Head_r6.setPos(0.0F, 0.0F, 4.0F);
        Head.addChild(Head_r6);
        setRotationAngle(Head_r6, -0.1571F, 0.0F, 0.0F);
        Head_r6.texOffs(25, 0).addBox(-6.0F, -16.0F, 2.0F, 12.0F, 16.0F, 0.0F, 0.0F, false);

        Head_r7 = new ModelRenderer(this);
        Head_r7.setPos(0.0F, 0.0F, -4.0F);
        Head.addChild(Head_r7);
        setRotationAngle(Head_r7, 0.1571F, 0.0F, 0.0F);
        Head_r7.texOffs(25, 0).addBox(-6.0F, -16.0F, -2.0F, 12.0F, 16.0F, 0.0F, 0.0F, false);

        Head_r8 = new ModelRenderer(this);
        Head_r8.setPos(0.0F, 0.0F, -4.0F);
        Head.addChild(Head_r8);
        setRotationAngle(Head_r8, 0.1222F, 0.0F, 0.0F);
        Head_r8.texOffs(38, 17).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, 0.0F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);


        LeftArm_r1 = new ModelRenderer(this);
        LeftArm_r1.setPos(-1.0F, 10.0F, 2.0F);
        RightArm.addChild(LeftArm_r1);
        setRotationAngle(LeftArm_r1, 0.192F, 0.0F, 0.0F);
        LeftArm_r1.texOffs(50, 0).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r2 = new ModelRenderer(this);
        LeftArm_r2.setPos(-1.0F, 10.0F, 2.0F);
        RightArm.addChild(LeftArm_r2);
        setRotationAngle(LeftArm_r2, 0.1571F, 0.0F, 0.0F);
        LeftArm_r2.texOffs(47, 55).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r3 = new ModelRenderer(this);
        LeftArm_r3.setPos(-1.0F, 10.0F, -2.0F);
        RightArm.addChild(LeftArm_r3);
        setRotationAngle(LeftArm_r3, -0.192F, 0.0F, 0.0F);
        LeftArm_r3.texOffs(50, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r4 = new ModelRenderer(this);
        LeftArm_r4.setPos(-1.0F, 10.0F, -2.0F);
        RightArm.addChild(LeftArm_r4);
        setRotationAngle(LeftArm_r4, -0.1571F, 0.0F, 0.0F);
        LeftArm_r4.texOffs(47, 55).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r5 = new ModelRenderer(this);
        LeftArm_r5.setPos(1.0F, 10.0F, 0.0F);
        RightArm.addChild(LeftArm_r5);
        setRotationAngle(LeftArm_r5, 0.0F, 0.0F, -0.192F);
        LeftArm_r5.texOffs(0, 38).addBox(2.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm_r6 = new ModelRenderer(this);
        LeftArm_r6.setPos(1.0F, 10.0F, 0.0F);
        RightArm.addChild(LeftArm_r6);
        setRotationAngle(LeftArm_r6, 0.0F, 0.0F, -0.1571F);
        LeftArm_r6.texOffs(30, 38).addBox(0.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm_r7 = new ModelRenderer(this);
        LeftArm_r7.setPos(-3.0F, 10.0F, 0.0F);
        RightArm.addChild(LeftArm_r7);
        setRotationAngle(LeftArm_r7, 0.0F, 0.0F, 0.192F);
        LeftArm_r7.texOffs(0, 38).addBox(-2.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm_r8 = new ModelRenderer(this);
        LeftArm_r8.setPos(-3.0F, 10.0F, 0.0F);
        RightArm.addChild(LeftArm_r8);
        setRotationAngle(LeftArm_r8, 0.0F, 0.0F, 0.1571F);
        LeftArm_r8.texOffs(30, 38).addBox(0.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);


        LeftArm_r9 = new ModelRenderer(this);
        LeftArm_r9.setPos(1.0F, 10.0F, 2.0F);
        LeftArm.addChild(LeftArm_r9);
        setRotationAngle(LeftArm_r9, 0.192F, 0.0F, 0.0F);
        LeftArm_r9.texOffs(0, 59).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r10 = new ModelRenderer(this);
        LeftArm_r10.setPos(1.0F, 10.0F, 2.0F);
        LeftArm.addChild(LeftArm_r10);
        setRotationAngle(LeftArm_r10, 0.1571F, 0.0F, 0.0F);
        LeftArm_r10.texOffs(47, 55).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r11 = new ModelRenderer(this);
        LeftArm_r11.setPos(1.0F, 10.0F, -2.0F);
        LeftArm.addChild(LeftArm_r11);
        setRotationAngle(LeftArm_r11, -0.192F, 0.0F, 0.0F);
        LeftArm_r11.texOffs(0, 59).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r12 = new ModelRenderer(this);
        LeftArm_r12.setPos(1.0F, 10.0F, -2.0F);
        LeftArm.addChild(LeftArm_r12);
        setRotationAngle(LeftArm_r12, -0.1571F, 0.0F, 0.0F);
        LeftArm_r12.texOffs(47, 55).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);

        LeftArm_r13 = new ModelRenderer(this);
        LeftArm_r13.setPos(-1.0F, 10.0F, 0.0F);
        LeftArm.addChild(LeftArm_r13);
        setRotationAngle(LeftArm_r13, 0.0F, 0.0F, 0.192F);
        LeftArm_r13.texOffs(47, 34).addBox(-2.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm_r14 = new ModelRenderer(this);
        LeftArm_r14.setPos(-1.0F, 10.0F, 0.0F);
        LeftArm.addChild(LeftArm_r14);
        setRotationAngle(LeftArm_r14, 0.0F, 0.0F, 0.1571F);
        LeftArm_r14.texOffs(30, 38).addBox(0.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm_r15 = new ModelRenderer(this);
        LeftArm_r15.setPos(3.0F, 10.0F, 0.0F);
        LeftArm.addChild(LeftArm_r15);
        setRotationAngle(LeftArm_r15, 0.0F, 0.0F, -0.192F);
        LeftArm_r15.texOffs(47, 34).addBox(2.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);

        LeftArm_r16 = new ModelRenderer(this);
        LeftArm_r16.setPos(3.0F, 10.0F, 0.0F);
        LeftArm.addChild(LeftArm_r16);
        setRotationAngle(LeftArm_r16, 0.0F, 0.0F, -0.1571F);
        LeftArm_r16.texOffs(30, 38).addBox(0.0F, 0.0F, -4.0F, 0.0F, 12.0F, 8.0F, 0.0F, false);
        head = Head;
        leftArm = LeftArm;
        rightArm = RightArm;
    }

    @Override
    public void setupAnim(LivingEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(matrixStack, buffer, packedLight, packedOverlay);
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void renderHead(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay) {
        Head.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void renderRightArm(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay) {
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void renderLeftArm(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay) {
        LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
