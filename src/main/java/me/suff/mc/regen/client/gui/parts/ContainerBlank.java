package me.suff.mc.regen.client.gui.parts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class ContainerBlank extends Container {

    public ContainerBlank() {
        super(null, 0);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return false;
    }

}
