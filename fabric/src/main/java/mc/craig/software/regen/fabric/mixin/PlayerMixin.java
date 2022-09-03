package mc.craig.software.regen.fabric.mixin;

import mc.craig.software.regen.common.RegenLogic;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PlayerMixin {

    @Inject(at = @At("HEAD"), method = "isDeadOrDying()Z", cancellable = true)
    private void isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (RegenLogic.canRegenerate(livingEntity) && !livingEntity.isRemoved()) {
            cir.setReturnValue(false);
        }
    }

}
