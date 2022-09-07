package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.item.HandItem;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenSources;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void noFire(LivingAttackEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent((iRegen -> {
            // Entity is immune to explosion and fire (if configured) damage while regenerating
            if (iRegen.regenState() == RegenStates.REGENERATING && (RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFire() || event.getSource().isExplosion())) {
                event.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity == null) return;

        RegenerationData.get(livingEntity).ifPresent(iRegen -> {
            Entity trueSource = event.getSource().getEntity();

            if (trueSource instanceof Player player && event.getEntity() != null) {
                // Player punched something
                RegenerationData.get(player).ifPresent((data) -> data.stateManager().onPunchEntity(event.getEntity()));
            }

            // Stop certain damages
            if (event.getSource() == RegenSources.REGEN_DMG_KILLED)
                return;

            //Update Death Message
            iRegen.setDeathMessage(event.getSource().getLocalizedDeathMessage(livingEntity).getString());

            //Handle Post
            if (iRegen.regenState() == RegenStates.POST && event.getSource() != DamageSource.OUT_OF_WORLD && event.getSource() != RegenSources.REGEN_DMG_HAND) {
                event.setAmount(1.5F);
                PlayerUtil.sendMessage(livingEntity, Component.translatable("regen.messages.reduced_dmg"), true);
            }

            //Handle Death
            if (iRegen.regenState() == RegenStates.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFire() || iRegen.regenState() == RegenStates.REGENERATING && event.getSource().isExplosion()) {
                event.setCanceled(true);//cancels damage, in case the above didn't cut it
                return;
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void adMortemInimicusButForGrace(LivingDamageEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent((cap -> {
            if ((cap.regenState().isGraceful()) && event.getEntity().getHealth() - event.getAmount() < 0) {
                //uh oh, we're dying in grace. Forcibly regenerate before all (?) death prevention mods
                boolean notDead = cap.stateManager().onKilled(event.getSource());
                event.setCanceled(notDead);
            }
        }));
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void adMortemInimicus(LivingDeathEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent((cap) -> {
            if ((event.getSource() == RegenSources.REGEN_DMG_CRITICAL || event.getSource() == RegenSources.REGEN_DMG_KILLED)) {
                if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                    cap.extractRegens(cap.regens());
                }
                if (event.getEntity() instanceof ServerPlayer)
                    cap.syncToClients((ServerPlayer) event.getEntity());
                return;
            }
            if (cap.stateManager() == null) return;
            boolean notDead = cap.stateManager().onKilled(event.getSource());
            event.setCanceled(notDead);
        });

    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntity();
        RegenerationData.get(livingEntity).ifPresent((data) -> event.setCanceled(data.regenState() == RegenStates.REGENERATING));
    }

    @SubscribeEvent
    public static void onTrackPlayer(PlayerEvent.StartTracking startTracking) {
        RegenerationData.get(startTracking.getEntity()).ifPresent(iRegen -> iRegen.syncToClients(null));
    }

    @SubscribeEvent
    public static void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntity().level.isClientSide) return;
        RegenerationData.get(e.getEntity()).ifPresent((data) -> data.stateManager().onPunchBlock(e.getPos(), e.getLevel().getBlockState(e.getPos()), e.getEntity()));
    }

    @SubscribeEvent
    public static void onLive(LivingEvent.LivingTickEvent livingUpdateEvent) {
        RegenerationData.get(livingUpdateEvent.getEntity()).ifPresent(IRegen::tick);
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        RegenCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onCut(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() instanceof DiggerItem || event.getItemStack().getItem() instanceof SwordItem) {
            Player player = event.getEntity();
            RegenerationData.get(player).ifPresent((data) -> {
                if (data.regenState() == RegenStates.POST && player.isShiftKeyDown() & data.handState() == IRegen.Hand.NO_GONE) {
                    HandItem.createHand(player);
                }
            });
        }
    }


}
