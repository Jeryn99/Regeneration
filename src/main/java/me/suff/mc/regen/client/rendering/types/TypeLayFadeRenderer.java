package me.suff.mc.regen.client.rendering.types;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.types.RegenTypes;
import me.suff.mc.regen.common.types.TypeLayFade;
import me.suff.mc.regen.util.client.ClientUtil;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by Craig on 29/08/2019 @ 15:18
 */
public class TypeLayFadeRenderer extends ATypeRenderer<TypeLayFade> {

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
                model.head.xRot = (float) Math.toRadians(0);
                model.head.yRot = (float) Math.toRadians(0);
                model.head.zRot = (float) Math.toRadians(0);

                model.leftLeg.zRot = (float) -Math.toRadians(5);
                model.rightLeg.zRot = (float) Math.toRadians(5);

                model.leftArm.zRot = (float) -Math.toRadians(5);
                model.rightArm.zRot = (float) Math.toRadians(5);
                if (model instanceof PlayerModel) {
                    ClientUtil.copyAnglesToWear((PlayerModel) model);
                }
            }
        });
    }
}
