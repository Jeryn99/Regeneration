package me.suff.regeneration.handlers;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.commands.CommandRegen;
import me.suff.regeneration.debugger.DummyRegenDebugger;
import me.suff.regeneration.debugger.GraphicalRegenDebugger;
import me.suff.regeneration.util.PlayerUtil;
import me.suff.regeneration.util.RegenState;
import me.suff.regeneration.util.RegenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

import static me.suff.regeneration.RegenerationMod.DEBUGGER;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RegenEventHandler {
	
	// =========== CAPABILITY HANDLING =============
	
	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityPlayer && living.isAlive())
			CapabilityRegeneration.getForPlayer((EntityPlayer) living).ifPresent(IRegeneration::tick);
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		IStorage<IRegeneration> storage = CapabilityRegeneration.CAPABILITY.getStorage();
		CapabilityRegeneration.getForPlayer(event.getOriginal()).ifPresent((old) -> CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).ifPresent((data) -> {
			NBTTagCompound nbt = (NBTTagCompound) storage.writeNBT(CapabilityRegeneration.CAPABILITY, old, null);
			storage.readNBT(CapabilityRegeneration.CAPABILITY, data, null, nbt);
		}));
	}
	
	@SubscribeEvent
	public void playerTracking(PlayerEvent.StartTracking event) {
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).ifPresent(IRegeneration::sync);
	}
	
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		CapabilityRegeneration.getForPlayer(event.getPlayer()).ifPresent(IRegeneration::sync);
	}
	
	@SubscribeEvent
	public void onDeathEvent(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			CapabilityRegeneration.getForPlayer((EntityPlayer) e.getEntityLiving()).ifPresent(IRegeneration::sync);
		}
	}
	
	@SubscribeEvent
	public void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
		if (e.getEntityPlayer().world.isRemote)
			return;
		CapabilityRegeneration.getForPlayer(e.getEntityPlayer()).ifPresent((cap) -> cap.getStateManager().onPunchBlock(e));
		
	}
	
	// ============ USER EVENTS ==========
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHurt(LivingDamageEvent event) {
		Entity trueSource = event.getSource().getTrueSource();
		
		if (trueSource instanceof EntityPlayer && event.getEntityLiving() instanceof EntityLiving) {
			EntityPlayer player = (EntityPlayer) trueSource;
			CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
				cap.getStateManager().onPunchEntity(event);
			});
			return;
		}
		
		if (!(event.getEntity() instanceof EntityPlayer) || event.getSource() == RegenObjects.REGEN_DMG_CRITICAL)
			return;
		
		EntityPlayer player = (EntityPlayer) event.getEntity();
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			cap.setDeathSource(event.getSource().getDeathMessage(player).getUnformattedComponentText());
			if (cap.getState() == RegenState.POST && player.posY > 0) {
				if (event.getSource() == DamageSource.FALL) {
					PlayerUtil.applyPotionIfAbsent(player, MobEffects.NAUSEA, 200, 4, false, false);
					if (event.getAmount() > 8.0F) {
						if (player.world.getGameRules().getBoolean("mobGriefing")) {
							RegenUtil.genCrater(player.world, player.getPosition(), 3);
						}
						event.setAmount(0.5F);
						PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.fall_dmg"), true);
						return;
					}
				} else {
					event.setAmount(0.5F);
					PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.reduced_dmg"), true);
				}
				return;
			}
			
			if (cap.getState() == RegenState.REGENERATING && RegenConfig.COMMON.regenFireImmune.get() && event.getSource().isFireDamage()) {
				event.setCanceled(true); // TODO still "hurts" the client view
			} else if (player.getHealth() + player.getAbsorptionAmount() - event.getAmount() <= 0) { // player has actually died
				boolean notDead = cap.getStateManager().onKilled(event.getSource());
				event.setCanceled(notDead);
			}
			
		});
		
	}
	
	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			CapabilityRegeneration.getForPlayer((EntityPlayer) event.getEntityLiving()).ifPresent((cap) -> {
				if (cap.getState() == RegenState.REGENERATING) {
					event.setCanceled(true);
				}
			});
		}
	}
	
	@SubscribeEvent
	public void registerLoot(LootTableLoadEvent event) {
		if (!event.getName().toString().toLowerCase().matches(RegenConfig.COMMON.lootRegex.get()) || RegenConfig.COMMON.disableLoot.get())
			return;
		
		// TODO configurable chances? Maybe by doing a simple loot table tutorial?
		LootEntryTable entry = new LootEntryTable(RegenerationMod.LOOT_FILE, 1, 0, new LootCondition[0], "regeneration_inject_entry");
		LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(1), "regeneration_inject_pool");
		event.getTable().addPool(pool);
	}
	
	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event) {
		CommandRegen.register(event.getCommandDispatcher());
		
		DEBUGGER = GraphicsEnvironment.isHeadless() ? new DummyRegenDebugger() : new GraphicalRegenDebugger();
		MinecraftForge.EVENT_BUS.register(DEBUGGER);
	}
	
	@SubscribeEvent
	public void serverStop(FMLServerStoppingEvent event) {
		MinecraftForge.EVENT_BUS.unregister(DEBUGGER);
		DEBUGGER.dispose();
		DEBUGGER = null;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity.getClass().equals(EntityItem.class)) {
			ItemStack stack = ((EntityItem) entity).getItem();
			Item item = stack.getItem();
			if (item.hasCustomEntity(stack)) {
				Entity newEntity = item.createEntity(event.getWorld(), entity, stack);
				if (newEntity != null) {
					entity.remove();
					event.setCanceled(true);
					event.getWorld().spawnEntity(newEntity);
				}
			}
		}
	}
	
	public static class WHYGOD {
		@SubscribeEvent
		public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof EntityPlayer) {
				event.addCapability(CapabilityRegeneration.CAP_REGEN_ID, new ICapabilitySerializable<NBTTagCompound>() {
					final CapabilityRegeneration regen = new CapabilityRegeneration((EntityPlayer) event.getObject());
					
					final LazyOptional<IRegeneration> regenInstance = LazyOptional.of(() -> regen);
					
					@Override
					public NBTTagCompound serializeNBT() {
						return regen.serializeNBT();
					}
					
					@Override
					public void deserializeNBT(NBTTagCompound nbt) {
						regen.deserializeNBT(nbt);
					}
					
					@Nullable
					@SuppressWarnings("unchecked")
					@Override
					public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
						if (capability == CapabilityRegeneration.CAPABILITY)
							return (LazyOptional<T>) regenInstance;
						return LazyOptional.empty();
					}
				});
			}
		}
	}
	
	
}
