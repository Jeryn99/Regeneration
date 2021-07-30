package me.suff.mc.regen.client.rendering.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class SteveArmModel extends EntityModel<Entity> {
    private final ModelPart RightArm;

    public SteveArmModel() {
        texWidth = 64;
        texHeight = 64;

        RightArm = new ModelPart(this);
        RightArm.setPos(-3.0F, 2.0F, -3.0F);
        RightArm.texOffs(40, 16).addBox(0.5F, 10.0F, 0.5F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightArm.texOffs(40, 32).addBox(0.5F, 10.0F, 0.5F, 4.0F, 12.0F, 4.0F, 0.25F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}