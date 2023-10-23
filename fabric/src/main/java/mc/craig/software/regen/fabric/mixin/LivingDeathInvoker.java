package mc.craig.software.regen.fabric.mixin;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.config.RegenConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {LivingEntity.class, Player.class, ServerPlayer.class})
public class LivingDeathInvoker {
    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void die(DamageSource source, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        RegenerationData data = RegenerationData.get(livingEntity).get();

        if (data.stateManager() == null && livingEntity.level().isClientSide()) return;
        boolean notDead = data.stateManager().onKilled(source);
        if (notDead) {
            ci.cancel();
        } else {
            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                data.extractRegens(data.regens());
            }
        }

        data.syncToClients(null);

    }
}