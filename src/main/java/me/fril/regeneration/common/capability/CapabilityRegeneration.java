package me.fril.regeneration.common.capability;

import java.awt.Color;

import javax.annotation.Nonnull;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.types.IRegenType;
import me.fril.regeneration.common.types.RegenTypes;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.network.MessageSynchroniseRegeneration;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.Scheduler;
import me.fril.regeneration.util.TimerChannel;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;

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
	private int regenerationsLeft, ticksRegenerating;
	private IRegenType type = RegenTypes.FIERY;
	
	private RegenState state = RegenState.ALIVE;
	private final RegenerationStateManager stateManager;
	
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
		this.stateManager = null;
	}
	
	public CapabilityRegeneration(EntityPlayer player) {
		this.player = player;
		if (!player.world.isRemote)
			this.stateManager = new RegenerationStateManager();
		else
			this.stateManager = null;
	}
	
	
	
	
	
	@Override
	public void tick() {
		if (!player.world.isRemote && state != RegenState.ALIVE) //ticking only on the server for simplicity
			stateManager.tick();
		
		if (state == RegenState.REGENERATING) {
		    ticksRegenerating++;
			type.onUpdateMidRegen(player, this);
		} else {
		    ticksRegenerating = 0;
        }
	}
	
	private void setState(RegenState state) {
		this.state = state;
	}
	
	
	
	
	
	@Override
	public void synchronise() {
		NBTTagCompound nbt = serializeNBT();
		nbt.removeTag("stateManager");
		NetworkHandler.INSTANCE.sendToAll(new MessageSynchroniseRegeneration(player, nbt));
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("state", state.toString());
		nbt.setInteger("regenerationsLeft", regenerationsLeft);
		nbt.setTag("style", getStyle());
        nbt.setInteger("ticks", ticksRegenerating);
		if (!player.world.isRemote)
			nbt.setTag("stateManager", stateManager.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		setState(nbt.hasKey("state") ? RegenState.valueOf(nbt.getString("state")) : RegenState.ALIVE); //I need to check for versions before 1.3 (TODO update this version comment)
		regenerationsLeft = nbt.getInteger("regenerationsLeft");
		setStyle(nbt.getCompoundTag("style"));
		ticksRegenerating = nbt.getInteger("ticks");
		if (nbt.hasKey("stateManager"))
			stateManager.deserializeNBT(nbt.getCompoundTag("stateManager"));
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
    public int getTicksRegenerating() {
        return ticksRegenerating;
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
	public Vec3d getPrimaryColor() {
		return new Vec3d(primaryRed, primaryGreen, primaryBlue);
	}
	
	@Override
	public Vec3d getSecondaryColor() {
		return new Vec3d(secondaryRed, secondaryGreen, secondaryBlue);
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
	
	
	
	
	
	
	
	
	
	
	
	
	public class RegenerationStateManager implements IRegenerationStateManager {
		
		private final Scheduler scheduler;
		//private IDebugChannel debugChannel;
		
		private RegenerationStateManager() {
			this.scheduler = new Scheduler(RegenerationMod.DEBUGGER.getChannelFor(player));
		}
		
		
		
		
		@Override
		public boolean onKilled() { //TODO add state-validity checkers
			if (state == RegenState.ALIVE) {
				
				//We're entering grace period...
				scheduler.scheduleInSeconds(TimerChannel.GRACE_CRITICAL, RegenConfig.Grace.gracePeriodLength, this::enterCriticalPhase); //... schedule the transition to critical phase
				
				/*if (!player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(slownessModifier)) { TODO reimplement slowness
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slownessModifier); NOW move to external event handler
				}*/
				
				startGlowing();
				return true;
				
			} else if (state.isGraceful()) {
				
				//we're being forced to regenerate...
				scheduler.cancel(TimerChannel.REGENERATION_TRIGGER); //... cancel the original regeneration trigger
				scheduler.cancel(TimerChannel.GRACE_CRITICAL); //... cancel the shift to critical phase
				scheduler.cancel(TimerChannel.GRACE_GLOWING); //... cancel the start of glowing
				
				triggerRegeneration();
				return true;
				
			} else if (state == RegenState.REGENERATING) {
				
				//We've been killed mid regeneration!
				scheduler.cancel(TimerChannel.REGENERATION_FINISH); //... cancel the finishing of the regeneration
				midSequenceKill();
				return false;
				
			} else throw new IllegalStateException("Unknown cap.getState(): "+state);
		}
		
		@Override
		public void onPunchBlock() {
			//We're punching away our glow...
			if (state == RegenState.GRACE_GLOWING || state == RegenState.GRACE_CRIT) { //... check if we're actually glowing
				state = state == RegenState.GRACE_GLOWING ? RegenState.GRACE_STD : RegenState.GRACE_CRIT;
				scheduler.cancel(TimerChannel.REGENERATION_TRIGGER); //... cancel the allowed regeneration trigger
				scheduler.scheduleInSeconds(TimerChannel.GRACE_GLOWING, RegenConfig.Grace.handGlowInterval, this::startGlowing); //... schedule the new glowing
				synchronise();
			}
		}

		@Override
		public void onPunchEntity(EntityLivingBase entity) {
			//We're punching away our glow and healing mobs...
			if (state == RegenState.GRACE_GLOWING || state == RegenState.GRACE_CRIT) { //... check if we're actually glowing
				if (entity.getHealth() < entity.getMaxHealth() && getState() == RegenState.GRACE_GLOWING) {
					float healthNeeded = entity.getMaxHealth() - entity.getHealth();
					entity.heal(healthNeeded);
					player.attackEntityFrom(RegenObjects.REGEN_HEAL, healthNeeded);

					state = state == RegenState.GRACE_GLOWING ? RegenState.GRACE_STD : RegenState.GRACE_CRIT;
					scheduler.cancel(TimerChannel.REGENERATION_TRIGGER); //... cancel the allowed regeneration trigger
					scheduler.scheduleInSeconds(TimerChannel.GRACE_GLOWING, RegenConfig.Grace.handGlowInterval, this::startGlowing); //... schedule the new glowing

					synchronise();
				}
			}
		}


		private void tick() {
			if (player.world.isRemote)
				throw new IllegalStateException("Ticking state manager on the client");
			
			scheduler.tick();
			
			/*for (TimerChannel tc : TimerChannel.values()) {
				System.out.println(tc + ": " + timers.get(tc).ticksLeft());
			}
			System.out.println();*/
			
			/*if (player.getHealth() < player.getMaxHealth()) { TODO actually regenerate health
				player.setHealth(player.getHealth() + 1); NOW move to external event handler
			}
			player.heal(2.0F);
			TODO random damage*/
		}
		
		
		
		//TODO post events to client?
		//This'll only be called from tick which is serverside only TODO javadoc all these things
		private void triggerRegeneration() { //FUTURE do animation using a ticksLeft() method on the scheduled task
			//We're actually regenerating!
			state = RegenState.REGENERATING;
			scheduler.cancel(TimerChannel.GRACE_CRITICAL); //... cancel the transition to critical phase
			scheduler.cancel(TimerChannel.GRACE_GLOWING); //... cancel the scheduled glowing
			scheduler.cancel(TimerChannel.GRACE_CRITICAL_DEATH); //... cancel the scheduled critical death
			
			//TODO configurable regeneration length based on the type
			scheduler.scheduleInSeconds(TimerChannel.REGENERATION_FINISH, 10, this::finishRegeneration); //... schedule the finishing of the regeneration
			type.onStartRegeneration(player, CapabilityRegeneration.this);
			
			synchronise();
			
			/*player.dismountRidingEntity(); NOW move to external event handler
			player.removePassengers();
			player.setAbsorptionAmount(RegenConfig.absorbtionLevel * 2);
			
			extractRegeneration(1);
			ExplosionUtil.regenerationExplosion(player);*/
			
			//TODO play music on client
			//PlayerUtil.playMovingSound(player, getType().getSound(), SoundCategory.PLAYERS);
			//TODO toast notification
		}
		
		//@SideOnly(Side.SERVER) (not enforced because of synchronization, but this'll only be called from tick which is serverside only)
		private void startGlowing() {
			//We're starting to glow...
			state = RegenState.GRACE_GLOWING;
			scheduler.scheduleInSeconds(TimerChannel.REGENERATION_TRIGGER, RegenConfig.Grace.allowedRegenDelay, this::triggerRegeneration); //... schedule allowed regeneration in 20 seconds
			synchronise();
			
			//this causes these things to happen each time the player starts to glow. It's a feature.
			/*player.clearActivePotions(); NOW move to external event handler
			player.extinguish();
			player.setArrowCountInEntity(0);*/
			
			//TODO play music on client
			//PlayerUtil.playMovingSound(player, RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS);
		}
		
		//@SideOnly(Side.SERVER) (not enforced because of synchronization, but this'll only be called from tick which is serverside only)
		private void enterCriticalPhase() {
			//We're entering critical phase...
			state = RegenState.GRACE_CRIT;
			scheduler.scheduleInSeconds(TimerChannel.GRACE_CRITICAL_DEATH, RegenConfig.Grace.criticalPhaseLength, this::midSequenceKill);
			synchronise();
			
			//TODO play music on client
			//TODO nausea
			/*PlayerUtil.playMovingSound(player, RegenObjects.Sounds.CRITICAL_STAGE, SoundCategory.PLAYERS);
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 800, 0, false, false)); // could be removed with milk, but I think that's not that bad*/
			//TODO toast notification
		}
		
		//@SideOnly(Side.SERVER) (not enforced because of synchronization, but this'll only be called from tick which is serverside only)
		private void midSequenceKill() {
			state = RegenState.ALIVE;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			player.setHealth(-1); //in case this method was called by the 15 minute counter
			
			reset();
			synchronise();
		}
		
		//@SideOnly(Side.SERVER) (not enforced because of synchronization, but this'll only be called from tick which is serverside only)
		private void finishRegeneration() {
			state = RegenState.ALIVE;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			
			reset();
			synchronise();
			
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
		
		
		
		
		
		private void reset() {
			for (TimerChannel tc : TimerChannel.values())
				scheduler.cancel(tc);
			scheduler.reset();
		}
		
		
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			for (TimerChannel tc : TimerChannel.values())
				nbt.setLong(tc.toString(), scheduler.getTicksLeft(tc));
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			scheduler.reset();
			
			scheduler.scheduleInTicks(TimerChannel.REGENERATION_TRIGGER, nbt.getLong(TimerChannel.REGENERATION_TRIGGER.toString()), this::triggerRegeneration);
			scheduler.scheduleInTicks(TimerChannel.REGENERATION_FINISH,  nbt.getLong(TimerChannel.REGENERATION_FINISH.toString()),  this::finishRegeneration);
			scheduler.scheduleInTicks(TimerChannel.GRACE_CRITICAL,       nbt.getLong(TimerChannel.GRACE_CRITICAL.toString()),       this::enterCriticalPhase);
			scheduler.scheduleInTicks(TimerChannel.GRACE_GLOWING,        nbt.getLong(TimerChannel.GRACE_GLOWING.toString()),        this::startGlowing);
			scheduler.scheduleInTicks(TimerChannel.GRACE_CRITICAL_DEATH, nbt.getLong(TimerChannel.GRACE_CRITICAL_DEATH.toString()), this::midSequenceKill);
		}




		@Override
		@Deprecated
		public Scheduler getScheduler() {
			return scheduler;
		}
		
	}
	
}
