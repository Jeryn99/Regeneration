package mc.craig.software.regen.fabric.mixin;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.RegenSources;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", cancellable = true)
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData data = RegenerationData.get(livingEntity).get();

        // Stop certain damages
        if (source == RegenSources.REGEN_DMG_KILLED)
            return;

        //Handle Death
        if (data.regenState() == RegenStates.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && source.isFire() || data.regenState() == RegenStates.REGENERATING && source.isExplosion()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyArgs(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z", shift = At.Shift.BY, by = 2))
    private void modifyDamageAmount(Args args) {
        //TODO Modify damage amount if in post
       /* LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData data = RegenerationData.get(livingEntity).get();
        DamageSource damageSource = args.get(0);
        if (data.regenState() == RegenStates.POST && damageSource != DamageSource.OUT_OF_WORLD && damageSource != RegenSources.REGEN_DMG_HAND) {
            args.set(1, 1.5F); //Set amount to 1.5
            PlayerUtil.sendMessage(livingEntity, Component.translatable("regen.messages.reduced_dmg"), true);
        };*/
    }

    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData.get(livingEntity).ifPresent(RegenerationData::tick);
    }

}