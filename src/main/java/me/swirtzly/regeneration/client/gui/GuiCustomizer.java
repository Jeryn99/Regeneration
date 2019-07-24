package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.gui.parts.GuiColorSlider;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.network.MessageSaveStyle;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;

public class GuiCustomizer extends ContainerScreen {
    public static final int ID = 0;

    private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");

    private GuiColorSlider slidePrimaryRed, slidePrimaryGreen, slidePrimaryBlue, slideSecondaryRed, slideSecondaryGreen, slideSecondaryBlue;

    private Vec3d initialPrimary, initialSecondary;

    public GuiCustomizer() {
        super(new BlankContainer(), null, new TranslationTextComponent(""));
        xSize = 176;
        ySize = 186;
    }


    @Override
    public void init() {
        super.init();

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        CapabilityRegeneration.getForPlayer(minecraft.player).ifPresent((data) -> {
            initialPrimary = data.getPrimaryColor();
            initialSecondary = data.getSecondaryColor();
        });

        float primaryRed = (float) initialPrimary.x, primaryGreen = (float) initialPrimary.y, primaryBlue = (float) initialPrimary.z;
        float secondaryRed = (float) initialSecondary.x, secondaryGreen = (float) initialSecondary.y, secondaryBlue = (float) initialSecondary.z;

        final int btnW = 60, btnH = 18;
        final int sliderW = 70, sliderH = 20;

        //Reset Style Button
        this.addButton(new GuiButtonExt(cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.undo").getFormattedText(),
                button -> {
                    slidePrimaryRed.setValue(initialPrimary.x);
                    slidePrimaryGreen.setValue(initialPrimary.y);
                    slidePrimaryBlue.setValue(initialPrimary.z);

                    slideSecondaryRed.setValue(initialSecondary.x);
                    slideSecondaryGreen.setValue(initialSecondary.y);
                    slideSecondaryBlue.setValue(initialSecondary.z);
                }));

        //Reset Skin Button
        this.addButton(new GuiButtonExt(cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.reset_skin").getFormattedText(), p_onPress_1_ -> ClientUtil.sendSkinResetPacket()));

        //Customize Button
        this.addButton(new GuiButtonExt(cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.customize").getFormattedText(), button -> Minecraft.getInstance().displayGuiScreen(new GuiSkinCustomizer())));

        //Default Button
        this.addButton(new GuiButtonExt(cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.default").getFormattedText(), button -> {
            slidePrimaryRed.setValue(0.93F);
            slidePrimaryGreen.setValue(0.61F);
            slidePrimaryBlue.setValue(0F);

            slideSecondaryRed.setValue(1F);
            slideSecondaryGreen.setValue(0.5F);
            slideSecondaryBlue.setValue(0.18F);

            onChangeSliderValue(null);
        }));

        slidePrimaryRed = new GuiColorSlider(cx + 10, cy + 65, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.red").getFormattedText(), "", 0, 1, primaryRed, true, true, this::onChangeSliderValue);
        slidePrimaryGreen = new GuiColorSlider(cx + 10, cy + 84, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.green").getFormattedText(), "", 0, 1, primaryGreen, true, true, this::onChangeSliderValue);
        slidePrimaryBlue = new GuiColorSlider(cx + 10, cy + 103, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.blue").getFormattedText(), "", 0, 1, primaryBlue, true, true, this::onChangeSliderValue);

        slideSecondaryRed = new GuiColorSlider(cx + 96, cy + 65, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.red").getFormattedText(), "", 0, 1, secondaryRed, true, true, this::onChangeSliderValue);
        slideSecondaryGreen = new GuiColorSlider(cx + 96, cy + 84, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.green").getFormattedText(), "", 0, 1, secondaryGreen, true, true, this::onChangeSliderValue);
        slideSecondaryBlue = new GuiColorSlider(cx + 96, cy + 103, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.blue").getFormattedText(), "", 0, 1, secondaryBlue, true, true, this::onChangeSliderValue);

        addButton(slidePrimaryRed);
        addButton(slidePrimaryGreen);
        addButton(slidePrimaryBlue);

        addButton(slideSecondaryRed);
        addButton(slideSecondaryGreen);
        addButton(slideSecondaryBlue);

    }


    private void onChangeSliderValue(Button button) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("PrimaryRed", (float) slidePrimaryRed.getValue());
        nbt.putFloat("PrimaryGreen", (float) slidePrimaryGreen.getValue());
        nbt.putFloat("PrimaryBlue", (float) slidePrimaryBlue.getValue());

        nbt.putFloat("SecondaryRed", (float) slideSecondaryRed.getValue());
        nbt.putFloat("SecondaryGreen", (float) slideSecondaryGreen.getValue());
        nbt.putFloat("SecondaryBlue", (float) slideSecondaryBlue.getValue());

        NetworkHandler.sendToServer(new MessageSaveStyle(nbt));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        Minecraft.getInstance().getTextureManager().bindTexture(background);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        RenderUtil.drawRect(cx + 10, cy + 44, cx + 81, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderUtil.drawRect(cx + 11, cy + 45, cx + 80, cy + 60, (float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue(), 1);

        RenderUtil.drawRect(cx + 95, cy + 44, cx + 166, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderUtil.drawRect(cx + 96, cy + 45, cx + 165, cy + 60, (float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue(), 1);

        Vec3d primaryColor = new Vec3d((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue()),
                secondaryColor = new Vec3d((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue());

        CapabilityRegeneration.getForPlayer(minecraft.player).ifPresent((cap) -> {
            String str = new TranslationTextComponent("regeneration.gui.primary").getFormattedText();
            int length = minecraft.fontRenderer.getStringWidth(str);
            font.drawString(str, cx + 45 - length / 2, cy + 49, RenderUtil.calculateColorBrightness(primaryColor) > 0.179 ? 0x0 : 0xFFFFFF);

            str = new TranslationTextComponent("regeneration.gui.secondary").getFormattedText();
            length = minecraft.fontRenderer.getStringWidth(str);
            font.drawString(str, cx + 131 - length / 2, cy + 49, RenderUtil.calculateColorBrightness(secondaryColor) > 0.179 ? 0x0 : 0xFFFFFF);

            if (RegenConfig.COMMON.infiniteRegeneration.get())
                str = new TranslationTextComponent("regeneration.gui.infinite_regenerations").getFormattedText();
            else
                str = new TranslationTextComponent("regeneration.gui.remaining_regens.status").getFormattedText() + " " + cap.getRegenerationsLeft();
            length = minecraft.fontRenderer.getStringWidth(str);
            font.drawString(str, cx + 86 - length / 2, cy + 21, Color.DARK_GRAY.getRGB());
        });
    }


}
