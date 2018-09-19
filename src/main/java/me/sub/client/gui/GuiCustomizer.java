package me.sub.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class GuiCustomizer extends GuiScreen {

    private final EntityPlayer player;
    private NBTTagCompound styleTag;

    public GuiCustomizer(NBTTagCompound nbtTagCompound) {
        this.styleTag = nbtTagCompound;
        this.player = Minecraft.getMinecraft().player;
    }



}
