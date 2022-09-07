package mc.craig.software.regen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import mc.craig.software.regen.client.visual.FogTracker;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(at = @At("TAIL"), cancellable = true, method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V")
    private static void setupFog(Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance, boolean bl, float f, CallbackInfo ci) {
        if (camera.getEntity() instanceof LivingEntity livingEntity) {
            FogTracker.setUpFog(livingEntity);
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "levelFogColor()V")
    private static void setupColor(CallbackInfo callbackInfo) {
        if (Minecraft.getInstance().player != null && FogTracker.getSitutionalFogColor() != null) {
            RenderSystem.setShaderFogColor((float) FogTracker.getSitutionalFogColor().x, (float) FogTracker.getSitutionalFogColor().y, (float) FogTracker.getSitutionalFogColor().z);
            callbackInfo.cancel();
        }
    }

}