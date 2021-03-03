package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import java.util.Random;

public class EnderDragonTransitionRenderer implements TransitionRenderer {
    public static final EnderDragonTransitionRenderer INSTANCE = new EnderDragonTransitionRenderer();
    //Taken from vanilla
    private static float field_229057_l_ = (float) (Math.sqrt(3.0D) / 2.0D);

    private static void func_229061_a_(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void func_229060_a_(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
        p_229060_0_.pos(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void func_229062_b_(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
        p_229062_0_.pos(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void func_229063_c_(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
        p_229063_0_.pos(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(255, 0, 255, 0).endVertex();
    }

    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {

    }

    @Override
    public void onPlayerRenderPost(RenderPlayerEvent.Post post) {

    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HandSide side, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(BipedModel< ? > bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        MatrixStack matrix = matrixStackIn;
        ;

        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            int ticksAnimating = iRegen.getTicksAnimating() / 2;
            if (ticksAnimating > 0 && iRegen.getCurrentState() == RegenStates.REGENERATING) {
                float f5 = ((float) ticksAnimating + Minecraft.getInstance().getRenderPartialTicks()) / 200.0F;
                float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
                Random random = new Random(432L);
                IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderType.getLightning());
                matrix.push();
                bipedModel.bipedBody.translateRotate(matrix);
                matrix.translate(0.0D, 0.5D, 0);

                for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
                    matrix.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
                    float randomFloat = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                    float randomFloat2 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                    Matrix4f matrix4f = matrix.getLast().getMatrix();
                    int j = (int) (255.0F * (1.0F - f7));
                    func_229061_a_(vertexBuilder, matrix4f, j);
                    func_229060_a_(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    func_229062_b_(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    func_229061_a_(vertexBuilder, matrix4f, j);
                    func_229062_b_(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    func_229063_c_(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    func_229061_a_(vertexBuilder, matrix4f, j);
                    func_229063_c_(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    func_229060_a_(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                }
                matrix.pop();
            }
        });
    }

    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                float armRotY = (float) iRegen.getTicksAnimating() * 2.5F;
                float armRotZ = (float) iRegen.getTicksAnimating() * 2.5F;
                float headRot = (float) iRegen.getTicksAnimating() * 2.5F;

                if (armRotY > 120) {
                    armRotY = 120;
                }

                if (armRotZ > 120) {
                    armRotZ = 120;
                }

                if (headRot > 45) {
                    headRot = 45;
                }


                // ARMS
                bipedModel.bipedLeftArm.rotateAngleY = 0;
                bipedModel.bipedRightArm.rotateAngleY = 0;

                bipedModel.bipedLeftArm.rotateAngleX = 0;
                bipedModel.bipedRightArm.rotateAngleX = 0;

                bipedModel.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRotZ);
                bipedModel.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRotZ);
                bipedModel.bipedLeftArm.rotateAngleY = (float) -Math.toRadians(armRotY);
                bipedModel.bipedRightArm.rotateAngleY = (float) Math.toRadians(armRotY);

                // BODY
                bipedModel.bipedBody.rotateAngleX = 0;
                bipedModel.bipedBody.rotateAngleY = 0;
                bipedModel.bipedBody.rotateAngleZ = 0;

                // LEGS
                bipedModel.bipedLeftLeg.rotateAngleY = 0;
                bipedModel.bipedRightLeg.rotateAngleY = 0;

                bipedModel.bipedLeftLeg.rotateAngleX = 0;
                bipedModel.bipedRightLeg.rotateAngleX = 0;

                bipedModel.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
                bipedModel.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

                bipedModel.bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);
                bipedModel.bipedHead.rotateAngleY = (float) Math.toRadians(0);
                bipedModel.bipedHead.rotateAngleZ = (float) Math.toRadians(0);
            }
        });
    }
}
