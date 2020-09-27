package me.swirtzly.regen.client.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.IRegen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderHandEvent;

public class LayFadeTransitionRenderer implements TransitionRenderer{

    public static final LayFadeTransitionRenderer INSTANCE = new LayFadeTransitionRenderer();


    @Override
    public void firstPersonHand(IRegen iRegen, RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void animation(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
