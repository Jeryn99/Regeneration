package micdoodle8.mods.galacticraft.api.client.tabs;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

//https://github.com/micdoodle8/Galacticraft/
public abstract class AbstractTab extends Button
{
    ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    ItemStack renderStack;
    public int potionOffsetLast;
    protected ItemRenderer itemRender;
    private int index;

    public AbstractTab(int index, ItemStack renderStack)
    {
        super(0, 0, 28, 32, StringTextComponent.EMPTY, (b) -> { ((AbstractTab) b).onTabClicked(); });
        this.renderStack = renderStack;
        this.itemRender = Minecraft.getInstance().getItemRenderer();
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int newPotionOffset = TabRegistry.getPotionOffsetNEI();
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof InventoryScreen)
        {
            newPotionOffset += TabRegistry.getRecipeBookOffset((InventoryScreen) screen) - TabRegistry.recipeBookOffset;
        }
        if (newPotionOffset != this.potionOffsetLast)
        {
            this.x += newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
        }
        if (this.visible)
        {
            GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);

            int yTexPos = this.active ? 3 : 32;
            int ySize = this.active ? 25 : 32;
            int yPos = this.y + (this.active ? 3 : 0);

            Minecraft mc = Minecraft.getInstance();
            mc.textureManager.bind(this.texture);
            this.blit(stack, this.x, yPos, index * 28, yTexPos, 28, ySize);

            RenderHelper.turnBackOn();
            this.setBlitOffset(100);
            this.itemRender.blitOffset = 100.0F;
            GlStateManager._enableLighting();
            GlStateManager._enableRescaleNormal();
            this.itemRender.renderGuiItem(this.renderStack, this.x + 6, this.y + 8);
            this.itemRender.renderGuiItemDecorations(mc.font, this.renderStack, this.x + 6, this.y + 8, null);
            GlStateManager._disableLighting();
            GlStateManager._enableBlend();
            this.itemRender.blitOffset = 0.0F;
            this.setBlitOffset(0);
            RenderHelper.turnOff();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int i) {
        boolean inWindow = this.active && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (inWindow)
        {
            this.onTabClicked();
        }

        return inWindow;
    }

    public abstract void onTabClicked();

    public abstract boolean shouldAddToList();
}
