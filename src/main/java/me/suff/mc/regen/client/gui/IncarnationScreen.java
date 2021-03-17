package me.suff.mc.regen.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.client.skin.CommonSkin;
import me.suff.mc.regen.client.skin.SkinHandler;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.NextSkinMessage;
import me.suff.mc.regen.util.ClientUtil;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class IncarnationScreen extends ContainerScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/customizer_background.png");
    private static final PlayerModel< ? > ALEX_MODEL = new PlayerModel<>(0.1f, true);
    private static final PlayerModel< ? > STEVE_MODEL = new PlayerModel<>(0.1f, false);
    public static boolean isAlex = true;
    private static ResourceLocation PLAYER_TEXTURE = DefaultPlayerSkin.getDefaultSkin();
    private static List< File > skins = null;
    private static int position = 0;
    private static PlayerUtil.SkinType currentSkinType = RegenCap.get(Objects.requireNonNull(Minecraft.getInstance().player)).orElse(null).preferredModel();
    private static PlayerUtil.SkinType renderChoice = currentSkinType;
    private RCheckbox excludeTrending;


    private TextFieldWidget searchField;
    private ArrayList< DescButton > descButtons = new ArrayList<>();


    public IncarnationScreen() {
        super(new BlankContainer(), Objects.requireNonNull(Minecraft.getInstance().player).inventory, new TranslationTextComponent("Next Incarnation"));
        imageWidth = 256;
        imageHeight = 173;
    }

    public static void updateModels() {
        PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
        isAlex = skins.get(position).toPath().startsWith(CommonSkin.SKIN_DIRECTORY_ALEX.toPath().toString());
        renderChoice = isAlex ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE;
    }


    @Override
    public void init() {
        super.init();

        TabRegistry.updateTabValues(leftPos + 2, topPos, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.tabList) {
            addButton(button);
        }
        int buttonOffset = 35;
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 55, btnH = 18;
        position = 0;
        skins = CommonSkin.listAllSkins(currentSkinType);


        this.searchField = new TextFieldWidget(this.font, cx + 15, cy + 145, imageWidth - 70, 20, this.searchField, new TranslationTextComponent("selectWorld.search"));

        this.searchField.setResponder((p_214329_1_) -> {
            position = 0;
            skins.removeIf(file -> !file.getName().toLowerCase().contains(searchField.getValue().toLowerCase()));
            if (skins.isEmpty() || searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }

            if (!excludeTrending.selected()) {
                skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("namemc"));
            }

            Collections.sort(skins);
            updateModels();
        });
        this.children.add(this.searchField);
        this.setInitialFocus(this.searchField);


        DescButton btnNext = new DescButton(cx + 140, cy + 60, 20, 20, new TranslationTextComponent("regen.gui.previous"), button -> {
            if (searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }
            if (!excludeTrending.selected()) {
                skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("namemc"));
            }

            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position >= skins.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                updateModels();
            }
        });
        DescButton btnPrevious = new DescButton(cx + 215, cy + 60, 20, 20, new TranslationTextComponent("regen.gui.next"), button -> {
            // Previous
            if (searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }

            if (!excludeTrending.selected()) {
                skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("namemc"));
            }

            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position > 0) {
                    position--;
                } else {
                    position = skins.size() - 1;
                }
                PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        });
        DescButton btnBack = new DescButton(cx + 10, cy + 115 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.back"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Minecraft.getInstance().setScreen(new PreferencesScreen());
            }
        });
        DescButton btnOpenFolder = new DescButton(cx + 90 - 20, cy + 115 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.open_folder"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Util.getPlatform().openFile(CommonSkin.SKIN_DIRECTORY);
            }
        });
        DescButton btnSave = new DescButton(cx + 90 - 20, cy + 90 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.save"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                updateModels();
                NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new NextSkinMessage(RegenUtil.fileToBytes(skins.get(position)), isAlex));
            }
        });
        DescButton btnResetSkin = new DescButton(cx + 10, cy + 90 - buttonOffset, btnW, btnH + 2, new TranslationTextComponent("regen.gui.reset_skin"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                SkinHandler.sendResetMessage();
            }
        });

        //TODO Translate
        btnResetSkin.setDescription(new TranslationTextComponent[]{
                new TranslationTextComponent("Resets your Skin to Mojang Skin")
        });

        btnPrevious.setDescription(new TranslationTextComponent[]{
                new TranslationTextComponent("Next Skin")
        });

        btnNext.setDescription(new TranslationTextComponent[]{
                new TranslationTextComponent("Previous Skin")
        });

        btnSave.setDescription(new TranslationTextComponent[]{
                new TranslationTextComponent("Upload information")
        });

        btnOpenFolder.setDescription(new TranslationTextComponent[]{
                new TranslationTextComponent("Open Local Skins Folder")
        });

        btnBack.setDescription(new TranslationTextComponent[]{
                new TranslationTextComponent("Return to Preferences menu")
        });


        this.excludeTrending = new RCheckbox(cx + 10, cy + 25, 150, 20, new TranslationTextComponent("Trending?"), true, checkboxButton -> {
            if (checkboxButton instanceof CheckboxButton) {
                CheckboxButton check = (CheckboxButton) checkboxButton;
                position = 0;
                if (!check.selected()) {
                    skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("namemc"));
                } else {
                    skins = CommonSkin.listAllSkins(currentSkinType);
                }
                updateModels();
            }
        });
        this.addButton(this.excludeTrending);


      /*  List< Path > files;
        AtomicInteger offset = new AtomicInteger(btnH + 5);
        try {
           Files.list(new File(RegenConfig.COMMON.skinDir.get() + "/Regeneration Data/Skins/alex").toPath())
                    .limit(10)
                    .forEach(path -> {
                        System.out.println(path);
                        RCheckbox bob = new RCheckbox(cx + 10, cy + 25 + offset.get(), 150, 20, new TranslationTextComponent(path.toString()), true, checkboxButton -> {
                            position = 0;
                            if (!checkboxButton.isChecked()) {
                                skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains(path.toString()));
                            } else {
                                skins = CommonSkin.listAllSkins(currentSkinType);
                            }
                            updateModels();
                        });
                        offset.addAndGet(btnH + 5);
                        addButton(bob);
                    });
        } catch (IOException exception) {
            exception.printStackTrace();
        }*/


        addButton(btnNext);
        addButton(btnPrevious);
        addButton(btnOpenFolder);
        addButton(btnBack);
        addButton(btnSave);
        addButton(btnResetSkin);

        for (Widget widget : buttons) {
            if (widget instanceof DescButton) {
                descButtons.add((DescButton) widget);
            }
        }

        RegenCap.get(minecraft.player).ifPresent((data) -> currentSkinType = data.preferredModel());

        RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> currentSkinType = data.preferredModel());
        updateModels();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        ALEX_MODEL.young = false;
        STEVE_MODEL.young = false;
        renderSkinToGui(matrixStack, x, y);

        drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent("regen.gui.current_skin").getString(), width / 2 + 60, height / 2 + 30, Color.WHITE.getRGB());
        if (!skins.isEmpty() && position < skins.size()) {
            matrixStack.pushPose();
            String name = skins.get(position).getName().replaceAll(".png", "");
            drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent(name), width / 2 + 60, height / 2 + 40, Color.WHITE.getRGB());
            matrixStack.popPose();
        }

        drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent("Search"), width / 2 - 95, height / 2 + 45, Color.WHITE.getRGB());

    }

    private void renderSkinToGui(MatrixStack matrixStack, int x, int y) {
        matrixStack.pushPose();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        ResourceLocation backup = player.playerInfo.getSkinLocation();
        boolean backupSkinType = ClientUtil.isAlex(player);
        SkinHandler.setPlayerSkin(Minecraft.getInstance().player, PLAYER_TEXTURE);
        SkinHandler.setPlayerSkinType(Minecraft.getInstance().player, renderChoice == PlayerUtil.SkinType.ALEX);
        InventoryScreen.renderEntityInInventory(width / 2 + 60, height / 2 + 20, 45, (float) (leftPos + 170) - x, (float) (topPos + 75 - 25) - y, Minecraft.getInstance().player);
        SkinHandler.setPlayerSkin(Minecraft.getInstance().player, backup);
        SkinHandler.setPlayerSkinType(Minecraft.getInstance().player, backupSkinType);
        matrixStack.popPose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.searchField.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position >= skins.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                Minecraft.getInstance().getTextureManager().release(PLAYER_TEXTURE);
                PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position > 0) {
                    position--;
                } else {
                    position = skins.size() - 1;
                }
                PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        }

        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.searchField.render(matrixStack, mouseX, mouseY, partialTicks);

        for (DescButton descButton : descButtons) {
            if (descButton.isHovered()) {
                renderToolTip(matrixStack, descButton.getDescription(), mouseX, mouseY, Minecraft.getInstance().font);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.searchField.tick();
        excludeTrending.active = searchField.getValue().isEmpty();
    }
}