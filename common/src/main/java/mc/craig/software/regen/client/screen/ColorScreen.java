package mc.craig.software.regen.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mc.craig.software.regen.client.screen.widgets.ColorWidget;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.network.messages.ColorChangeMessage;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class ColorScreen extends Screen {
    public static final ResourceLocation PREFERENCES_BUTTON_LOCATION = new ResourceLocation(RConstants.MODID, "textures/gui/preferences_button.png");
    public static final ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/customizer.png");

    private static final int IMAGE_WIDTH = 256;
    private static final int IMAGE_HEIGHT = 173;

    private Vec3 initialPrimary, initialSecondary;
    private ColorWidget colorChooserPrimary, colorChooserSecondary;
    private int centerX, centerY;

    public ColorScreen() {
        super(Component.translatable("gui.regen.color_gui"));
    }

    @Override
    public void init() {
        centerX = (width - IMAGE_WIDTH) / 2;
        centerY = (height - IMAGE_HEIGHT) / 2;

        RegenerationData.get(Minecraft.getInstance().player).ifPresent(data -> {
            initialPrimary = data.getPrimaryColors();
            initialSecondary = data.getSecondaryColors();
        });

        addRenderableWidget(new ImageButton(4, 4, 20, 18, 0, 0, 19, PREFERENCES_BUTTON_LOCATION,
                button -> Minecraft.getInstance().setScreen(null)));

        addButton("gui.regen.undo", centerX + 100, centerY + 145, this::resetColors);
        addButton("gui.regen.back", centerX + 25, centerY + 145, () -> Minecraft.getInstance().setScreen(new PreferencesScreen()));
        addButton("gui.regen.default", centerX + 180, centerY + 145, this::setDefaultColors);

        colorChooserPrimary = new ColorWidget(font, centerX + 20, centerY + 35, 70, 20, Component.literal("Regen"),
                getColorRGB(initialPrimary), widget -> updateScreenAndServer());
        colorChooserSecondary = new ColorWidget(font, centerX + 150, centerY + 35, 70, 20, Component.literal("Regen"),
                getColorRGB(initialSecondary), widget -> updateScreenAndServer());

        addRenderableWidget(colorChooserPrimary);
        addRenderableWidget(colorChooserSecondary);
    }

    private void addButton(String translationKey, int x, int y, Runnable onClick) {
        addRenderableWidget(Button.builder(Component.translatable(translationKey), button -> onClick.run())
                .bounds(x, y, 60, 20).build());
    }

    private void resetColors() {
        colorChooserPrimary.setColor(getColorRGB(initialPrimary));
        colorChooserSecondary.setColor(getColorRGB(initialSecondary));
        updateScreenAndServer();
    }

    private void setDefaultColors() {
        RegenerationData.get(Minecraft.getInstance().player).ifPresent(data -> {
            TransitionType regenType = data.transitionType();
            colorChooserPrimary.setColor(getColorRGB(regenType.getDefaultPrimaryColor()));
            colorChooserSecondary.setColor(getColorRGB(regenType.getDefaultSecondaryColor()));
            updateScreenAndServer();
        });
    }

    private void updateScreenAndServer() {
        CompoundTag nbt = new CompoundTag();
        setColorData(nbt, RConstants.PRIMARY_RED, RConstants.PRIMARY_GREEN, RConstants.PRIMARY_BLUE, colorChooserPrimary);
        setColorData(nbt, RConstants.SECONDARY_RED, RConstants.SECONDARY_GREEN, RConstants.SECONDARY_BLUE, colorChooserSecondary);
        new ColorChangeMessage(nbt).send();
    }

    private void setColorData(CompoundTag nbt, String redKey, String greenKey, String blueKey, ColorWidget widget) {
        Color color = new Color(widget.getColor());
        nbt.putFloat(redKey, color.getRed() / 255F);
        nbt.putFloat(greenKey, color.getGreen() / 255F);
        nbt.putFloat(blueKey, color.getBlue() / 255F);
    }

    private int getColorRGB(Vec3 vec) {
        return new Color((float) vec.x, (float) vec.y, (float) vec.z).getRGB();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        guiGraphics.blit(BACKGROUND, centerX, centerY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        colorChooserPrimary.render(guiGraphics, mouseX, mouseY, partialTick);
        colorChooserSecondary.render(guiGraphics, mouseX, mouseY, partialTick);

        drawColorLabels(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void drawColorLabels(GuiGraphics guiGraphics) {
        RegenerationData.get(Minecraft.getInstance().player).ifPresent(data -> {
            drawCenteredText(guiGraphics, "gui.regen.primary", centerX + 55, centerY + 19);
            drawCenteredText(guiGraphics, "gui.regen.secondary", centerX + 185, centerY + 19);
        });
    }

    private void drawCenteredText(GuiGraphics guiGraphics, String translationKey, int x, int y) {
        String text = Component.translatable(translationKey).getString();
        int textWidth = Minecraft.getInstance().font.width(text);
        guiGraphics.drawString(font, text, x - textWidth / 2, y, Color.white.getRGB());
    }

    @Override
    public void tick() {
        colorChooserPrimary.tick();
        colorChooserSecondary.tick();
        super.tick();
    }
}
