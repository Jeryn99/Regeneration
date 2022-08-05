package craig.software.mc.regen.mixin;

import craig.software.mc.regen.client.skin.SkinHandler;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow
    protected abstract PlayerInfo getPlayerInfo();

    @Inject(at = @At("TAIL"), cancellable = true, method = "getSkinTextureLocation()Lnet/minecraft/resources/ResourceLocation;")
    protected void getSkinTextureLocation(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (SkinHandler.PLAYER_SKINS.containsKey(getPlayerInfo().getProfile().getId())) {
            ResourceLocation skin = SkinHandler.PLAYER_SKINS.get(getPlayerInfo().getProfile().getId());
            callbackInfoReturnable.setReturnValue(skin);
        }
    }
}
