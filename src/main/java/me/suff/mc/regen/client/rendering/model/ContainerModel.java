package me.suff.mc.regen.client.rendering.model;// Made with Blockbench 4.0.0-beta.0
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ContainerModel extends EntityModel {
    public static ResourceLocation CONTAINER_TEXTURE = new ResourceLocation(RConstants.MODID, "textures/tile/container.png");
    public final ModelPart lid;
    private final ModelPart jar;

    public ContainerModel(ModelPart root) {
        this.lid = root.getChild("lid");
        this.jar = root.getChild("jar");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -8.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(0, 22).addBox(-4.0F, -2.0F, -8.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 4.0F));

        PartDefinition jar = partdefinition.addOrReplaceChild("jar", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -13.0F, 4.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(17, 43).addBox(-3.0F, -12.0F, 3.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(35, 47).addBox(-2.5F, -11.75F, -3.5F, 5.0F, 10.0F, 7.0F, new CubeDeformation(-0.2F))
                .texOffs(26, 26).addBox(-2.5F, -13.0F, -3.0F, 5.0F, 11.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-3.0F, -12.0F, -4.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -10.0F, -5.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 14).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        lid.render(poseStack, buffer, packedLight, packedOverlay);
        jar.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(Entity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

    }
}