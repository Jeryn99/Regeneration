package me.suff.regeneration.handlers;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.capability.RegenerationProvider;
import me.suff.regeneration.debugger.DummyRegenDebugger;
import me.suff.regeneration.debugger.GraphicalRegenDebugger;
import me.suff.regeneration.util.PlayerUtil;
import me.suff.regeneration.util.RegenConfigNew;
import me.suff.regeneration.util.RegenState;
import me.suff.regeneration.util.RegenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.awt.*;

import static me.suff.regeneration.RegenerationMod.DEBUGGER;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class RegenEventHandler {
	
	@SubscribeEvent
	public static void serverStart(FMLServerStartingEvent event) {
		//	event.registerServerCommand(new RegenDebugCommand());
		
		DEBUGGER = GraphicsEnvironment.isHeadless() ? new DummyRegenDebugger() : new GraphicalRegenDebugger();
		MinecraftForge.EVENT_BUS.register(DEBUGGER);
	}
	
	@SubscribeEvent
	public static void serverStop(FMLServerStoppingEvent event) {
		MinecraftForge.EVENT_BUS.unregister(DEBUGGER);
		DEBUGGER.dispose();
		DEBUGGER = null;
	}
	
	
	// =========== CAPABILITY HANDLING =============
	
	@SubscribeEvent
	public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer)
			CapabilityRegeneration.getForPlayer((EntityPlayer) event.getEntityLiving()).tick();
	}
	
	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(CapabilityRegeneration.CAP_REGEN_ID, new RegenerationProvider(new CapabilityRegeneration((EntityPlayer) event.getObject())));
		}
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		IStorage<IRegeneration> storage = CapabilityRegeneration.CAPABILITY.getStorage();
		
		IRegeneration oldCap = CapabilityRegeneration.getForPlayer(event.getOriginal());
		IRegeneration newCap = CapabilityRegeneration.getForPlayer(event.getEntityPlayer());
		
		NBTTagCompound nbt = (NBTTagCompound) storage.writeNBT(CapabilityRegeneration.CAPABILITY, oldCap, null);
		storage.readNBT(CapabilityRegeneration.CAPABILITY, newCap, null, nbt);
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).synchronise();
	}
	
	@SubscribeEvent
	public static void playerTracking(PlayerEvent.StartTracking event) {
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).synchronise();
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		if (!RegenConfigNew.COMMON.firstStartGiftOnly.get())
			CapabilityRegeneration.getForPlayer(event.player).receiveRegenerations(RegenConfigNew.COMMON.freeRegenerations.get());
		
		CapabilityRegeneration.getForPlayer(event.player).synchronise();
	}
	
	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		CapabilityRegeneration.getForPlayer(event.player).synchronise();
	}
	
	@SubscribeEvent
	public static void onDeathEvent(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			CapabilityRegeneration.getForPlayer((EntityPlayer) e.getEntityLiving()).synchronise();
		}
	}
	
	// ============ USER EVENTS ==========
	
	@SubscribeEvent
	public static void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
		if (e.getEntityPlayer().world.isRemote)
			return;
		CapabilityRegeneration.getForPlayer(e.getEntityPlayer()).getStateManager().onPunchBlock(e);
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHurt(LivingDamageEvent event) {
		Entity trueSource = event.getSource().getTrueSource();
		
		if (trueSource instanceof EntityPlayer && event.getEntityLiving() instanceof EntityLiving) {
			EntityPlayer player = (EntityPlayer) trueSource;
			CapabilityRegeneration.getForPlayer(player).getStateManager().onPunchEntity(event);
			return;
		}
		
		if (!(event.getEntity() instanceof EntityPlayer) || event.getSource() == RegenObjects.REGEN_DMG_CRITICAL)
			return;
		
		EntityPlayer player = (EntityPlayer) event.getEntity();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
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
		
		if (cap.getState() == RegenState.REGENERATING && RegenConfig.regenFireImmune && event.getSource().isFireDamage()) {
			event.setCanceled(true); // TODO still "hurts" the client view
		} else if (player.getHealth() + player.getAbsorptionAmount() - event.getAmount() <= 0) { // player has actually died
			boolean notDead = cap.getStateManager().onKilled(event.getSource());
			event.setCanceled(notDead);
		}
	}
	
	
	@SubscribeEvent
	public static void onKnockback(LivingKnockBackEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			if (CapabilityRegeneration.getForPlayer((EntityPlayer) event.getEntityLiving()).getState() == RegenState.REGENERATING) {
				event.setCanceled(true);
			}
		}
	}
	
	// ================ OTHER ==============
	@SubscribeEvent
	public static void onLogin(PlayerLoggedInEvent event) {
		if (event.player.world.isRemote)
			return;
		
		NBTTagCompound nbt = event.player.getEntityData(), persist = (NBTTagCompound) nbt.getTag(EntityPlayer.PERSISTED_NBT_TAG);
		if (!persist.getBoolean("loggedInBefore"))
			CapabilityRegeneration.getForPlayer(event.player).receiveRegenerations(RegenConfigNew.COMMON.freeRegenerations.get());
		persist.setBoolean("loggedInBefore", true);
		nbt.setTag(EntityPlayer.PERSISTED_NBT_TAG, persist);
	}
	
	@SubscribeEvent
	public static void registerLoot(LootTableLoadEvent event) {
		if (!event.getName().toString().toLowerCase().matches(RegenConfig.loot.lootRegex) || RegenConfig.loot.disableLoot)
			return;
		
		// TODO configurable chances? Maybe by doing a simple loot table tutorial?
		LootEntryTable entry = new LootEntryTable(RegenerationMod.LOOT_FILE, 1, 0, new LootCondition[0], "regeneration_inject_entry");
		LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(1), "regeneration_inject_pool");
		event.getTable().addPool(pool);
	}
	
	
}
