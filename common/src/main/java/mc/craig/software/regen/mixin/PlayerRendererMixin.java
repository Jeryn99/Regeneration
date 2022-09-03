package mc.craig.software.regen.mixin;

import mc.craig.software.regen.client.visual.VisualManipulator;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("HEAD"), method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void getTextureLocation(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        VisualManipulator.getSkin(entity.getUUID()).ifPresent(callbackInfoReturnable::setReturnValue);
    }

}
