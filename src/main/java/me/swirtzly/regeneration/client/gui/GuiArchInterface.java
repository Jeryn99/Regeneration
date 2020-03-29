package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.ContainerArch;
import me.swirtzly.regeneration.client.gui.parts.InventoryTabArch;
import me.swirtzly.regeneration.common.item.arch.capability.ArchInventory;
import me.swirtzly.regeneration.network.MessageUseArch;
import me.swirtzly.regeneration.network.NetworkHandler;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiArchInterface extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RegenerationMod.MODID, "textures/gui/hij.png");
    private final ArchInventory archInv;

    public GuiArchInterface(EntityPlayer player, ArchInventory archInv) {
        super(new ContainerArch(player, archInv));
        this.archInv = archInv;
        xSize = 256;
        ySize = 173;
    }

    @Override
    public void initGui() {
        super.initGui();
        TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabArch.class);
        TabRegistry.addTabsToList(this.buttonList);

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 68, btnH = 17;

        GuiButtonExt btnClose = new GuiButtonExt(98, width / 2 - 30, cy + 20, 71, btnH, new TextComponentTranslation("regeneration.gui.use").getFormattedText());
        addButton(btnClose);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 98) {
            NetworkHandler.INSTANCE.sendToServer(new MessageUseArch());
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
