package me.craig.software.regen.mixin;

import com.terraforged.noise.combiner.Min;
import me.craig.software.regen.client.screen.IncarnationScreen;
import me.craig.software.regen.client.skin.SkinHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("HEAD"), method = "getTextureLocation(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)Lnet/minecraft/util/ResourceLocation;", cancellable = true)
    private void getTextureLocation(AbstractClientPlayerEntity entity, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {

        if(Minecraft.getInstance().screen instanceof IncarnationScreen){
            IncarnationScreen screen = (IncarnationScreen) Minecraft.getInstance().screen;
            if(!screen.isAfterRendering) {
                callbackInfoReturnable.setReturnValue(IncarnationScreen.currentTexture);
            }
        }

        if (SkinHandler.PLAYER_SKINS.containsKey(entity.getUUID())) {
            callbackInfoReturnable.setReturnValue(SkinHandler.PLAYER_SKINS.get(entity.getUUID()));
        }
    }


}
