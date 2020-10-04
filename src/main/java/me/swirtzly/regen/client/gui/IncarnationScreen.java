package me.swirtzly.regen.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.skin.CommonSkin;
import me.swirtzly.regen.client.skin.SkinHandler;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.NextSkinMessage;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
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

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;


public class IncarnationScreen extends ContainerScreen {

    private static final ResourceLocation background = new ResourceLocation(RConstants.MODID, "textures/gui/customizer_background.png");
    private static final PlayerModel<?> ALEX_MODEL = new PlayerModel<>(0.1f, true);
    private static final PlayerModel<?> STEVE_MODEL = new PlayerModel<>(0.1f, false);
    public static boolean isAlex = true;
    private static ResourceLocation PLAYER_TEXTURE = DefaultPlayerSkin.getDefaultSkinLegacy();
    private static List<File> skins = null;
    private static int position = 0;
    private static PlayerUtil.SkinType choices = RegenCap.get(Objects.requireNonNull(Minecraft.getInstance().player)).orElse(null).getPreferredModel();

    public IncarnationScreen() {
        super(new BlankContainer(), Objects.requireNonNull(Minecraft.getInstance().player).inventory, new TranslationTextComponent("Next Incarnation"));
        xSize = 176;
        ySize = 186;
    }

    public static void updateModels() {
        isAlex = skins.get(position).toPath().startsWith(CommonSkin.SKIN_DIRECTORY_ALEX.toPath().toString());
        //choices = isAlex ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE;
    }

    //TODO Seems to clip out the left arm of the models wear, not a high priority
    public static void drawModelToGui(EntityModel entityModel, MatrixStack matrixStack) {
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        int k = (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        RenderSystem.scaled(1.5, 1.5, 1.5);
        RenderSystem.translated(0.4 - 0.05, -0.3 + 0.05, 0);
        RenderSystem.viewport((Minecraft.getInstance().currentScreen.width - 320) / 2 * k, (Minecraft.getInstance().currentScreen.height - 240) / 2 * k, 320 * k, 240 * k);
        RenderSystem.translatef(-0.34F, 0.23F, 0.0F);
        RenderSystem.multMatrix(Matrix4f.perspective(100.0D, 1.3333334F, 9.0F, 100.0F));
        RenderSystem.matrixMode(5888);
        matrixStack.push();
        MatrixStack.Entry matrixstack$entry = matrixStack.getLast();
        matrixstack$entry.getMatrix().setIdentity();
        matrixstack$entry.getNormal().setIdentity();
        matrixStack.translate(0.1D, 3.3F, 1984.0D);
        matrixStack.scale(5.0F, 5.0F, 5.0F);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(20.0F));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
        // matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation));
        RenderSystem.enableRescaleNormal();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(RenderType.getEntityTranslucent(PLAYER_TEXTURE));
        entityModel.render(matrixStack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        irendertypebuffer$impl.finish();
        matrixStack.pop();
        RenderSystem.matrixMode(5889);
        RenderSystem.viewport(0, 0, Minecraft.getInstance().getMainWindow().getFramebufferWidth(), Minecraft.getInstance().getMainWindow().getFramebufferHeight());
        RenderSystem.popMatrix();
    }

    @Override
    public void init() {
        super.init();
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 60, btnH = 18;
        position = 0;
        skins = CommonSkin.listAllSkins(choices);
        Button btnNext = new Button(cx + 25, cy + 75, 20, 20, new TranslationTextComponent("regeneration.gui.previous"), button -> {
            skins = CommonSkin.listAllSkins(choices);

            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getLocationSkin())) {
                if (position >= skins.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        });
        Button btnPrevious = new Button(cx + 130, cy + 75, 20, 20, new TranslationTextComponent("regeneration.gui.next"), button -> {
            // Previous
            skins = CommonSkin.listAllSkins(choices);
            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getLocationSkin())) {
                if (position > 0) {
                    position--;
                } else {
                    position = skins.size() - 1;
                }
                PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
                updateModels();
            }
        });
        Button btnBack = new Button(cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.back"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Minecraft.getInstance().displayGuiScreen(new PreferencesScreen());
            }
        });
        Button btnOpenFolder = new Button(cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.open_folder"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Util.getOSType().openFile(CommonSkin.SKIN_DIRECTORY);
            }
        });
        Button btnSave = new Button(cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.save"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                updateModels();
                NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new NextSkinMessage(RegenUtil.fileToBytes(skins.get(position)), isAlex));
            }
        });
        Button btnResetSkin = new Button(cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.reset_skin"), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                SkinHandler.sendResetMessage();
            }
        });

        addButton(btnNext);
        addButton(btnPrevious);
        addButton(btnOpenFolder);
        addButton(btnBack);
        addButton(btnSave);
        addButton(btnResetSkin);

        RegenCap.get(minecraft.player).ifPresent((data) -> choices = data.getPreferredModel());

        PLAYER_TEXTURE = CommonSkin.fileTotexture(skins.get(position));
        RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> choices = data.getPreferredModel());
        updateModels();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        Minecraft.getInstance().getTextureManager().bindTexture(background);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        ALEX_MODEL.isChild = false;
        STEVE_MODEL.isChild = false;
        switch (choices) {
            case ALEX:
                drawModelToGui(ALEX_MODEL, matrixStack);
                break;
            case STEVE:
                drawModelToGui(STEVE_MODEL, matrixStack);
                break;
            case EITHER:
                drawModelToGui(ALEX_MODEL, matrixStack);
                drawModelToGui(STEVE_MODEL, matrixStack);
                break;
        }

        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, new TranslationTextComponent("regeneration.gui.current_skin").getString(), width / 2, height / 2 + 5, Color.WHITE.getRGB());
        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, new TranslationTextComponent(skins.get(position).getName().replaceAll(".png", "")).getString(), width / 2, height / 2 + 15, Color.WHITE.getRGB());

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float) this.titleX, (float) this.titleY, 4210752);
    }
}