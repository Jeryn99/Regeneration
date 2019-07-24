package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.RegenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

import javax.annotation.Nonnull;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class RegenEventHandler {
	
	// =========== CAPABILITY HANDLING =============
	
	@SubscribeEvent
	public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			CapabilityRegeneration.getForPlayer(player).ifPresent(IRegeneration::tick);

		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		IStorage<IRegeneration> storage = CapabilityRegeneration.CAPABILITY.getStorage();
		event.getOriginal().revive();
		CapabilityRegeneration.getForPlayer(event.getOriginal()).ifPresent((old) -> CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).ifPresent((data) -> {
			CompoundNBT nbt = (CompoundNBT) storage.writeNBT(CapabilityRegeneration.CAPABILITY, old, null);
			storage.readNBT(CapabilityRegeneration.CAPABILITY, data, null, nbt);
		}));
	}

	@SubscribeEvent
	public void onPlayerTracked(PlayerEvent.StartTracking event) {
		CapabilityRegeneration.getForPlayer(event.getEntityPlayer()).ifPresent(IRegeneration::synchronise);
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
	//	if (!RegenConfig.COMMON.firstStartGiftOnly)
	//		CapabilityRegeneration.getForPlayer(event.getPlayer()).receiveRegenerations(RegenConfig.freeRegenerations);

		CapabilityRegeneration.getForPlayer(event.getPlayer()).ifPresent(IRegeneration::synchronise);
	}
	
	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		CapabilityRegeneration.getForPlayer(event.getPlayer()).ifPresent(IRegeneration::synchronise);
	}
	
	@SubscribeEvent
	public static void onDeathEvent(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof PlayerEntity) {
			CapabilityRegeneration.getForPlayer((PlayerEntity) e.getEntityLiving()).ifPresent(IRegeneration::synchronise);
		}
	}
	
	// ============ USER EVENTS ==========
	
	@SubscribeEvent
	public static void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
		if (e.getEntityPlayer().world.isRemote)
			return;

		CapabilityRegeneration.getForPlayer(e.getEntityPlayer()).ifPresent((data) -> data.getStateManager().onPunchBlock(e));

	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHurt(LivingHurtEvent event) {
		Entity trueSource = event.getSource().getTrueSource();
		
		if (trueSource instanceof PlayerEntity && event.getEntityLiving() instanceof MobEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;
			CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> data.getStateManager().onPunchEntity(event));
			return;
		}

		if (!(event.getEntity() instanceof PlayerEntity) || event.getSource() == RegenObjects.REGEN_DMG_CRITICAL || event.getSource() == RegenObjects.REGEN_DMG_KILLED)
			return;
		
		PlayerEntity player = (PlayerEntity) event.getEntity();
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {

			cap.setDeathSource(event.getSource().getDeathMessage(player).getUnformattedComponentText());

			if (cap.getState() == PlayerUtil.RegenState.POST && player.posY > 0) {
				if (event.getSource() == DamageSource.FALL) {
					PlayerUtil.applyPotionIfAbsent(player, Effects.NAUSEA, 200, 4, false, false);
					if (event.getAmount() > 8.0F) {
						if (player.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && RegenConfig.COMMON.genGreator) {
							RegenUtil.genCrater(player.world, player.getPosition(), 3);
						}
						event.setAmount(0.5F);
						PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.fall_dmg"), true);
						return;
					}
				} else {
					event.setAmount(0.5F);
					PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.reduced_dmg"), true);
				}
				return;
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
	public static void onKnockback(LivingKnockBackEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> {
				if(data.getState() == PlayerUtil.RegenState.REGENERATING){
					event.setCanceled(true);
				}
			});
		}
	}


	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(CapabilityRegeneration.CAP_REGEN_ID, new ICapabilitySerializable<CompoundNBT>() {
				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
					if (cap == CapabilityRegeneration.CAPABILITY)
						return (LazyOptional<T>) regenInstance;
					return LazyOptional.empty();
				}

				final CapabilityRegeneration regen = new CapabilityRegeneration((PlayerEntity) event.getObject());

				final LazyOptional<IRegeneration> regenInstance = LazyOptional.of(() -> regen);

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


	
	// ================ OTHER ==============
	@SubscribeEvent
	public static void onLogin(PlayerLoggedInEvent event) {
		if (event.getPlayer().world.isRemote)
			return;
		
		CompoundNBT nbt = event.getPlayer().getEntityData(), persist = nbt.get(PlayerEntity.PERSISTED_NBT_TAG);
		if (!persist.getBoolean("loggedInBefore"))
			CapabilityRegeneration.getForPlayer(event.getPlayer()).receiveRegenerations(RegenConfig.freeRegenerations);
		persist.putBoolean("loggedInBefore", true);
		nbt.put(PlayerEntity.PERSISTED_NBT_TAG, persist);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity.getClass().equals(ItemEntity.class)) {
			ItemStack stack = ((ItemEntity) entity).getItem();
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
	
	/**
	 * Update checker thing, tells the player that the mods out of date if they're on a old build
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent e) {
		PlayerEntity player = e.player;
		if (!player.world.isRemote && RegenConfig.enableUpdateChecker) {
			ForgeVersion.CheckResult version = ForgeVersion.getResult(Loader.instance().activeModContainer());
			if (version.status.equals(ForgeVersion.Status.OUTDATED)) {
				StringTextComponent url = new StringTextComponent(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "UPDATE");
				url.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.curseforge.com/projects/regeneration"));
				url.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Open URL")));
				
				player.sendMessage(new StringTextComponent(TextFormatting.GOLD + "[Regeneration] : ").appendSibling(url));
				String changes = version.changes.get(version.target);
				player.sendMessage(new StringTextComponent(TextFormatting.GOLD + "Changes: " + TextFormatting.BLUE + changes));
			}
		}
	}
	 */
}
