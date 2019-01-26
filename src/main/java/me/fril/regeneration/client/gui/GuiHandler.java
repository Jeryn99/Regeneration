package me.fril.regeneration.client.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case CustomizerGui.ID:
				return new BlankContainer();
			default:
				return null;
		}
	}
	
	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case CustomizerGui.ID:
				return new CustomizerGui();
			default:
				return null;
		}
	}
	
}
