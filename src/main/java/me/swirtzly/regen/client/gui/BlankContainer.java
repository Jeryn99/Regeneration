package me.swirtzly.regen.client.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import org.jetbrains.annotations.Nullable;

public class BlankContainer extends net.minecraft.inventory.container.Container {

    public BlankContainer() {
        super(null, 999);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
