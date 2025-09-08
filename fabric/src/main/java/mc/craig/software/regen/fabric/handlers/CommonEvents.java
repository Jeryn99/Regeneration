package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.RegenUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommonEvents {

    private static final ResourceLocation MARS_TEMPLE_LOOT_TABLE_ID = new ResourceLocation("ad_astra:chests/temple/mars/temple");

    public static void init() {
        Logger LOGGER = LoggerFactory.getLogger("RegenInit");

        /*ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            AtomicBoolean cancelDeath = new AtomicBoolean(false);

            LOGGER.info("[Regen] ALLOW_DEATH triggered for entity: {} (DamageSource: {}, DamageAmount: {})",
                    entity.getName(), damageSource.getMsgId(), damageAmount);

            RegenerationData.get(entity).ifPresent(data -> {
                LOGGER.info("[Regen] Found RegenerationData for {}", entity.getName());

                // Grace period handling
                if (data.shouldRetryGracePeriod()) {
                    LOGGER.info("[Regen] {} is eligible for grace period reset.", entity.getName());

                    data.setRetryGracePeriod(false);
                    data.setHasSetSkin(false);
                    LOGGER.info("[Regen] Reset RetryGracePeriod and HasSetSkin flags for {}", entity.getName());

                    if (data.canRegenerate()) {
                        LOGGER.info("[Regen] {} can regenerate. Entering GRACE state.", entity.getName());
                        RegenerationData.StateManager stateManager = data.stateManager();

                        stateManager.scheduleTransitionInSeconds(RegenStates.Transition.ENTER_CRITICAL,
                                RegenConfig.COMMON.gracePhaseLength.get());
                        stateManager.scheduleHandGlowTrigger();

                        data.setCurrentState(RegenStates.GRACE);
                        data.syncToClients(null);

                        ActingForwarder.onEnterGrace(data);
                        cancelDeath.set(true);
                        LOGGER.info("[Regen] Death cancelled and grace state started for {}", entity.getName());
                        return;
                    }
                }

                // Regular regeneration/death logic (from your Mixin)
                if (data.stateManager() != null) {
                    boolean notDead = data.stateManager().onKilled(damageSource);
                    LOGGER.info("[Regen] onKilled() result for {}: {}", entity.getName(), notDead);

                    if (notDead) {
                        cancelDeath.set(true);
                        LOGGER.info("[Regen] Death cancelled due to active regeneration for {}", entity.getName());
                    } else {
                        if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                            LOGGER.info("[Regen] Removing {} regens from {}", data.regens(), entity.getName());
                            data.extractRegens(data.regens());
                        } else {
                            LOGGER.info("[Regen] Keeping regen count: {} for {}", data.regens(), entity.getName());
                        }
                    }
                } else {
                    LOGGER.debug("[Regen] StateManager is null for {}. Cannot process regeneration.", entity.getName());
                }

                data.syncToClients(null);
            });

            return cancelDeath.get();
        });
*/

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, damageSource, damageAmount) -> {
            AtomicBoolean allowDamage = new AtomicBoolean(true);

            // Only trigger regeneration/grace logic if this damage would be lethal
            if (entity.getHealth() - damageAmount <= 0) {
                LOGGER.info("[Regen] ALLOW_DEATH triggered for {} (DamageSource: {}, DamageAmount: {})",
                        entity.getName(), damageSource.getMsgId(), damageAmount);

                RegenerationData.get(entity).ifPresent(data -> {
                    LOGGER.info("[Regen] Found RegenerationData for {}", entity.getName());

                    // Grace period handling
                    if (data.shouldRetryGracePeriod()) {
                        LOGGER.info("[Regen] {} is eligible for grace period reset.", entity.getName());

                        data.setRetryGracePeriod(false);
                        data.setHasSetSkin(false);

                        if (data.canRegenerate()) {
                            LOGGER.info("[Regen] {} can regenerate. Entering GRACE state.", entity.getName());
                            RegenerationData.StateManager stateManager = data.stateManager();

                            stateManager.scheduleTransitionInSeconds(RegenStates.Transition.ENTER_CRITICAL,
                                    RegenConfig.COMMON.gracePhaseLength.get());
                            stateManager.scheduleHandGlowTrigger();

                            data.setCurrentState(RegenStates.GRACE);
                            data.syncToClients(null);

                            ActingForwarder.onEnterGrace(data);
                            allowDamage.set(false);
                            LOGGER.info("[Regen] Death cancelled and grace state started for {}", entity.getName());
                            return;
                        }
                    }

                    // Regular regeneration/death logic
                    if (data.stateManager() != null) {
                        boolean notDead = data.stateManager().onKilled(damageSource);
                        LOGGER.info("[Regen] onKilled() result for {}: {}", entity.getName(), notDead);

                        if (notDead) {
                            LOGGER.info("[Regen] Death cancelled due to active regeneration for {}", entity.getName());
                        } else if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                            LOGGER.info("[Regen] Removing {} regens from {}", data.regens(), entity.getName());
                            data.extractRegens(data.regens());
                        } else {
                            LOGGER.info("[Regen] Keeping regen count: {} for {}", data.regens(), entity.getName());
                        }
                    } else {
                        LOGGER.debug("[Regen] StateManager is null for {}. Cannot process regeneration.", entity.getName());
                    }

                    data.syncToClients(null);
                });
            }

            // Damage is allowed normally if cancelDeath is false
            return allowDamage.get();
        });



        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (MARS_TEMPLE_LOOT_TABLE_ID.equals(id)) {
                LOGGER.info("Modifying loot table: {}", id);

                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(RItems.ZINC.get())
                                .setWeight(15)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(RItems.FOB.get())
                                .setWeight(2)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

                tableBuilder.pool(poolBuilder.build());
                LOGGER.debug("Added Zinc (weight=15) and FOB (weight=2) to {}", id);
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LOGGER.info("Registering RegenCommand");
            RegenCommand.register(dispatcher);
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            LOGGER.debug("Player {} moved from {} to {}", player.getGameProfile().getName(),
                    origin.dimension().location(), destination.dimension().location());
            RegenerationData.get(player).ifPresent(data -> {
                data.syncToClients(null);
                LOGGER.trace("Synced regen data for {}", player.getGameProfile().getName());
            });
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("Server started, setting up names");
            RegenUtil.setupNames();
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClientSide) return InteractionResult.PASS;

            LOGGER.debug("Player {} attacked block at {} in {}", player.getGameProfile().getName(), pos, world.dimension().location());

            AtomicBoolean stopBreak = new AtomicBoolean(false);
            RegenerationData.get(player).ifPresent(data -> {
                boolean blocked = data.stateManager().onPunchBlock(pos, world.getBlockState(pos), player);
                stopBreak.set(blocked);
                if (blocked) {
                    LOGGER.info("Block break prevented at {} for player {}", pos, player.getGameProfile().getName());
                }
            });

            return stopBreak.get() ? InteractionResult.FAIL : InteractionResult.PASS;
        });

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (trackedEntity instanceof LivingEntity livingEntity) {
                LOGGER.debug("Player {} started tracking entity {}", player.getGameProfile().getName(), livingEntity.getName().getString());
                RegenerationData.get(livingEntity).ifPresent(data -> {
                    data.syncToClients(null);
                    LOGGER.trace("Synced regen data for entity {}", livingEntity.getName().getString());
                });
            }
        });

        LOGGER.info("Regen init complete — all event listeners registered");
    }


}
