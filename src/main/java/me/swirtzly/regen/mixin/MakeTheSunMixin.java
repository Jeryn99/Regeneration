package me.swirtzly.regen.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.renderer.WorldRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 04/03/2021 */
@Mixin(WorldRenderer.class)
public class MakeTheSunMixin {

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/WorldRenderer;MOON_PHASES_TEXTURES:Lnet/minecraft/util/ResourceLocation;", opcode = Opcodes.GETSTATIC), method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V")
    public void renderSky(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci) {
        ClientUtil.renderSky(matrixStackIn, partialTicks);
    }
}
