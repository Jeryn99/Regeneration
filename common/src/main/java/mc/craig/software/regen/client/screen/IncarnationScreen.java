package mc.craig.software.regen.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mc.craig.software.regen.client.screen.widgets.RCheckbox;
import mc.craig.software.regen.client.skin.SkinRetriever;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.messages.NextSkinMessage;
import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class IncarnationScreen extends Screen {

    private static final ResourceLocation screenBackground = new ResourceLocation(RConstants.MODID, "textures/gui/customizer.png");
    public static boolean isAlex = true;
    public static ResourceLocation currentTexture = DefaultPlayerSkin.getDefaultSkin();
    private static PlayerUtil.SkinType currentSkinType = RegenerationData.get(Objects.requireNonNull(Minecraft.getInstance().player)).orElse(null).preferredModel();
    private static PlayerUtil.SkinType renderChoice = currentSkinType;
    private static List<File> skins = null;
    private static int position = 0;
    public boolean postRenderedPlayer;
    private RCheckbox excludeTrending;
    private EditBox searchField;

    protected int imageWidth, imageHeight = 0;
    private int leftPos, topPos;

    public IncarnationScreen() {
        super(Component.literal("Next Incarnation"));
        imageWidth = 256;
        imageHeight = 173;
    }

    public static void updateModels() {
        Minecraft.getInstance().getTextureManager().release(currentTexture);
        if (!skins.isEmpty()) {
            isAlex = skins.get(position).getAbsolutePath().contains("slim");
            renderChoice = isAlex ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE;
            currentTexture = SkinRetriever.fileToTexture(skins.get(position));
        } else {
            checkForMissingSkins();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    /**
     * @param text  - The text you'd like to draw
     * @param width - The max width of the text, scales to maintain this width if larger than it
     */
    public static void renderWidthScaledText(String text, GuiGraphics guiGraphics, Font font, float x, float y, int color, int width) {
        guiGraphics.pose().pushPose();
        int textWidth = font.width(text);
        float scale = width / (float) textWidth;
        scale = Mth.clamp(scale, 0.0F, 1.0F);
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, text, 0, 0, color);
        guiGraphics.pose().popPose();
    }

    private void stripTrending() {
        if (!excludeTrending.selected()) {
            skins.removeIf(file -> file.getPath().contains("web"));
        }
    }

    private void renderSkinToGui(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.pose().pushPose();
        LocalPlayer player = Minecraft.getInstance().player;
        boolean backupSkinType = ClientUtil.isAlex(player);
        postRenderedPlayer = true;
        VisualManipulator.setPlayerSkinType(Minecraft.getInstance().player, renderChoice == PlayerUtil.SkinType.ALEX);
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, width / 2 + 60, height / 2 + 20, 45, (float) (leftPos + 170) - mouseX, (float) (topPos + 75 - 25) - mouseY, Minecraft.getInstance().player);
        postRenderedPlayer = false;
        VisualManipulator.setPlayerSkinType(Minecraft.getInstance().player, backupSkinType);
        guiGraphics.pose().popPose();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.searchField.getValue();
        super.resize(minecraft, width, height);
        this.searchField.setValue(s);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        searchField.keyPressed(keyCode, scanCode, modifiers);

        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (position >= skins.size() - 1) {
                position = 0;
            } else {
                position++;
            }
            updateModels();
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (position > 0) {
                position--;
            } else {
                position = skins.size() - 1;
            }
            updateModels();
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        RenderSystem.setShaderTexture(0, screenBackground);
        guiGraphics.blit(screenBackground, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderSkinToGui(guiGraphics, mouseX, mouseY);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("gui.regen.current_skin").getString(), width / 2 + 60, height / 2 + 30, Color.WHITE.getRGB());
        if (!skins.isEmpty() && position < skins.size()) {
            guiGraphics.pose().pushPose();
            String name = skins.get(position).getName().replaceAll(".png", "");
            renderWidthScaledText(name, guiGraphics, this.font, width / 2 + 60, height / 2 + 40, Color.WHITE.getRGB(), 100);
            guiGraphics.pose().popPose();
        }
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("Search"), (width - imageWidth) / 2 + 28, (height - imageHeight) / 2 + 20 + 15, Color.WHITE.getRGB());

        this.searchField.render(guiGraphics, mouseX, mouseY, partialTicks);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);


    }

    @Override
    public void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int buttonOffset = 15;
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 55, btnH = 18;
        position = 0;
        skins = SkinRetriever.listAllSkins(PlayerUtil.SkinType.EITHER);
        checkForMissingSkins();

        this.addRenderableWidget(new ImageButton(4, 4, 20, 18, 0, 0, 19, ColorScreen.PREFERENCES_BUTTON_LOCATION, (button) -> {
            Minecraft.getInstance().setScreen(null);
        }));

        this.searchField = new EditBox(this.font, cx + 10, cy + 30 + buttonOffset, cx - 15, 20, this.searchField, Component.translatable("skins.search"));

        this.searchField.setMaxLength(128);
        this.searchField.setFocused(true);

        this.searchField.setResponder((s) -> {
            position = 0;
            skins.removeIf(file -> !file.getName().toLowerCase().contains(s.toLowerCase()));
            if (skins.isEmpty() || searchField.getValue().isEmpty()) {
                skins = SkinRetriever.listAllSkins(currentSkinType);
            }
            stripTrending();
            updateModels();
        });
        this.setInitialFocus(this.searchField);
        this.addWidget(this.searchField);


        Button btnPrevious = Button.builder(Component.translatable("gui.regen.previous"), button -> {
            if (searchField.getValue().isEmpty()) {
                skins = SkinRetriever.listAllSkins(currentSkinType);
            }
            stripTrending();

            if (position >= skins.size() - 1) {
                position = 0;
            } else {
                position++;
            }
            updateModels();
        }).bounds(cx + 140, cy + 60, 20, 20).tooltip(Tooltip.create(Component.translatable("button_tooltip.regen.previous_skin"))).build();

        Button btnNext = Button.builder(Component.translatable("gui.regen.next"), button -> {
            // Previous
            if (searchField.getValue().isEmpty()) {
                skins = SkinRetriever.listAllSkins(currentSkinType);
            }

            stripTrending();

            if (position > 0) {
                position--;
            } else {
                position = skins.size() - 1;
            }
            updateModels();
        }).bounds(cx + 215, cy + 60, 20, 20).tooltip(Tooltip.create(Component.translatable("button_tooltip.regen.next_skin"))).build();

        Button btnBack = Button.builder(Component.translatable("gui.regen.back"), button -> Minecraft.getInstance().setScreen(new PreferencesScreen())).bounds(cx + 10, cy + 115 - buttonOffset, btnW, btnH + 2).build();

        Button btnOpenFolder = Button.builder(Component.translatable("gui.regen.open_folder"), button -> Util.getPlatform().openFile(SkinRetriever.SKINS_DIR)).bounds(cx + 90 - 20, cy + 115 - buttonOffset, btnW, btnH + 2).tooltip(Tooltip.create(Component.translatable("button_tooltip.regen.open_folder"))).build();

        Button btnSave = Button.builder(Component.translatable("gui.regen.save"), button -> {
            updateModels();
            new NextSkinMessage(RegenUtil.fileToBytes(skins.get(position)), isAlex).send();
        }).bounds(cx + 90 - 20, cy + 90 - buttonOffset, btnW, btnH + 2)
                .tooltip(Tooltip.create(Component.translatable("button_tooltip.regen.save_skin"))).build();

        Button btnResetSkin = Button.builder(Component.translatable("gui.regen.reset_skin"), button -> VisualManipulator.sendResetMessage()).bounds(cx + 10, cy + 90 - buttonOffset, btnW, btnH + 2).tooltip(Tooltip.create(Component.translatable("button_tooltip.regen.reset_mojang"))).build();

        this.excludeTrending = new RCheckbox(cx + 10, cy + 145, 150, 20, Component.translatable("Include Web Skins?"), true, checkboxButton -> {
            if (checkboxButton instanceof Checkbox check) {
                position = 0;
                searchField.setValue("");
                if (!check.selected()) {
                    skins.removeIf(file -> file.getAbsolutePath().contains("web"));
                } else {
                    skins = SkinRetriever.listAllSkins(currentSkinType);
                }
                updateModels();
            }
        });
        this.addRenderableWidget(this.excludeTrending);

        addRenderableWidget(btnNext);
        addRenderableWidget(btnPrevious);
        addRenderableWidget(btnOpenFolder);
        addRenderableWidget(btnBack);
        addRenderableWidget(btnSave);
        addRenderableWidget(btnResetSkin);

        RegenerationData.get(minecraft.player).ifPresent((data) -> currentSkinType = data.preferredModel());

        RegenerationData.get(Minecraft.getInstance().player).ifPresent((data) -> currentSkinType = data.preferredModel());
        updateModels();

        excludeTrending.active = true;


    }

    private static void checkForMissingSkins() {
        if (skins.isEmpty()) {
            Minecraft.getInstance().setScreen(new RErrorScreen(Component.translatable("No Skins for " + Component.translatable("regeneration.skin_type." + currentSkinType.name().toLowerCase()).getString()), Component.translatable("Please place skins in the local Directory")));
        }
    }

}