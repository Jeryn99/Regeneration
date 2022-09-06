package mc.craig.software.regen.fabric.mixin;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PlayerMixin {

    @Inject(at = @At("HEAD"), method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", cancellable = true)
    private void isHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        RegenerationData.get(livingEntity).ifPresent(regenerationData -> {
            if (!livingEntity.level.isClientSide) {
                if (regenerationData.stateManager().onKilled(source)) {
                    cir.setReturnValue(false);
                } else {
                    cir.setReturnValue(RegenUtil.isHurt(source, livingEntity, amount));
                }
            }
        });
    }

    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData.get(livingEntity).ifPresent(RegenerationData::tick);
    }

}
