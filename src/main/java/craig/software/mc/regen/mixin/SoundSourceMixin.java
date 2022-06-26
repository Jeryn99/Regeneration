package craig.software.mc.regen.mixin;

import com.mojang.blaze3d.audio.Channel;
import craig.software.mc.regen.client.sound.SoundReverb;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 24/02/2021 */
@Mixin(Channel.class)
public class SoundSourceMixin {

    @Shadow
    @Mutable
    @Final
    private int source;

    @Inject(at = @At("RETURN"), method = "setSelfPosition(Lnet/minecraft/world/phys/Vec3;)V")
    private void setSelfPosition(Vec3 vector3d, CallbackInfo callbackInfo) {
        SoundReverb.setSelfPosition(source);
    }

}
