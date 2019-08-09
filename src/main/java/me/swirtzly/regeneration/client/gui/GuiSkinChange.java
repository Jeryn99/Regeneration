package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.gui.parts.InventoryTabRegeneration;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.network.MessageNextSkin;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static me.swirtzly.regeneration.util.ClientUtil.playerModelAlex;
import static me.swirtzly.regeneration.util.ClientUtil.playerModelSteve;
import static me.swirtzly.regeneration.util.RenderUtil.drawModelToGui;

public class GuiSkinChange extends GuiContainer {

    public static final int ID = 3;
    private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background_small.png");
    public static boolean isAlex = true;
    private static ResourceLocation PLAYER_TEXTURE = Minecraft.getMinecraft().player.getLocationSkin();
    private static SkinChangingHandler.EnumChoices choices = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getPreferredModel();
    private static List<File> skins = FileUtil.listAllSkins(choices);
    private static int position = 0;
    private float rotation = 0;

    public GuiSkinChange() {
        super(new BlankContainer());
        xSize = 176;
        ySize = 186;
    }

    public static void updateModels() {
        isAlex = skins.get(position).toPath().startsWith(SkinChangingHandler.SKIN_DIRECTORY_ALEX.toPath().toString());
        choices = isAlex ? SkinChangingHandler.EnumChoices.ALEX : SkinChangingHandler.EnumChoices.STEVE;
    }

    @Override
    public void initGui() {
        super.initGui();
        TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabRegeneration.class);
        TabRegistry.addTabsToList(this.buttonList);
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 60, btnH = 18;
        rotation = 0;
        position = 0;

        GuiButtonExt btnNext = new GuiButtonExt(44, cx + 25, cy + 80, 20, 20, new TextComponentTranslation("regeneration.gui.previous").getFormattedText());
        GuiButtonExt btnPrevious = new GuiButtonExt(55, cx + 130, cy + 80, 20, 20, new TextComponentTranslation("regeneration.gui.next").getFormattedText());
        GuiButtonExt btnBack = new GuiButtonExt(66, cx + 25, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.back").getFormattedText());
        GuiButtonExt btnOpenFolder = new GuiButtonExt(77, cx + 90, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.open_folder").getFormattedText());
        GuiButtonExt btnSave = new GuiButtonExt(88, cx + 90, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.save").getFormattedText());
        GuiButtonExt btnResetSkin = new GuiButtonExt(100, cx + 25, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.reset_skin").getFormattedText());


        buttonList.add(btnNext);
        buttonList.add(btnPrevious);
        buttonList.add(btnOpenFolder);
        buttonList.add(btnBack);
        buttonList.add(btnSave);
        buttonList.add(btnResetSkin);

        PLAYER_TEXTURE = SkinChangingHandler.createGuiTexture(skins.get(position));
        choices = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getPreferredModel();
        skins = FileUtil.listAllSkins(choices);
        updateModels();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(PLAYER_TEXTURE);
        playerModelAlex.isChild = false;
        playerModelSteve.isChild = false;
        switch (choices) {
            case ALEX:
                drawModelToGui(playerModelAlex, width / 2, height / 2 - 45, 1.0f, rotation);
                break;
            case STEVE:
                drawModelToGui(playerModelSteve, width / 2, height / 2 - 45, 1.0f, rotation);
                break;
            case EITHER:
                drawModelToGui(playerModelAlex, width / 2 - 40, height / 2 - 45, 1.0f, rotation);
                drawModelToGui(playerModelSteve, width / 2 + 40, height / 2 - 45, 1.0f, rotation);
                break;
        }
        GlStateManager.popMatrix();

        drawCenteredString(Minecraft.getMinecraft().fontRenderer, new TextComponentTranslation("regeneration.gui.next_incarnation").getUnformattedText(), width / 2, height / 2 - 80, Color.WHITE.getRGB());

        String skinName = skins.get(position).getName();
        skinName = skinName.substring(0, 1).toUpperCase() + skinName.substring(1);
        drawCenteredString(Minecraft.getMinecraft().fontRenderer, skinName.replaceAll(".png", ""), width / 2, height / 2 + 15, Color.WHITE.getRGB());

    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        rotation++;
        if (rotation > 360) {
            rotation = 0;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        switch (button.id) {
            case 66:
                Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiPreferences.ID, Minecraft.getMinecraft().world, 0, 0, 0);
                break;

            case 55:
                //Next
                if (!PLAYER_TEXTURE.equals(Minecraft.getMinecraft().player.getLocationSkin())) {
                    if (position >= skins.size() - 1) {
                        position = 0;
                    } else {
                        position++;
                    }
                    textureManager.deleteTexture(PLAYER_TEXTURE);
                    PLAYER_TEXTURE = SkinChangingHandler.createGuiTexture(skins.get(position));
                    updateModels();
                }
                break;

            case 44:
                //Previous
                if (!PLAYER_TEXTURE.equals(Minecraft.getMinecraft().player.getLocationSkin())) {
                    if (position > 0) {
                        position--;
                    } else {
                        position = skins.size() - 1;
                    }
                    textureManager.deleteTexture(PLAYER_TEXTURE);
                    PLAYER_TEXTURE = SkinChangingHandler.createGuiTexture(skins.get(position));
                    updateModels();
                }
                break;

            case 88:
                updateModels();
                NetworkHandler.INSTANCE.sendToServer(new MessageNextSkin(SkinChangingHandler.imageToPixelData(skins.get(position)), isAlex));
                break;

            case 100:
                ClientUtil.sendSkinResetPacket();
                break;

            case 77:
                try {
                    Desktop.getDesktop().open(SkinChangingHandler.SKIN_DIRECTORY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}