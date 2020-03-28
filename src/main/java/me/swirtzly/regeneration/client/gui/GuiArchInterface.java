package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.ContainerArch;
import me.swirtzly.regeneration.common.item.arch.capability.ArchInventory;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
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
