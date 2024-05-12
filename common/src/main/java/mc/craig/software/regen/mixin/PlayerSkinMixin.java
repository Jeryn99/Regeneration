package mc.craig.software.regen.mixin;

import com.mojang.authlib.GameProfile;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerSkin.class)
public abstract class PlayerSkinMixin {



    @Inject(at = @At("HEAD"), method = "texture()Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void getSkinLocation(CallbackInfoReturnable<ResourceLocation> cir) {

        ResourceLocation resourceLocation = ClientUtil.redirectSkin(UUID.randomUUID());
        if (resourceLocation != null) {
            cir.setReturnValue(resourceLocation);
        }
    }

}
