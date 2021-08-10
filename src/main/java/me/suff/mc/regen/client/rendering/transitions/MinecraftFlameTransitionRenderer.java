package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.rendering.model.RegenerationConeModel;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class MinecraftFlameTransitionRenderer extends FieryTransitionRenderer {

    public static MinecraftFlameTransitionRenderer INSTANCE = new MinecraftFlameTransitionRenderer();
    public int timer = 0;
    RegenerationConeModel regenerationConeModel = new RegenerationConeModel();

    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.animate(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
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
    public void layer(BipedModel<?> bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entitylivingbaseIn;
            RegenCap.get(livingEntity).ifPresent(iRegen -> {

                if (iRegen.regenState() == RegenStates.REGENERATING) {
                    if (entitylivingbaseIn.tickCount % 5 == 0) {
                        timer++;
                        if (timer > 3) {
                            timer = 0;
                        }
                    }
                    //Hand
                    for (HandSide side : HandSide.values()) {
                        matrixStackIn.pushPose();
                        ModelRenderer modelArm = side == HandSide.LEFT ? regenerationConeModel.getLeftArm() : regenerationConeModel.getRightArm();
                        bipedModel.translateToHand(side, matrixStackIn);
                        matrixStackIn.translate(side == HandSide.LEFT ? -0.3 : 0.3, -0.2, 0);
                        modelArm.render(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(new ResourceLocation(RConstants.MODID, "textures/entity/regen_cone/regen_cone_" + timer + ".png"))), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.5F);
                        matrixStackIn.popPose();
                    }


                    //Head
                    matrixStackIn.pushPose();
                    bipedModel.head.translateAndRotate(matrixStackIn);
                    regenerationConeModel.getHead().render(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(new ResourceLocation(RConstants.MODID, "textures/entity/regen_cone/regen_cone_" + timer + ".png"))), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.5F);
                    matrixStackIn.popPose();

                }

            });
        }
    }
}
