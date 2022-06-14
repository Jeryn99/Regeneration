package me.suff.mc.regen.client.rendering.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ArmModel extends EntityModel<Entity> {
    private final ModelPart mainArm;

    public ArmModel(ModelPart root) {
        this.mainArm = root.getChild("mainArm");
    }

    public static LayerDefinition createMesh(boolean isAlex) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        if (isAlex) {
            partdefinition.addOrReplaceChild("mainArm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 32).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.5F, 18.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
            return LayerDefinition.create(meshdefinition, 64, 64);
        }
        partdefinition.addOrReplaceChild("mainArm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 32).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 18.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack matrixStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        mainArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}