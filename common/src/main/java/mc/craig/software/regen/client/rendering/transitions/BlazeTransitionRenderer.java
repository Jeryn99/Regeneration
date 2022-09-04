package mc.craig.software.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
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
    public void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenerationData.get(entitylivingbaseIn).ifPresent(iRegen -> {
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
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
