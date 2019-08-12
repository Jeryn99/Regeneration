package me.swirtzly.regeneration.client.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Swirtzly
 * on 12/08/2019 @ 15:40
 */
public class ButtonHoverText extends GuiButtonExt {

    public String desc;

    public ButtonHoverText(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public ButtonHoverText(int id, int xPos, int yPos, int width, int height, String displayString, String desc) {
        super(id, xPos, yPos, width, height, displayString);
        this.desc = desc;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        super.drawButton(mc, mouseX, mouseY, partial);
        GlStateManager.pushMatrix();
        if (hovered) {
            GlStateManager.translate(-x * 2, y + 5, 0);
            int x = mouseX + mc.fontRenderer.getStringWidth(desc);
            GuiUtils.drawHoveringText(Collections.singletonList(desc), x, y, width, height, 300, mc.fontRenderer);
            GlStateManager.translate(x, -(y + 5), 0);
        }
        GlStateManager.popMatrix();
    }

    private void drawHoveringText(List list, int x, int y, FontRenderer font) {
        if (list.isEmpty())
            return;

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int k = 0;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
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

        if (j2 + k > this.width) {
            j2 -= 28 + k;
        }

        if (k2 + i1 + 6 > this.height) {
            k2 = this.height - i1 - 6;
        }

        this.zLevel = 900.0F;
        Minecraft.getMinecraft().getRenderItem().zLevel = 900.0F;
        int j1 = -267386864;
        this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
        this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
        this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
        this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
        int k1 = 1347420415;
        int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
        this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
        this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
        this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

        for (int i2 = 0; i2 < list.size(); ++i2) {
            String s1 = (String) list.get(i2);
            font.drawStringWithShadow(s1, j2, k2, -1);

            if (i2 == 0) {
                k2 += 2;
            }

            k2 += 10;
        }

        this.zLevel = 0.0F;
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}
