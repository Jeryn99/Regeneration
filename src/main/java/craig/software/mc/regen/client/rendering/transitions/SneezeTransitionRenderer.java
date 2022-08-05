package craig.software.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import craig.software.mc.regen.client.rendering.types.RenderTypes;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.common.regen.transitions.SneezeTransition;
import craig.software.mc.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class SneezeTransitionRenderer implements TransitionRenderer {

    public static SneezeTransitionRenderer INSTANCE = new SneezeTransitionRenderer();

    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {
        Player player = pre.getEntity();

    }

    @Override
    public void onPlayerRenderPost(RenderPlayerEvent.Post post) {
        Player player = post.getEntity();
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                player.yBodyRot = player.yBodyRotO = player.yHeadRot;
            }
        });
    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING && entitylivingbaseIn instanceof Player) {
                float opacity = Mth.clamp(Mth.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
            }
        });
    }

    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

}
