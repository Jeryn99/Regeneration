package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.IRegen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public interface TransitionRenderer {

    void onPlayerRenderPre(RenderPlayerEvent.Pre pre);

    void onPlayerRenderPost(RenderPlayerEvent.Post post);

    void firstPersonHand(RenderHandEvent renderHandEvent);

    void thirdPersonHand(HandSide side, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    void layer(BipedModel<?> bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    void animation(BipedModel<?> bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    boolean isLaying(IRegen data);

}
