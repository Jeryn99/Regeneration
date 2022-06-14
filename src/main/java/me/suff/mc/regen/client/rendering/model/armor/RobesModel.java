package me.suff.mc.regen.client.rendering.model.armor;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class RobesModel extends HumanoidModel<LivingEntity> implements LivingArmor {

    private final ModelPart headPiece;
    private LivingEntity livingEntity = null;
    private EquipmentSlot slot = EquipmentSlot.HEAD;

    public RobesModel(ModelPart root, EquipmentSlot slot) {
        super(root);
        this.slot = slot;
        headPiece = root.getChild("collar");
    }

    public static LayerDefinition createBodyLayer(boolean isAlex) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition Body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(54, 16).addBox(-5.0F, -0.27F, 3.0F, 10.0F, 23.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("collar", CubeListBuilder.create().texOffs(0, 84).addBox(-7.5F, -24.275F, -2.5F, 15.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-7.5F, -36.275F, -2.5F, 15.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));


        if (isAlex) {
            PartDefinition RightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

            PartDefinition LeftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        } else {
            PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

            PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        }
        PartDefinition RightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 100, 100);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack matrixStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (slot == EquipmentSlot.HEAD) {
            matrixStack.pushPose();
            if (livingEntity.isShiftKeyDown()) {
                matrixStack.translate(0, 0.1, 0);
            }
            headPiece.render(matrixStack, buffer, packedLight, packedOverlay);
            matrixStack.popPose();
        }
        if (slot == EquipmentSlot.CHEST) {
            body.render(matrixStack, buffer, packedLight, packedOverlay);
            leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
            rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        }
        if (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
            rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
            leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        }
    }

    @Override
    public LivingEntity getLiving() {
        return livingEntity;
    }

    @Override
    public void setLiving(LivingEntity entity) {
        this.livingEntity = entity;
    }
}