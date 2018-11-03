package me.fril.regeneration.common.handlers;

import static me.fril.regeneration.common.capability.CapabilityRegeneration.*;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.capability.RegenerationProvider;
import me.fril.regeneration.util.RegenConfig;
import me.fril.regeneration.util.RegenObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
public class RegenerationHandler {
	
	@SubscribeEvent
	public static void breakBlock(PlayerInteractEvent.LeftClickBlock e) {
		EntityPlayer player = e.getEntityPlayer();
		IRegeneration regenInfo = CapabilityRegeneration.getForPlayer(player);
		boolean inGracePeriod = regenInfo.isInGracePeriod() && regenInfo.isGlowing();
		
		if (inGracePeriod) {
			regenInfo.setGlowing(false);
			regenInfo.setTicksGlowing(0);
			regenInfo.sync();
		}
	}
	
	@SubscribeEvent
	public static void registerLoot(LootTableLoadEvent e) {
		if (!e.getName().toString().toLowerCase().matches(RegenConfig.Loot.lootRegex) || RegenConfig.Loot.disableLoot)
			return;
		
		LootCondition[] condAlways = new LootCondition[] { new RandomChance(1F) };
		LootEntry entry = new LootEntryTable(new ResourceLocation(RegenerationMod.MODID, "inject/fob_watch_loot"), 1, 1, condAlways, "regeneration:fob-watch-entry");
		LootPool lootPool = new LootPool(new LootEntry[] { entry }, condAlways, new RandomValueRange(1), new RandomValueRange(1), "regeneration:fob-watch-pool");
		e.getTable().addPool(lootPool);
	}
	
	@SubscribeEvent
	public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IRegeneration handler = CapabilityRegeneration.getForPlayer(player);
			handler.update();
		}
	}
	
	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof EntityPlayer) {
			event.addCapability(REGEN_ID, new RegenerationProvider(new CapabilityRegeneration((EntityPlayer) event.getObject())));
		}
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		NBTTagCompound nbt = (NBTTagCompound) CapabilityRegeneration.CAPABILITY.getStorage().writeNBT(CapabilityRegeneration.CAPABILITY, CapabilityRegeneration.getForPlayer(event.getEntityPlayer()), null);
		CapabilityRegeneration.CAPABILITY.getStorage().readNBT(CapabilityRegeneration.CAPABILITY, CapabilityRegeneration.getForPlayer(event.getEntityPlayer()), null, nbt);
	}
	
	@SubscribeEvent
	public static void playerTracking(PlayerEvent.StartTracking event) {
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).sync();
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		CapabilityRegeneration.getForPlayer(event.player).sync();
	}
	
	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		CapabilityRegeneration.getForPlayer(event.player).sync();
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		CapabilityRegeneration.getForPlayer(event.player).sync();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHurt(LivingHurtEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer))
			return;
		
		EntityPlayer player = (EntityPlayer) e.getEntity();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (player.getHealth() + player.getAbsorptionAmount() - e.getAmount() > 0 ||
				!cap.isCapable() || cap.isRegenerating()) {
			
			if (cap.isRegenerating()) {
				//FIXME correctly handle death mid-regen
				cap.reset();
				
				//XXX does not work, I remember something about onHurt only firing on server, but how do I fix it?
				if (player.world.isRemote && player.getEntityId() == Minecraft.getMinecraft().player.getEntityId()) {
					//FIXME perspective not resetting when killed mid-regen
					Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
				}
			}
			return;
		}
		
		IRegeneration handler = CapabilityRegeneration.getForPlayer(player);
		e.setCanceled(true);
		handler.setRegenerating(true);
		
		player.clearActivePotions();
		player.extinguish();
		player.setArrowCountInEntity(0);
		
		if (handler.isRegenerating() && handler.isInGracePeriod()) {
			player.world.playSound(null, player.posX, player.posY, player.posZ, RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, 1.0F, 1.0F);
			handler.setInGracePeriod(false);
			handler.setSolaceTicks(199);
		}
	}
	
	@SubscribeEvent
	public static void onLogin(PlayerLoggedInEvent e) {
		if (!RegenConfig.startAsTimelord || e.player.world.isRemote)
			return;
		
		NBTTagCompound nbt = e.player.getEntityData();
		boolean loggedInBefore = nbt.getBoolean("loggedInBefore");
		if (!loggedInBefore) {
			e.player.inventory.addItemStackToInventory(new ItemStack(RegenObjects.Items.FOB_WATCH));
			nbt.setBoolean("loggedInBefore", true);
		}
	}
}
