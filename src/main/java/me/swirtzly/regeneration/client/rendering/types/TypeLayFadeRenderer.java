package me.swirtzly.regeneration.client.rendering.types;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.RegenTypes;
import me.swirtzly.regeneration.common.types.TypeLayFade;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by Swirtzly on 29/08/2019 @ 15:18
 */
public class TypeLayFadeRenderer extends ATypeRenderer< TypeLayFade > {

    public static final TypeLayFadeRenderer INSTANCE = new TypeLayFadeRenderer();

    public TypeLayFadeRenderer() {
    }


    @Override
    protected void onRenderPre(TypeLayFade type, RenderPlayerEvent.Pre event, IRegen capability) {
        /* This method has no implementation for this Regeneration type */
    }

    @Override
    protected void onRenderPost(TypeLayFade type, RenderPlayerEvent.Post event, IRegen capability) {
        /* This method has no implementation for this Regeneration type */
    }

    @Override
    protected void onRenderLayer(TypeLayFade type, LivingRenderer renderLivingBase, IRegen capability, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        FieryRenderer.renderOverlay(renderLivingBase, entityPlayer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public void renderHand(LivingEntity player, HandSide handSide, LivingRenderer render) {
        /* This method has no implementation for this Regeneration type */
    }

    @Override
    public void preRenderCallback(LivingRenderer renderer, LivingEntity entity) {
        RegenCap.get(entity).ifPresent((data) -> {
            if (data.getState() == PlayerUtil.RegenState.REGENERATING && data.getRegenType() == RegenTypes.HARTNELL) {
                GlStateManager.rotatef(15, 1, 0, 0);
            }
        });
    }

    @Override
    public void animateEntity(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(entity).ifPresent((data) -> {
            if (data.getState() == PlayerUtil.RegenState.REGENERATING && data.getRegenType() == RegenTypes.HARTNELL) {
                model.bipedHead.rotateAngleX = (float) Math.toRadians(0);
                model.bipedHead.rotateAngleY = (float) Math.toRadians(0);
                model.bipedHead.rotateAngleZ = (float) Math.toRadians(0);

                model.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
                model.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

                model.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(5);
                model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(5);
                if (model instanceof PlayerModel) {
                    ClientUtil.copyAnglesToWear((PlayerModel) model);
                }
            }
        });
    }
}
