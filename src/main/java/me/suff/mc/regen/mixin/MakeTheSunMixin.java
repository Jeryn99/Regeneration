package me.suff.mc.regen.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.renderer.LevelRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 04/03/2021 */
@Mixin(LevelRenderer.class)
public class MakeTheSunMixin {

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/WorldRenderer;MOON_LOCATION:Lnet/minecraft/util/ResourceLocation;", opcode = Opcodes.GETSTATIC), method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V")
    private void renderSky(PoseStack matrixStackIn, float partialTicks, CallbackInfo ci) {
        ClientUtil.renderSky(matrixStackIn);
    }
}
