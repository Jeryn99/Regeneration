package mc.craig.software.regen.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.util.PlayerUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("HEAD"), method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void getTextureLocation(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (VisualManipulator.PLAYER_SKINS.containsKey(entity.getUUID())) {
            callbackInfoReturnable.setReturnValue(VisualManipulator.PLAYER_SKINS.get(entity.getUUID()));
        }
    }

    @Inject(at = @At("TAIL"), method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    protected void setupRotations(AbstractClientPlayer entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        RegenerationData.get(entityLiving).ifPresent(iRegen -> {
            if (PlayerUtil.isPlayerAboveZeroGrid(entityLiving) && iRegen.regenState() == RegenStates.POST) {
                poseStack.mulPose(Vector3f.YP.rotationDegrees(entityLiving.yBodyRot));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(90));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(270.0F));
                float offset = Mth.cos(entityLiving.tickCount * 0.1F) * -0.09F;
                poseStack.translate(0, -1, 0);
                poseStack.translate(0, 0, -1);
                poseStack.translate(0, 0, -offset);
            }
        });
    }

}
