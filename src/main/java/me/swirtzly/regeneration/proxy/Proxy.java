package me.swirtzly.regeneration.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Created by Craig on 17/09/2018.
 */
public interface Proxy {

    default void preInit() {
    }

    default void init() {
    }

    default void postInit() {
    }

    default void closeGui() {

    }

    World getClientWorld();

    PlayerEntity getClientPlayer();

}
