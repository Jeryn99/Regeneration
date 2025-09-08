package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.forge.RegenerationDataImpl;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("RegenLogger");

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent serverStartingEvent) {
        LOGGER.info("[Regen] Server starting – setting up names.");
        RegenUtil.setupNames();
    }

    @SubscribeEvent
    public static void onLevelJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            LOGGER.info("[Regen] Player {} joined world. Syncing regeneration data.", serverPlayer.getGameProfile().getName());
            RegenerationData.get(serverPlayer).ifPresent(regenerationData -> regenerationData.syncToClients(null));
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity == null) return;

        LOGGER.debug("[Regen] LivingHurtEvent: {} took {} damage from {}.",
                livingEntity.getName().getString(),
                event.getAmount(),
                event.getSource().getMsgId());

        RegenerationData.get(livingEntity).ifPresent(data -> {

            if (event.getSource().is(RegenDamageTypes.REGEN_DMG_KILLED)) {
                LOGGER.debug("[Regen] Ignoring hurt event – source is REGEN_DMG_KILLED.");
                return;
            }

            if (data.regenState() == RegenStates.POST
                    && event.getSource() != event.getEntity().damageSources().fellOutOfWorld()
                    && !event.getSource().is(RegenDamageTypes.REGEN_DMG_HAND)) {
                LOGGER.info("[Regen] {} is in POST state. Reducing damage to 1.5.", livingEntity.getName().getString());
                event.setAmount(1.5F);
                PlayerUtil.sendMessage(livingEntity, Component.translatable(RMessages.POST_REDUCED_DAMAGE), true);
            }

            if (data.regenState() == RegenStates.REGENERATING) {
                if (RegenConfig.COMMON.regenFireImmune.get() && event.getSource().is(DamageTypes.ON_FIRE)) {
                    LOGGER.info("[Regen] Cancelling fire damage on regenerating entity: {}", livingEntity.getName().getString());
                    event.setCanceled(true);
                }
                if (event.getSource().is(DamageTypes.EXPLOSION)) {
                    LOGGER.info("[Regen] Cancelling explosion damage on regenerating entity: {}", livingEntity.getName().getString());
                    event.setCanceled(true);
                }
            }
        });
    }

    @SubscribeEvent
    public static void playerCloneEvent(final PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            LOGGER.info("[Regen] Cloning player {} due to death.", event.getEntity().getGameProfile().getName());
            Player player = event.getOriginal();
            player.reviveCaps();

            player.getCapability(RegenerationDataImpl.REGENERATION_DATA).ifPresent(cap ->
                    event.getEntity().getCapability(RegenerationDataImpl.REGENERATION_DATA).ifPresent(newcap -> {
                        LOGGER.debug("[Regen] Copying regen capability data on player clone.");
                        newcap.deserializeNBT(cap.serializeNBT());
                    })
            );

            player.invalidateCaps();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void adMortemInimicusButForGrace(LivingDamageEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent(cap -> {
            if (cap.regenState().isGraceful() && event.getEntity().getHealth() - event.getAmount() < 0) {
                LOGGER.info("[Regen] Entity {} is dying in GRACEFUL state. Forcing regeneration attempt.",
                        event.getEntity().getName().getString());
                boolean notDead = cap.stateManager().onKilled(event.getSource());
                LOGGER.debug("[Regen] onKilled() returned {} for {}.", notDead, event.getEntity().getName().getString());
                event.setCanceled(notDead);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void adMortemInimicus(LivingDeathEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent(cap -> {
            LOGGER.info("[Regen] Processing death event for {}. Source: {}",
                    event.getEntity().getName().getString(),
                    event.getSource().getMsgId());

            if (event.getSource().is(RegenDamageTypes.REGEN_DMG_CRITICAL) || event.getSource().is(RegenDamageTypes.REGEN_DMG_KILLED)) {
                LOGGER.info("[Regen] Death source is regen-critical. Handling forced death.");
                if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                    LOGGER.info("[Regen] Config requires regen loss. Removing {} regens.", cap.regens());
                    cap.extractRegens(cap.regens());
                }
                if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                    LOGGER.debug("[Regen] Syncing regen data for player {} after critical death.", serverPlayer.getGameProfile().getName());
                    cap.syncToClients(serverPlayer);
                }
                return;
            }

            if (cap.stateManager() == null) {
                LOGGER.warn("[Regen] No stateManager present for {} during death event.", event.getEntity().getName().getString());
                return;
            }

            boolean notDead = cap.stateManager().onKilled(event.getSource());
            LOGGER.info("[Regen] onKilled() returned {} for {}. Cancelling death: {}", notDead,
                    event.getEntity().getName().getString(),
                    notDead);
            event.setCanceled(notDead);
        });
    }

    @SubscribeEvent
    public static void onTrackPlayer(PlayerEvent.StartTracking startTracking) {
        LOGGER.debug("[Regen] Player {} started tracking {}.",
                startTracking.getEntity().getGameProfile().getName(),
                startTracking.getTarget().getName().getString());

        RegenerationData.get(startTracking.getEntity()).ifPresent(iRegen -> iRegen.syncToClients(null));
    }

    @SubscribeEvent
    public static void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntity().level().isClientSide) return;
        LOGGER.debug("[Regen] {} punched block at {}.", e.getEntity().getName().getString(), e.getPos());
        RegenerationData.get(e.getEntity()).ifPresent(data ->
                data.stateManager().onPunchBlock(e.getPos(), e.getLevel().getBlockState(e.getPos()), e.getEntity()));
    }

    @SubscribeEvent
    public static void onLive(LivingEvent.LivingTickEvent livingUpdateEvent) {
        RegenerationData.get(livingUpdateEvent.getEntity()).ifPresent(cap -> {
            LOGGER.trace("[Regen] Ticking regen data for {}.", livingUpdateEvent.getEntity().getName().getString());
            cap.tick();
        });
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        LOGGER.info("[Regen] Registering Regen commands.");
        RegenCommand.register(event.getDispatcher());
    }
}
