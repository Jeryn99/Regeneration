package me.fril.common.states;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType {
    String getName();

    void onUpdateInitial(EntityPlayer player);

    void onUpdateMidRegen(EntityPlayer player);

    void onFinish(EntityPlayer player);

    SoundEvent getSound();

    boolean blockMovement();

    boolean isLaying();
}
