package me.fril.regeneration.common.capability;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.events.RegenStateEvents.RegenEnterGraceEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenFinishEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenGoCriticalEvent;
import me.fril.regeneration.common.events.RegenStateEvents.RegenTriggerEvent;
import me.fril.regeneration.common.types.IRegenType;
import me.fril.regeneration.common.types.RegenTypes;
import me.fril.regeneration.debugger.util.DebuggableScheduledAction;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.network.MessageSynchronisationRequest;
import me.fril.regeneration.network.MessageSynchroniseRegeneration;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.RegenState.Transition;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
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
	
	private final EntityPlayer player;
	private int regenerationsLeft;
	private long animationTicks; //NOW move to type level
	private RegenState state = RegenState.ALIVE;
	private IRegenType type = RegenTypes.FIERY;
	
	private final RegenerationStateManager stateManager;
	
	private float primaryRed = 0.93f, primaryGreen = 0.61f, primaryBlue = 0.0f;
	private float secondaryRed = 1f, secondaryGreen = 0.5f, secondaryBlue = 0.18f;
	
	
	
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
	
	
	private boolean setup = false;
	
	@Override
	public void tick() {
		if (!setup && player.world.isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new MessageSynchronisationRequest(player));
			setup = true;
		}
		
		if (!player.world.isRemote && state != RegenState.ALIVE) //ticking only on the server for simplicity
			stateManager.tick();
		
		if (state == RegenState.REGENERATING) {
			type.onUpdateMidRegen(player, this);
			animationTicks++;
		} else animationTicks = 0;
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
		nbt.setLong("animationTicks", animationTicks);
		if (!player.world.isRemote)
			nbt.setTag("stateManager", stateManager.serializeNBT());
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		regenerationsLeft = nbt.getInteger("regenerationsLeft");
		setStyle(nbt.getCompoundTag("style"));
		animationTicks = nbt.getLong("animationTicks");
		state = nbt.hasKey("state") ? RegenState.valueOf(nbt.getString("state")) : RegenState.ALIVE; //I need to check for versions before the new state-ticking system
		
		if (nbt.hasKey("stateManager"))
			stateManager.deserializeNBT(nbt.getCompoundTag("stateManager"));
	}
	
	
	
	
	
	@Override
	public int getRegenerationsLeft() {
		return regenerationsLeft;
	}
	
	@Override
	public double getAnimationProgress() {
		return Math.min(1, animationTicks / (double)type.getAnimationLength());
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
	
	@Override
	public RegenState getState() {
		return state;
	}
	
	
	
	
	
	@Override
	public void triggerRegeneration() {
		if (player.world.isRemote)
			throw new IllegalStateException("Triggering regeneration via capability instance on the client side");
		stateManager.triggerRegeneration();
	}
	
	
	
	
	
	public class RegenerationStateManager implements IRegenerationStateManager {
		
		private final Map<Transition, Runnable> callbacks;
		private DebuggableScheduledAction nextTransition;
		
		private RegenerationStateManager() {
			this.callbacks = new HashMap<>();
			callbacks.put(Transition.ENTER_CRITICAL, this::enterCriticalPhase);
			callbacks.put(Transition.CRITICAL_DEATH, this::midSequenceKill);
			callbacks.put(Transition.FINISH_REGENERATION, this::finishRegeneration);
		}
		
		
		private void scheduleInTicks(Transition transition, long inTicks) {
			if (nextTransition != null && nextTransition.getTicksLeft() > 0)
				throw new IllegalStateException("Overwriting non-completed/cancelled transition");
			nextTransition = new DebuggableScheduledAction(transition, player, callbacks.get(transition), inTicks);
		}
		
		private void scheduleInSeconds(Transition transition, long inSeconds) {
			scheduleInTicks(transition, inSeconds*20);
		}
		
		
		
		@Override
		public boolean onKilled() {
			if (state == RegenState.ALIVE) { //FIXME check if we can regenerate
				
				//We're entering grace period...
				scheduleInSeconds(Transition.ENTER_CRITICAL, RegenConfig.Grace.gracePeriodLength);
				state = RegenState.GRACE;
				synchronise();
				MinecraftForge.EVENT_BUS.post(new RegenEnterGraceEvent(CapabilityRegeneration.this));
				return true;
				
			} else if (state.isGraceful()) {
				
				//We're being forced to regenerate...
				triggerRegeneration();
				return true;
				
			} else if (state == RegenState.REGENERATING) {
				
				//We've been killed mid regeneration!
				nextTransition.cancel(); //... cancel the finishing of the regeneration
				midSequenceKill();
				return false;
				
			} else throw new IllegalStateException("Unknown state: "+state);
		}
		
		@Override
		public void onPunchEntity(EntityLivingBase entity) {
			//We're healing mobs...
			if (state.isGraceful() && entity.getHealth() < entity.getMaxHealth()) { //... check if we're in grace and if the mob needs health
				float healthNeeded = entity.getMaxHealth() - entity.getHealth();
				entity.heal(healthNeeded);
				player.attackEntityFrom(RegenObjects.REGEN_HEAL, healthNeeded);
			}
		}
		
		
		private void tick() {
			if (player.world.isRemote)
				throw new IllegalStateException("Ticking state manager on the client");
			
			nextTransition.tick();
		}
		
		
		
		private void triggerRegeneration() {
			//We're starting a regeneration!
			state = RegenState.REGENERATING;
			nextTransition.cancel(); //... cancel any state shift we had planned
			scheduleInTicks(Transition.FINISH_REGENERATION, type.getAnimationLength());
			
			MinecraftForge.EVENT_BUS.post(new RegenTriggerEvent(CapabilityRegeneration.this));
			type.onStartRegeneration(player, CapabilityRegeneration.this);
			synchronise();
		}
		
		private void enterCriticalPhase() {
			//We're entering critical phase...
			state = RegenState.GRACE_CRIT;
			scheduleInSeconds(Transition.CRITICAL_DEATH, RegenConfig.Grace.criticalPhaseLength);
			MinecraftForge.EVENT_BUS.post(new RegenGoCriticalEvent(CapabilityRegeneration.this));
			synchronise();
		}
		
		private void midSequenceKill() {
			state = RegenState.ALIVE;
			nextTransition = null;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			player.setHealth(-1); //in case this method was called by critical death
			synchronise();
		}
		
		private void finishRegeneration() {
			state = RegenState.ALIVE;
			nextTransition = null;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			MinecraftForge.EVENT_BUS.post(new RegenFinishEvent(CapabilityRegeneration.this));
			synchronise();
		}
		
		
		
		@Override
		@Deprecated
		/** @deprecated Debug purposes */
		public Pair<Transition, Long> getScheduledEvent() {
			return nextTransition == null ? null : Pair.of(nextTransition.action, nextTransition.getTicksLeft());
		}
		
		@Override
		@Deprecated
		/** @deprecated Debug purposes */
		public void fastForward() {
			while (!nextTransition.tick());
		}
		
		
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			if (nextTransition != null) {
				nbt.setString("transitionId", nextTransition.action.toString());
				nbt.setLong("transitionInTicks", nextTransition.getTicksLeft());
			}
			return nbt;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if (nbt.hasKey("transitionId"))
				scheduleInTicks(Transition.valueOf(nbt.getString("transitionId")), nbt.getLong("transitionInTicks"));
		}
		
	}
	
}
