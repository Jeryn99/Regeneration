package me.fril.regeneration.client.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.fril.regeneration.util.RegenObjects;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class TabRegeneration extends AbstractTab {
	
	public TabRegeneration() {
		super(0, 0, 0, new ItemStack(RegenObjects.Items.FOB_WATCH));
		displayString = "Regeneration";
	}
	
	@Override
	public void onTabClicked() {
		Minecraft.getMinecraft().addScheduledTask(()->Minecraft.getMinecraft().displayGuiScreen(new GuiCustomizer()));
	}
	
	@Override
	public boolean shouldAddToList() {
		return true;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		
		super.drawButton(minecraft, mouseX, mouseY, partialTicks);
		
		if (enabled) {
			Minecraft mc = Minecraft.getMinecraft();
			boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if (hovered) {
				int x = mouseX + mc.fontRenderer.getStringWidth(displayString);
				GlStateManager.translate(x, y + 2, 0);
				drawHoveringText(Arrays.asList(displayString), 0, 0, mc.fontRenderer);
				GlStateManager.translate(-x, -(y + 2), 0);
			}
		}
	}
	
	protected void drawHoveringText(List<String> list, int x, int y, FontRenderer font) {
		if (list.isEmpty())
			return;
		
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		int k = 0;
		Iterator<String> iterator = list.iterator();
		
		while (iterator.hasNext()) {
			String s = iterator.next();
			int l = font.getStringWidth(s);
			
			if (l > k) {
				k = l;
			}
		}
		
		int j2 = x + 12;
		int k2 = y - 12;
		int i1 = 8;
		
		if (list.size() > 1) {
			i1 += 2 + (list.size() - 1) * 10;
		}
		
		if (j2 + k > width) {
			j2 -= 28 + k;
		}
		
		if (k2 + i1 + 6 > height) {
			k2 = height - i1 - 6;
		}
		
		zLevel = 300.0F;
		itemRender.zLevel = 300.0F;
		int j1 = -267386864;
		drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
		drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
		drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
		drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
		drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
		int k1 = 1347420415;
		int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
		drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
		drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
		drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
		drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
		
		for (int i2 = 0; i2 < list.size(); ++i2) {
			String s1 = list.get(i2);
			font.drawStringWithShadow(s1, j2, k2, -1);
			
			if (i2 == 0) {
				k2 += 2;
			}
			
			k2 += 10;
		}
		
		zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
	}
}
