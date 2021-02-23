package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.common.types.RegenTypes;
import me.swirtzly.regeneration.compat.ArchHelper;
import me.swirtzly.regeneration.util.common.LootUtils;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;

/**
 * Created by Sub on 16/09/2018.
 */
public class CommonHandler {


    @SubscribeEvent
    public void registerDim(RegisterDimensionsEvent event) {
        RegenObjects.GALLIFREY_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation(Regeneration.MODID, "gallifrey"), RegenObjects.Dimensions.GALLIFREY.get(), null, true);
    }

    // =========== CAPABILITY HANDLING =============

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        RegenCap.get(event.getEntityLiving()).ifPresent(IRegen::tick);
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        IStorage< IRegen > storage = RegenCap.CAPABILITY.getStorage();
        event.getOriginal().revive();
        RegenCap.get(event.getOriginal()).ifPresent((old) -> RegenCap.get(event.getPlayer()).ifPresent((data) -> {
            CompoundNBT nbt = (CompoundNBT) storage.writeNBT(RegenCap.CAPABILITY, old, null);
            storage.readNBT(RegenCap.CAPABILITY, data, null, nbt);
        }));
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent e) {
        if (e.getName().toString().toLowerCase().contains("minecraft:chests/")) {
            LootTable lootTable = e.getTable();
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.ROBES_HEAD.get(), 6, 0.5f, 0, 2, "robes_head");
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.GUARD_CHEST.get(), 6, 0.2f, 0, 1, "guard_chest");
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.GUARD_FEET.get(), 6, 0.3f, 0, 1, "guard_feet");
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.GUARD_LEGGINGS.get(), 6, 0.3f, 0, 1, "guard_leggings");
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.GUARD_HEAD.get(), 6, 0.2f, 0, 2, "guard_head");
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.DIAL.get(), 4, 0.3f, 0, 1, "dial");
            LootUtils.addItemToTable(lootTable, RegenObjects.Items.ROBES_CHEST.get(), 6, 0.5f, 0, 2, "robes_chest");
        }
    }

    @SubscribeEvent
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract entityInteract) {
        LivingEntity interactor = entityInteract.getEntityLiving();
        Entity interactedWith = entityInteract.getTarget();

        if (interactedWith instanceof LivingEntity) {
            LivingEntity toBeTimelord = (LivingEntity) interactedWith;
            ItemStack interactorsItem = interactor.getHeldItemMainhand();

            if (!toBeTimelord.world.isRemote && interactorsItem.getItem() == RegenObjects.Items.ARCH_PART.get() && ArchHelper.getRegenerations(interactorsItem) > 0) {
                if (interactor.isSneaking()) {
                    entityInteract.setCanceled(true);
                    RegenCap.get(toBeTimelord).ifPresent((data) -> {
                        int amount = ArchHelper.getRegenerations(interactorsItem);
                        ArchHelper.storeRegenerations(interactorsItem, 0);
                        data.receiveRegenerations(amount);
                        data.setRegenType(RegenTypes.HARTNELL);
                        data.synchronise();
                        PlayerUtil.sendMessage(interactor, new TranslationTextComponent("message.regeneration.transferred_regens", interactedWith.getName(), amount), true);
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTracked(PlayerEvent.StartTracking event) {
        RegenCap.get(event.getPlayer()).ifPresent(IRegen::synchronise);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        RegenCap.get(event.getPlayer()).ifPresent(IRegen::synchronise);
    }

    @SubscribeEvent
    public void onDeathEvent(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            RegenCap.get(e.getEntityLiving()).ifPresent((data) -> {
                if (data.getRegenerationsLeft() == 0) {
                    TraitManager.IDna trait = TraitManager.getDnaEntry(data.getTrait());
                    trait.onRemoved(data);
                    data.setTrait(TraitManager.DNA_BORING.getRegistryName());
                    TraitManager.DNA_BORING.onAdded(data);
                }
                data.synchronise();
            });
        }
    }

    @SubscribeEvent
    public void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getPlayer().world.isRemote) return;
        RegenCap.get(e.getPlayer()).ifPresent((data) -> data.getStateManager().onPunchBlock(e));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();

        if (trueSource instanceof PlayerEntity && event.getEntityLiving() instanceof MobEntity) {
            PlayerEntity player = (PlayerEntity) trueSource;
            RegenCap.get(player).ifPresent((data) -> data.getStateManager().onPunchEntity(event));
            //return;
        }

        if (event.getSource() == RegenObjects.REGEN_DMG_CRITICAL || event.getSource() == RegenObjects.REGEN_DMG_KILLED)
            return;

        LivingEntity player = event.getEntityLiving();
        RegenCap.get(player).ifPresent((cap) -> {

            cap.setDeathSource(event.getSource().getDeathMessage(player).getUnformattedComponentText());

            if (cap.getState() == PlayerUtil.RegenState.POST && player.posY > 0) {
                if (event.getSource() == DamageSource.FALL) {
                    PlayerUtil.applyPotionIfAbsent(player, Effects.NAUSEA, 200, 4, false, false);
                    if (event.getAmount() > 8.0F) {
                        if (player.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && RegenConfig.COMMON.genCrater.get()) {
                            RegenUtil.genCrater(player.world, player.getPosition(), 3);
                        }
                        event.setAmount(0.5F);
                        PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.fall_dmg"), true);
                        return;
                    }
                } else {
                    event.setAmount(0.5F);
                    PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.reduced_dmg"), true);

                    if (event.getSource().getTrueSource() instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) event.getSource().getTrueSource();
                        if (PlayerUtil.isSharp(livingEntity.getHeldItemMainhand())) {
                            if (!cap.hasDroppedHand() && cap.getState() == PlayerUtil.RegenState.POST) {
                                PlayerUtil.createHand(player);
                            }
                        }
                    }

                }
                return;
            }

            if (cap.getLivingEntity() instanceof TimelordEntity) {
                if (event.getSource() == DamageSource.FALL) {
                    return;
                }
            }

            if (cap.getState() == PlayerUtil.RegenState.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFireDamage() || cap.getState() == PlayerUtil.RegenState.REGENERATING && event.getSource().isExplosion()) {
                event.setCanceled(true); // TODO still "hurts" the client view
            } else if (player.getHealth() + player.getAbsorptionAmount() - event.getAmount() <= 0) { // player has actually died
                boolean notDead = cap.getStateManager().onKilled(event.getSource());
                event.setCanceled(notDead);
            }
        });
    }

    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        RegenCap.get(livingEntity).ifPresent((data) -> {
            if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                event.setCanceled(true);
            }
        });
    }


    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent< Entity > event) {
        if (canBeGiven(event.getObject())) {
            event.addCapability(RegenCap.CAP_REGEN_ID, new ICapabilitySerializable< CompoundNBT >() {
                final RegenCap regen = new RegenCap((LivingEntity) event.getObject());
                final LazyOptional< IRegen > regenInstance = LazyOptional.of(() -> regen);

                @Nonnull
                @Override
                public < T > LazyOptional< T > getCapability(@Nonnull Capability< T > cap, @javax.annotation.Nullable Direction side) {
                    if (cap == RegenCap.CAPABILITY) return (LazyOptional< T >) regenInstance;
                    return LazyOptional.empty();
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

    public static boolean canBeGiven(Entity entity){
        boolean isliving = entity instanceof LivingEntity;
        boolean ignoresConfig = entity.getType() == RegenObjects.EntityEntries.TIMELORD.get() || entity.getType() == EntityType.PLAYER;

        if(isliving){
            if(ignoresConfig){
                return true;
            } else {
                return RegenConfig.COMMON.mobsHaveRegens.get();
            }
        }
        return false;
    }

    //I hate this.
    @SubscribeEvent
    public void fix(LivingSpawnEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (!(living instanceof TimelordEntity)) {
            RegenCap.get(living).ifPresent(iRegen -> iRegen.setRegenerationsLeft(0));
        }
    }

    @SubscribeEvent
    public void onCut(PlayerInteractEvent.RightClickItem event) {
        if (PlayerUtil.isSharp(event.getItemStack())) {
            PlayerEntity player = event.getPlayer();
            RegenCap.get(player).ifPresent((data) -> {
                if (data.getState() == PlayerUtil.RegenState.POST && !data.hasDroppedHand()) {
                    PlayerUtil.createHand(player);
                }
            });
        }
    }
}
