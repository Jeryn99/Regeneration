package me.sub.client.gui;

import me.sub.Regeneration;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.network.NetworkHandler;
import me.sub.network.packets.MessageRegenerationStyle;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

//TODO - Make look nicer
//TODO - Make EnumRegenTypes cycleable
public class GuiCustomizer extends GuiScreen {

    private static ResourceLocation DEFAULT_TEX = new ResourceLocation(Regeneration.MODID, "textures/gui/smallbg.png");
    private int guiLeft, guiTop, xSize, ySize;
    private float primaryRed, primaryGreen, primaryBlue, secondaryRed, secondaryGreen, secondaryBlue;
    private boolean textured;
    private EntityPlayer player = Minecraft.getMinecraft().player;

    public GuiCustomizer() {
        xSize = 182;
        ySize = 185;
    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        NBTTagCompound old = CapabilityRegeneration.get(mc.player).getStyle();
        primaryRed = old.getFloat("PrimaryRed");
        primaryGreen = old.getFloat("PrimaryGreen");
        primaryBlue = old.getFloat("PrimaryBlue");
        secondaryRed = old.getFloat("SecondaryRed");
        secondaryGreen = old.getFloat("SecondaryGreen");
        secondaryBlue = old.getFloat("SecondaryBlue");
        textured = old.getBoolean("textured");

        TabRegistry.updateTabValues(guiLeft + 2, guiTop + 8, GuiCustomizer.class);
        TabRegistry.addTabsToList(buttonList);


        buttonList.add(new GuiButtonExt(1, guiLeft + 10, guiTop + 176, 45, 15, new TextComponentTranslation("regeneration.info.save").getFormattedText()));
        buttonList.add(new GuiButtonExt(2, guiLeft + 70, guiTop + 176, 45, 15, new TextComponentTranslation("regeneration.info.reset").getFormattedText()));
        buttonList.add(new GuiButtonExt(3, guiLeft + 130, guiTop + 176, 45, 15, new TextComponentTranslation("gui.cancel").getFormattedText()));

    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(DEFAULT_TEX);
        drawTexturedModalRect(guiLeft, guiTop + 8, 0, 0, xSize, 192);
        super.drawScreen(i, j, f);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiInventory.drawEntityOnScreen(guiLeft + 130, guiTop + 130, 40, guiLeft + 130 - i, guiTop + 60 - j, player);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (button.id == 1) {
            NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getStyleNBTTag()));
            player.closeScreen();
        }
        if (button.id == 2) {
            NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getDefaultStyle()));
            player.closeScreen();
        }
        if (button.id == 3) {
            player.closeScreen();
        }

    }

    private NBTTagCompound getStyleNBTTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("PrimaryRed", primaryRed);
        nbt.setFloat("PrimaryGreen", primaryGreen);
        nbt.setFloat("PrimaryBlue", primaryBlue);
        nbt.setFloat("SecondaryRed", secondaryRed);
        nbt.setFloat("SecondaryGreen", secondaryGreen);
        nbt.setFloat("SecondaryBlue", secondaryBlue);
        nbt.setBoolean("textured", textured);
        return nbt;
    }

    private NBTTagCompound getDefaultStyle() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("PrimaryRed", 1.0f);
        nbt.setFloat("PrimaryGreen", 0.78f);
        nbt.setFloat("PrimaryBlue", 0.0f);
        nbt.setFloat("SecondaryRed", 1.0f);
        nbt.setFloat("SecondaryGreen", 0.47f);
        nbt.setFloat("SecondaryBlue", 0.0f);
        nbt.setBoolean("textured", false);
        return nbt;
    }

}