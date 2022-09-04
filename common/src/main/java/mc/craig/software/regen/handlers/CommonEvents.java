package mc.craig.software.regen.handlers;

import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.item.HandItem;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.traits.RegenTraitRegistry;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RConstants;
import mc.craig.software.regen.util.RegenSources;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class CommonEvents {


    public static boolean canBeGiven(Entity entity) {
        boolean isLiving = entity instanceof LivingEntity && entity.getType() != EntityType.ARMOR_STAND;
        boolean ignoresConfig = entity.getType() == REntities.TIMELORD.get() || entity.getType() == EntityType.PLAYER;

        if (isLiving && ignoresConfig) {
            return true;
        }

        if (isLiving) { //Always make sure the entity is living, because we are explicility casting to LivingEntity later on
            return RegenConfig.COMMON.mobsHaveRegens.get();    //Base on the config value
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void noFire(LivingAttackEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent((iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFire() || iRegen.regenState() == RegenStates.REGENERATING && event.getSource().isExplosion()) {
                event.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void noFall(LivingFallEvent event) {
        if (event.getEntity() == null) return;
        RegenerationData.get(event.getEntity()).ifPresent((iRegen -> {
            if (RegenTraitRegistry.getTraitLocation(iRegen.trait()).toString().equals(RegenTraitRegistry.getTraitLocation(RegenTraitRegistry.LEAP.get()).toString())) {
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
            if (event.getSource().isFire() && RegenTraitRegistry.getTraitLocation(iRegen.trait()).toString().equals(RegenTraitRegistry.getTraitLocation(RegenTraitRegistry.FIRE.get()).toString())) {
                event.setCanceled(true);
                event.setAmount(0.0F);
                return;
            }

            if (trueSource instanceof Player player && event.getEntity() != null) {
                RegenerationData.get(player).ifPresent((data) -> data.stateManager().onPunchEntity(event));
            }

            // Stop certain damages
            if (event.getSource() == RegenSources.REGEN_DMG_KILLED)
                return;

            //Update Death Message
            iRegen.setDeathMessage(event.getSource().getLocalizedDeathMessage(livingEntity).getString());

            //Stop falling for leap trait
            if (RegenTraitRegistry.getTraitLocation(iRegen.trait()).toString().equals(RegenTraitRegistry.getTraitLocation(RegenTraitRegistry.LEAP.get()).toString())) {
                if (event.getSource() == DamageSource.FALL) {
                    event.setCanceled(true);//cancels damage, in case the above didn't cut it
                    return;
                }
            }

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

            //regen and death checks moved to LivingDamageEvent and LivingDeathEvent
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
                cap.setTrait(RegenTraitRegistry.BORING.get());
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
        RegenerationData.get(e.getEntity()).ifPresent((data) -> data.stateManager().onPunchBlock(e));
    }

    @SubscribeEvent
    public static void onLive(LivingEvent.LivingTickEvent livingUpdateEvent) {
        RegenerationData.get(livingUpdateEvent.getEntity()).ifPresent(IRegen::tick);

        if (livingUpdateEvent.getEntity() instanceof ServerPlayer) {
            if (shouldGiveCouncilAdvancement((ServerPlayer) livingUpdateEvent.getEntity())) {
                TriggerManager.COUNCIL.trigger((ServerPlayer) livingUpdateEvent.getEntity());
            }
        }
    }

    public static boolean shouldGiveCouncilAdvancement(ServerPlayer serverPlayerEntity) {
        EquipmentSlot[] equipmentSlotTypes = new EquipmentSlot[]{EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET};
        for (EquipmentSlot equipmentSlotType : equipmentSlotTypes) {
            if (!ForgeRegistries.ITEMS.getKey(serverPlayerEntity.getItemBySlot(equipmentSlotType).getItem()).getPath().contains("robes")) {
                return false;
            }
        }
        return true;
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
