package me.swirtzly.regeneration.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class CommonProxy implements Proxy {
	
	@Override
    public void postInit() {

    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Only run this on the client!");
    }

    @Override
    public PlayerEntity getClientPlayer() {
        throw new IllegalStateException("Only run this on the client!");
    }
}
