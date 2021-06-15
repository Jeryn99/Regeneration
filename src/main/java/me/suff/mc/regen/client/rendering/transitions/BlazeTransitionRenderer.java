package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

/* Created by Craig on 31/01/2021 */
public class BlazeTransitionRenderer implements TransitionRenderer {

    public static final BlazeTransitionRenderer INSTANCE = new BlazeTransitionRenderer();


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
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                if (entitylivingbaseIn.level.random.nextInt(24) == 0) {
                    entitylivingbaseIn.level.playLocalSound(entitylivingbaseIn.getX() + 0.5D, entitylivingbaseIn.getY() + 0.5D, entitylivingbaseIn.getZ() + 0.5D, SoundEvents.BLAZE_BURN, entitylivingbaseIn.getSoundSource(), 1.0F + entitylivingbaseIn.level.random.nextFloat(), entitylivingbaseIn.level.random.nextFloat() * 0.7F + 0.3F, false);
                }
                for (int i = 0; i < 2; ++i) {
                    entitylivingbaseIn.level.addParticle(ParticleTypes.LARGE_SMOKE, entitylivingbaseIn.getRandomX(0.5D), entitylivingbaseIn.getRandomY(), entitylivingbaseIn.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    entitylivingbaseIn.level.addParticle(ParticleTypes.FLAME, entitylivingbaseIn.getRandomX(0.5D), entitylivingbaseIn.getRandomY(), entitylivingbaseIn.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
            }
        });
    }

    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
