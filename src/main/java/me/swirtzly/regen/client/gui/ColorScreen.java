package me.swirtzly.regen.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.ColorChangeMessage;
import me.swirtzly.regen.util.RConstants;
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
        xSize = 256;
        ySize = 173;
    }

    @Override
    public void init() {
        super.init();
        TabRegistry.updateTabValues(guiLeft + 2, guiTop, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.tabList) {
            addButton(button);
        }
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

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
        this.addButton(new Button(cx + 25, cy + 148, btnW, btnH, new TranslationTextComponent("regen.gui.back"), button -> Minecraft.getInstance().displayGuiScreen(new PreferencesScreen())));

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
    protected void drawGuiContainerForegroundLayer(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.func_243248_b(p_230451_1_, this.title, (float) this.titleX, (float) this.titleY, 4210752);
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
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);

        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(BACKGROUND);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        }

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        RegenCap.get(getMinecraft().player).ifPresent((cap) -> {
            String str = new TranslationTextComponent("regen.gui.primary").getString();
            int length = getMinecraft().fontRenderer.getStringWidth(str);
            this.font.func_243248_b(matrixStack, new StringTextComponent(str), (float) cx + 55 - length / 2, cy + 19, 4210752);
            str = new TranslationTextComponent("regen.gui.secondary").getString();
            length = font.getStringWidth(str);
            this.font.func_243248_b(matrixStack, new StringTextComponent(str), cx + 185 - length / 2, cy + 19, 4210752);
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
