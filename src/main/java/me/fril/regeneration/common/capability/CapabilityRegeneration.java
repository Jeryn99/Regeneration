package me.fril.regeneration.common.capability;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import org.apache.commons.lang3.tuple.Pair;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class CapabilityRegeneration implements IRegeneration {
	
	@CapabilityInject(IRegeneration.class)
	public static final Capability<IRegeneration> CAPABILITY = null;
	public static final ResourceLocation CAP_REGEN_ID = new ResourceLocation(RegenerationMod.MODID, "regeneration");
	private static String ENCODED_SKIN = "NONE";

	private final EntityPlayer player;
	private int regenerationsLeft;
	private RegenState state = RegenState.ALIVE;
	private IRegenType<?> type = new TypeFiery();
	
	private final RegenerationStateManager stateManager;
	
	private float primaryRed = 0.93f, primaryGreen = 0.61f, primaryBlue = 0.0f;
	private float secondaryRed = 1f, secondaryGreen = 0.5f, secondaryBlue = 0.18f;
	private boolean skinLoaded = false;


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
	
	
	private boolean didSetup = false;
	
	@Override
	public void tick() {
		if (!didSetup && player.world.isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new MessageSynchronisationRequest(player));
			didSetup = true;
		}
		
		if (!player.world.isRemote && state != RegenState.ALIVE) //ticking only on the server for simplicity
			stateManager.tick();
		
		if (state == RegenState.REGENERATING) {
			type.onUpdateMidRegen(player, this);
		}
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
		nbt.setTag("type", type.serializeNBT());
		nbt.setString("encoded_skin", ENCODED_SKIN);
		nbt.setBoolean("skinLoaded", skinLoaded);
		if (!player.world.isRemote)
			nbt.setTag("stateManager", stateManager.serializeNBT());
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		regenerationsLeft = Math.min(RegenConfig.regenCapacity, nbt.getInteger(nbt.hasKey("livesLeft") ? "livesLeft" : "regenerationsLeft"));

		//v1.3+ has a sub-tag 'style' for styles. If it exists we pull the data from this tag, otherwise we pull it from the parent tag
		setStyle(nbt.hasKey("style") ? nbt.getCompoundTag("style") : nbt);
		
		if (nbt.hasKey("type")) //v1.3+ saves have a type tag
			type = IRegenType.getType(type, nbt.getCompoundTag("type"));
		else //for previous save versions set to default 'fiery' type
			type = new TypeFiery();
		
		state = nbt.hasKey("state") ? RegenState.valueOf(nbt.getString("state")) : RegenState.ALIVE; //I need to check for versions before the new state-ticking system
		setSkinLoaded(nbt.getBoolean("skinLoaded"));
		setEncodedSkin(nbt.getString("encoded_skin"));

		if (nbt.hasKey("stateManager"))
			stateManager.deserializeNBT(nbt.getCompoundTag("stateManager"));
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
	public IRegenType<?> getType() {
		return type;
	}

	@Override
	public String getEncodedSkin() {
		return ENCODED_SKIN;
	}

	@Override
	public void setEncodedSkin(String string) {
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
	
	
	@Deprecated
	@Override
	public void setRegenerationsLeft(int amount) {
		regenerationsLeft = amount;
	}

	@Override
	public boolean isSkinLoaded() {
		return skinLoaded;
	}

	@Override
	public void setSkinLoaded(boolean b) {
		skinLoaded = b;
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
	
	
	
	
	/** ONLY EXISTS ON THE SERVER SIDE */
	public class RegenerationStateManager implements IRegenerationStateManager {
		
		private final Map<Transition, Runnable> transitionRunnables;
		private DebuggableScheduledAction nextTransition;
		
		private RegenerationStateManager() {
			this.transitionRunnables = new HashMap<>();
			transitionRunnables.put(Transition.ENTER_CRITICAL, this::enterCriticalPhase);
			transitionRunnables.put(Transition.CRITICAL_DEATH, this::midSequenceKill);
			transitionRunnables.put(Transition.FINISH_REGENERATION, this::finishRegeneration);
		}
		
		
		@SuppressWarnings("deprecation")
		private void scheduleTransitionInTicks(Transition transition, long inTicks) {
			if (nextTransition != null && nextTransition.getTicksLeft() > 0)
				throw new IllegalStateException("Overwriting non-completed/cancelled transition");
			nextTransition = new DebuggableScheduledAction(transition, player, transitionRunnables.get(transition), inTicks);
		}
		
		private void scheduleTransitionInSeconds(Transition transition, long inSeconds) {
			scheduleTransitionInTicks(transition, inSeconds*20);
		}
		
		
		
		@Override
		public boolean onKilled() {
			if (state == RegenState.ALIVE) {
				
				if (!canRegenerate()) //that's too bad :(
					return false;
				
				//We're entering grace period...
				scheduleTransitionInSeconds(Transition.ENTER_CRITICAL, RegenConfig.Grace.gracePhaseLength);
				state = RegenState.GRACE;
				synchronise();
				ActingForwarder.onEnterGrace(CapabilityRegeneration.this);
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
				player.attackEntityFrom(RegenObjects.REGEN_DMG_HEALING, healthNeeded);
			}
		}

		@Override
		public void onPunchBlock(IBlockState blockState) {

		}


		private void tick() {
			if (player.world.isRemote)
				throw new IllegalStateException("Ticking state manager on the client"); //the state manager shouldn't even exist on the client
			if (state == RegenState.ALIVE)
				throw new IllegalStateException("Ticking dormant state manager (state == ALIVE)"); //would NPE when ticking the transition, but this is a more clear message
			
			ActingForwarder.onRegenTick(CapabilityRegeneration.this);
			nextTransition.tick();
		}
		
		
		
		private void triggerRegeneration() {
			//We're starting a regeneration!
			state = RegenState.REGENERATING;
			PlayerUtil.sendMessageToAll(new TextComponentTranslation("message.regeneration.isregenerating", player.getName()));
			nextTransition.cancel(); //... cancel any state shift we had planned
			scheduleTransitionInTicks(Transition.FINISH_REGENERATION, type.getAnimationLength());
			
			ActingForwarder.onRegenTrigger(CapabilityRegeneration.this);
			type.onStartRegeneration(player, CapabilityRegeneration.this);
			synchronise();
		}
		
		private void enterCriticalPhase() {
			//We're entering critical phase...
			state = RegenState.GRACE_CRIT;
			scheduleTransitionInSeconds(Transition.CRITICAL_DEATH, RegenConfig.Grace.criticalPhaseLength);
			ActingForwarder.onGoCritical(CapabilityRegeneration.this);
			synchronise();
		}
		
		private void midSequenceKill() {
			state = RegenState.ALIVE;
			nextTransition = null;
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			player.setHealth(-1); //in case this method was called by critical death
			
			/* SuB For re-implementing the dont-lose-regens-on-death option:
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
			type.onFinishRegeneration(player, CapabilityRegeneration.this);
			ActingForwarder.onRegenFinish(CapabilityRegeneration.this);
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
		public double getStateProgress() {
			return nextTransition.getProgress();
		}
		
		
		
		@SuppressWarnings("deprecation")
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
				scheduleTransitionInTicks(Transition.valueOf(nbt.getString("transitionId")), nbt.getLong("transitionInTicks"));
		}
		
	}
	
}
