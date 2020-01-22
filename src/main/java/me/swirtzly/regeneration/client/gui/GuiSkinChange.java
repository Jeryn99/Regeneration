package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankButton;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.gui.parts.InventoryTabRegeneration;
import me.swirtzly.regeneration.client.image.ImageDownloadAlt;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.network.MessageNextSkin;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public int posX;
    public int posY;
    private ArrayList<GuiButton> scrollButtonList = new ArrayList<>();
    private int scrollbarChangeReq;
    private GuiTextField textFieldValue;


    public GuiSkinChange() {
        super(new BlankContainer());
        xSize = 176;
        ySize = 186;
        choices = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getPreferredModel();
        skins = FileUtil.listAllSkins(choices);
        if (skins.size() > 0) {
            PLAYER_TEXTURE = SkinChangingHandler.createGuiTexture(skins.get(position));
        } else try {
            throw new Exception("NO SKINS COULD BE FOUND.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GuiButtonExt btnBack, btnOpenFolder, btnSave, btnResetSkin;

    public static void updateModels() {
        try {
            isAlex = ImageDownloadAlt.isAlexSkin(ImageIO.read(skins.get(position)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        choices = isAlex ? SkinChangingHandler.EnumChoices.ALEX : SkinChangingHandler.EnumChoices.STEVE;
        //   PLAYER_TEXTURE = SkinChangingHandler.createGuiTexture(skins.get(position));
    }
    private int scrollbarPosY;

    @Override
    protected void keyTyped(char eventChar, int eventKey) throws IOException {
        this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(PLAYER_TEXTURE);
        playerModelAlex.isChild = false;
        playerModelSteve.isChild = false;
        float rotation = Minecraft.getMinecraft().player.ticksExisted * 2;
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
        textFieldValue.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        textFieldValue.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        skins = FileUtil.listAllSkins(choices);
        updateModels();

        switch (button.id) {
            case 66:
                Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiPreferences.ID, Minecraft.getMinecraft().world, 0, 0, 0);
                break;
            case 88:
                updateModels();
                NetworkHandler.INSTANCE.sendToServer(new MessageNextSkin(SkinChangingHandler.imageToPixelData(skins.get(position)), ImageDownloadAlt.isAlexSkin(ImageIO.read(skins.get(position)))));
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
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
        }

        if (button instanceof BlankButton) {
            BlankButton buttonUpdate = (BlankButton) button;
            PLAYER_TEXTURE = SkinChangingHandler.createGuiTexture(buttonUpdate.getFile());
            updateModels();
        }
    }

    private int scrollbarIndex;
    private int scrollbarChange;
    private boolean isScrollPressed;

    @Override
    public void initGui() {

        this.scrollButtonList.clear();
        for (File skin : skins) {
            BlankButton BUTTON = new BlankButton(scrollButtonList.size() + 3, posX + 8, posY + 7 + (24 * (scrollButtonList.size())), skin.getName().replaceAll(".png", ""));
            BUTTON.setFile(skin);
            this.scrollButtonList.add(BUTTON);
        }

        textFieldValue = new GuiTextField(10, this.mc.fontRenderer, posX + 170, posY + 170, 177, 16);
        textFieldValue.setMaxStringLength(10000);
        textFieldValue.setText("Enter Search");
        textFieldValue.setEnabled(true);
        textFieldValue.setFocused(true);

        super.initGui();
        TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabRegeneration.class);
        TabRegistry.addTabsToList(this.buttonList);
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 68, btnH = 17;
        position = 0;

        btnBack = new GuiButtonExt(66, cx + 20, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.back").getFormattedText());
        btnOpenFolder = new GuiButtonExt(77, cx + 90, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.open_folder").getFormattedText());
        btnSave = new GuiButtonExt(88, cx + 90, cy + 127, btnW, btnH, new TextComponentTranslation("regeneration.gui.save").getFormattedText());
        btnResetSkin = new GuiButtonExt(100, cx + 20, cy + 127, btnW, btnH, new TextComponentTranslation("regeneration.gui.reset_skin").getFormattedText());

        this.updateButtonsList();
        updateModels();
    }

    public boolean needsScrollbar() {
        return this.scrollButtonList.size() > 5;
    }

    public void updateButtonsList() {
        this.buttonList.clear();
        addButton(btnOpenFolder);
        addButton(btnBack);
        addButton(btnSave);
        addButton(btnResetSkin);
        int id = 1000;
        for (int i = this.scrollbarIndex; i < this.scrollbarIndex + 5 && i < this.scrollButtonList.size(); i++) {
            BlankButton but = (BlankButton) this.scrollButtonList.get(i);

            BlankButton BUTTON = new BlankButton(id, posX + 160, posY + 197 + (24 * (i - this.scrollbarIndex)), but.displayString);
            BUTTON.setFile(but.getFile());
            this.buttonList.add(BUTTON);
            id++;
        }
    }

    public void updateScrollPositon(int change) {
        if (change != 0 && this.needsScrollbar()) {
            this.scrollbarPosY = change;
            if (this.scrollbarPosY > 85)
                this.scrollbarPosY = 85;
            if (this.scrollbarPosY < 00)
                this.scrollbarPosY = 00;
            this.updateScrollIndex();
        }
    }

    public void updateScrollIndex() {
        if (this.scrollbarPosY <= this.scrollbarChangeReq) {
            this.scrollbarIndex = 0;
            this.scrollbarChange = this.scrollbarPosY;

            this.updateButtonsList();
        } else if (this.scrollbarPosY - this.scrollbarChange >= this.scrollbarChangeReq) {
            this.scrollbarIndex++;
            this.scrollbarChange = this.scrollbarPosY;
            this.updateButtonsList();
        } else if (this.scrollbarPosY - this.scrollbarChange <= (-1 * this.scrollbarChangeReq)) {
            this.scrollbarIndex--;
            this.scrollbarChange = this.scrollbarPosY;

            if (this.scrollbarIndex < 0)
                this.scrollbarIndex = 0;

            this.updateButtonsList();
        }
    }

    private boolean isMouseOverArea(int mouseX, int mouseY, int posX, int posY, int width, int height) {
        return (mouseX >= posX && mouseX < posX + width && mouseY >= posY && mouseY < posY + height);
    }

    @Override
    public void handleMouseInput() {
        try {
            super.handleMouseInput();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int wheelState;
        if ((wheelState = Mouse.getEventDWheel()) != 0)
            this.updateScrollPositon(this.scrollbarPosY - (wheelState / 10));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

        this.textFieldValue.mouseClicked(mouseX, mouseY, button);

        try {
            super.mouseClicked(mouseX, mouseY, button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int posX = (this.width - this.xSize) / 2;
        int posY = (this.height - this.ySize) / 2;

        if (button != 0 || !this.needsScrollbar())
            return;

        if (isMouseOverArea(mouseX, mouseY, posX + 154, posY + 7 + this.scrollbarPosY, 12, 15))
            this.isScrollPressed = true;

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int button, long timeSince) {
        super.mouseClickMove(mouseX, mouseY, button, timeSince);

        int posX = (this.width - this.xSize) / 2;
        int posY = (this.height - this.ySize) / 2;

        //if(isMouseOverArea(mouseX, mouseY, posX + 234, posY + 26, 14, 102))
        if (Mouse.isButtonDown(0) && this.isScrollPressed) {
            this.updateScrollPositon(mouseY - posY);

            // If they release the button, set isScrollPressed to false
            if (!Mouse.isButtonDown(0))
                this.isScrollPressed = false;
        }
    }

}