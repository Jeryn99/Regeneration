package me.fril.regeneration.common.capability;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.skinhandling.SkinInfo;
import me.fril.regeneration.common.types.IRegenType;
import me.fril.regeneration.common.types.TypeFiery;
import me.fril.regeneration.debugger.util.DebuggableScheduledAction;
import me.fril.regeneration.handlers.ActingForwarder;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.network.MessageSynchronisationRequest;
import me.fril.regeneration.network.MessageSynchroniseRegeneration;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.RegenState.Transition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class CapabilityRegeneration implements IRegeneration {
	
	@CapabilityInject(IRegeneration.class)
	public static final Capability<IRegeneration> CAPABILITY = null;
	public static final ResourceLocation CAP_REGEN_ID = new ResourceLocation(RegenerationMod.MODID, "regeneration");
	
	private final EntityPlayer player;
	private final RegenerationStateManager stateManager;
	public String deathSource = "";
	private boolean didSetup = false;
	private int regenerationsLeft;
	
	private RegenState state = RegenState.ALIVE;
	private IRegenType<?> type = new TypeFiery();
	
	private byte[] ENCODED_SKIN = new byte[0];
	private SkinInfo.SkinType skinType = SkinInfo.SkinType.ALEX;
	private float primaryRed = 0.93f, primaryGreen = 0.61f, primaryBlue = 0.0f;
	private float secondaryRed = 1f, secondaryGreen = 0.5f, secondaryBlue = 0.18f;
	
	
	/**
	 * WHY THIS IS A SEPERATE FIELD: the hands are glowing if <code>stateManager.handGlowTimer.getTransition() == Transition.HAND_GLOW_TRIGGER</code>, however the state manager isn't available on the client.
	 * This property is synced over to the client to solve this
	 */
	private boolean handsAreGlowingClient;
	
	
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
	
	@Nonnull
	public static IRegeneration getForPlayer(EntityPlayer player) {
		if (player.hasCapability(CAPABILITY, null)) {
			return player.getCapability(CAPABILITY, null);
		}
		throw new IllegalStateException("Missing Regeneration capability: " + player + ", please report this to the issue tracker");
	}
	
	
	@Override
	public void tick() {
		if (!didSetup && player.world.isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new MessageSynchronisationRequest(player));
			didSetup = true;
		}
		
		if (!player.world.isRemote && state != RegenState.ALIVE) // ticking only on the server for simplicity
			stateManager.tick();
		
		if (state == RegenState.REGENERATING) {
			type.onUpdateMidRegen(player, this);
		}
	}
	
	@Override
	public void synchronise() {
		if (player.world.isRemote)
			throw new IllegalStateException("Don't sync client -> server");
		
		handsAreGlowingClient = state.isGraceful() && stateManager.handGlowTimer.getTransition() == Transition.HAND_GLOW_TRIGGER;
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
		nbt.setTag("type", type.serializeNBT());
		nbt.setByteArray("encoded_skin", ENCODED_SKIN);
		nbt.setString("skinType", skinType.name());
		nbt.setBoolean("handsAreGlowing", handsAreGlowingClient);
		if (!player.world.isRemote)
			nbt.setTag("stateManager", stateManager.serializeNBT());
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		regenerationsLeft = Math.min(RegenConfig.regenCapacity, nbt.getInteger(nbt.hasKey("livesLeft") ? "livesLeft" : "regenerationsLeft"));
		
		if (nbt.hasKey("skinType")) {
			setSkinType(nbt.getString("skinType"));
		} else {
			setSkinType("ALEX");
		}
		
		if (nbt.hasKey("handsAreGlowing")) {
			handsAreGlowingClient = nbt.getBoolean("handsAreGlowing");
		}
		
		/*if (nbt.hasKey("ticksGlowing")) {
			nbt.setInteger("ticksGlowing", ticksGlowing);
		}*/
		
		// v1.3+ has a sub-tag 'style' for styles. If it exists we pull the data from this tag, otherwise we pull it from the parent tag
		setStyle(nbt.hasKey("style") ? nbt.getCompoundTag("style") : nbt);
		
		if (nbt.hasKey("type")) // v1.3+ saves have a type tag
			type = IRegenType.getType(type, nbt.getCompoundTag("type"));
		else // for previous save versions set to default 'fiery' type
			type = new TypeFiery();
		
		state = nbt.hasKey("state") ? RegenState.valueOf(nbt.getString("state")) : RegenState.ALIVE; // I need to check for versions before the new state-ticking system
		setEncodedSkin(nbt.getByteArray("encoded_skin"));
		
		if (nbt.hasKey("stateManager"))
			stateManager.deserializeNBT(nbt.getCompoundTag("stateManager"));
	}
	
	
	@Override
	public int getRegenerationsLeft() {
		return regenerationsLeft;
	}
	
	@Deprecated
	@Override
	public void setRegenerationsLeft(int amount) {
		regenerationsLeft = amount;
	}
	
	
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}
	
	@Override
	public IRegenType<?> getType() {
		return type;
	}
	
	
	@Override
	public byte[] getEncodedSkin() {
		return ENCODED_SKIN;
	}
	
	@Override
	public void setEncodedSkin(byte[] string) {
		ENCODED_SKIN = string;
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
		if (RegenConfig.infiniteRegeneration)
			regenerationsLeft = RegenConfig.regenCapacity;
		else
			regenerationsLeft += amount;
		synchronise();
	}
	
	@Override
	public void extractRegeneration(int amount) {
		if (RegenConfig.infiniteRegeneration)
			regenerationsLeft = RegenConfig.regenCapacity;
		else
			regenerationsLeft -= amount;
		synchronise();
	}
	
	
	@Override
	public SkinInfo.SkinType getSkinType() {
		return skinType;
	}
	
	@Override
	public void setSkinType(String skinType) {
		this.skinType = SkinInfo.SkinType.valueOf(skinType);
	}
	
	
	@Override
	public boolean areHandsGlowing() {
		return handsAreGlowingClient;
		//return handsGlowing;
	}
	
	
	@Override
	public String getDeathSource() {
		return deathSource;
	}
	
	@Override
	public void setDeathSource(String source) {
		deathSource = source;
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
	
	
	/**
	 * ONLY EXISTS ON THE SERVER SIDE
	 */
	public class RegenerationStateManager implements IRegenerationStateManager {
		
		private final Map<Transition, Runnable> transitionCallbacks;
		private DebuggableScheduledAction nextTransition, handGlowTimer;
		
		private RegenerationStateManager() {
			this.transitionCallbacks = new HashMap<>();
			transitionCallbacks.put(Transition.ENTER_CRITICAL, this::enterCriticalPhase);
			transitionCallbacks.put(Transition.CRITICAL_DEATH, this::midSequenceKill);
			transitionCallbacks.put(Transition.FINISH_REGENERATION, this::finishRegeneration);
			
			Runnable err = () -> {
				throw new IllegalStateException("Can't use HAND_GLOW_* transitions as state transitions");
			};
			transitionCallbacks.put(Transition.HAND_GLOW_START, err);
			transitionCallbacks.put(Transition.HAND_GLOW_TRIGGER, err);
			//FIXME 'corrupt' transition isn't handled properly, causing tests to fail. I assume it's just not implemented yet apart from the enum?
		}
		
		@SuppressWarnings("deprecation")
		private void scheduleTransitionInTicks(Transition transition, long inTicks) {
			if (nextTransition != null && nextTransition.getTicksLeft() > 0)
				throw new IllegalStateException("Overwriting non-completed/cancelled transition");
			
			if (transition == Transition.HAND_GLOW_START || transition == Transition.HAND_GLOW_TRIGGER)
				throw new IllegalStateException("Can't use HAND_GLOW_* transitions as state transitions");
			
			nextTransition = new DebuggableScheduledAction(transition, player, transitionCallbacks.get(transition), inTicks);
		}
		
		private void scheduleTransitionInSeconds(Transition transition, long inSeconds) {
			scheduleTransitionInTicks(transition, inSeconds * 20);
		}
		
		
		@SuppressWarnings("deprecation")
		private void scheduleNextHandGlow() {
			if (handGlowTimer != null && handGlowTimer.getTicksLeft() > 0)
				throw new IllegalStateException("Overwriting running hand-glow timer with new next hand glow");
			handGlowTimer = new DebuggableScheduledAction(Transition.HAND_GLOW_START, player, this::scheduleHandGlowTrigger, RegenConfig.grace.handGlowInterval * 20);
			synchronise();
		}
		
		@SuppressWarnings("deprecation")
		private void scheduleHandGlowTrigger() {
			if (handGlowTimer != null && handGlowTimer.getTicksLeft() > 0)
				throw new IllegalStateException("Overwriting running hand-glow timer with trigger timer prematurely");
			handGlowTimer = new DebuggableScheduledAction(Transition.HAND_GLOW_TRIGGER, player, this::triggerRegeneration, RegenConfig.grace.handGlowTriggerDelay * 20);
			ActingForwarder.onHandsStartGlowing(CapabilityRegeneration.this);
			synchronise();
		}
		
		
		@Override
		public boolean onKilled(DamageSource source) {
			if (state == RegenState.ALIVE) {
				if (!canRegenerate()) // that's too bad :(
					return false;
				
				// We're entering grace period...
				scheduleTransitionInSeconds(Transition.ENTER_CRITICAL, RegenConfig.grace.gracePhaseLength);
				scheduleHandGlowTrigger();
				
				state = RegenState.GRACE;
				synchronise();
				ActingForwarder.onEnterGrace(CapabilityRegeneration.this);
				return true;
				
			} else if (state.isGraceful()) {
				
				// We're being forced to regenerate...
				triggerRegeneration();
				return true;
				
			} else if (state == RegenState.REGENERATING) {
				
				// We've been killed mid regeneration!
				nextTransition.cancel(); // ... cancel the finishing of the regeneration
				midSequenceKill();
				return false;
				
			} else
				throw new IllegalStateException("Unknown state: " + state);
		}
		
		@Override
		public void onPunchEntity(EntityLivingBase entity) {
			// We're healing mobs...
			if (state.isGraceful() && entity.getHealth() < entity.getMaxHealth()) { // ... check if we're in grace and if the mob needs health
				float healthNeeded = entity.getMaxHealth() - entity.getHealth();
				entity.heal(healthNeeded);
				player.attackEntityFrom(RegenObjects.REGEN_DMG_HEALING, healthNeeded);
			}
		}
		
		@Override
		public void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
			if (getState().isGraceful() && areHandsGlowing()) {
				handGlowTimer.cancel();
				scheduleNextHandGlow();
				if(!player.world.isRemote){
					PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.regen_delayed"), true);
				}
			}
		}
		
		private void tick() {
			if (player.world.isRemote)
				throw new IllegalStateException("Ticking state manager on the client"); // the state manager shouldn't even exist on the client
			if (state == RegenState.ALIVE)
				throw new IllegalStateException("Ticking dormant state manager (state == ALIVE)"); // would NPE when ticking the transition, but this is a more clear message
			
			if (state.isGraceful())
				handGlowTimer.tick();
			
			ActingForwarder.onRegenTick(CapabilityRegeneration.this);
			nextTransition.tick();
		}
		
		private void triggerRegeneration() {
			// We're starting a regeneration!
			state = RegenState.REGENERATING;
			
			if (RegenConfig.sendRegenDeathMessages) {
				TextComponentTranslation text = new TextComponentTranslation("regeneration.messages.regen_chat_message", player.getName());
				text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(getDeathSource())));
				PlayerUtil.sendMessageToAll(text);
			}
			
			nextTransition.cancel(); // ... cancel any state shift we had planned
			handGlowTimer.cancel();
			scheduleTransitionInTicks(Transition.FINISH_REGENERATION, type.getAnimationLength());
			
			ActingForwarder.onRegenTrigger(CapabilityRegeneration.this);
			type.onStartRegeneration(player, CapabilityRegeneration.this);
			synchronise();
		}
		
		private void enterCriticalPhase() {
			// We're entering critical phase...
			state = RegenState.GRACE_CRIT;
			scheduleTransitionInSeconds(Transition.CRITICAL_DEATH, RegenConfig.grace.criticalPhaseLength);
			ActingForwarder.onGoCritical(CapabilityRegeneration.this);
			synchronise();
		}
		
		private void midSequenceKill() {
			state = RegenState.ALIVE;
			nextTransition = null;
			handGlowTimer = null;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			if (player.getLastDamageSource() != null) {
				player.attackEntityFrom(player.getLastDamageSource(), Integer.MAX_VALUE);
			} else {
				player.setHealth(-1);
			}
			
			/*
			 * SuB For re-implementing the dont-lose-regens-on-death option:
			 * We never explicitly reset the live count, but it still gets reset.
			 * From my understanding this is because the capability data isn't cloned over properly when the player dies.
			 * Soooo how should we handle it then? Save the last regen count and giving that back on respawn?
			 * Can we copy the data over on death (I assume so) and how?
			 *
			 * WAFFLE Use the LivingDeathEvent and just copy the data over
			 */
			
			synchronise();
		}
		
		private void finishRegeneration() {
			state = RegenState.ALIVE;
			nextTransition = null;
			handGlowTimer = null;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			ActingForwarder.onRegenFinish(CapabilityRegeneration.this);
			synchronise();
		}
		
		@Override
		@Deprecated
		/** @deprecated Debug purposes */
		public Pair<Transition, Long> getScheduledEvent() {
			return nextTransition == null ? null : Pair.of(nextTransition.transition, nextTransition.getTicksLeft());
		}
		
		@Override
		@Deprecated
		/** @deprecated Debug purposes */
		public void fastForward() {
			while (!nextTransition.tick())
				;
		}
		
		@Override
		@Deprecated
		/** @deprecated Debug purposes */
		public void fastForwardHandGlow() {
			while (!handGlowTimer.tick())
				;
		}
		
		@Override
		public double getStateProgress() {
			return nextTransition.getProgress();
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			if (nextTransition != null && nextTransition.getTicksLeft() >= 0) {
				nbt.setString("transitionId", nextTransition.transition.toString());
				nbt.setLong("transitionInTicks", nextTransition.getTicksLeft());
			}
			
			if (handGlowTimer != null && handGlowTimer.getTicksLeft() >= 0) {
				nbt.setString("handGlowState", handGlowTimer.transition.toString());
				nbt.setLong("handGlowScheduledTicks", handGlowTimer.getTicksLeft());
			}
			return nbt;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if (nbt.hasKey("transitionId"))
				scheduleTransitionInTicks(Transition.valueOf(nbt.getString("transitionId")), nbt.getLong("transitionInTicks"));
			
			if (nbt.hasKey("handGlowState")) {
				Transition transition = Transition.valueOf(nbt.getString("handGlowState"));
				
				Runnable callback;
				if (transition == Transition.HAND_GLOW_START)
					callback = this::scheduleHandGlowTrigger;
				else if (transition == Transition.HAND_GLOW_TRIGGER)
					callback = this::triggerRegeneration;
				else
					throw new IllegalStateException("Illegal hand glow timer transition");
				
				handGlowTimer = new DebuggableScheduledAction(transition, player, callback, nbt.getLong("handGlowScheduledTicks"));
			}
		}
		
	}
	
}
