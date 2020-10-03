package me.swirtzly.regen.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.ColorChangeMessage;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.awt.*;

import static me.swirtzly.regen.util.RegenUtil.colorToHex;

//TODO Finish
public class ColorScreen extends ContainerScreen implements Slider.ISlider {

    private static final ResourceLocation background = new ResourceLocation(RConstants.MODID, "textures/gui/customizer_background.png");

    private static Slider slidePrimaryRed, slidePrimaryGreen, slidePrimaryBlue, slideSecondaryRed, slideSecondaryGreen, slideSecondaryBlue;

    private TextFieldWidget inputPrimaryColor, inputSecondColor;

    private Vector3d initialPrimary, initialSecondary;

    public ColorScreen() {
        super(new BlankContainer(), Minecraft.getInstance().player.inventory, new TranslationTextComponent("regeneration.gui.color_gui"));
        xSize = 176;
        ySize = 186;
    }

    @Override
    public void init() {
        super.init();
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        RegenCap.get(getMinecraft().player).ifPresent((data) -> {
            initialPrimary = data.getPrimaryColors();
            initialSecondary = data.getSecondaryColors();
        });

        float primaryRed = (float) initialPrimary.x, primaryGreen = (float) initialPrimary.y, primaryBlue = (float) initialPrimary.z;
        float secondaryRed = (float) initialSecondary.x, secondaryGreen = (float) initialSecondary.y, secondaryBlue = (float) initialSecondary.z;

        final int btnW = 60, btnH = 18;
        final int sliderW = 70, sliderH = 20;


        // Reset Style Button
        this.addButton(new Button(cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.undo"), button -> {
            slidePrimaryRed.setValue(initialPrimary.x);
            slidePrimaryGreen.setValue(initialPrimary.y);
            slidePrimaryBlue.setValue(initialPrimary.z);

            slideSecondaryRed.setValue(initialSecondary.x);
            slideSecondaryGreen.setValue(initialSecondary.y);
            slideSecondaryBlue.setValue(initialSecondary.z);
        }));

        this.getMinecraft().keyboardListener.enableRepeatEvents(true);
        this.inputPrimaryColor = new TextFieldWidget(this.font, cx + 25, cy + 21, btnW, btnH, this.inputPrimaryColor, new TranslationTextComponent("Input"));
        this.inputSecondColor = new TextFieldWidget(this.font, cx + 90, cy + 21, btnW, btnH, this.inputPrimaryColor, new TranslationTextComponent("Input"));

        addButton(this.inputPrimaryColor);
        addButton(this.inputSecondColor);

        // Color input Primary button
        this.addButton(new Button(cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.input_color"), button -> {
            String primaryColorText = inputPrimaryColor.getText();
            String secondColourText = inputSecondColor.getText();

            if (!primaryColorText.startsWith("#") && !primaryColorText.isEmpty()) {
                primaryColorText = "#" + primaryColorText;
                inputPrimaryColor.setText(primaryColorText);
            }

            if (!secondColourText.startsWith("#") && !secondColourText.isEmpty()) {
                secondColourText = "#" + secondColourText;
                inputSecondColor.setText(secondColourText);
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
        this.addButton(new Button(cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.close"), button -> Minecraft.getInstance().displayGuiScreen(null)));

        // Default Button
        this.addButton(new Button(cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.default"), button -> {
            RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> {
                TransitionType regenType = data.getTransitionType().create();
                slidePrimaryRed.setValue(regenType.getDefaultPrimaryColor().x);
                slidePrimaryGreen.setValue(regenType.getDefaultPrimaryColor().y);
                slidePrimaryBlue.setValue(regenType.getDefaultPrimaryColor().z);

                slideSecondaryRed.setValue(regenType.getDefaultSecondaryColor().x);
                slideSecondaryGreen.setValue(regenType.getDefaultSecondaryColor().y);
                slideSecondaryBlue.setValue(regenType.getDefaultSecondaryColor().z);
            });

            onChangeSliderValue(null);
        }));

        slidePrimaryRed = new Slider(cx + 10, cy + 65, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.red"), StringTextComponent.EMPTY, 0, 1, primaryRed, true, true, button -> {

        }, this);
        slidePrimaryGreen = new Slider(cx + 10, cy + 84, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.green"), StringTextComponent.EMPTY, 0, 1, primaryGreen, true, true, p_onPress_1_ -> {

        }, this);
        slidePrimaryBlue = new Slider(cx + 10, cy + 103, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.blue"), StringTextComponent.EMPTY, 0, 1, primaryBlue, true, true, p_onPress_1_ -> {

        }, this);
        slideSecondaryRed = new Slider(cx + 96, cy + 65, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.red"), StringTextComponent.EMPTY, 0, 1, secondaryRed, true, true, p_onPress_1_ -> {

        }, this);
        slideSecondaryGreen = new Slider(cx + 96, cy + 84, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.green"), StringTextComponent.EMPTY, 0, 1, secondaryGreen, true, true, p_onPress_1_ -> {

        }, this);
        slideSecondaryBlue = new Slider(cx + 96, cy + 103, sliderW, sliderH, new TranslationTextComponent("regeneration.gui.blue"), StringTextComponent.EMPTY, 0, 1, secondaryBlue, true, true, p_onPress_1_ -> {

        }, this);

        addButton(slidePrimaryRed);
        addButton(slidePrimaryGreen);
        addButton(slidePrimaryBlue);

        addButton(slideSecondaryRed);
        addButton(slideSecondaryGreen);
        addButton(slideSecondaryBlue);

        this.addButton(this.inputPrimaryColor);
        this.addButton(this.inputSecondColor);

        inputPrimaryColor.setText(colorToHex(new Color((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue())));
        inputSecondColor.setText(colorToHex(new Color((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue())));

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.func_243248_b(p_230451_1_, this.title, (float) this.titleX, (float) this.titleY, 4210752);
    }

    @Override
    public void onChangeSliderValue(Slider slider) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat(RConstants.PRIMARY_RED, (float) slidePrimaryRed.getValue());
        nbt.putFloat(RConstants.PRIMARY_GREEN, (float) slidePrimaryGreen.getValue());
        nbt.putFloat(RConstants.PRIMARY_BLUE, (float) slidePrimaryBlue.getValue());

        nbt.putFloat(RConstants.SECONDARY_RED, (float) slideSecondaryRed.getValue());
        nbt.putFloat(RConstants.SECONDARY_GREEN, (float) slideSecondaryGreen.getValue());
        nbt.putFloat(RConstants.SECONDARY_BLUE, (float) slideSecondaryBlue.getValue());
        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ColorChangeMessage(nbt));
        inputPrimaryColor.setText(colorToHex(new Color((float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue())));
        inputSecondColor.setText(colorToHex(new Color((float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue())));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(background);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.blit(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
        }

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        RenderHelp.drawRect(cx + 10, cy + 44, cx + 81, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderHelp.drawRect(cx + 11, cy + 45, cx + 80, cy + 60, (float) slidePrimaryRed.getValue(), (float) slidePrimaryGreen.getValue(), (float) slidePrimaryBlue.getValue(), 1);

        RenderHelp.drawRect(cx + 95, cy + 44, cx + 166, cy + 61, 0.1F, 0.1F, 0.1F, 1);
        RenderHelp.drawRect(cx + 96, cy + 45, cx + 165, cy + 60, (float) slideSecondaryRed.getValue(), (float) slideSecondaryGreen.getValue(), (float) slideSecondaryBlue.getValue(), 1);

        //TODO For some reason, this text doesnt render on the screen
        RegenCap.get(getMinecraft().player).ifPresent((cap) -> {
            String str = new TranslationTextComponent("regeneration.gui.primary").getUnformattedComponentText();
            int length = getMinecraft().fontRenderer.getStringWidth(str);
            this.font.func_243248_b(p_230450_1_, new StringTextComponent(str), (float) cx + 45 - length / 2, cy + 49, 4210752);
            str = new TranslationTextComponent("regeneration.gui.secondary").getUnformattedComponentText();
            length = font.getStringWidth(str);
            this.font.func_243248_b(p_230450_1_, new StringTextComponent(str), cx + 131 - length / 2, cy + 49, 4210752);
        });

    }
}
