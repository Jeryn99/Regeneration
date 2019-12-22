package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.gui.parts.GuiColorSlider;
import me.swirtzly.regeneration.client.gui.parts.InventoryTabRegeneration;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.IRegenType;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.network.MessageSaveStyle;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.RenderUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import javax.annotation.Nullable;
import java.awt.*;

public class GuiCustomizer extends GuiContainer {
    public static final int ID = 0;

    private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background_small.png");

    private GuiButtonExt btnDefault, btnReset, btnBack;
    private GuiColorSlider slidePrimaryRed, slidePrimaryGreen, slidePrimaryBlue, slideSecondaryRed, slideSecondaryGreen, slideSecondaryBlue;

    private Vec3d initialPrimary, initialSecondary;

    public GuiCustomizer() {
        super(new BlankContainer());
        xSize = 176;
        ySize = 186;
    }

    @Override
    public void initGui() {
        super.initGui();
        TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabRegeneration.class);
        TabRegistry.addTabsToList(this.buttonList);
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        IRegeneration cap = CapabilityRegeneration.getForPlayer(mc.player);
        initialPrimary = cap.getPrimaryColor();
        initialSecondary = cap.getSecondaryColor();

        float primaryRed = (float) initialPrimary.x, primaryGreen = (float) initialPrimary.y, primaryBlue = (float) initialPrimary.z;
        float secondaryRed = (float) initialSecondary.x, secondaryGreen = (float) initialSecondary.y, secondaryBlue = (float) initialSecondary.z;

        final int btnW = 68, btnH = 17;
        final int sliderW = 70, sliderH = 20;

        // WE CAN'T USE BUTTON ID'S 2 & 3 HERE BECAUSE THEY ARE USED BY THE INVENTORY TAB BUTTONS
        btnReset = new GuiButtonExt(1, cx + 10, cy + 125, sliderW, btnH, new TextComponentTranslation("regeneration.gui.undo").getFormattedText());
        btnDefault = new GuiButtonExt(4, cx + 96, cy + 125, sliderW, btnH, new TextComponentTranslation("regeneration.gui.default").getFormattedText());
        btnBack = new GuiButtonExt(99, cx + 10, cy + 145, btnW + 90, btnH, new TextComponentTranslation("regeneration.gui.back").getFormattedText());

        btnReset.enabled = false;
        buttonList.add(btnReset);
        buttonList.add(btnDefault);
        buttonList.add(btnBack);

        slidePrimaryRed = new GuiColorSlider(5, cx + 10, cy + 65, sliderW, sliderH, new TextComponentTranslation("regeneration.gui.red").getFormattedText(), "", 0, 1, primaryRed, true, true, this::onChangeSliderValue);
        slidePrimaryGreen = new GuiColorSlider(6, cx + 10, cy + 84, sliderW, sliderH, new TextComponentTranslation("regeneration.gui.green").getFormattedText(), "", 0, 1, primaryGreen, true, true, this::onChangeSliderValue);
        slidePrimaryBlue = new GuiColorSlider(7, cx + 10, cy + 103, sliderW, sliderH, new TextComponentTranslation("regeneration.gui.blue").getFormattedText(), "", 0, 1, primaryBlue, true, true, this::onChangeSliderValue);

        slideSecondaryRed = new GuiColorSlider(8, cx + 96, cy + 65, sliderW, sliderH, new TextComponentTranslation("regeneration.gui.red").getFormattedText(), "", 0, 1, secondaryRed, true, true, this::onChangeSliderValue);
        slideSecondaryGreen = new GuiColorSlider(9, cx + 96, cy + 84, sliderW, sliderH, new TextComponentTranslation("regeneration.gui.green").getFormattedText(), "", 0, 1, secondaryGreen, true, true, this::onChangeSliderValue);
        slideSecondaryBlue = new GuiColorSlider(10, cx + 96, cy + 103, sliderW, sliderH, new TextComponentTranslation("regeneration.gui.blue").getFormattedText(), "", 0, 1, secondaryBlue, true, true, this::onChangeSliderValue);

        buttonList.add(slidePrimaryRed);
        buttonList.add(slidePrimaryGreen);
        buttonList.add(slidePrimaryBlue);

        buttonList.add(slideSecondaryRed);
        buttonList.add(slideSecondaryGreen);
        buttonList.add(slideSecondaryBlue);

    }

    private void onChangeSliderValue(@Nullable GuiSlider slider) {
        btnReset.enabled = true;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("PrimaryRed", (float) slidePrimaryRed.getValue());
        nbt.setFloat("PrimaryGreen", (float) slidePrimaryGreen.getValue());
        nbt.setFloat("PrimaryBlue", (float) slidePrimaryBlue.getValue());

        nbt.setFloat("SecondaryRed", (float) slideSecondaryRed.getValue());
        nbt.setFloat("SecondaryGreen", (float) slideSecondaryGreen.getValue());
        nbt.setFloat("SecondaryBlue", (float) slideSecondaryBlue.getValue());

        NetworkHandler.INSTANCE.sendToServer(new MessageSaveStyle(nbt));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == btnReset.id) {
            slidePrimaryRed.setValue(initialPrimary.x);
            slidePrimaryGreen.setValue(initialPrimary.y);
            slidePrimaryBlue.setValue(initialPrimary.z);

            slideSecondaryRed.setValue(initialSecondary.x);
            slideSecondaryGreen.setValue(initialSecondary.y);
            slideSecondaryBlue.setValue(initialSecondary.z);

            btnReset.enabled = false;
        } else if (button.id == btnDefault.id) {
            IRegenType type = TypeHandler.getTypeInstance(CapabilityRegeneration.getForPlayer(mc.player).getType());
            slidePrimaryRed.setValue(type.getDefaultPrimaryColor().x);
            slidePrimaryGreen.setValue(type.getDefaultPrimaryColor().y);
            slidePrimaryBlue.setValue(type.getDefaultPrimaryColor().z);

            slideSecondaryRed.setValue(type.getDefaultSecondaryColor().x);
            slideSecondaryGreen.setValue(type.getDefaultSecondaryColor().y);
            slideSecondaryBlue.setValue(type.getDefaultSecondaryColor().z);

            onChangeSliderValue(null);
        } else if (button.id == btnBack.id) {
            Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiPreferences.ID, Minecraft.getMinecraft().world, 0, 0, 0);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 173);

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        RenderUtil.drawRect(cx + 10, cy + 44, cx + 81, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderUtil.drawRect(cx + 11, cy + 45, cx + 80, cy + 60, (float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue(), 1);

        RenderUtil.drawRect(cx + 95, cy + 44, cx + 166, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderUtil.drawRect(cx + 96, cy + 45, cx + 165, cy + 60, (float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue(), 1);

        Vec3d primaryColor = new Vec3d((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue()),
                secondaryColor = new Vec3d((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue());

        String str = new TextComponentTranslation("regeneration.gui.primary").getFormattedText();
        int length = mc.fontRenderer.getStringWidth(str);
        fontRenderer.drawString(str, cx + 45 - length / 2, cy + 49, RenderUtil.calculateColorBrightness(primaryColor) > 0.179 ? 0x0 : 0xFFFFFF);

        str = new TextComponentTranslation("regeneration.gui.secondary").getFormattedText();
        length = mc.fontRenderer.getStringWidth(str);
        fontRenderer.drawString(str, cx + 131 - length / 2, cy + 49, RenderUtil.calculateColorBrightness(secondaryColor) > 0.179 ? 0x0 : 0xFFFFFF);

        drawCenteredString(Minecraft.getMinecraft().fontRenderer, new TextComponentTranslation("regeneration.gui.color_gui").getUnformattedText(), width / 2, height / 2 - 80, Color.WHITE.getRGB());

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

}