package micdoodle8.mods.galacticraft.api.client.tabs;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.lcm.regeneration.client.gui.GuiRegenCustomisation;
import com.lcm.regeneration.events.RObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class InventoryTabRegeneration extends AbstractTab {
	private static final ModelPlayer biped = new ModelPlayer(0, true);
	
	public InventoryTabRegeneration() {
		super(0, 0, 0, new ItemStack(RObjects.Items.chameleonArch, 1, 3));
		displayString = I18n.translateToLocal("menu.regeneration");
	}

	@Override
	public void onTabClicked() {
		Minecraft.getMinecraft().addScheduledTask(() -> {
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new GuiRegenCustomisation());
        });
	}

	@Override
	public boolean shouldAddToList() {
		return true;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (!visible){
            super.drawButton(minecraft, mouseX, mouseY, partialTicks);
            return;
        }
      
        if(enabled){
	        Minecraft mc = Minecraft.getMinecraft();
	        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	        if(hovered){
	        	int x = mouseX + mc.fontRenderer.getStringWidth(displayString);
	        	GlStateManager.translate(x, y + 2, 0);
	        	drawHoveringText(Arrays.asList(new String[] {displayString}), 0, 0, mc.fontRenderer);
	        	GlStateManager.translate(-x, -(y + 2), 0);
	        }
        }
        super.drawButton(minecraft, mouseX, mouseY, partialTicks);
    }

	 private void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder vertexBuffer = tessellator.getBuffer();
	        for (int i = 0; i < 8; i++) {
	            GlStateManager.pushMatrix();
	            GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
	            GlStateManager.scale(1.0f, 1.0f, 0.65f);
	            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
	            vertexBuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
	            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
	            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
	            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
	            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
	            tessellator.draw();
	            GlStateManager.popMatrix();
	        }
	    }
	
    protected void drawHoveringText(List list, int x, int y, FontRenderer font){
        if (list.isEmpty())
        	return;
        
    	GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int k = 0;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()){
            String s = (String)iterator.next();
            int l = font.getStringWidth(s);

            if (l > k){
                k = l;
            }
        }

        int j2 = x + 12;
        int k2 = y - 12;
        int i1 = 8;

        if (list.size() > 1){
            i1 += 2 + (list.size() - 1) * 10;
        }

        if (j2 + k > this.width){
            j2 -= 28 + k;
        }

        if (k2 + i1 + 6 > this.height){
            k2 = this.height - i1 - 6;
        }

        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
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

        for (int i2 = 0; i2 < list.size(); ++i2){
            String s1 = (String)list.get(i2);
            font.drawStringWithShadow(s1, j2, k2, -1);

            if (i2 == 0){
                k2 += 2;
            }

            k2 += 10;
        }

        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}