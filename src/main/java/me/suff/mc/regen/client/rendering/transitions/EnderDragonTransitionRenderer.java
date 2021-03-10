package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
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
    private static float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

    private static void vertex01(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void vertex2(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
        p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
        p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
        p_229063_0_.vertex(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(255, 0, 255, 0).endVertex();
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
                float f5 = ((float) ticksAnimating + Minecraft.getInstance().getFrameTime()) / 200.0F;
                float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
                Random random = new Random(432L);
                IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderType.lightning());
                matrix.pushPose();
                bipedModel.body.translateAndRotate(matrix);
                matrix.translate(0.0D, 0.5D, 0);

                for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
                    matrix.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                    matrix.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
                    float randomFloat = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                    float randomFloat2 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                    Matrix4f matrix4f = matrix.last().pose();
                    int j = (int) (255.0F * (1.0F - f7));
                    vertex01(vertexBuilder, matrix4f, j);
                    vertex2(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    vertex3(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    vertex01(vertexBuilder, matrix4f, j);
                    vertex3(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    vertex4(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    vertex01(vertexBuilder, matrix4f, j);
                    vertex4(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                    vertex2(vertexBuilder, matrix4f, randomFloat, randomFloat2);
                }
                matrix.popPose();
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
                bipedModel.leftArm.yRot = 0;
                bipedModel.rightArm.yRot = 0;

                bipedModel.leftArm.xRot = 0;
                bipedModel.rightArm.xRot = 0;

                bipedModel.leftArm.zRot = (float) -Math.toRadians(armRotZ);
                bipedModel.rightArm.zRot = (float) Math.toRadians(armRotZ);
                bipedModel.leftArm.yRot = (float) -Math.toRadians(armRotY);
                bipedModel.rightArm.yRot = (float) Math.toRadians(armRotY);

                // BODY
                bipedModel.body.xRot = 0;
                bipedModel.body.yRot = 0;
                bipedModel.body.zRot = 0;

                // LEGS
                bipedModel.leftLeg.yRot = 0;
                bipedModel.rightLeg.yRot = 0;

                bipedModel.leftLeg.xRot = 0;
                bipedModel.rightLeg.xRot = 0;

                bipedModel.leftLeg.zRot = (float) -Math.toRadians(5);
                bipedModel.rightLeg.zRot = (float) Math.toRadians(5);

                bipedModel.head.xRot = (float) Math.toRadians(-headRot);
                bipedModel.head.yRot = (float) Math.toRadians(0);
                bipedModel.head.zRot = (float) Math.toRadians(0);
            }
        });
    }
}
