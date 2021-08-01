package me.suff.mc.regen.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 09/03/2021 */
@Mixin(PlayerRenderer.class)
public class RenderPlayerMixin {

    @Inject(at = @At("TAIL"), method = "setupRotations(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;FFF)V")
    protected void setupRotations(AbstractClientPlayer entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {

        RegenCap.get(entityLiving).ifPresent(iRegen -> {
            if (PlayerUtil.isPlayerAboveZeroGrid(entityLiving) && iRegen.regenState() == RegenStates.POST) {
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entityLiving.yRot));
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90));
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F));
                float offset = Mth.cos(entityLiving.tickCount * 0.1F) * -0.09F;
                matrixStackIn.translate(0, -1, 0);
                matrixStackIn.translate(0, 0, -1);
                matrixStackIn.translate(0, 0, -offset);
            }
        });
    }


}
