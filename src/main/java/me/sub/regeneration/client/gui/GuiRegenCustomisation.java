package me.sub.regeneration.client.gui;

import java.io.IOException;

import me.sub.regeneration.Regeneration;
import me.sub.regeneration.common.capability.CapabilityRegeneration;
import me.sub.regeneration.networking.RNetwork;
import me.sub.regeneration.networking.packets.MessageRegenerationStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiRegenCustomisation extends GuiContainer implements GuiSlider.ISlider {
	
	public static ResourceLocation DEFAULT_TEX = new ResourceLocation(Regeneration.MODID, "textures/gui/customizer.png");
	
	public GuiRegenCustomisation() {
		super(new Blankcontainer());
	}

	public float primaryRed;
	public float primaryGreen;
	public float primaryBlue;
	public float secondaryRed;
	public float secondaryGreen;
	public float secondaryBlue;
	public boolean textured;
	public GuiButton texturedButton;
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.xSize = 256;
		this.ySize = 189;
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		NBTTagCompound old = Minecraft.getMinecraft().player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null).getStyle();
		primaryRed = old.getFloat("PrimaryRed");
		primaryGreen = old.getFloat("PrimaryGreen");
		primaryBlue = old.getFloat("PrimaryBlue");
		secondaryRed = old.getFloat("SecondaryRed");
		secondaryGreen = old.getFloat("SecondaryGreen");
		secondaryBlue = old.getFloat("SecondaryBlue");
		textured = old.getBoolean("textured");
		
		this.buttonList.add(new GuiButtonExt(0, i + 4, j + 167, 50, 18, I18n.translateToLocal("lcm-regen.info.save")));
		this.buttonList.add(new GuiButtonExt(1, i + 202, j + 167, 50, 18, I18n.translateToLocal("gui.cancel")));
		this.texturedButton = new GuiButton(2, i + this.xSize/2 - 25, j + 45, 50, 20, I18n.translateToLocal(""));
		this.buttonList.add(texturedButton);
		
		this.buttonList.add(new GuiColorSlider(6, i + 20, j + 90, 80, 20, I18n.translateToLocal("lcm-regen.info.red"), "", 0, 1, primaryRed, true, true, this));
		this.buttonList.add(new GuiColorSlider(7, i + 20, j + 110, 80, 20, I18n.translateToLocal("lcm-regen.info.green"), "", 0, 1, primaryGreen, true, true, this));
		this.buttonList.add(new GuiColorSlider(8, i + 20, j + 130, 80, 20, I18n.translateToLocal("lcm-regen.info.blue"), "", 0, 1, primaryBlue, true, true, this));
		
		this.buttonList.add(new GuiColorSlider(9, i + 135, j + 90, 80, 20, I18n.translateToLocal("lcm-regen.info.red"), "", 0, 1, secondaryRed, true, true, this));
		this.buttonList.add(new GuiColorSlider(10, i + 135, j + 110, 80, 20, I18n.translateToLocal("lcm-regen.info.green"), "", 0, 1, secondaryGreen, true, true, this));
		this.buttonList.add(new GuiColorSlider(11, i + 135, j + 130, 80, 20, I18n.translateToLocal("lcm-regen.info.blue"), "", 0, 1, secondaryBlue, true, true, this));
	}
	

	public NBTTagCompound getStyleNBTTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", primaryRed);
		nbt.setFloat("PrimaryGreen", primaryGreen);
		nbt.setFloat("PrimaryBlue", primaryBlue);
		nbt.setFloat("SecondaryRed", secondaryRed);
		nbt.setFloat("SecondaryGreen", secondaryGreen);
		nbt.setFloat("SecondaryBlue", secondaryBlue);
		nbt.setBoolean("textured", textured);
		return nbt;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
			
		mc.getTextureManager().bindTexture(DEFAULT_TEX);
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
		String name = I18n.translateToLocal("lcm-regen.info.textured");
		int length = mc.fontRenderer.getStringWidth(name);
		this.drawString(mc.fontRenderer, name, i + this.xSize/2 - length/2, j + 30, 0xffffff);
		
		this.texturedButton.displayString = (textured) ? "Yes" : "No";
		
		name = I18n.translateToLocal("lcm-regen.info.primary");
		length = mc.fontRenderer.getStringWidth(name);
		this.drawString(mc.fontRenderer, name, i + 70 - length / 2, j + 75, 0xffffff);
		
		name = I18n.translateToLocal("lcm-regen.info.secondary");
		length = mc.fontRenderer.getStringWidth(name);
		this.drawString(mc.fontRenderer, name, i + 185 - length / 2, j + 75, 0xffffff);
		
		drawRect(i + 99, j + 90, i + 121, j + 150, 0.1F, 0.1F, 0.1F, 1);
		drawRect(i + 100, j + 91, i + 120, j + 149, primaryRed, primaryGreen, primaryBlue, 1);
		
		drawRect(i + 214, j + 90, i + 236, j + 150, 0.1F, 0.1F, 0.1F, 1);
		drawRect(i + 215, j + 91, i + 235, j + 149, secondaryRed, secondaryGreen, secondaryBlue, 1);
		
		this.drawString(mc.fontRenderer, I18n.translateToLocal("lcm-regen.info.customizer"), i + 5, j + 5, 0xffffff);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			sendStyleNBTTagToServer();
			mc.player.closeScreen();
		}
		if (button.id == 1) mc.player.closeScreen();
		if (button.id == 2) textured = !textured;
	}
	
	private void sendStyleNBTTagToServer() {
		RNetwork.INSTANCE.sendToServer(new MessageRegenerationStyle(getStyleNBTTag()));
	}
	
	@Override
	public void onChangeSliderValue(GuiSlider slider) {
		if (slider.id == 6)
			this.primaryRed = (float) slider.sliderValue;
		else if (slider.id == 7)
			this.primaryGreen = (float) slider.sliderValue;
		else if (slider.id == 8)
			this.primaryBlue = (float) slider.sliderValue;
		else if (slider.id == 9)
			this.secondaryRed = (float) slider.sliderValue;
		else if (slider.id == 10)
			this.secondaryGreen = (float) slider.sliderValue;
		else if (slider.id == 11) this.secondaryBlue = (float) slider.sliderValue;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	
	public static void drawRect(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
		if (left < right) {
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			int j = top;
			top = bottom;
			bottom = j;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
		worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
		worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
		worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	
}