package craig.software.mc.regen.client.rendering.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import craig.software.mc.regen.common.blockentity.BioContainerBlockEntity;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ContainerModel extends HierarchicalModel {


    public static final AnimationDefinition CLOSE = AnimationDefinition.Builder.withLength(0.5416666666666666f).addAnimation("lid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("lid", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(-52.5f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("lid", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe(0.5416666666666666f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.LINEAR))).build();
    public static final AnimationDefinition OPEN = AnimationDefinition.Builder.withLength(0.5416666666666666f).addAnimation("lid", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("lid", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(-52.5f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("lid", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.LINEAR))).build();
    public static ResourceLocation CONTAINER_TEXTURE = new ResourceLocation(RConstants.MODID, "textures/tile/container.png");
    public final ModelPart lid;
    private final ModelPart jar;
    private final ModelPart root;

    public ContainerModel(ModelPart root) {
        this.root = root;
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
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        lid.render(poseStack, buffer, packedLight, packedOverlay);
        jar.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(@NotNull Entity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    public void animate(BioContainerBlockEntity containerBlock){
        this.root().getAllParts().forEach(ModelPart::resetPose);
        animate(containerBlock.getOpenState(), CLOSE, Minecraft.getInstance().player.tickCount);
        animate(containerBlock.getCloseState(), OPEN, Minecraft.getInstance().player.tickCount);
    }
}