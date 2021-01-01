package me.swirtzly.regeneration.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.gui.parts.ContainerBlank;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.skin.HandleSkins;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.NextSkinMessage;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.client.TexUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.util.List;

import static me.swirtzly.regeneration.util.client.RenderUtil.drawModelToGui;

public class SkinChoiceScreen extends ContainerScreen {

    private static final ResourceLocation background = new ResourceLocation(Regeneration.MODID, "textures/gui/customizer_background.png");
    public static boolean isAlex = true;
    private static ResourceLocation PLAYER_TEXTURE = DefaultPlayerSkin.getDefaultSkinLegacy();
    private static final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
    private static List<File> skins = null;
    private static int position = 0;
    private static final PlayerModel ALEX_MODEL = new PlayerModel(0.1f, true);
    private static final PlayerModel STEVE_MODEL = new PlayerModel(0.1f, false);
    private static PlayerUtil.EnumChoices choices = null;
    private float rotation = 0;

    public SkinChoiceScreen() {
        super(new ContainerBlank(), null, new TranslationTextComponent("Regeneration"));
        xSize = 176;
        ySize = 186;
    }

    public static void updateModels() {
        isAlex = skins.get(position).toPath().startsWith(HandleSkins.SKIN_DIRECTORY_ALEX.toPath().toString());
        choices = isAlex ? PlayerUtil.EnumChoices.ALEX : PlayerUtil.EnumChoices.STEVE;
    }

    @Override
    public void init() {
        super.init();
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 60, btnH = 18;
        rotation = 0;
        position = 0;
        GuiButtonExt btnNext = new GuiButtonExt(cx + 25, cy + 75, 20, 20, new TranslationTextComponent("regeneration.gui.previous").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getLocationSkin())) {
                    if (position >= skins.size() - 1) {
                        position = 0;
                    } else {
                        position++;
                    }
                    textureManager.deleteTexture(PLAYER_TEXTURE);
                    PLAYER_TEXTURE = TexUtil.fileTotexture(skins.get(position));
                    updateModels();
                }
            }
        });
        GuiButtonExt btnPrevious = new GuiButtonExt(cx + 130, cy + 75, 20, 20, new TranslationTextComponent("regeneration.gui.next").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                // Previous
                if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getLocationSkin())) {
                    if (position > 0) {
                        position--;
                    } else {
                        position = skins.size() - 1;
                    }
                    textureManager.deleteTexture(PLAYER_TEXTURE);
                    PLAYER_TEXTURE = TexUtil.fileTotexture(skins.get(position));
                    updateModels();
                }
            }
        });
        GuiButtonExt btnBack = new GuiButtonExt(cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.back").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Minecraft.getInstance().displayGuiScreen(new ColorScreen());
            }
        });
        GuiButtonExt btnOpenFolder = new GuiButtonExt(cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.open_folder").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Util.getOSType().openFile(HandleSkins.SKIN_DIRECTORY);
            }
        });
        GuiButtonExt btnSave = new GuiButtonExt(cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.save").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                updateModels();
                NetworkDispatcher.sendToServer(new NextSkinMessage(HandleSkins.imageToPixelData(skins.get(position)), isAlex));
            }
        });
        GuiButtonExt btnResetSkin = new GuiButtonExt(cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.reset_skin").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                ClientUtil.sendSkinResetPacket();
            }
        });

        addButton(btnNext);
        addButton(btnPrevious);
        addButton(btnOpenFolder);
        addButton(btnBack);
        addButton(btnSave);
        addButton(btnResetSkin);

        RegenCap.get(minecraft.player).ifPresent((data) -> choices = data.getPreferredModel());

        skins = SkinManipulation.listAllSkins(choices);
        PLAYER_TEXTURE = TexUtil.fileTotexture(skins.get(position));
        RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> choices = data.getPreferredModel());
        updateModels();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        Minecraft.getInstance().getTextureManager().bindTexture(background);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        GlStateManager.pushMatrix();
        ALEX_MODEL.isChild = false;
        STEVE_MODEL.isChild = false;
        Minecraft.getInstance().getTextureManager().bindTexture(PLAYER_TEXTURE);
        switch (choices) {
            case ALEX:
                drawModelToGui(ALEX_MODEL, width / 2, height / 2 - 50, 1.0f, rotation);
                break;
            case STEVE:
                drawModelToGui(STEVE_MODEL, width / 2, height / 2 - 50, 1.0f, rotation);
                break;
            case EITHER:
                drawModelToGui(ALEX_MODEL, width / 2 - 40, height / 2 - 50, 1.0f, rotation);
                drawModelToGui(STEVE_MODEL, width / 2 + 40, height / 2 - 50, 1.0f, rotation);
                break;
        }
        GlStateManager.popMatrix();

        drawCenteredString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent("regeneration.gui.current_skin").getUnformattedComponentText(), width / 2, height / 2 + 5, Color.WHITE.getRGB());
        drawCenteredString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent(skins.get(position).getName().replaceAll(".png", "")).getUnformattedComponentText(), width / 2, height / 2 + 15, Color.WHITE.getRGB());

    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {

        if(p_keyPressed_1_ == GLFW.GLFW_KEY_RIGHT){
            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getLocationSkin())) {
                if (position >= skins.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                textureManager.deleteTexture(PLAYER_TEXTURE);
                PLAYER_TEXTURE = TexUtil.fileTotexture(skins.get(position));
                updateModels();
            }
        }

        if(p_keyPressed_1_ == GLFW.GLFW_KEY_LEFT){
            if (!PLAYER_TEXTURE.equals(Minecraft.getInstance().player.getLocationSkin())) {
                if (position > 0) {
                    position--;
                } else {
                    position = skins.size() - 1;
                }
                textureManager.deleteTexture(PLAYER_TEXTURE);
                PLAYER_TEXTURE = TexUtil.fileTotexture(skins.get(position));
                updateModels();
            }
        }


        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        rotation++;
        if (rotation > 360) {
            rotation = 0;
        }
    }

}
