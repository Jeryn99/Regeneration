package me.sub.client.gui;

import me.sub.Regeneration;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import me.sub.network.NetworkHandler;
import me.sub.network.packets.MessageRegenerationStyle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;

public class GuiCustomizer extends GuiContainer implements GuiSlider.ISlider {

    public static ResourceLocation DEFAULT_TEX = new ResourceLocation(Regeneration.MODID, "textures/gui/longbg.png");
    public boolean textured = false;
    //private GuiColorPicker colorpicker;
    private Color selectedColor = Color.WHITE;
    private float primaryRed, primaryGreen, primaryBlue, secondaryRed, secondaryGreen, secondaryBlue;
    public GuiCustomizer() {
        super(new BlankContainer());
    }

    public static void drawRect(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void initGui() {
        super.initGui();

        this.xSize = 256;
        this.ySize = 189;
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        NBTTagCompound old = CapabilityRegeneration.get(mc.player).getStyle();
        primaryRed = old.getFloat("PrimaryRed");
        primaryGreen = old.getFloat("PrimaryGreen");
        primaryBlue = old.getFloat("PrimaryBlue");
        secondaryRed = old.getFloat("SecondaryRed");
        secondaryGreen = old.getFloat("SecondaryGreen");
        secondaryBlue = old.getFloat("SecondaryBlue");
        textured = old.getBoolean("textured");

        this.buttonList.add(new GuiButtonExt(0, i + 4, j + 167, 50, 18, new TextComponentTranslation("regeneration.info.save").getFormattedText()));
        this.buttonList.add(new GuiButtonExt(3, i + 100, j + 167, 50, 18, new TextComponentTranslation("regeneration.info.reset").getFormattedText()));
        this.buttonList.add(new GuiButtonExt(1, i + 202, j + 167, 50, 18, new TextComponentTranslation("gui.cancel").getFormattedText()));
        // this.texturedButton = new GuiButton(2, i + this.xSize/2 - 25, j + 45, 50, 20, new TextComponentTranslation("").getFormattedText());
        //this.buttonList.add(texturedButton);


        this.buttonList.add(new GuiColorSlider(6, i + 20, j + 90, 80, 20, new TextComponentTranslation("regeneration.info.red").getFormattedText(), "", 0, 1, primaryRed, true, true, this));
        this.buttonList.add(new GuiColorSlider(7, i + 20, j + 110, 80, 20, new TextComponentTranslation("regeneration.info.green").getFormattedText(), "", 0, 1, primaryGreen, true, true, this));
        this.buttonList.add(new GuiColorSlider(8, i + 20, j + 130, 80, 20, new TextComponentTranslation("regeneration.info.blue").getFormattedText(), "", 0, 1, primaryBlue, true, true, this));

        this.buttonList.add(new GuiColorSlider(9, i + 135, j + 90, 80, 20, new TextComponentTranslation("regeneration.info.red").getFormattedText(), "", 0, 1, secondaryRed, true, true, this));
        this.buttonList.add(new GuiColorSlider(10, i + 135, j + 110, 80, 20, new TextComponentTranslation("regeneration.info.green").getFormattedText(), "", 0, 1, secondaryGreen, true, true, this));
        this.buttonList.add(new GuiColorSlider(11, i + 135, j + 130, 80, 20, new TextComponentTranslation("regeneration.info.blue").getFormattedText(), "", 0, 1, secondaryBlue, true, true, this));
    }

    public NBTTagCompound getStyleNBTTag() {
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

    public NBTTagCompound getDefaultStyle() {
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        mc.getTextureManager().bindTexture(DEFAULT_TEX);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        IRegeneration capa = CapabilityRegeneration.get(mc.player);
        String name = "sdfsdfsdfdsfsd"; //= new TextComponentTranslation("regeneration.current_trait").getFormattedText() + " " + capa.getTrait().getTranslatedName().getFormattedText();
        int length;
        //this.drawString(mc.fontRenderer, name, i + this.xSize / 2 - length / 2, j + 30, 0xffffff);

//        this.texturedButton.displayString = (textured) ? "Yes" : "No";

        name = new TextComponentTranslation("regeneration.info.primary").getFormattedText();
        length = mc.fontRenderer.getStringWidth(name);
        this.drawString(mc.fontRenderer, name, i + 70 - length / 2, j + 75, 0xffffff);

        name = new TextComponentTranslation("regeneration.info.secondary").getFormattedText();
        length = mc.fontRenderer.getStringWidth(name);
        this.drawString(mc.fontRenderer, name, i + 185 - length / 2, j + 75, 0xffffff);

        drawRect(i + 99, j + 90, i + 121, j + 150, 0.1F, 0.1F, 0.1F, 1);
        drawRect(i + 100, j + 91, i + 120, j + 149, primaryRed, primaryGreen, primaryBlue, 1);

        drawRect(i + 214, j + 90, i + 236, j + 150, 0.1F, 0.1F, 0.1F, 1);
        drawRect(i + 215, j + 91, i + 235, j + 149, secondaryRed, secondaryGreen, secondaryBlue, 1);

        this.drawString(mc.fontRenderer, new TextComponentTranslation("regeneration.info.customizer").getFormattedText(), i + 5, j + 5, 0xffffff);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            sendStyleNBTTagToServer(true);
            mc.player.closeScreen();
        }
        if (button.id == 1) mc.player.closeScreen();
        if (button.id == 2) textured = !textured;
        if (button.id == 3) sendStyleNBTTagToServer(false);
    }

    private void sendStyleNBTTagToServer(boolean notReset) {
        if (notReset) {
            NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getStyleNBTTag()));
        } else {
            NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getDefaultStyle()));
        }
    }

    @Override
    public void onChangeSliderValue(GuiSlider slider) {
        if (slider.id == 6)
            this.primaryRed = (float) slider.sliderValue;
        else if (slider.id == 7)
            this.primaryGreen = (float) slider.sliderValue;
        else if (slider.id == 8)
            this.primaryBlue = (float) slider.sliderValue;
        else if (slider.id == 9)
            this.secondaryRed = (float) slider.sliderValue;
        else if (slider.id == 10)
            this.secondaryGreen = (float) slider.sliderValue;
        else if (slider.id == 11) this.secondaryBlue = (float) slider.sliderValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }


}