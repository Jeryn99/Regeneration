package mc.craig.software.regen.fabric.mixin;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "knockback(DDD)V", cancellable = true)
    public void knockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData.get(livingEntity).ifPresent(regenerationData -> {
            if (regenerationData.isTraitActive() && regenerationData.getCurrentTrait() == TraitRegistry.KNOCKBACK.get()) {
                ci.cancel();
            }
        });
    }

    @Inject(at = @At("HEAD"), method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", cancellable = true)
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData data = RegenerationData.get(livingEntity).get();

        // Stop certain damages
        if (source.is(RegenDamageTypes.REGEN_DMG_KILLED))
            return;

        if (data.isTraitActive() && (data.getCurrentTrait() == TraitRegistry.FIRE_RESISTANCE.get() && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)) || data.getCurrentTrait() == TraitRegistry.ARROW_DODGE.get() && source.is(DamageTypes.MOB_PROJECTILE))) {
            cir.setReturnValue(false);
        }

        //Handle Death
        if (data.regenState() == RegenStates.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)) || data.regenState() == RegenStates.REGENERATING && source.is(DamageTypes.EXPLOSION)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"))
    private float modifyDamageAmount(float value) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData data = RegenerationData.get(livingEntity).get();
        if (data.regenState() == RegenStates.POST && value != Integer.MAX_VALUE) { //TODO Do this damage source dependent
            PlayerUtil.sendMessage(livingEntity, Component.translatable(RMessages.POST_REDUCED_DAMAGE), true);
            return 1.5F;
        }
        return value;
    }

    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData.get(livingEntity).ifPresent(RegenerationData::tick);
    }

}