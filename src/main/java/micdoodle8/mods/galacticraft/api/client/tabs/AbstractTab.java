package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractTab extends AbstractButton {
	ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	ItemStack renderStack;
	public int potionOffsetLast;
	public int id;

	public AbstractTab(int id, int posX, int posY, ItemStack renderStack) {
		super(posX, posY, 28, 32, "");
		this.renderStack = renderStack;
		this.id = id;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int newPotionOffset = TabRegistry.getPotionOffsetNEI();
		if (newPotionOffset != this.potionOffsetLast) {
			this.x += newPotionOffset - this.potionOffsetLast;
			this.potionOffsetLast = newPotionOffset;
		}
		if (this.visible) {
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft mc = Minecraft.getInstance();
			int yTexPos = this.active ? 3 : 32;
			int ySize = this.active ? 25 : 32;
			int xOffset = this.id == 2 ? 0 : 1;
			int yPos = this.y + (this.active ? 3 : 0);
			ItemRenderer itemRender = mc.getItemRenderer();
			mc.getTextureManager().bindTexture(this.texture);
			this.blit(this.x, yPos, xOffset * 28, yTexPos, 28, ySize);

			RenderHelper.enableGUIStandardItemLighting();
			this.blitOffset += 300;
			itemRender.zLevel = 100.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			itemRender.renderItemAndEffectIntoGUI(this.renderStack, this.x + 6, this.y + 8);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.renderStack, this.x + 6, this.y + 8, null);
			GlStateManager.disableLighting();
			itemRender.zLevel = 0.0F;
			this.blitOffset -= 300;
			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.onTabClicked();
	}

	@Override
	public void onPress() {

	}

	public abstract void onTabClicked();

	public abstract boolean shouldAddToList();
}
