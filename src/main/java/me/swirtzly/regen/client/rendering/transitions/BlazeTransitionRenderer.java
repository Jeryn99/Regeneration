package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
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
    public void layer(BipedModel< ? > bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                if (entitylivingbaseIn.world.rand.nextInt(24) == 0) {
                    entitylivingbaseIn.world.playSound(entitylivingbaseIn.getPosX() + 0.5D, entitylivingbaseIn.getPosY() + 0.5D, entitylivingbaseIn.getPosZ() + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, entitylivingbaseIn.getSoundCategory(), 1.0F + entitylivingbaseIn.world.rand.nextFloat(), entitylivingbaseIn.world.rand.nextFloat() * 0.7F + 0.3F, false);
                }
                for (int i = 0; i < 2; ++i) {
                    entitylivingbaseIn.world.addParticle(ParticleTypes.LARGE_SMOKE, entitylivingbaseIn.getPosXRandom(0.5D), entitylivingbaseIn.getPosYRandom(), entitylivingbaseIn.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                    entitylivingbaseIn.world.addParticle(ParticleTypes.FLAME, entitylivingbaseIn.getPosXRandom(0.5D), entitylivingbaseIn.getPosYRandom(), entitylivingbaseIn.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                }
            }
        });
    }

    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
