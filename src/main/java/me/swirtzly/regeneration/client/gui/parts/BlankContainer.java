package me.swirtzly.regeneration.client.gui.parts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public class BlankContainer extends Container {

	public BlankContainer(@Nullable ContainerType<?> type, int id) {
		super(type, id);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return false;
	}
	
}
