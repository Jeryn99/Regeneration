package me.sub.common.capability;

import me.sub.common.states.RegenType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegeneration extends INBTSerializable<NBTTagCompound> {

    void update();

    int getTicksRegenerating();

    //Regen Ticks
    void setTicksRegenerating(int ticks);

    //Returns the player
    EntityPlayer getPlayer();

    //Lives
    int getLivesLeft();

    void setLivesLeft(int left);

    int getTimesRegenerated();

    void setTimesRegenerated(int times);

    NBTTagCompound getStyle();

    void setStyle(NBTTagCompound nbt);

    //Sync
    void sync();

    RegenType getType();

    boolean isCapable();

    void setCapable(boolean capable);

    boolean isRegenerating();

    void setRegenerating(boolean regenerating);
}
