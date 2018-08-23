package me.sub.regeneration.common.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import me.sub.regeneration.common.events.RegenerationEvent;
import me.sub.regeneration.common.events.RegenerationFinishEvent;
import me.sub.regeneration.common.events.RegenerationStartEvent;
import me.sub.regeneration.common.trait.ITrait;
import me.sub.regeneration.common.trait.TraitHandler;
import me.sub.regeneration.networking.RNetwork;
import me.sub.regeneration.networking.packets.MessageChangeRegenState;
import me.sub.regeneration.networking.packets.MessageChangeView;
import me.sub.regeneration.networking.packets.MessageSyncTimelordData;
import me.sub.regeneration.utils.RegenConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Created by Nictogen on 3/16/18.
 */
public class CapabilityRegeneration implements IRegenerationCapability {
	
	@CapabilityInject(IRegenerationCapability.class)
	public static final Capability<IRegenerationCapability> TIMELORD_CAP = null;
	public int regenerationsLeft = RegenConfig.REGENERATION.regenCapacity, timesRegenerated, regenTicks;
	public RegenerationState state = RegenerationState.NONE;
	private boolean isTimelord;
	private EntityPlayer player;
	private NBTTagCompound styleTag = getDefaultStyle();
	private boolean dirty = true;
	public TraitHandler.Trait trait = TraitHandler.Trait.NONE;
	private float primaryRed = 1.0f;
	private float primaryGreen = 0.78f;
	private float primaryBlue = 0.0f;
	private float secondaryGreen = 0.47f;
	private float secondaryRed = 1.0f;
	private float secondaryBlue = 0.0f;
	private boolean textured = false;
	
	public CapabilityRegeneration() {}
	
	public CapabilityRegeneration(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public NBTTagCompound getDefaultStyle() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", 1.0f);
		nbt.setFloat("PrimaryGreen", 0.78f);
		nbt.setFloat("PrimaryBlue", 0.0f);
		nbt.setFloat("SecondaryRed", 1.0f);
		nbt.setFloat("SecondaryGreen", 0.47f);
		nbt.setFloat("SecondaryBlue", 0.0f);
		nbt.setBoolean("textured", false);
		return nbt;
	}
	
	@Override
	public void update() {
		
		if (!player.world.isRemote) {
			
			if (getRegenTicks() > 0) {
				RNetwork.INSTANCE.sendTo(new MessageChangeView(), (EntityPlayerMP) player);
			}
			
			if (dirty) {
				dirty = false;
				syncToAll();
			}
			CapabilityRegeneration.RegenerationState nextState = determineState();
			if (this.state != nextState) {
				RNetwork.INSTANCE.sendToAll(new MessageChangeRegenState(player, nextState));
				changeState(nextState);
			}
		}
		
		switch (state) {
			case NONE:
				break;
			case REGENERATING:
				syncToAll();
				MinecraftForge.EVENT_BUS.post(new RegenerationEvent(player, this));
				break;
			case EXPLODING:
				syncToAll();
				MinecraftForge.EVENT_BUS.post(new RegenerationEvent.RegenerationExplosionEvent(player, this));
				break;
		}
	}
	
	@Override
	public void changeState(RegenerationState state) {
		this.state = state;
		switch (state) {
			case NONE:
				MinecraftForge.EVENT_BUS.post(new RegenerationFinishEvent(player, this));
				break;
			case REGENERATING:
				MinecraftForge.EVENT_BUS.post(new RegenerationStartEvent(player, this));
				break;
			case EXPLODING:
				break;
		}
	}
	
	private RegenerationState determineState() {
		if (regenTicks <= 0)
			return RegenerationState.NONE;
		if (++regenTicks > 0 && regenTicks < 100)
			return RegenerationState.REGENERATING;
		if (regenTicks >= 100 && regenTicks < 200)
			return RegenerationState.EXPLODING;
		regenTicks = 0;
		return RegenerationState.NONE;
	}
	
	@Override
	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setFloat("PrimaryRed", primaryRed);
		compound.setFloat("PrimaryGreen", primaryGreen);
		compound.setFloat("PrimaryBlue", primaryBlue);
		compound.setFloat("SecondaryRed", secondaryRed);
		compound.setFloat("SecondaryGreen", secondaryGreen);
		compound.setFloat("SecondaryBlue", secondaryBlue);
		compound.setInteger("regenerationsLeft", regenerationsLeft);
		compound.setInteger("timesRegenerated", timesRegenerated);
		compound.setBoolean("isTimelord", isTimelord);
		compound.setInteger("regenTicks", regenTicks);
		compound.setBoolean("textured", textured);
		styleTag = compound;
		return compound;
	}
	
	@Override
	public void readNBT(NBTTagCompound compound) {
		regenerationsLeft = compound.getInteger("regenerationsLeft");
		timesRegenerated = compound.getInteger("timesRegenerated");
		isTimelord = compound.getBoolean("isTimelord");
		regenTicks = compound.getInteger("regenTicks");
		primaryRed = compound.getFloat("PrimaryRed");
		primaryGreen = compound.getFloat("PrimaryGreen");
		primaryBlue = compound.getFloat("PrimaryBlue");
		secondaryRed = compound.getFloat("SecondaryRed");
		secondaryGreen = compound.getFloat("SecondaryGreen");
		secondaryBlue =compound.getFloat("SecondaryBlue");
		textured = compound.getBoolean("textured");
	}
	
	@Override
	public void syncToAll() {
		RNetwork.INSTANCE.sendToAll(new MessageSyncTimelordData(player, writeNBT()));
	}
	
	@Override
	public boolean isTimelord() {
		return isTimelord;
	}
	
	@Override
	public void setTimelord(boolean timelord) {
		isTimelord = timelord;
	}
	
	@Override
	public int getRegenTicks() {
		return regenTicks;
	}
	
	@Override
	public void setRegenTicks(int ticks) {
		regenTicks = ticks;
	}
	
	@Override
	public int getRegensLeft() {
		return regenerationsLeft;
	}
	
	@Override
	public void setRegensLeft(int left) {
		regenerationsLeft = left;
	}
	
	@Override
	public int getTimesRegenerated() {
		return timesRegenerated;
	}
	
	@Override
	public void setTimesRegenerated(int times) {
		timesRegenerated = times;
	}
	
	@Override
	public RegenerationState getState() {
		return state;
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}
	
	@Override
	public NBTTagCompound getStyle() {
		return styleTag;
	}
	
	@Override
	public ITrait getTrait() {
		return trait.getTrait();
	}
	
	@Override
	public void setTrait(TraitHandler.Trait trait) {
		this.trait = trait;
	}
	
	
	@Override
	public void setStyle(NBTTagCompound compound) {
		primaryRed = compound.getFloat("PrimaryRed");
		primaryGreen = compound.getFloat("PrimaryGreen");
		primaryBlue = compound.getFloat("PrimaryBlue");
		secondaryRed = compound.getFloat("SecondaryRed");
		secondaryGreen = compound.getFloat("SecondaryGreen");
		secondaryBlue =compound.getFloat("SecondaryBlue");
		textured = compound.getBoolean("textured");
	}
	
	public enum RegenerationState {
		NONE, REGENERATING, EXPLODING
	}
	
	
	public static class CapabilityTimelordProvider implements ICapabilitySerializable<NBTTagCompound> {
		
		private IRegenerationCapability capability;
		
		public CapabilityTimelordProvider(IRegenerationCapability capability) {
			this.capability = capability;
		}
		
		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return TIMELORD_CAP != null && capability == TIMELORD_CAP;
		}
		
		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			return capability == TIMELORD_CAP ? TIMELORD_CAP.cast(this.capability) : null;
		}
		
		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) TIMELORD_CAP.getStorage().writeNBT(TIMELORD_CAP, this.capability, null);
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			TIMELORD_CAP.getStorage().readNBT(TIMELORD_CAP, this.capability, null, nbt);
		}
	}
	
	public static class Storage implements Capability.IStorage<IRegenerationCapability> {
		
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IRegenerationCapability> capability, IRegenerationCapability instance, EnumFacing side) {
			return instance.writeNBT();
		}
		
		@Override
		public void readNBT(Capability<IRegenerationCapability> capability, IRegenerationCapability instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT((NBTTagCompound) nbt);
		}
	}
}
