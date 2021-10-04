package com.afg.regeneration.capability;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.network.MessageSyncTimelordCap;
import com.afg.regeneration.sounds.SoundReg;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by AFlyingGrayson on 12/30/17
 */
public class TimelordCapability implements ITimelordCapability {

    @CapabilityInject(ITimelordCapability.class)
    public static final Capability<ITimelordCapability> TIMELORD_CAP = null;

    public EntityPlayer player;
    public int regenCount, regenTicks;
    public boolean isTimelord;

    public TimelordCapability(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public NBTTagCompound writeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("regenCount", regenCount);
        compound.setInteger("regenTicks", regenTicks);
        compound.setBoolean("isTimelord", isTimelord);
        return compound;
    }

    @Override
    public void readNBT(NBTTagCompound nbt) {
        regenCount = nbt.getInteger("regenCount");
        regenTicks = nbt.getInteger("regenTicks");
        isTimelord = nbt.getBoolean("isTimelord");
    }

    @Override
    public void syncToPlayer() {
        Regeneration.NETWORK_WRAPPER.sendToAll(new MessageSyncTimelordCap(this.player));
    }

    @Override
    public boolean isTimelord() {
        return this.isTimelord;
    }

    @Override
    public void setTimelord(boolean timelord) {
        this.isTimelord = timelord;
    }

    @Override
    public int getRegenTicks() {
        return this.regenTicks;
    }

    @Override
    public void setRegenTicks(int ticks) {
        this.regenTicks = ticks;
    }

    @Override
    public int getRegenCount() {
        return this.regenCount;
    }

    @Override
    public void setRegenCount(int count) {
        this.regenCount = count;
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

    public static class EventHandler {
        @SubscribeEvent
        public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (!(event.getObject() instanceof EntityPlayer) || event.getObject().hasCapability(TIMELORD_CAP, null))
                return;
            event.addCapability(new ResourceLocation(Regeneration.MODID, "timelord"),
                    new CapabilityTimelordProvider(new TimelordCapability((EntityPlayer) event.getObject())));
        }

        @SubscribeEvent
        public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
            if (event.getTarget().hasCapability(TIMELORD_CAP, null)) {
                event.getTarget().getCapability(TIMELORD_CAP, null).syncToPlayer();
            }
        }

        @SubscribeEvent
        public void onPlayerClone(PlayerEvent.Clone event) {
            NBTTagCompound nbt = (NBTTagCompound) TIMELORD_CAP.getStorage().writeNBT(TIMELORD_CAP, event.getOriginal().getCapability(TIMELORD_CAP, null), null);
            TIMELORD_CAP.getStorage().readNBT(TIMELORD_CAP, event.getEntityPlayer().getCapability(TIMELORD_CAP, null), null, nbt);
        }

        @SubscribeEvent
        public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
            if (event.getEntity() instanceof EntityPlayer) {
                if (event.getEntity().hasCapability(TimelordCapability.TIMELORD_CAP, null) && event.getEntity()
                        .getCapability(TimelordCapability.TIMELORD_CAP, null)
                        .isTimelord()) {
                    ITimelordCapability capability = event.getEntity().getCapability(TimelordCapability.TIMELORD_CAP, null);
                    EntityPlayer player = (EntityPlayer) event.getEntity();
                    if (capability.getRegenTicks() > 0) {
                        capability.setRegenTicks(capability.getRegenTicks() + 1);
                        capability.syncToPlayer();

                        if (player.world.isRemote && Minecraft.getMinecraft().player.getName().equals(player.getName()))
                            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;

                        if (!player.world.isRemote && capability.getRegenTicks() > 100) {
                            player.extinguish();
                            if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
                                player.world.setBlockToAir(player.getPosition());

                            double x = player.posX + player.getRNG().nextGaussian() * 2;
                            double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
                            double z = player.posZ + player.getRNG().nextGaussian() * 2;

                            player.world.newExplosion(player, x, y, z, 1, true, false);
                        }
                    }

                    if (capability.getRegenTicks() > 200) {
                        capability.setRegenTicks(0);
                        player.setHealth(player.getMaxHealth());
                        player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 3600, 3, false, false));
                        capability.setRegenCount(capability.getRegenCount() + 1);

                        if (player.world.isRemote && Minecraft.getMinecraft().player.getName().equals(player.getName()))
                            Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
                    }
                }
            }

        }

        @SubscribeEvent
        public void onAttacked(LivingAttackEvent event) {
            if (event.getEntity() instanceof EntityPlayer) {
                if (event.getEntity().hasCapability(TimelordCapability.TIMELORD_CAP, null) && event.getEntity()
                        .getCapability(TimelordCapability.TIMELORD_CAP, null)
                        .isTimelord()) {
                    ITimelordCapability capability = event.getEntity().getCapability(TimelordCapability.TIMELORD_CAP, null);

                    if ((event.getSource().isExplosion() || event.getSource().isFireDamage()) && capability.getRegenTicks() >= 100) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent
        public void onDeath(LivingDeathEvent event) {
            if (event.getEntity() instanceof EntityPlayer) {
                if (event.getEntity().hasCapability(TimelordCapability.TIMELORD_CAP, null) && event.getEntity().getCapability(TimelordCapability.TIMELORD_CAP, null)
                        .isTimelord()) {
                    ITimelordCapability capability = event.getEntity().getCapability(TimelordCapability.TIMELORD_CAP, null);
                    capability.setRegenTicks(0);
                }
            }
        }

        @SubscribeEvent
        public void onHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                if (event.getEntity().hasCapability(TimelordCapability.TIMELORD_CAP, null) && event.getEntity().getCapability(TimelordCapability.TIMELORD_CAP, null).isTimelord()) {
                    ITimelordCapability capability = event.getEntity().getCapability(TimelordCapability.TIMELORD_CAP, null);
                    SoundEvent[] RegenSounds = new SoundEvent[]{SoundReg.Reg_1, SoundReg.Reg_2};
                    SoundEvent Sound = RegenSounds[player.world.rand.nextInt(RegenSounds.length)];

                    player.world.playSound(null, player.posX, player.posY, player.posZ, Sound, SoundCategory.PLAYERS, 1.0F, 1.0F);

                    if (player.getHealth() - event.getAmount() <= 0) {
                        if (capability.getRegenCount() < 12 && capability.getRegenTicks() == 0) {
                            event.setCanceled(true);
                            player.setHealth(1.5f);
                            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1, false, false));
                            if (capability.getRegenTicks() == 0)
                                capability.setRegenTicks(1);
                            LucraftCoreUtil.sendSuperpowerUpdatePacketToAllPlayers(player);

                            String time = "" + (capability.getRegenCount() + 1);
                            switch (capability.getRegenCount() + 1) {
                                case 1:
                                    time = time + "st";
                                    break;
                                case 2:
                                    time = time + "nd";
                                    break;
                                case 3:
                                    time = time + "rd";
                                    break;
                                default:
                                    time = time + "th";
                                    break;
                            }
                            player.sendStatusMessage(new TextComponentString(
                                    "You're regenerating for the " + time + " time, you have " + (11 - capability.getRegenCount()) + " regenerations left."), true);

                        } else if (capability.getRegenCount() >= 12)
                            player.sendStatusMessage(new TextComponentString("You're out of regenerations. You're dying for real this time."), true);
                    }
                    capability.syncToPlayer();
                }
            }
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
