package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ColorChangeMessage;
import me.suff.mc.regen.util.RConstants;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColorScreen extends AbstractContainerScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/customizer.png");

    private Vec3 initialPrimary, initialSecondary;
    private ColorWidget colorChooserPrimary, colorChooserSecondary;

    public ColorScreen() {
        super(new BlankContainer(), Minecraft.getInstance().player.getInventory(), Component.translatable("regen.gui.color_gui"));
        imageWidth = 256;
        imageHeight = 173;
    }

    @Override
    public void init() {
        super.init();
        TabRegistry.updateTabValues(leftPos + 2, topPos, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.tabList) {
            addRenderableWidget(button);
        }
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        RegenCap.get(getMinecraft().player).ifPresent((data) -> {
            initialPrimary = data.getPrimaryColors();
            initialSecondary = data.getSecondaryColors();
        });


        final int btnW = 60, btnH = 18;


        // Reset Style Button
        this.addRenderableWidget(new Button(cx + 100, cy + 145, btnW, btnH + 2, Component.translatable("regen.gui.undo"), button -> {
            Color primaryColour = new Color((float) initialPrimary.x, (float) initialPrimary.y, (float) initialPrimary.z);
            Color secondaryColour = new Color((float) initialSecondary.x, (float) initialSecondary.y, (float) initialSecondary.z);
            colorChooserPrimary.setColor(primaryColour.getRGB());
            colorChooserSecondary.setColor(secondaryColour.getRGB());
            updateScreenAndServer();
        }));

        // Close Button
        this.addRenderableWidget(new Button(cx + 25, cy + 145, btnW, btnH + 2, Component.translatable("regen.gui.back"), button -> Minecraft.getInstance().setScreen(new PreferencesScreen())));

        // Default Button
        this.addRenderableWidget(new Button(cx + (90 * 2), cy + 145, btnW, btnH + 2, Component.translatable("regen.gui.default"), button -> RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> {
            TransitionType regenType = data.transitionType();
            Vec3 primColor = regenType.getDefaultPrimaryColor();
            Vec3 secColor = regenType.getDefaultSecondaryColor();
            Color primaryColour = new Color((float) primColor.x, (float) primColor.y, (float) primColor.z);
            Color secondaryColour = new Color((float) secColor.x, (float) secColor.y, (float) secColor.z);
            colorChooserPrimary.setColor(primaryColour.getRGB());
            colorChooserSecondary.setColor(secondaryColour.getRGB());
            updateScreenAndServer();
        })));

        colorChooserPrimary = new ColorWidget(font, cx + 20, cy + 35, 70, 20, Component.literal("Regen"), new Color((float) initialPrimary.x, (float) initialPrimary.y, (float) initialPrimary.z).getRGB(), p_onPress_1_ -> updateScreenAndServer());

        colorChooserSecondary = new ColorWidget(font, cx + 150, cy + 35, 70, 20, Component.literal("Regen"), new Color((float) initialSecondary.x, (float) initialSecondary.y, (float) initialSecondary.z).getRGB(), p_onPress_1_ -> updateScreenAndServer());

        addRenderableWidget(colorChooserPrimary);
        addRenderableWidget(colorChooserSecondary);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.draw(p_230451_1_, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    public void updateScreenAndServer() {
        CompoundTag nbt = new CompoundTag();
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
    protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int x, int y) {

        super.tick();
        colorChooserPrimary.tick();
        colorChooserSecondary.tick();

        this.renderBackground(matrixStack);

        if (this.minecraft != null) {
            RenderSystem.setShaderTexture(0, BACKGROUND);
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        }

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        RegenCap.get(getMinecraft().player).ifPresent((cap) -> {
            String str = Component.translatable("regen.gui.primary").getString();
            int length = getMinecraft().font.width(str);
            this.font.draw(matrixStack, Component.literal(str).getString(), (float) cx + 55 - length / 2, cy + 19, 4210752);
            str = Component.translatable("regen.gui.secondary").getString();
            length = font.width(str);
            this.font.draw(matrixStack, Component.literal(str).getString(), cx + 185 - length / 2, cy + 19, 4210752);
        });

        colorChooserPrimary.render(matrixStack, x, y, partialTicks);
        colorChooserSecondary.render(matrixStack, x, y, partialTicks);
    }
}
