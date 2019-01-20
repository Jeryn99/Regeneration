package me.fril.regeneration.handlers;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.capability.RegenerationProvider;
import me.fril.regeneration.util.RegenState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class RegenEventHandler {
	
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
		IRegeneration cap = CapabilityRegeneration.getForPlayer(event.getEntityPlayer());
		
		NBTTagCompound nbt = (NBTTagCompound) storage.writeNBT(CapabilityRegeneration.CAPABILITY, cap, null);
		storage.readNBT(CapabilityRegeneration.CAPABILITY, cap, null, nbt);
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).synchronise();
	}
	
	@SubscribeEvent
	public static void playerTracking(PlayerEvent.StartTracking event) {
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).synchronise(); //SUB do we need this?
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		if (!RegenConfig.firstStartGiftOnly)
			CapabilityRegeneration.getForPlayer(event.player).receiveRegenerations(RegenConfig.freeRegenerations);
		
		CapabilityRegeneration.getForPlayer(event.player).synchronise();
	}
	
	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		CapabilityRegeneration.getForPlayer(event.player).synchronise();
	}
	
	@SubscribeEvent
	public static void onDeathEvent(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			CapabilityRegeneration.getForPlayer((EntityPlayer) e.getEntityLiving()).synchronise(); //SOON test this
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
			CapabilityRegeneration.getForPlayer(player).getStateManager().onPunchEntity(event.getEntityLiving());
		}
		
		if (!(event.getEntity() instanceof EntityPlayer))
			return;
		
		if (event.getSource() == RegenObjects.REGEN_DMG_CRITICAL) {
			return;
		}
		
		EntityPlayer player = (EntityPlayer) event.getEntity();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (event.getSource() != RegenObjects.REGEN_DMG_CRITICAL) {
			cap.setDeathSource(event.getSource().getDeathMessage(player).getUnformattedText());
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
		
		NBTTagCompound nbt = event.player.getEntityData(),
				persist = nbt.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if (!persist.getBoolean("loggedInBefore"))
			CapabilityRegeneration.getForPlayer(event.player).receiveRegenerations(RegenConfig.freeRegenerations);
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
	
	@SubscribeEvent
	public static void onRenderPlayer(RenderPlayerEvent.Pre e) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(e.getEntityPlayer());
		
		if (cap.getState() == RegenState.REGENERATING) {
			cap.getType().getRenderer().onRenderRegeneratingPlayerPre(cap.getType(), e, cap);
		}
	}
	
}
