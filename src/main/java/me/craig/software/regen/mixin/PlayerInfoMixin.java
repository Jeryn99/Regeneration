package me.craig.software.regen.mixin;

import com.mojang.authlib.GameProfile;
import me.craig.software.regen.client.screen.IncarnationScreen;
import me.craig.software.regen.client.skin.SkinHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(NetworkPlayerInfo.class)
public abstract class PlayerInfoMixin {

    @Shadow
    @Final
    private GameProfile profile;

    @Inject(at = @At("HEAD"), method = "getSkinLocation()Lnet/minecraft/util/ResourceLocation;", cancellable = true)
    private void getSkinLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        UUID uuid = profile.getId();

        if (Minecraft.getInstance().screen instanceof IncarnationScreen) {
            cir.setReturnValue(IncarnationScreen.currentTexture);
        }

        if (SkinHandler.PLAYER_SKINS.containsKey(uuid)) {
            cir.setReturnValue(SkinHandler.PLAYER_SKINS.get(uuid));
        }
    }

}