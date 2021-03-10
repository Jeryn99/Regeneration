package me.suff.mc.regen.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ColorChangeMessage;
import me.suff.mc.regen.util.RConstants;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class ColorScreen extends ContainerScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/customizer_background.png");

    private Vector3d initialPrimary, initialSecondary;
    private ColorWidget colorChooserPrimary, colorChooserSecondary;

    public ColorScreen() {
        super(new BlankContainer(), Minecraft.getInstance().player.inventory, new TranslationTextComponent("regen.gui.color_gui"));
        imageWidth = 256;
        imageHeight = 173;
    }

    @Override
    public void init() {
        super.init();
        TabRegistry.updateTabValues(leftPos + 2, topPos, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.tabList) {
            addButton(button);
        }
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        RegenCap.get(getMinecraft().player).ifPresent((data) -> {
            initialPrimary = data.getPrimaryColors();
            initialSecondary = data.getSecondaryColors();
        });


        final int btnW = 60, btnH = 18;


        // Reset Style Button
        this.addButton(new Button(cx + 100, cy + 148, btnW, btnH, new TranslationTextComponent("regen.gui.undo"), button -> {
            Color primaryColour = new Color((float) initialPrimary.x, (float) initialPrimary.y, (float) initialPrimary.z);
            Color secondaryColour = new Color((float) initialSecondary.x, (float) initialSecondary.y, (float) initialSecondary.z);
            colorChooserPrimary.setColor(primaryColour.getRGB());
            colorChooserSecondary.setColor(secondaryColour.getRGB());
            updateScreenAndServer();
        }));

        // Close Button
        this.addButton(new Button(cx + 25, cy + 148, btnW, btnH, new TranslationTextComponent("regen.gui.back"), button -> Minecraft.getInstance().setScreen(new PreferencesScreen())));

        // Default Button
        this.addButton(new Button(cx + (90 * 2), cy + 148, btnW, btnH, new TranslationTextComponent("regen.gui.default"), button -> {
            RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> {
                TransitionType regenType = data.getTransitionType().get();
                Vector3d primColor = regenType.getDefaultPrimaryColor();
                Vector3d secColor = regenType.getDefaultSecondaryColor();
                Color primaryColour = new Color((float) primColor.x, (float) primColor.y, (float) primColor.z);
                Color secondaryColour = new Color((float) secColor.x, (float) secColor.y, (float) secColor.z);
                colorChooserPrimary.setColor(primaryColour.getRGB());
                colorChooserSecondary.setColor(secondaryColour.getRGB());
                updateScreenAndServer();
            });

        }));

        colorChooserPrimary = new ColorWidget(font, cx + 20, cy + 35, 70, 20, new StringTextComponent("Regen"), new Color((float) initialPrimary.x, (float) initialPrimary.y, (float) initialPrimary.z).getRGB(), p_onPress_1_ -> updateScreenAndServer());

        colorChooserSecondary = new ColorWidget(font, cx + 150, cy + 35, 70, 20, new StringTextComponent("Regen"), new Color((float) initialSecondary.x, (float) initialSecondary.y, (float) initialSecondary.z).getRGB(), p_onPress_1_ -> updateScreenAndServer());

        children.add(colorChooserPrimary);
        children.add(colorChooserSecondary);
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.drawShadow(p_230451_1_, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    public void updateScreenAndServer() {
        CompoundNBT nbt = new CompoundNBT();
        Color primary = new Color(colorChooserPrimary.getColor());
        Color secondary = new Color(colorChooserSecondary.getColor());
        nbt.putFloat(RConstants.PRIMARY_RED, (float) primary.getRed() / 255F);
        nbt.putFloat(RConstants.PRIMARY_GREEN, (float) primary.getGreen() / 255F);
        nbt.putFloat(RConstants.PRIMARY_BLUE, (float) primary.getBlue() / 255F);

        nbt.putFloat(RConstants.SECONDARY_RED, (float) secondary.getRed() / 255F);
        nbt.putFloat(RConstants.SECONDARY_GREEN, (float) secondary.getGreen() / 255F);
        nbt.putFloat(RConstants.SECONDARY_BLUE, (float) secondary.getBlue() / 255F);
        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ColorChangeMessage(nbt));
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);

        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bind(BACKGROUND);
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        }

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        RegenCap.get(getMinecraft().player).ifPresent((cap) -> {
            String str = new TranslationTextComponent("regen.gui.primary").getString();
            int length = getMinecraft().font.width(str);
            this.font.drawShadow(matrixStack, new StringTextComponent(str).getString(), (float) cx + 55 - length / 2, cy + 19, 4210752);
            str = new TranslationTextComponent("regen.gui.secondary").getString();
            length = font.width(str);
            this.font.drawShadow(matrixStack, new StringTextComponent(str).getString(), cx + 185 - length / 2, cy + 19, 4210752);
        });

        colorChooserPrimary.render(matrixStack, x, y, partialTicks);
        colorChooserSecondary.render(matrixStack, x, y, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        colorChooserPrimary.tick();
        colorChooserSecondary.tick();
    }
}
