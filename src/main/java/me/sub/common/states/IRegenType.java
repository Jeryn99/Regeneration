package me.sub.common.states;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType {
    String getName();

    void onInitial(EntityPlayer player);

    void onMidRegen(EntityPlayer player);

    void onFinish(EntityPlayer player);

    SoundEvent getSound();

    boolean blockMovement();

    boolean isLaying();
}
