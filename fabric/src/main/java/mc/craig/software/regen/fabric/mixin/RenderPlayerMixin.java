package mc.craig.software.regen.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class RenderPlayerMixin {

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", cancellable = true)
    private void renderHead(AbstractClientPlayer abstractClientPlayer, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        VisualManipulator.tick(abstractClientPlayer);

        RegenerationData.get(abstractClientPlayer).ifPresent(iRegen -> {
            PlayerRenderer renderer = (PlayerRenderer) (Object) this;
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPre(abstractClientPlayer, renderer, partialTicks, matrixStack, buffer, packedLight);
        });
    }

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", cancellable = true)
    private void renderTail(AbstractClientPlayer abstractClientPlayer, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        RegenerationData.get(abstractClientPlayer).ifPresent(iRegen -> {
            PlayerRenderer renderer = (PlayerRenderer) (Object) this;
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPost(abstractClientPlayer, renderer, partialTicks, matrixStack, buffer, packedLight);
        });
    }

}
