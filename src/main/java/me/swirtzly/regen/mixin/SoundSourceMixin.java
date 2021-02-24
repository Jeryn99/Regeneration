package me.swirtzly.regen.mixin;

import me.swirtzly.regen.client.sound.SoundReverb;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 24/02/2021 */
@Mixin(SoundSource.class)
public class SoundSourceMixin {

    @Shadow
    @Final
    @Mutable
    private int id;

    @Inject(at = @At("RETURN"), method = "updateSource(Lnet/minecraft/util/math/vector/Vector3d;)V")
    private void updateSource(Vector3d vector3d, CallbackInfo callbackInfo) {
        SoundReverb.onPlaySound(id);
    }

}
