package me.suff.regeneration.client.gui;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiSkinCustomizer extends GuiContainer {
	
	public static final ModelPlayer playerModelSteve = new ModelPlayer(0.1F, false);
	public static final ModelPlayer playerModelAlex = new ModelPlayer(0.1F, true);
	public static final int ID = 1;
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
	private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");
	private static SkinChangingHandler.EnumChoices choices = ClientUtil.isSlimSkin(Minecraft.getMinecraft().player.getUniqueID()) ? SkinChangingHandler.EnumChoices.ALEX : SkinChangingHandler.EnumChoices.STEVE;
	private float rotation = 0;
	
	public GuiSkinCustomizer() {
		super(new BlankContainer());
		xSize = 176;
		ySize = 186;
	}
	
	public static void drawModelToGui(ModelBase model, int xPos, int yPos, float par2, float rotation) {
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GlStateManager.translate(xPos, yPos, 100);
		GlStateManager.rotate(-25, 1, 0, 0);
		GlStateManager.rotate(rotation, 0, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();
		
		GlStateManager.glLightModel(2899, RenderHelper.setColorBuffer(0.75F, 0.75F, 0.75F, 1F));
		GlStateManager.scale(38 * par2, 34 * par2, 38 * par2);
		GlStateManager.scale(-1, 1, 1);
		model.render(Minecraft.getMinecraft().player, 0, 0, Minecraft.getMinecraft().player.ticksExisted, 0, 0, 0.0625f);
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GlStateManager.popMatrix();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		final int btnW = 60, btnH = 18;
		rotation = 0;
		GuiButtonExt btnNext = new GuiButtonExt(1, cx + 25, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.previous").getFormattedText());
		GuiButtonExt btnPrevious = new GuiButtonExt(4, cx + 90, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.next").getFormattedText());
		GuiButtonExt btnBack = new GuiButtonExt(98, cx + 25, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.back").getFormattedText());
		GuiButtonExt btnOpenFolder = new GuiButtonExt(99, cx + 90, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.open_folder").getFormattedText());
		
		buttonList.add(btnNext);
		buttonList.add(btnPrevious);
		buttonList.add(btnOpenFolder);
		buttonList.add(btnBack);
		
		choices = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getPreferredModel();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		GlStateManager.pushMatrix();
		switch (choices) {
			case ALEX:
				playerModelAlex.isChild = false;
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_ALEX);
				drawModelToGui(playerModelAlex, width / 2, height / 2 - 40, 1.0f, rotation);
				break;
			case STEVE:
				playerModelSteve.isChild = false;
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_STEVE);
				drawModelToGui(playerModelSteve, width / 2, height / 2 - 40, 1.0f, rotation);
				break;
			case EITHER:
				break;
		}
		GlStateManager.popMatrix();
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		switch (button.id) {
			case 98:
				Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiCustomizer.ID, Minecraft.getMinecraft().world, 0, 0, 0);
				break;
			
			case 4:
				//Next
				if (choices.previous() != null) {
					choices = (SkinChangingHandler.EnumChoices) choices.previous();
				} else {
					choices = SkinChangingHandler.EnumChoices.EITHER;
				}
				PlayerUtil.updateModel(choices);
				break;
			
			case 1:
				//Previous
				if (choices.next() != null) {
					choices = (SkinChangingHandler.EnumChoices) choices.next();
				} else {
					choices = SkinChangingHandler.EnumChoices.ALEX;
				}
				PlayerUtil.updateModel(choices);
				break;
			
			case 99:
				try {
					Desktop.getDesktop().open(SkinChangingHandler.SKIN_DIRECTORY);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.pushMatrix();
		rotation++;
		if (rotation > 360) {
			rotation = 0;
		}
		GlStateManager.popMatrix();
	}
	
}
