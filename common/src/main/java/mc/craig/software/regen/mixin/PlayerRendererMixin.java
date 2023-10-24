package mc.craig.software.regen.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.util.ClientUtil;
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

import java.util.UUID;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("HEAD"), method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    public void getTextureLocation(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> cir) {
        UUID uuid = entity.getUUID();

        ResourceLocation resourceLocation = ClientUtil.redirectSkin(uuid);
        if (resourceLocation != null) {
            cir.setReturnValue(resourceLocation);
        }
    }

    @Inject(at = @At("TAIL"), method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    protected void setupRotations(AbstractClientPlayer abstractClientPlayer, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        RegenerationData.get(abstractClientPlayer).ifPresent(iRegen -> {
            if (PlayerUtil.isPlayerAboveZeroGrid(abstractClientPlayer) && iRegen.regenState() == RegenStates.POST) {
                poseStack.mulPose(Axis.YP.rotationDegrees(abstractClientPlayer.yBodyRot));
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                float offset = Mth.cos(abstractClientPlayer.tickCount * 0.1F) * -0.09F;
                poseStack.translate(0, -1, 0);
                poseStack.translate(0, 0, -1);
                poseStack.translate(0, 0, -offset);
            }
        });
    }

}
