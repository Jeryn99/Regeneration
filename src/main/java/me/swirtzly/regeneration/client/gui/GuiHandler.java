package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.client.gui.parts.HIJContainer;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case GuiHij.ID:
                return new HIJContainer(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)), player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case GuiCustomizer.ID:
                return new GuiCustomizer();
            case GuiPreferences.ID:
                return new GuiPreferences();
            case GuiSkinChange.ID:
                return new GuiSkinChange();
            case GuiHij.ID:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                return new GuiHij(player.inventory, (IInventory) tile, (TileEntityHandInJar) tile);
            default:
                return null;
        }
    }

}
