package me.fril.regeneration.common.capability;

import java.awt.Color;

import javax.annotation.Nonnull;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.types.IRegenType;
import me.fril.regeneration.common.types.RegenTypes;
import me.fril.regeneration.network.MessageSynchroniseRegeneration;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.Scheduler;
import me.fril.regeneration.util.Scheduler.ScheduledTask;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class CapabilityRegeneration implements IRegeneration {
	
	@CapabilityInject(IRegeneration.class)
	public static final Capability<IRegeneration> CAPABILITY = null;
	public static final ResourceLocation CAP_REGEN_ID = new ResourceLocation(RegenerationMod.MODID, "regeneration");
	//private static final UUID SLOWNESS_ID = UUID.fromString("f9aa2c36-f3f3-4d76-a148-86d6f2c87782");
	
	
	private final EntityPlayer player;
	private int regenerationsLeft;
	private IRegenType type = RegenTypes.FIERY;
	
	private RegenState state = RegenState.ALIVE;
	private RegenerationStateManager stateManager = new RegenerationStateManager();
	
	private float primaryRed = 0.93f, primaryGreen = 0.61f, primaryBlue = 0.0f;
	private float secondaryRed = 1f, secondaryGreen = 0.5f, secondaryBlue = 0.18f;
	
	//private AttributeModifier slownessModifier = new AttributeModifier(SLOWNESS_ID, "slow", -0.5D, 1);
	
	
	
	@Nonnull
	public static IRegeneration getForPlayer(EntityPlayer player) {
		if (player.hasCapability(CAPABILITY, null)) {
			return player.getCapability(CAPABILITY, null);
		}
		throw new IllegalStateException("Missing Regeneration capability: " + player + ", please report this to the issue tracker");
	}
	
	
	
	public CapabilityRegeneration() {
		this.player = null;
	}
	
	public CapabilityRegeneration(EntityPlayer player) {
		this.player = player;
	}
	
	
	
	
	
	@Override
	public void tick() {
		if (!player.world.isRemote) //ticking only on the server for simplicity
			stateManager.tick();
		
		if (state == RegenState.REGENERATING) {
			type.onUpdateMidRegen(player, this);
		}
	}
	
	private void setState(RegenState state) {
		this.state = state;
	}
	
	
	
	
	
	@Override
	public void synchronise() {
		NetworkHandler.INSTANCE.sendToAll(new MessageSynchroniseRegeneration(player, serializeNBT()));
	}

	@Override
	public NBTTagCompound serializeNBT() { //FIXME serialize scheduler
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("state", state.toString());
		nbt.setInteger("regenerationsLeft", regenerationsLeft);
		nbt.setTag("style", getStyle());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		setStyle(nbt.getCompoundTag("style"));
		setState(nbt.hasKey("state") ? RegenState.valueOf(nbt.getString("state")) : RegenState.ALIVE); //I need to check for versions before 1.2
		regenerationsLeft = nbt.getInteger("regenerationsLeft");
	}
	
	
	
	
	
	@Override
	public RegenState getState() {
		return state;
	}
	
	@Override
	public int getRegenerationsLeft() {
		return regenerationsLeft;
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}
	
	
	
	
	
	@Override
	public NBTTagCompound getStyle() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", primaryRed);
		nbt.setFloat("PrimaryGreen", primaryGreen);
		nbt.setFloat("PrimaryBlue", primaryBlue);
		
		nbt.setFloat("SecondaryRed", secondaryRed);
		nbt.setFloat("SecondaryGreen", secondaryGreen);
		nbt.setFloat("SecondaryBlue", secondaryBlue);
		return nbt;
	}
	
	@Override
	public void setStyle(NBTTagCompound nbt) {
		primaryRed = nbt.getFloat("PrimaryRed");
		primaryGreen = nbt.getFloat("PrimaryGreen");
		primaryBlue = nbt.getFloat("PrimaryBlue");
		
		secondaryRed = nbt.getFloat("SecondaryRed");
		secondaryGreen = nbt.getFloat("SecondaryGreen");
		secondaryBlue = nbt.getFloat("SecondaryBlue");
	}
	
	@Override
	public Color getPrimaryColor() {
		return new Color(primaryRed, primaryGreen, primaryBlue);
	}
	
	@Override
	public Color getSecondaryColor() {
		return new Color(secondaryRed, secondaryGreen, secondaryBlue);
	}
	
	
	
	
	
	@Override
	public void receiveRegenerations(int amount) {
		regenerationsLeft += amount;
		synchronise();
	}
	
	
	@Override
	public void extractRegeneration(int amount) {
		regenerationsLeft -= amount;
		synchronise();
	}
	
	
	
	
	
	@Override
	public void onRenderRegenerationLayer(RenderPlayer playerRenderer, IRegeneration cap, EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		type.onRenderRegenerationLayer(playerRenderer, this, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}
	
	@Override
	public void onRenderRegeneratingPlayerPre(RenderPlayerEvent.Pre event) {
		type.onRenderRegeneratingPlayerPre(event, this);
	}
	
	
	
	
	
	@Override
	public IRegenerationStateManager getStateManager() {
		return stateManager;
	}
	
	
	
	
	
	public class RegenerationStateManager implements IRegenerationStateManager { //FIXME synchronisation
		private final Scheduler scheduler;
		
		private ScheduledTask scheduledRegenerationTrigger, scheduledRegenerationFinish,
		                      scheduledGraceCritical, scheduledGraceGlowing, scheduledCriticalDeath;
		
		private RegenerationStateManager() {
			this.scheduler = new Scheduler();
		}
		
		@SideOnly(Side.SERVER)
		private void tick() {
			scheduler.tick();
			
			/*if (player.getHealth() < player.getMaxHealth()) { TODO actually regenerate health
				player.setHealth(player.getHealth() + 1); NOW move to external event handler
			}
			player.heal(2.0F);
			TODO random damage*/
		}
		
		
		
		//TODO post events to client?
		@SideOnly(Side.SERVER)
		private void triggerRegeneration() { //FUTURE do animation using a ticksLeft() method on the scheduled task
			//We're actually regenerating!
			state = RegenState.REGENERATING;
			scheduledGraceCritical.cancel(); //... cancel the transition to critical phase
			scheduledGraceGlowing.cancel(); //... cancel the scheduled glowing
			scheduledCriticalDeath.cancel(); //... cancel the scheduled critical death (FIXME this will NPE if never entered critical phase)
			scheduledRegenerationFinish = scheduler.schedule(10, this::finishRegeneration); //... schedule the finishing of the regeneration
			
			type.onStartRegeneration(player, CapabilityRegeneration.this);
			
			/*player.dismountRidingEntity(); NOW move to external event handler
			player.removePassengers();
			player.setAbsorptionAmount(RegenConfig.absorbtionLevel * 2);
			
			extractRegeneration(1);
			ExplosionUtil.regenerationExplosion(player);*/
			
			//TODO play music on client
			//PlayerUtil.playMovingSound(player, getType().getSound(), SoundCategory.PLAYERS);
			//TODO toast notification
		}
		
		@SideOnly(Side.SERVER)
		private void startGlowing() {
			//We're starting to glow...
			state = RegenState.GRACE_GLOWING;
			scheduledRegenerationTrigger = scheduler.schedule(20, this::triggerRegeneration); //... schedule letted regeneration in 20 seconds
			
			//this causes these things to happen each time the player starts to glow. It's a feature.
			/*player.clearActivePotions(); NOW move to external event handler
			player.extinguish();
			player.setArrowCountInEntity(0);*/
			
			//TODO play music on client
			//PlayerUtil.playMovingSound(player, RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS);
		}
		
		@SideOnly(Side.SERVER)
		private void enterCriticalPhase() {
			//We're entering critical phase...
			state = RegenState.GRACE_CRIT;
			scheduledCriticalDeath = scheduler.schedule(60, this::midSequenceKill);
			
			//TODO play music on client
			//TODO nausea
			/*PlayerUtil.playMovingSound(player, RegenObjects.Sounds.CRITICAL_STAGE, SoundCategory.PLAYERS);
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 800, 0, false, false)); // could be removed with milk, but I think that's not that bad*/
			//TODO toast notification
		}
		
		@SideOnly(Side.SERVER)
		private void midSequenceKill() {
			state = RegenState.ALIVE;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			
			player.setHealth(-1); //in case this method was called by the 15 minute counter
		}
		
		@SideOnly(Side.SERVER)
		private void finishRegeneration() {
			state = RegenState.ALIVE;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			
			/*if (RegenConfig.resetHunger) { NOW move to external event handler
				FoodStats foodStats = player.getFoodStats();
				foodStats.setFoodLevel(foodStats.getFoodLevel() + 1);
			}
			
			if (RegenConfig.resetOxygen) {
				player.setAir(player.getAir() + 1);
			}
			
			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.postRegenerationDuration * 2, RegenConfig.postRegenerationLevel - 1, false, false));*/
			
			/*if (player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) { TODO reimplement slowness
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_ID);
			}*/
		}
		
		
		
		
		@Override
		public boolean onKilled() { //TODO add invariant checkers
			if (state == RegenState.ALIVE) {
				
				//We're entering grace period...
				scheduledGraceCritical = scheduler.schedule(15 * 60, this::enterCriticalPhase); //... schedule the transition to critical phase in 15 minutes
				/*if (!player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) { TODO reimplement slowness
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slownessModifier); NOW move to external event handler
				}*/
				
				startGlowing();
				return true;
				
			} else if (state.isGraceful()) {
				
				//we're being forced to regenerate...
				scheduledRegenerationTrigger.cancel(); //... cancel the original regeneration trigger
				scheduledGraceCritical.cancel(); //... cancel the shift to critical phase
				scheduledGraceGlowing.cancel(); //... cancel the start of glowing
				
				triggerRegeneration();
				return true;
				
			} else if (state == RegenState.REGENERATING) {
				
				//We've been killed mid regeneration!
				scheduledRegenerationFinish.cancel(); //... cancel the finishing of the regeneration
				
				midSequenceKill();
				return false;
				
			} else throw new IllegalStateException("Unknown cap.getState(): "+state);
		}
		
		@Override
		public void onPunchBlock() {
			//We're punching away our glow...
			if (state == RegenState.GRACE_GLOWING || state == RegenState.GRACE_CRIT) { //... check if we're actually glowing
				scheduledRegenerationTrigger.cancel(); //... cancel the letted regeneration trigger
				scheduledGraceGlowing = scheduler.schedule(60, this::startGlowing); //... schedule the new glowing
				state = state == RegenState.GRACE_GLOWING ? RegenState.GRACE_STD : RegenState.GRACE_CRIT;
			}
		}
		
	}
	
}
