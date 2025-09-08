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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(value = {LivingEntity.class, Player.class, ServerPlayer.class})
public class LivingDeathInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger("RegenLogger");

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void die(DamageSource source, CallbackInfo ci) {
    /*    LivingEntity livingEntity = (LivingEntity) (Object) this;
        LOGGER.info("[Regen] Intercepted death event for entity: {} (UUID: {}) | DamageSource: {}",
                livingEntity.getName().getString(),
                livingEntity.getUUID(),
                source.getMsgId());

        RegenerationData data = RegenerationData.get(livingEntity).orElse(null);
        if (data == null) {
            LOGGER.warn("[Regen] No RegenerationData found for entity: {} (UUID: {}). Skipping regen handling.",
                    livingEntity.getName().getString(),
                    livingEntity.getUUID());
            return;
        }

        if (data.stateManager() == null) {
            LOGGER.debug("[Regen] StateManager is null for entity: {} (UUID: {}).",
                    livingEntity.getName().getString(),
                    livingEntity.getUUID());
            if (livingEntity.level().isClientSide()) {
                LOGGER.debug("[Regen] Entity {} is on client side; ignoring death interception.",
                        livingEntity.getName().getString());
                return;
            }
        }

        boolean notDead = data.stateManager().onKilled(source);
        LOGGER.info("[Regen] onKilled() result for {} (UUID: {}): {}",
                livingEntity.getName().getString(),
                livingEntity.getUUID(),
                notDead);

        if (notDead) {
            LOGGER.info("[Regen] Cancelling death of {} (UUID: {}) due to active regeneration.",
                    livingEntity.getName().getString(),
                    livingEntity.getUUID());
            ci.cancel();
        } else {
            LOGGER.info("[Regen] {} (UUID: {}) is fully dead. Checking config for regen loss...",
                    livingEntity.getName().getString(),
                    livingEntity.getUUID());
            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                LOGGER.info("[Regen] Config requires regen loss. Extracting {} regen(s) from {} (UUID: {}).",
                        data.regens(),
                        livingEntity.getName().getString(),
                        livingEntity.getUUID());
                data.extractRegens(data.regens());
            } else {
                LOGGER.info("[Regen] Config does not require regen loss. Keeping regen count: {} for {} (UUID: {}).",
                        data.regens(),
                        livingEntity.getName().getString(),
                        livingEntity.getUUID());
            }
        }

        LOGGER.debug("[Regen] Syncing RegenerationData for {} (UUID: {}) to clients.",
                livingEntity.getName().getString(),
                livingEntity.getUUID());
        data.syncToClients(null);

        LOGGER.info("[Regen] Death interception for {} (UUID: {}) completed.",
                livingEntity.getName().getString(),
                livingEntity.getUUID());*/
    }
}
