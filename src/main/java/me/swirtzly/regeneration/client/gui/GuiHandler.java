package me.swirtzly.regeneration.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
	
	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
				return null;
	}
	
	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case GuiCustomizer.ID:
				return new GuiCustomizer();
			case GuiModelChoice.ID:
				return new GuiModelChoice();
			case GuiSkinChange.ID:
				return new GuiSkinChange();
			default:
				return null;
		}
	}
	
}
