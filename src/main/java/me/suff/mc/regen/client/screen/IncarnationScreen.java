package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class IncarnationScreen extends AbstractContainerScreen {

    private static final ResourceLocation screenBackground = new ResourceLocation(RConstants.MODID, "textures/gui/customizer.png");
    private static final ModelPart alexModel = ClientUtil.getPlayerModel(true);
    private static final ModelPart steveModel = ClientUtil.getPlayerModel(false);
    public static boolean isAlex = true;
    private static ResourceLocation currentTexture = DefaultPlayerSkin.getDefaultSkin();
    private static PlayerUtil.SkinType currentSkinType = RegenCap.get(Objects.requireNonNull(Minecraft.getInstance().player)).orElse(null).preferredModel();
    private static PlayerUtil.SkinType renderChoice = currentSkinType;
    private static List<File> skins = null;
    private static int position = 0;
    private final ArrayList<DescButton> descButtons = new ArrayList<>();
    private RCheckbox excludeTrending;
    private EditBox searchField;
    private Button uploadToMcBtn;


    public IncarnationScreen() {
        super(new BlankContainer(), Objects.requireNonNull(Minecraft.getInstance().player).getInventory(), new TranslatableComponent("Next Incarnation"));
        imageWidth = 256;
        imageHeight = 173;
    }

    public static void updateModels() {
        if (!skins.isEmpty()) {
            currentTexture = CommonSkin.fileTotexture(skins.get(position));
            isAlex = skins.get(position).toPath().startsWith(CommonSkin.SKIN_DIRECTORY_ALEX.toPath().toString());
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
        for (AbstractTab button : TabRegistry.tabList) {
            addRenderableWidget(button);
        }
        int buttonOffset = 35;
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 55, btnH = 18;
        position = 0;
        skins = CommonSkin.listAllSkins(PlayerUtil.SkinType.EITHER);
        try {
            getHash();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (skins.isEmpty()) {
            Minecraft.getInstance().setScreen(new RErrorScreen(new TranslatableComponent("No Skins for " + new TranslatableComponent("regeneration.skin_type." + currentSkinType.name().toLowerCase()).getString()), new TranslatableComponent("Please place skins in the local Directory")));
        }

        this.searchField = new EditBox(this.font, cx + 15, cy + 145, imageWidth - 70, 20, this.searchField, new TranslatableComponent("skins.search"));

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
        this.renderables.add(this.searchField);
        this.setInitialFocus(this.searchField);

        uploadToMcBtn = new DescButton(cx + 10, cy + 105, btnW * 2 + 5, btnH + 2, new TranslatableComponent("Upload to Minecraft"), button -> {
            String imgurLink = null;
            try {
                imgurLink = ClientUtil.getImgurLink(RegenUtil.encodeFileToBase64Binary(skins.get(position)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = "https://www.minecraft.net/en-us/profile/skin/remote?url=" + imgurLink + "&model=" + (isAlex ? "slim" : "classic");
            Util.getPlatform().openUri(url);
        }).setDescription(new String[]{"button.tooltip.upload2mc"});

        DescButton btnPrevious = new DescButton(cx + 140, cy + 60, 20, 20, new TranslatableComponent("regen.gui.previous"), button -> {
            if (searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }
            stripTrending();

            if (!currentTexture.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position >= skins.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                updateModels();
            }
        }).setDescription(new String[]{"button.tooltip.previous_skin"});

        DescButton btnNext = new DescButton(cx + 215, cy + 60, 20, 20, new TranslatableComponent("regen.gui.next"), button -> {
            // Previous
            if (searchField.getValue().isEmpty()) {
                skins = CommonSkin.listAllSkins(currentSkinType);
            }

            stripTrending();

            if (!currentTexture.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position > 0) {
                    position--;
                } else {
                    position = skins.size() - 1;
                }
                currentTexture = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        }).setDescription(new String[]{"button.tooltip.next_skin"});

        DescButton btnBack = new DescButton(cx + 10, cy + 115 - buttonOffset, btnW, btnH + 2, new TranslatableComponent("regen.gui.back"), button -> Minecraft.getInstance().setScreen(new PreferencesScreen()));

        DescButton btnOpenFolder = new DescButton(cx + 90 - 20, cy + 115 - buttonOffset, btnW, btnH + 2, new TranslatableComponent("regen.gui.open_folder"), button -> Util.getPlatform().openFile(CommonSkin.SKIN_DIRECTORY)).setDescription(new String[]{"button.tooltip.open_folder"});

        DescButton btnSave = new DescButton(cx + 90 - 20, cy + 90 - buttonOffset, btnW, btnH + 2, new TranslatableComponent("regen.gui.save"), button -> {
            updateModels();
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new NextSkinMessage(RegenUtil.fileToBytes(skins.get(position)), isAlex));
        }).setDescription(new String[]{"button.tooltip.save_skin"});

        DescButton btnResetSkin = new DescButton(cx + 10, cy + 90 - buttonOffset, btnW, btnH + 2, new TranslatableComponent("regen.gui.reset_skin"), new Button.OnPress() {
            @Override
            public void onPress(Button button) {
                SkinHandler.sendResetMessage();
            }
        }).setDescription(new String[]{"button.tooltip.reset_mojang"});

        this.excludeTrending = new RCheckbox(cx + 10, cy + 25, 150, 20, new TranslatableComponent("Trending?"), true, checkboxButton -> {
            if (checkboxButton instanceof Checkbox) {
                Checkbox check = (Checkbox) checkboxButton;
                position = 0;
                if (!check.selected()) {
                    skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("namemc"));
                } else {
                    skins = CommonSkin.listAllSkins(currentSkinType);
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
        addRenderableWidget(this.uploadToMcBtn);

        for (Widget widget : renderables) {
            if (widget instanceof DescButton) {
                descButtons.add((DescButton) widget);
            }
        }

        RegenCap.get(minecraft.player).ifPresent((data) -> currentSkinType = data.preferredModel());

        RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> currentSkinType = data.preferredModel());
        updateModels();
    }

    private void stripTrending() {
        if (!excludeTrending.selected()) {
            skins.removeIf(file -> file.getAbsoluteFile().toPath().toString().contains("namemc"));
        }
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        RenderSystem.setShaderTexture(0, screenBackground);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        //alexModel.young = false;
        //steveModel.young = false;
        renderSkinToGui(matrixStack, x, y);

        drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslatableComponent("regen.gui.current_skin").getString(), width / 2 + 60, height / 2 + 30, Color.WHITE.getRGB());
        if (!skins.isEmpty() && position < skins.size()) {
            matrixStack.pushPose();
            String name = skins.get(position).getName().replaceAll(".png", "");
            renderWidthScaledText(name + skins.get(position), matrixStack, this.font, width / 2 + 60, height / 2 + 40, Color.WHITE.getRGB(), 100);
            matrixStack.popPose();
        }

        drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslatableComponent("Search"), width / 2 - 95, height / 2 + 45, Color.WHITE.getRGB());

    }

    private void renderSkinToGui(PoseStack matrixStack, int x, int y) {
        matrixStack.pushPose();
        LocalPlayer player = Minecraft.getInstance().player;
        ResourceLocation backup = ClientUtil.getPlayerInfo(player).getSkinLocation();
        boolean backupSkinType = ClientUtil.isAlex(player);
        SkinHandler.setPlayerSkin(Minecraft.getInstance().player, currentTexture);
        SkinHandler.setPlayerSkinType(Minecraft.getInstance().player, renderChoice == PlayerUtil.SkinType.ALEX);
        InventoryScreen.renderEntityInInventory(width / 2 + 60, height / 2 + 20, 45, (float) (leftPos + 170) - x, (float) (topPos + 75 - 25) - y, Minecraft.getInstance().player);
        SkinHandler.setPlayerSkin(Minecraft.getInstance().player, backup);
        SkinHandler.setPlayerSkinType(Minecraft.getInstance().player, backupSkinType);
        matrixStack.popPose();
    }


    public static void getHash() throws IOException {
        StringBuilder end = new StringBuilder();
        for (File file : skins) {
            if (!file.getName().startsWith("mk_")) {
                String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(file));

                String md5Line = "\n&quot;" + md5 + "&quot;:{";
                String authorLine = "\n&quot;author&quot;:&quot;" + "author_here" + "&quot;";
                String nameLine = "\n&quot;name&quot;:&quot;" + file.getName() + "&quot;}\n";

                String line = md5Line+authorLine+nameLine;
                end.append(line);
            }
        }
        System.out.println(end);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.searchField.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (!currentTexture.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position >= skins.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                Minecraft.getInstance().getTextureManager().release(currentTexture);
                currentTexture = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (!currentTexture.equals(Minecraft.getInstance().player.getSkinTextureLocation())) {
                if (position > 0) {
                    position--;
                } else {
                    position = skins.size() - 1;
                }
                currentTexture = CommonSkin.fileTotexture(skins.get(position));
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
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.searchField.tick();
        excludeTrending.active = searchField.getValue().isEmpty();
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
    public void renderWidthScaledText(String text, PoseStack matrix, Font font, float x, float y, int color, int width) {
        matrix.pushPose();
        int textWidth = font.width(text);
        float scale = width / (float) textWidth;
        scale = Mth.clamp(scale, 0.0F, 1.0F);
        matrix.translate(x, y, 0);
        matrix.scale(scale, scale, scale);
        drawCenteredString(matrix, Minecraft.getInstance().font, text, 0, 0, Color.WHITE.getRGB());
        matrix.popPose();
    }

}