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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.File;
import java.util.List;

import static me.swirtzly.regeneration.util.client.RenderUtil.drawModelToGui;

public class NewSkinSelectionScreen extends ContainerScreen {

    private static final ResourceLocation background = new ResourceLocation(Regeneration.MODID, "textures/gui/customizer_background.png");
    public static boolean isAlex = true;
    private static ResourceLocation PLAYER_TEXTURE = DefaultPlayerSkin.getDefaultSkinLegacy();
    private static SkinManipulation.EnumChoices choices = null;
    private static List<File> skins = null;
    private static int position = 0;
    private static TextureManager textureManager = Minecraft.getInstance().getTextureManager();
    private static PlayerModel ALEX_MODEL = new PlayerModel(0.1f, true);
    private static PlayerModel STEVE_MODEL = new PlayerModel(0.1f, false);
    private float rotation = 0;

    public NewSkinSelectionScreen() {
        super(new ContainerBlank(), null, new TranslationTextComponent("Regeneration"));
        xSize = 176;
        ySize = 186;
    }

    public static void updateModels() {
        isAlex = isPosAlex(position);
        choices = isAlex ? SkinManipulation.EnumChoices.ALEX : SkinManipulation.EnumChoices.STEVE;
    }

    public static boolean isPosAlex(int position) {
        return skins.get(position).toPath().startsWith(SkinManipulation.SKIN_DIRECTORY_ALEX.toPath().toString());
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
                Util.getOSType().openFile(SkinManipulation.SKIN_DIRECTORY);
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

        int offset = 0;
        for (int i = 0; i < skins.size(); i++) {
            if (i % 4 == 0) {
                offset += 10;
            }
            GlStateManager.pushMatrix();
            Minecraft.getInstance().getTextureManager().bindTexture(TexUtil.fileTotexture(skins.get(getPosition(i))));
            drawModelToGui(isPosAlex(getPosition(i)) ? ALEX_MODEL : STEVE_MODEL, width / 2 + 20 * i, height / 2 + offset, 1.5f, rotation);
            GlStateManager.popMatrix();
        }

   /*     GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(SkinManipulation.createGuiTexture(skins.get(getPosition(0))));
        drawModelToGui(isPosAlex(getPosition(0)) ? ALEX_MODEL : STEVE_MODEL, width / 2, height / 2 - 10, 1.5f, rotation);
        GlStateManager.popMatrix();*/


       /* //Left First
        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(SkinManipulation.createGuiTexture(skins.get(getPosition(1))));
        drawModelToGui(isPosAlex(getPosition(1)) ? ALEX_MODEL : STEVE_MODEL, width / 2 - 90, height / 2 - 10, 1.3f, 200);
        GlStateManager.popMatrix();

        //Right First
        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(SkinManipulation.createGuiTexture(skins.get(getPosition(3))));
        drawModelToGui(isPosAlex(getPosition(3)) ? ALEX_MODEL : STEVE_MODEL, width / 2 + 90, height / 2 - 10, 1.3f, -200);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(SkinManipulation.createGuiTexture(skins.get(getPosition(4))));
        drawModelToGui(isPosAlex(getPosition(4)) ? ALEX_MODEL : STEVE_MODEL, width / 2 - 170, height / 2 - 10, 1.2f, 200);
        GlStateManager.popMatrix();*/


        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(TexUtil.fileTotexture(skins.get(getPosition(2))));
        drawModelToGui(isPosAlex(getPosition(2)) ? ALEX_MODEL : STEVE_MODEL, width / 2 + 170, height / 2 - 10, 1.2f, -200);
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        drawCenteredString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent("regeneration.gui.current_skin").getUnformattedComponentText(), width / 2, height / 2 + 5, Color.WHITE.getRGB());
        drawCenteredString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent(skins.get(position).getName().replaceAll(".png", "")).getUnformattedComponentText(), width / 2, height / 2 + 15, Color.WHITE.getRGB());

    }

    public int getPosition(int increase) {
        return MathHelper.clamp(position + increase, 0, skins.size() - 1);
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
