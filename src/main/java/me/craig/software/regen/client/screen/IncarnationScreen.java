package me.craig.software.regen.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.craig.software.regen.client.skin.CommonSkin;
import me.craig.software.regen.client.skin.SkinHandler;
import me.craig.software.regen.network.NetworkDispatcher;
import me.craig.software.regen.network.messages.NextSkinMessage;
import me.craig.software.regen.util.ClientUtil;
import me.craig.software.regen.util.PlayerUtil;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.util.RegenUtil;
import me.craig.software.regen.common.regen.RegenCap;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class IncarnationScreen extends ContainerScreen {

    private static final ResourceLocation screenBackground = new ResourceLocation(RConstants.MODID, "textures/gui/customizer.png");
    public static boolean isAlex = true;
    public static ResourceLocation currentTexture = DefaultPlayerSkin.getDefaultSkin();
    private static PlayerUtil.SkinType currentSkinType = RegenCap.get(Objects.requireNonNull(Minecraft.getInstance().player)).orElse(null).preferredModel();
    private static PlayerUtil.SkinType renderChoice = currentSkinType;
    private static List<File> skins = null;
    private static int position = 0;
    private final ArrayList<DescButton> descButtons = new ArrayList<>();
    private RCheckbox excludeTrending;
    private TextFieldWidget searchField;

    public boolean isAfterRendering = false;

    public IncarnationScreen() {
        super(new BlankContainer(), Minecraft.getInstance().player.inventory, new StringTextComponent("Next Incarnation"));
        imageWidth = 256;
        imageHeight = 173;
    }

    public static void updateModels() {
        if (!skins.isEmpty()) {
            currentTexture = CommonSkin.fileTotexture(skins.get(position));
            System.out.println(skins.get(position).toPath() + " || " + CommonSkin.SKIN_DIRECTORY_ALEX);
            isAlex = skins.get(position).toString().contains("\\skins\\alex");
            renderChoice = isAlex ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void init() {
        super.init();

        TabRegistry.updateTabValues(leftPos + 2, topPos, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.getTabList()) {
            addWidget(button);
        }
        int buttonOffset = 35;
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 55, btnH = 18;
        position = 0;
        skins = CommonSkin.listAllSkins(PlayerUtil.SkinType.EITHER);
        if (skins.isEmpty()) {
            Minecraft.getInstance().setScreen(new RErrorScreen(new TranslationTextComponent("No Skins for " + new TranslationTextComponent("regeneration.skin_type." + currentSkinType.name().toLowerCase()).getString()), new TranslationTextComponent("Please place skins in the local Directory")));
        }

        this.searchField = new TextFieldWidget(this.font, cx + 15, cy + 145, imageWidth - 70, 20, this.searchField, new TranslationTextComponent("skins.search"));

        this.searchField.setResponder((p_214329_1_) -> {
            position = 0;
            skins.removeIf(file -> !file.getName().toLowerCase().contains(searchField.getValue().toLowerCase()));
            if (skins.isEmpty() || searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }
            stripTrending();
            Collections.sort(skins);
            updateModels();
        });
        this.children.add(this.searchField);
        this.setInitialFocus(this.searchField);

        DescButton btnPrevious = new DescButton(cx + 140, cy + 60, 20, 20, new TranslationTextComponent("regen.gui.previous"), button -> {
            if (searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }
            stripTrending();

            if (position >= skins.size() - 1) {
                position = 0;
            } else {
                position++;
            }
            updateModels();

        }).setDescription(new String[]{"button.tooltip.previous_skin"});

        DescButton btnNext = new DescButton(cx + 215, cy + 60, 20, 20, new TranslationTextComponent("regen.gui.next"), button -> {
            // Previous
            if (searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }

            stripTrending();

            if (position > 0) {
                position--;
            } else {
                position = skins.size() - 1;
            }
            currentTexture = CommonSkin.fileTotexture(skins.get(position));
            updateModels();

        }).setDescription(new String[]{"button.tooltip.next_skin"});

        DescButton btnBack = new DescButton(cx + 10, cy + 115 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.back"), button -> Minecraft.getInstance().setScreen(new PreferencesScreen()));

        DescButton btnOpenFolder = new DescButton(cx + 90 - 20, cy + 115 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.open_folder"), button -> Util.getPlatform().openFile(CommonSkin.SKIN_DIRECTORY)).setDescription(new String[]{"button.tooltip.open_folder"});

        DescButton btnSave = new DescButton(cx + 90 - 20, cy + 90 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.save"), button -> {
            updateModels();
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new NextSkinMessage(RegenUtil.fileToBytes(skins.get(position)), isAlex));
        }).setDescription(new String[]{"button.tooltip.save_skin"});

        DescButton btnResetSkin = new DescButton(cx + 10, cy + 90 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.reset_skin"), button -> SkinHandler.sendResetMessage()).setDescription(new String[]{"button.tooltip.reset_mojang"});

        this.excludeTrending = new RCheckbox(cx + 10, cy + 25, 150, 20, new TranslationTextComponent("Trending?"), true, checkboxButton -> {
            if (checkboxButton instanceof CheckboxButton) {
                CheckboxButton check = (CheckboxButton) checkboxButton;
                position = 0;
                searchField.setValue("");
                if (!check.selected()) {
                    skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("mineskin"));
                } else {
                    skins = CommonSkin.listAllSkins(currentSkinType);
                }
                updateModels();
            }
        });
        this.addButton(this.excludeTrending);

        addButton(btnNext);
        addButton(btnPrevious);
        addButton(btnOpenFolder);
        addButton(btnBack);
        addButton(btnSave);
        addButton(btnResetSkin);

        for (IGuiEventListener widget : buttons) {
            if (widget instanceof DescButton) {
                descButtons.add((DescButton) widget);
            }
        }

        RegenCap.get(minecraft.player).ifPresent((data) -> currentSkinType = data.preferredModel());

        RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> currentSkinType = data.preferredModel());
        updateModels();

        excludeTrending.active = true;


    }

    private void stripTrending() {
        if (!excludeTrending.selected()) {
            skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("mineskin"));
        }
    }

    @Override
    protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        Minecraft.getInstance().getTextureManager().bind(screenBackground);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        isAfterRendering = false;
        renderSkinToGui(matrixStack, x, y);
        isAfterRendering = true;

        drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent("regen.gui.current_skin").getString(), width / 2 + 60, height / 2 + 30, Color.WHITE.getRGB());
        if (!skins.isEmpty() && position < skins.size()) {
            matrixStack.pushPose();
            String name = skins.get(position).getName().replaceAll(".png", "");
            renderWidthScaledText(name, matrixStack, this.font, width / 2 + 60, height / 2 + 40, Color.WHITE.getRGB(), 100);
            matrixStack.popPose();
        }

        drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent("Search"), width / 2 - 95, height / 2 + 45, Color.WHITE.getRGB());
    }

    private void renderSkinToGui(MatrixStack matrixStack, int x, int y) {
        matrixStack.pushPose();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        boolean backupSkinType = ClientUtil.isAlex(player);
        SkinHandler.setPlayerSkinType(Minecraft.getInstance().player, renderChoice == PlayerUtil.SkinType.ALEX);
        InventoryScreen.renderEntityInInventory(width / 2 + 60, height / 2 + 20, 45, (float) (leftPos + 170) - x, (float) (topPos + 75 - 25) - y, Minecraft.getInstance().player);
        SkinHandler.setPlayerSkinType(Minecraft.getInstance().player, backupSkinType);
        matrixStack.popPose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.searchField.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (position >= skins.size() - 1) {
                position = 0;
            } else {
                position++;
                Minecraft.getInstance().getTextureManager().release(currentTexture);
                currentTexture = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (position > 0) {
                position--;
            } else {
                position = skins.size() - 1;
            }
            currentTexture = CommonSkin.fileTotexture(skins.get(position));
            updateModels();
        }

        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    protected void renderLabels(@NotNull MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.searchField.render(matrixStack, mouseX, mouseY, partialTicks);

        for (DescButton descButton : descButtons) {
            if (descButton.isHovered()) {
                if (descButton.getDescription() != null) {
                    renderToolTip(matrixStack, descButton.getDescription(), mouseX, mouseY, Minecraft.getInstance().font);
                }
            }
        }
    }

    //Spectre0987

    /**
     * @param text  - The text you'd like to draw
     * @param width - The max width of the text, scales to maintain this width if larger than it
     */
    public void renderWidthScaledText(String text, MatrixStack matrix, FontRenderer font, float x, float y, int color, int width) {
        matrix.pushPose();
        int textWidth = font.width(text);
        float scale = width / (float) textWidth;
        scale = MathHelper.clamp(scale, 0.0F, 1.0F);
        matrix.translate(x, y, 0);
        matrix.scale(scale, scale, scale);
        drawCenteredString(matrix, Minecraft.getInstance().font, text, 0, 0, Color.WHITE.getRGB());
        matrix.popPose();
    }

}