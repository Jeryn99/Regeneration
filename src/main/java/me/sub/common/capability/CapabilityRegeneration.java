package me.sub.common.capability;

import me.sub.Regeneration;
import me.sub.common.states.EnumRegenType;
import me.sub.network.NetworkHandler;
import me.sub.network.packets.MessageUpdateRegen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class CapabilityRegeneration implements IRegeneration {

    public static final ResourceLocation REGEN_ID = new ResourceLocation(Regeneration.MODID, "regeneration");
    @CapabilityInject(IRegeneration.class)
    public static final Capability<IRegeneration> CAPABILITY = null;
    private int timesRegenerated = 0, livesLeft = 12, regenTicks = 0;
    private EntityPlayer player;
    private boolean isRegenerating = false, isCapable = false;
    private String typeName = EnumRegenType.FIERY.getType().getName();

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
        return getDefaultStyle();
    }

    @Override
    public void setStyle(NBTTagCompound nbt) {

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
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("isRegenerating", isRegenerating);
        nbt.setInteger("timesRegenerated", timesRegenerated);
        nbt.setInteger("livesLeft", livesLeft);
        nbt.setBoolean("isCapable", isCapable);
        nbt.setInteger("regenTicks", regenTicks);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setRegenerating(nbt.getBoolean("isRegenerating"));
        setTimesRegenerated(nbt.getInteger("timesRegenerated"));
        setLivesLeft(nbt.getInteger("livesLeft"));
        setCapable(nbt.getBoolean("isCapable"));
        setTicksRegenerating(nbt.getInteger("regenTicks"));
    }

    private void startRegenerating() {
        player.dismountRidingEntity();
        player.removePassengers();

        setTicksRegenerating(getTicksRegenerating() + 1);

        if (getTicksRegenerating() == 1) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, getType().getType().getSound(), SoundCategory.PLAYERS, 0.5F, 1.0F);
        }

        if (getTicksRegenerating() > 0 && getTicksRegenerating() < 100)
            getType().getType().onInitial(player);

        if (getTicksRegenerating() >= 100 && getTicksRegenerating() < 200)
            getType().getType().onMidRegen(player);

        if (player.getHealth() < player.getMaxHealth()) {
            player.setHealth(player.getHealth() + 1);
        }

        if (getTicksRegenerating() == 200) {
            getType().getType().onFinish(player);
            setTicksRegenerating(0);
            setRegenerating(false);
            setLivesLeft(getLivesLeft() - 1);
            setTimesRegenerated(getTimesRegenerated() + 1);
        }
    }

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

}
