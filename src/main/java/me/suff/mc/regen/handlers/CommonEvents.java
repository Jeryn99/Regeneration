package me.suff.mc.regen.handlers;

import com.mojang.brigadier.CommandDispatcher;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.commands.RegenCommand;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RSoundSchemes;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.common.world.gen.RStructures;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    /* Attach Capability to all LivingEntities */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent< Entity > event) {
        if (canBeGiven(event.getObject())) {
            event.addCapability(RConstants.CAP_REGEN_ID, new ICapabilitySerializable< CompoundNBT >() {
                final RegenCap regen = new RegenCap((LivingEntity) event.getObject());
                final LazyOptional< IRegen > regenInstance = LazyOptional.of(() -> regen);

                @Nonnull
                @Override
                public < T > LazyOptional< T > getCapability(@Nonnull Capability< T > cap, @javax.annotation.Nullable Direction side) {
                    return cap == RegenCap.CAPABILITY ? (LazyOptional< T >) regenInstance : LazyOptional.empty();
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
        boolean isliving = entity instanceof LivingEntity;
        boolean ignoresConfig = entity.getType() == REntities.TIMELORD.get() || entity.getType() == EntityType.PLAYER;

        if (isliving) {
            if (ignoresConfig) {
                return true;
            } else {
                return RegenConfig.COMMON.mobsHaveRegens.get();
            }
        }
        return false;
    }


    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        if (livingEntity == null) return;

        RegenCap.get(livingEntity).ifPresent(iRegen -> {

            Entity trueSource = event.getSource().getTrueSource();


            if (event.getSource().isFireDamage() && iRegen.getTrait().getRegistryName().toString().equals(Traits.FIRE.get().getRegistryName().toString())) {
                event.setCanceled(true);
                event.setAmount(0.0F);
                return;
            }

            if (trueSource instanceof PlayerEntity && event.getEntityLiving() != null) {
                PlayerEntity player = (PlayerEntity) trueSource;
                RegenCap.get(player).ifPresent((data) -> data.getStateManager().onPunchEntity(event));
            }

            // Stop certain damages
            if (event.getSource() == RegenSources.REGEN_DMG_KILLED)
                return;

            //Update Death Message
            iRegen.setDeathMessage(event.getSource().getDeathMessage(livingEntity).getString());

            //Stop falling for leap trait
            if (iRegen.getTrait().getRegistryName().toString().equals(Traits.LEAP.get().getRegistryName().toString())) {
                if (event.getSource() == DamageSource.FALL) {
                    event.setCanceled(true);
                    return;
                }
            }

            //Handle Post
            if (iRegen.getCurrentState() == RegenStates.POST && event.getSource() != DamageSource.OUT_OF_WORLD && event.getSource() != RegenSources.REGEN_DMG_HAND) {
                event.setAmount(1.5F);
                PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regen.messages.reduced_dmg"), true);
            }

            //Handle Death
            if (iRegen.getCurrentState() == RegenStates.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFireDamage() || iRegen.getCurrentState() == RegenStates.REGENERATING && event.getSource().isExplosion()) {
                event.setCanceled(true);
            } else if (livingEntity.getHealth() + livingEntity.getAbsorptionAmount() - event.getAmount() <= 0) { // player has actually died
                boolean notDead = iRegen.getStateManager().onKilled(event.getSource());
                event.setCanceled(notDead);
            }
        });
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        RegenCap.get(livingEntity).ifPresent((data) -> event.setCanceled(data.getCurrentState() == RegenStates.REGENERATING));
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Capability.IStorage< IRegen > storage = RegenCap.CAPABILITY.getStorage();
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
        if (e.getPlayer().world.isRemote) return;
        RegenCap.get(e.getPlayer()).ifPresent((data) -> data.getStateManager().onPunchBlock(e));
    }

    @SubscribeEvent
    public static void onLive(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        RegenCap.get(livingUpdateEvent.getEntityLiving()).ifPresent(IRegen::tick);
    }

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent event) {
        CommandDispatcher< CommandSource > dispatcher = event.getServer().getCommandManager().getDispatcher();
        RegenCommand.register(dispatcher);
    }

    @SubscribeEvent
    public static void onCut(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() instanceof ToolItem || event.getItemStack().getItem() instanceof SwordItem) {
            PlayerEntity player = event.getPlayer();
            RegenCap.get(player).ifPresent((data) -> {
                if (data.getCurrentState() == RegenStates.POST && player.isSneaking() & data.getHandState() == IRegen.Hand.NO_GONE) {
                    HandItem.createHand(player);
                }
            });
        }
    }


    /**
     * Adds the structure's spacing for modded code made dimensions so that the structure's spacing remains
     * correct in any dimension or worldtype instead of not spawning.
     * In {@link RStructures#setupStructure(Structure, StructureSeparationSettings, boolean)} we call {@link DimensionStructuresSettings#field_236191_b_}
     * but this sometimes does not work in code made dimensions.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            /* Prevent spawning our structure in Vanilla's superflat world as
             * people seem to want their superflat worlds free of modded structures.
             * Also, vanilla superflat is really tricky and buggy to work with as mentioned in WAObjects#registerConfiguredStructure
             * BiomeModificationEvent does not seem to fire for superflat biomes...you can't add structures to superflat without mixin it seems.
             * */
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }
            //Only spawn Huts in the Overworld structure list
            if (serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                Map< Structure< ? >, StructureSeparationSettings > tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
                tempMap.put(RStructures.Structures.HUTS.get(), DimensionStructuresSettings.field_236191_b_.get(RStructures.Structures.HUTS.get()));
                serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
            }
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
            biomeLoadingEvent.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RStructures.GAl_ORE);
        }
    }

}
