package me.sub.common.capability;

import me.sub.Regeneration;
import me.sub.client.RKeyBinds;
import me.sub.common.init.RObjects;
import me.sub.common.states.EnumRegenType;
import me.sub.config.RegenConfig;
import me.sub.network.NetworkHandler;
import me.sub.network.packets.MessageUpdateRegen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class CapabilityRegeneration implements IRegeneration {

    public static final ResourceLocation REGEN_ID = new ResourceLocation(Regeneration.MODID, "regeneration");
    @CapabilityInject(IRegeneration.class)
    public static final Capability<IRegeneration> CAPABILITY = null;

    private int timesRegenerated = 0, livesLeft = RegenConfig.Regen.regenCapacity, regenTicks = 0, ticksInSolace = 0, ticksGlowing = 0;
    private EntityPlayer player;
    private boolean textured = false, isRegenerating = false, isCapable = false, isInGrace = false, isGraceGlowing = false;
    private String typeName = EnumRegenType.FIERY.name();

    private float primaryRed = 1.0f, primaryGreen = 0.78f, primaryBlue = 0.0f;
    private float secondaryGreen = 0.47f, secondaryRed = 1.0f, secondaryBlue = 0.0f;

    public CapabilityRegeneration() {
    }

    public CapabilityRegeneration(EntityPlayer player) {
        this.player = player;
    }

    public static void init() {
        CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
    }

    //Returns the players Regeneration capability
    public static IRegeneration get(EntityPlayer player) {
        if (player.hasCapability(CAPABILITY, null)) {
            return player.getCapability(CAPABILITY, null);
        }
        throw new IllegalStateException("Missing Regeneration capability: " + player + ", please report this to the issue tracker");
    }

    @Override
    public void update() {
        if (isRegenerating()) {
            startRegenerating();
            sync();
        } else {
            setSolaceTicks(0);
            setInGracePeriod(false);
            setTicksGlowing(0);
            setGlowing(false);
        }
    }

    @Override
    public boolean isRegenerating() {
        return isRegenerating;
    }

    @Override
    public void setRegenerating(boolean regenerating) {
        isRegenerating = regenerating;
    }

    @Override
    public boolean isGlowing() {
        return isGraceGlowing;
    }

    @Override
    public void setGlowing(boolean glowing) {
        isGraceGlowing = glowing;
    }

    @Override
    public int getTicksGlowing() {
        return ticksGlowing;
    }

    @Override
    public void setTicksGlowing(int ticks) {
        ticksGlowing = ticks;
    }

    @Override
    public boolean isInGracePeriod() {
        return isInGrace;
    }

    @Override
    public void setInGracePeriod(boolean gracePeriod) {
        isInGrace = gracePeriod;
    }

    @Override
    public int getSolaceTicks() {
        return ticksInSolace;
    }

    @Override
    public void setSolaceTicks(int ticks) {
        ticksInSolace = ticks;
    }

    @Override
    public boolean isCapable() {
        return isCapable && getLivesLeft() > 0 && player.posY > 0;
    }

    @Override
    public void setCapable(boolean capable) {
        isCapable = capable;
    }

    @Override
    public int getTicksRegenerating() {
        return regenTicks;
    }

    @Override
    public void setTicksRegenerating(int ticks) {
        regenTicks = ticks;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public int getLivesLeft() {
        return livesLeft;
    }

    @Override
    public void setLivesLeft(int left) {
        livesLeft = left;
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
    public NBTTagCompound getStyle() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("PrimaryRed", primaryRed);
        nbt.setFloat("PrimaryGreen", primaryGreen);
        nbt.setFloat("PrimaryBlue", primaryBlue);
        nbt.setFloat("SecondaryRed", secondaryRed);
        nbt.setFloat("SecondaryGreen", secondaryGreen);
        nbt.setFloat("SecondaryBlue", secondaryBlue);
        nbt.setBoolean("textured", textured);
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
        textured = nbt.getBoolean("textured");
    }

    @Override
    public void sync() {
        NetworkHandler.INSTANCE.sendToAll(new MessageUpdateRegen(player, serializeNBT()));
    }

    @Override
    public EnumRegenType getType() {
        return EnumRegenType.valueOf(typeName);
    }

    @Override
    public void setType(String name) {
        typeName = name;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = getStyle();

        nbt.setBoolean("isRegenerating", isRegenerating);
        nbt.setInteger("timesRegenerated", timesRegenerated);
        nbt.setInteger("livesLeft", livesLeft);
        nbt.setBoolean("isCapable", isCapable);
        nbt.setInteger("regenTicks", regenTicks);
        nbt.setBoolean("gracePeriod", isInGrace);
        nbt.setInteger("solaceTicks", ticksInSolace);
        nbt.setBoolean("handGlowing", isGraceGlowing);
        nbt.setString("type", typeName);

        nbt.setFloat("PrimaryRed", primaryRed);
        nbt.setFloat("PrimaryGreen", primaryGreen);
        nbt.setFloat("PrimaryBlue", primaryBlue);
        nbt.setFloat("SecondaryRed", secondaryRed);
        nbt.setFloat("SecondaryGreen", secondaryGreen);
        nbt.setFloat("SecondaryBlue", secondaryBlue);
        nbt.setBoolean("textured", textured);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setRegenerating(nbt.getBoolean("isRegenerating"));
        setTimesRegenerated(nbt.getInteger("timesRegenerated"));
        setLivesLeft(nbt.getInteger("livesLeft"));
        setCapable(nbt.getBoolean("isCapable"));
        setTicksRegenerating(nbt.getInteger("regenTicks"));
        setInGracePeriod(nbt.getBoolean("gracePeriod"));
        setSolaceTicks(nbt.getInteger("solaceTicks"));
        setGlowing(nbt.getBoolean("handGlowing"));
        setType(nbt.getString("type"));

        //Primary
        primaryRed = nbt.getFloat("PrimaryRed");
        primaryBlue = nbt.getFloat("PrimaryBlue");
        primaryGreen = nbt.getFloat("PrimaryGreen");

        //Secondary
        secondaryRed = nbt.getFloat("SecondaryRed");
        secondaryGreen = nbt.getFloat("SecondaryGreen");
        secondaryBlue = nbt.getFloat("SecondaryBlue");

        //textured
        textured = nbt.getBoolean("textured");
    }

    //Invokes the Regeneration and handles it.
    private void startRegenerating() {

        if (player.getHealth() <= 0) {
            setCapable(RegenConfig.Regen.dontLoseUponDeath);
        }

        setSolaceTicks(getSolaceTicks() + 1);

        if (getSolaceTicks() == 1 && !isInGracePeriod()) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, RObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        if (player.world.isRemote && getSolaceTicks() < 200 && !isInGracePeriod()) {
            if (ticksInSolace % 25 == 0) {
                player.sendStatusMessage(new TextComponentString("Grace: " + RKeyBinds.GRACE.getDisplayName() + " || Regenerate: " + RKeyBinds.JUSTDOIT.getDisplayName()), true);
            }
        }

        //The actual regeneration
        if (!isInGracePeriod() && getSolaceTicks() > 200) {
            player.dismountRidingEntity();
            player.removePassengers();

            setTicksRegenerating(getTicksRegenerating() + 1);

            if (getTicksRegenerating() == 1) {
                player.world.playSound(null, player.posX, player.posY, player.posZ, getType().getType().getSound(), SoundCategory.PLAYERS, 0.5F, 1.0F);
                setLivesLeft(getLivesLeft() - 1);
                setTimesRegenerated(getTimesRegenerated() + 1);
            }

            if (getTicksRegenerating() > 0 && getTicksRegenerating() < 100)
                getType().getType().onInitial(player);

            if (getTicksRegenerating() >= 100 && getTicksRegenerating() < 200) {
                getType().getType().onMidRegen(player);
                player.sendStatusMessage(new TextComponentString("This is Regeneration #" + getTimesRegenerated() + ", You have " + getLivesLeft() + " lives left!"), true);
            }

            if (player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getHealth() + 1);
            }

            if (RegenConfig.Regen.resetHunger) {
                FoodStats foodStats = player.getFoodStats();
                foodStats.setFoodLevel(foodStats.getFoodLevel() + 1);
            }

            if (RegenConfig.Regen.resetOxygen) {
                player.setAir(player.getAir() + 1);
            }

            if (getTicksRegenerating() == 200) {
                getType().getType().onFinish(player);
                setTicksRegenerating(0);
                setRegenerating(false);
                setSolaceTicks(0);
                setInGracePeriod(false);
            }
        }

        //Grace handling
        if (isInGracePeriod()) {
            if (isGlowing()) {
                setTicksGlowing(getTicksGlowing() + 1);
            }

            if (getTicksGlowing() >= 600) {
                setInGracePeriod(false);
            }

            if (getSolaceTicks() % 220 == 0) {
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    BlockPos pos = playerMP.getPosition();
                    playerMP.connection.sendPacket(new SPacketSoundEffect(RObjects.Sounds.HEART_BEAT, SoundCategory.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), 0.3F, 1));
                }
            }

            //Every Minute
            if (getSolaceTicks() % 1200 == 0) {
                setGlowing(true);
            }

            //Five minutes
            if (getSolaceTicks() == 6000) {

            }

            //14 Minutes - Critical stage start
            if (getSolaceTicks() == 17100) {
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    BlockPos pos = playerMP.getPosition();
                    playerMP.connection.sendPacket(new SPacketSoundEffect(RObjects.Sounds.CRITICAL_STAGE, SoundCategory.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), 0.3F, 1));
                }
            }

            //CRITICAL STAGE
            if (getSolaceTicks() > 16800 && getSolaceTicks() < 18000) {

            }

            //15 minutes all gone, rip user
            if (getSolaceTicks() >= 18000) {

                if (!player.world.isRemote) {
                    player.setDead();
                    setSolaceTicks(0);
                }

                setCapable(false);
                setLivesLeft(0);
                setInGracePeriod(false);
                setGlowing(false);
                setTicksGlowing(0);
                setTicksRegenerating(0);
                setRegenerating(false);
            }
        }

    }

    @Override
    public Color getPrimaryColor() {
        return new Color(primaryRed, primaryGreen, primaryBlue);
    }

    @Override
    public Color getSecondaryColor() {
        return new Color(secondaryRed, secondaryGreen, secondaryBlue);
    }

}
