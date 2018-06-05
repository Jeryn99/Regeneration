package com.lcm.regeneration.common.capabilities.timelord.capability;

import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationEvent;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationFinishEvent;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationStartEvent;
import com.lcm.regeneration.networking.RNetwork;
import com.lcm.regeneration.networking.packets.MessageChangeRegenState;
import com.lcm.regeneration.networking.packets.MessageSyncTimelordData;
import com.lcm.regeneration.utils.RegenConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Nictogen on 3/16/18.
 */
public class CapabilityTimelord implements ITimelordCapability {

    @CapabilityInject(ITimelordCapability.class)
    public static final Capability<ITimelordCapability> TIMELORD_CAP = null;
    public int regenerationsLeft = RegenConfig.regenCapacity, timesRegenerated, regenTicks;
    public RegenerationState state = RegenerationState.NONE;
    private boolean isTimelord;
    private EntityPlayer player;
    private NBTTagCompound styleTag = defaultStyle();
    private boolean dirty = true;

    public CapabilityTimelord(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {

        if (!player.world.isRemote) {
            if (dirty) {
                dirty = false;
                syncToAll();
            }
            CapabilityTimelord.RegenerationState nextState = determineState();
            if (this.state != nextState) {
                RNetwork.INSTANCE.sendToAll(new MessageChangeRegenState(player, nextState));
                changeState(nextState);
            }
        }

        switch (state) {
            case NONE:
                break;
            case REGENERATING:
                MinecraftForge.EVENT_BUS.post(new RegenerationEvent(player, this));
                break;
            case EXPLODING:
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

    private NBTTagCompound defaultStyle() {
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
        compound.setInteger("regenerationsLeft", regenerationsLeft);
        compound.setInteger("timesRegenerated", timesRegenerated);
        compound.setBoolean("isTimelord", isTimelord);
        return compound;
    }

    //	@Override public void onApplyPower() {
    //		this.getAbilities().clear();
    //		TimelordSuperpower.INSTANCE.addDefaultAbilities(this.getPlayer(), this.getAbilities());
    //		randomizeTraits();
    //		this.regenerationsLeft = 0;
    //	}

    //	public void randomizeTraits() {
    //		// Reset Karma
    //		if (LCConfig.modules.karma)
    //			for (KarmaStat karmaStat : KarmaStat.getKarmaStats())
    //				KarmaHandler.setKarmaStat(getPlayer(), karmaStat, 0);
    //
    //		getAbilities().forEach(ability -> ability.setUnlocked(false));
    //
    //		for (int i = 0; i < 2; i++) {
    //			Ability a = null;
    //			while (a == null || a instanceof INegativeTrait || a.isUnlocked())
    //				a = getAbilities().get(getPlayer().getRNG().nextInt(getAbilities().size()));
    //			a.setUnlocked(true);
    //		}
    //
    //		for (int i = 0; i < 2; i++) {
    //			Ability a = null;
    //			while (a == null || a.isUnlocked() || !(a instanceof INegativeTrait) || TimelordSuperpowerHandler.isAbilityUnlocked(this, ((INegativeTrait) a).getPositiveTrait()))
    //				a = getAbilities().get(getPlayer().getRNG().nextInt(getAbilities().size()));
    //			a.setUnlocked(true);
    //		}
    //
    //		String s = "";
    //		for (Ability ability : getAbilities())
    //			if (ability.isUnlocked())
    //				if (s.equals(""))
    //					s = ability.getDisplayName().substring(7);
    //				else
    //					s = s + ", " + ability.getDisplayName().substring(7);
    //		// handler.getPlayer().sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.newLife", s)), true);
    //	}

    @Override
    public void readNBT(NBTTagCompound compound) {
        regenerationsLeft = compound.getInteger("regenerationsLeft");
        timesRegenerated = compound.getInteger("timesRegenerated");
        isTimelord = compound.getBoolean("isTimelord");
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
    public void setStyle(NBTTagCompound nbtTagCompound) {
        styleTag = nbtTagCompound;
    }

    public enum RegenerationState {
        NONE, REGENERATING, EXPLODING
    }

    public static class CapabilityTimelordProvider implements ICapabilitySerializable<NBTTagCompound> {

        private ITimelordCapability capability;

        public CapabilityTimelordProvider(ITimelordCapability capability) {
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

    public static class Storage implements Capability.IStorage<ITimelordCapability> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ITimelordCapability> capability, ITimelordCapability instance, EnumFacing side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<ITimelordCapability> capability, ITimelordCapability instance, EnumFacing side, NBTBase nbt) {
            instance.readNBT((NBTTagCompound) nbt);
        }
    }
}
