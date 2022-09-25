package me.craig.software.regen.handlers;

import com.mojang.serialization.Codec;
import me.craig.software.regen.Regeneration;
import me.craig.software.regen.util.PlayerUtil;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.util.RegenSources;
import me.craig.software.regen.util.RegenUtil;
import me.craig.software.regen.common.advancement.TriggerManager;
import me.craig.software.regen.common.commands.RegenCommand;
import me.craig.software.regen.common.item.HandItem;
import me.craig.software.regen.common.objects.REntities;
import me.craig.software.regen.common.regen.IRegen;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.common.regen.state.RegenStates;
import me.craig.software.regen.common.traits.RegenTraitRegistry;
import me.craig.software.regen.common.world.gen.RStructures;
import me.craig.software.regen.config.RegenConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    /**
     * Adds the structure's spacing for modded code made dimensions so that the structure's spacing remains
     * correct in any dimension or worldtype instead of not spawning.
     * In {@link RStructures#setupStructure(Structure, StructureSeparationSettings, boolean)} we call {@link DimensionStructuresSettings#DEFAULTS}
     * but this sometimes does not work in code made dimensions.
     */
    private static Method GETCODEC_METHOD;

    /* Attach Capability to all LivingEntities */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (canBeGiven(event.getObject())) {
            event.addCapability(RConstants.CAP_REGEN_ID, new ICapabilitySerializable<CompoundNBT>() {
                final RegenCap regen = new RegenCap((LivingEntity) event.getObject());
                final LazyOptional<IRegen> regenInstance = LazyOptional.of(() -> regen);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
                    return cap == RegenCap.CAPABILITY ? (LazyOptional<T>) regenInstance : LazyOptional.empty();
                }

                @Override
                public CompoundNBT serializeNBT() {
                    return regen.serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundNBT nbt) {
                    regen.deserializeNBT(nbt);
                }
            });
        }
    }

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
        if (event.getEntityLiving() == null) return;
        RegenCap.get(event.getEntityLiving()).ifPresent((iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFire() || iRegen.regenState() == RegenStates.REGENERATING && event.getSource().isExplosion()) {
                event.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void noFall(LivingFallEvent event) {
        if (event.getEntityLiving() == null) return;
        RegenCap.get(event.getEntityLiving()).ifPresent((iRegen -> {
            if (iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.LEAP.get().getRegistryName().toString())) {
                event.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        if (livingEntity == null) return;

        RegenCap.get(livingEntity).ifPresent(iRegen -> {

            Entity trueSource = event.getSource().getEntity();
            if (event.getSource().isFire() && iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.FIRE.get().getRegistryName().toString())) {
                event.setCanceled(true);
                event.setAmount(0.0F);
                return;
            }

            if (trueSource instanceof PlayerEntity && event.getEntityLiving() != null) {
                PlayerEntity player = (PlayerEntity) trueSource;
                RegenCap.get(player).ifPresent((data) -> data.stateManager().onPunchEntity(event));
            }

            // Stop certain damages
            if (event.getSource() == RegenSources.REGEN_DMG_KILLED)
                return;

            //Update Death Message
            iRegen.setDeathMessage(event.getSource().getLocalizedDeathMessage(livingEntity).getString());

            //Stop falling for leap trait
            if (iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.LEAP.get().getRegistryName().toString())) {
                if (event.getSource() == DamageSource.FALL) {
                    event.setCanceled(true);//cancels damage, in case the above didn't cut it
                    return;
                }
            }

            //Handle Post
            if (iRegen.regenState() == RegenStates.POST && event.getSource() != DamageSource.OUT_OF_WORLD && event.getSource() != RegenSources.REGEN_DMG_HAND) {
                event.setAmount(1.5F);
                PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regen.messages.reduced_dmg"), true);
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
        if (event.getEntityLiving() == null) return;
        RegenCap.get(event.getEntityLiving()).ifPresent((cap -> {
            if ((cap.regenState().isGraceful()) && event.getEntityLiving().getHealth() - event.getAmount() < 0) {
                //uh oh, we're dying in grace. Forcibly regenerate before all (?) death prevention mods
                boolean notDead = cap.stateManager().onKilled(event.getSource());
                event.setCanceled(notDead);
            }
        }));

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void adMortemInimicus(LivingDeathEvent event) {
        if (event.getEntityLiving() == null) return;
        RegenCap.get(event.getEntityLiving()).ifPresent((cap) -> {
            if ((event.getSource() == RegenSources.REGEN_DMG_CRITICAL || event.getSource() == RegenSources.REGEN_DMG_KILLED)) {
                cap.setTrait(RegenTraitRegistry.BORING.get());
                if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                    cap.extractRegens(cap.regens());
                }
                if (event.getEntityLiving() instanceof ServerPlayerEntity)
                    cap.syncToClients((ServerPlayerEntity) event.getEntityLiving());
                return;
            }
            if (cap.stateManager() == null) return;
            boolean notDead = cap.stateManager().onKilled(event.getSource());
            event.setCanceled(notDead);
        });

    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        RegenCap.get(livingEntity).ifPresent((data) -> event.setCanceled(data.regenState() == RegenStates.REGENERATING));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Capability.IStorage<IRegen> storage = RegenCap.CAPABILITY.getStorage();
        event.getOriginal().revive();
        RegenCap.get(event.getOriginal()).ifPresent((old) -> RegenCap.get(event.getPlayer()).ifPresent((data) -> {
            CompoundNBT nbt = (CompoundNBT) storage.writeNBT(RegenCap.CAPABILITY, old, null);
            storage.readNBT(RegenCap.CAPABILITY, data, null, nbt);
        }));
    }

    @SubscribeEvent
    public static void onTrackPlayer(PlayerEvent.StartTracking startTracking) {
        RegenCap.get(startTracking.getPlayer()).ifPresent(iRegen -> {
            iRegen.syncToClients(null);
        });
    }

    @SubscribeEvent
    public static void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getPlayer().level.isClientSide) return;
        RegenCap.get(e.getPlayer()).ifPresent((data) -> data.stateManager().onPunchBlock(e));
    }

    @SubscribeEvent
    public static void onLive(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        RegenCap.get(livingUpdateEvent.getEntityLiving()).ifPresent(IRegen::tick);

        if (livingUpdateEvent.getEntityLiving() instanceof ServerPlayerEntity) {
            if (shouldGiveCouncilAdvancement((ServerPlayerEntity) livingUpdateEvent.getEntity())) {
                TriggerManager.COUNCIL.trigger((ServerPlayerEntity) livingUpdateEvent.getEntityLiving());
            }
        }
    }

    public static boolean shouldGiveCouncilAdvancement(ServerPlayerEntity serverPlayerEntity) {
        EquipmentSlotType[] equipmentSlotTypes = new EquipmentSlotType[]{EquipmentSlotType.HEAD,
                EquipmentSlotType.CHEST,
                EquipmentSlotType.LEGS,
                EquipmentSlotType.FEET};
        for (EquipmentSlotType equipmentSlotType : equipmentSlotTypes) {
            if (!serverPlayerEntity.getItemBySlot(equipmentSlotType).getItem().getRegistryName().getPath().contains("robes")) {
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
        if (event.getItemStack().getItem() instanceof ToolItem || event.getItemStack().getItem() instanceof SwordItem) {
            PlayerEntity player = event.getPlayer();
            RegenCap.get(player).ifPresent((data) -> {
                if (data.regenState() == RegenStates.POST && player.isShiftKeyDown() & data.handState() == IRegen.Hand.NO_GONE) {
                    HandItem.createHand(player);
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void space(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            ChunkGenerator chunkGenerator = serverWorld.getChunkSource().getGenerator();

            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(chunkGenerator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                Regeneration.LOG.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
                return;
            }
            //Only spawn Huts in the Overworld structure list
            if (serverWorld.dimension().equals(World.OVERWORLD)) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
                tempMap.put(RStructures.Structures.HUTS.get(), DimensionStructuresSettings.DEFAULTS.get(RStructures.Structures.HUTS.get()));
                serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
        }
    }

    @SubscribeEvent
    public static void onLoad(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
            RegenUtil.versionCheck(playerEntity);
        }
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent biomeLoadingEvent) {
        Biome.Category biomeCategory = biomeLoadingEvent.getCategory();

        if (biomeCategory != Biome.Category.ICY && biomeCategory != Biome.Category.MUSHROOM && biomeCategory != Biome.Category.JUNGLE && biomeCategory != Biome.Category.OCEAN && biomeCategory != Biome.Category.RIVER && biomeCategory != Biome.Category.DESERT) {
            biomeLoadingEvent.getGeneration().getStructures().add(() -> RStructures.ConfiguredStructures.CONFIGURED_HUTS);
            Regeneration.LOG.info("Added Huts to: " + biomeLoadingEvent.getName());
        }

        if (biomeCategory != Biome.Category.NETHER && biomeCategory != Biome.Category.THEEND) {
            biomeLoadingEvent.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RStructures.GAl_ORE);
        }
    }

}
