package me.suff.mc.regen.client.gui;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.gui.parts.ColorSliderWidget;
import me.suff.mc.regen.client.gui.parts.ContainerBlank;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.types.RegenType;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.UpdateColorMessage;
import me.suff.mc.regen.util.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;

import static me.suff.mc.regen.util.client.ClientUtil.colorToHex;

public class ColorScreen extends ContainerScreen implements GuiSlider.ISlider {

    private static final ResourceLocation background = new ResourceLocation(Regeneration.MODID, "textures/gui/customizer_background.png");

    private static ColorSliderWidget slidePrimaryRed, slidePrimaryGreen, slidePrimaryBlue, slideSecondaryRed, slideSecondaryGreen, slideSecondaryBlue;

    private TextFieldWidget inputPrimaryColor, inputSecondColor;

    private Vec3d initialPrimary, initialSecondary;

    public ColorScreen() {
        super(new ContainerBlank(), null, new TranslationTextComponent("Regeneration"));
        imageWidth = 176;
        imageHeight = 186;
    }

    @Override
    public void init() {
        super.init();

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        RegenCap.get(minecraft.player).ifPresent((data) -> {
            initialPrimary = data.getPrimaryColor();
            initialSecondary = data.getSecondaryColor();
        });

        float primaryRed = (float) initialPrimary.x, primaryGreen = (float) initialPrimary.y, primaryBlue = (float) initialPrimary.z;
        float secondaryRed = (float) initialSecondary.x, secondaryGreen = (float) initialSecondary.y, secondaryBlue = (float) initialSecondary.z;

        final int btnW = 60, btnH = 18;
        final int sliderW = 70, sliderH = 20;


        // Reset Style Button
        this.addButton(new GuiButtonExt(cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.undo").getColoredString(), button -> {
            slidePrimaryRed.setValue(initialPrimary.x);
            slidePrimaryGreen.setValue(initialPrimary.y);
            slidePrimaryBlue.setValue(initialPrimary.z);

            slideSecondaryRed.setValue(initialSecondary.x);
            slideSecondaryGreen.setValue(initialSecondary.y);
            slideSecondaryBlue.setValue(initialSecondary.z);
        }));

        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.inputPrimaryColor = new TextFieldWidget(this.font, cx + 25, cy + 21, btnW, btnH, this.inputPrimaryColor, I18n.get("Input"));
        this.inputSecondColor = new TextFieldWidget(this.font, cx + 90, cy + 21, btnW, btnH, this.inputPrimaryColor, I18n.get("Input"));

        this.children.add(this.inputPrimaryColor);
        this.children.add(this.inputSecondColor);

        // Color input Primary button 
        this.addButton(new GuiButtonExt(cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.input_color").getColoredString(), button -> {
            String primaryColorText = inputPrimaryColor.getValue();
            String secondColourText = inputSecondColor.getValue();

            if (!primaryColorText.startsWith("#") && !primaryColorText.isEmpty()) {
                primaryColorText = "#" + primaryColorText;
                inputPrimaryColor.setValue(primaryColorText);
            }

            if (!secondColourText.startsWith("#") && !secondColourText.isEmpty()) {
                secondColourText = "#" + secondColourText;
                inputSecondColor.setValue(secondColourText);
            }

            /* Get Primary colour from input and set it */
            try {
                Color color = Color.decode(primaryColorText);
                float red = (float) color.getRed() / 255;
                float green = (float) color.getGreen() / 255;
                float blue = (float) color.getBlue() / 255;
                slidePrimaryRed.setValue(red);
                slidePrimaryGreen.setValue(green);
                slidePrimaryBlue.setValue(blue);
                onChangeSliderValue(null);
            } catch (Exception e) {
                Regeneration.LOG.error(primaryColorText + ", is not a valid Color! [Primary Colour]");
            }

            /* Get Secondary colour from input and set it */
            try {
                Color color = Color.decode(secondColourText);
                float red = (float) color.getRed() / 255;
                float green = (float) color.getGreen() / 255;
                float blue = (float) color.getBlue() / 255;
                slideSecondaryRed.setValue(red);
                slideSecondaryGreen.setValue(green);
                slideSecondaryBlue.setValue(blue);
                onChangeSliderValue(null);
            } catch (Exception e) {
                Regeneration.LOG.error(secondColourText + ", is not a valid Color! [Secondary Colour]");
            }

        }));

        // Customize Button
        this.addButton(new GuiButtonExt(cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.close").getColoredString(), button -> Minecraft.getInstance().setScreen(null)));

        // Default Button
        this.addButton(new GuiButtonExt(cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.default").getColoredString(), button -> {
            RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> {
                RegenType regenType = data.getRegenType().create();
                slidePrimaryRed.setValue(regenType.getDefaultPrimaryColor().x);
                slidePrimaryGreen.setValue(regenType.getDefaultPrimaryColor().y);
                slidePrimaryBlue.setValue(regenType.getDefaultPrimaryColor().z);

                slideSecondaryRed.setValue(regenType.getDefaultSecondaryColor().x);
                slideSecondaryGreen.setValue(regenType.getDefaultSecondaryColor().y);
                slideSecondaryBlue.setValue(regenType.getDefaultSecondaryColor().z);
            });

            onChangeSliderValue(null);
        }));

        slidePrimaryRed = new ColorSliderWidget(cx + 10, cy + 65, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.red").getColoredString(), "", 0, 1, primaryRed, true, true, button -> {

        }, this);
        slidePrimaryGreen = new ColorSliderWidget(cx + 10, cy + 84, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.green").getColoredString(), "", 0, 1, primaryGreen, true, true, p_onPress_1_ -> {

        }, this);
        slidePrimaryBlue = new ColorSliderWidget(cx + 10, cy + 103, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.blue").getColoredString(), "", 0, 1, primaryBlue, true, true, p_onPress_1_ -> {

        }, this);
        slideSecondaryRed = new ColorSliderWidget(cx + 96, cy + 65, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.red").getColoredString(), "", 0, 1, secondaryRed, true, true, p_onPress_1_ -> {

        }, this);
        slideSecondaryGreen = new ColorSliderWidget(cx + 96, cy + 84, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.green").getColoredString(), "", 0, 1, secondaryGreen, true, true, p_onPress_1_ -> {

        }, this);
        slideSecondaryBlue = new ColorSliderWidget(cx + 96, cy + 103, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.blue").getColoredString(), "", 0, 1, secondaryBlue, true, true, p_onPress_1_ -> {

        }, this);

        addButton(slidePrimaryRed);
        addButton(slidePrimaryGreen);
        addButton(slidePrimaryBlue);

        addButton(slideSecondaryRed);
        addButton(slideSecondaryGreen);
        addButton(slideSecondaryBlue);

        this.setInitialFocus(this.inputPrimaryColor);
        this.setInitialFocus(this.inputSecondColor);

        inputPrimaryColor.setValue(colorToHex(new Color((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue())));
        inputSecondColor.setValue(colorToHex(new Color((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue())));

    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return this.inputPrimaryColor.charTyped(p_charTyped_1_, p_charTyped_2_) || this.inputSecondColor.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) || this.inputPrimaryColor.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) || this.inputSecondColor.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.inputPrimaryColor.render(p_render_1_, p_render_2_, p_render_3_);
        this.inputSecondColor.render(p_render_1_, p_render_2_, p_render_3_);
    }


    @Override
    protected void renderBg(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        Minecraft.getInstance().getTextureManager().bind(background);
        blit(leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        RenderUtil.drawRect(cx + 10, cy + 44, cx + 81, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderUtil.drawRect(cx + 11, cy + 45, cx + 80, cy + 60, (float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue(), 1);

        RenderUtil.drawRect(cx + 95, cy + 44, cx + 166, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderUtil.drawRect(cx + 96, cy + 45, cx + 165, cy + 60, (float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue(), 1);

        Vec3d primaryColor = new Vec3d((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue()), secondaryColor = new Vec3d((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue());

        RegenCap.get(minecraft.player).ifPresent((cap) -> {
            String str = new TranslationTextComponent("regeneration.gui.primary").getColoredString();
            int length = minecraft.font.width(str);
            font.draw(str, cx + 45 - length / 2, cy + 49, RenderUtil.calculateColorBrightness(primaryColor) > 0.179 ? 0x0 : 0xFFFFFF);

            str = new TranslationTextComponent("regeneration.gui.secondary").getColoredString();
            length = minecraft.font.width(str);
            font.draw(str, cx + 131 - length / 2, cy + 49, RenderUtil.calculateColorBrightness(secondaryColor) > 0.179 ? 0x0 : 0xFFFFFF);
        });
    }

    @Override
    public void onChangeSliderValue(GuiSlider slider) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("PrimaryRed", (float) slidePrimaryRed.getValue());
        nbt.putFloat("PrimaryGreen", (float) slidePrimaryGreen.getValue());
        nbt.putFloat("PrimaryBlue", (float) slidePrimaryBlue.getValue());

        nbt.putFloat("SecondaryRed", (float) slideSecondaryRed.getValue());
        nbt.putFloat("SecondaryGreen", (float) slideSecondaryGreen.getValue());
        nbt.putFloat("SecondaryBlue", (float) slideSecondaryBlue.getValue());
        NetworkDispatcher.sendToServer(new UpdateColorMessage(nbt));
        inputPrimaryColor.setValue(colorToHex(new Color((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue())));
        inputSecondColor.setValue(colorToHex(new Color((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue())));
    }

    @Override
    public void tick() {
        this.inputPrimaryColor.tick();
        this.inputSecondColor.tick();
    }
}
