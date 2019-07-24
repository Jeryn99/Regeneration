package me.swirtzly.regeneration.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;

import static me.suff.regeneration.util.RenderUtil.drawModelToGui;
import static me.swirtzly.regeneration.util.RenderUtil.drawModelToGui;

public class GuiSkinCustomizer extends ContainerScreen {
	
	public static final PlayerModel playerModelSteve = new PlayerModel(0.1F, false);
	public static final PlayerModel playerModelAlex = new PlayerModel(0.1F, true);
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
	private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");
	private static SkinChangingHandler.EnumChoices choices = SkinChangingHandler.wasAlex(Minecraft.getInstance().player) ? SkinChangingHandler.EnumChoices.ALEX : SkinChangingHandler.EnumChoices.STEVE;
	private float rotation = 0;
	
	public GuiSkinCustomizer() {
		super(new BlankContainer());
		xSize = 176;
		ySize = 186;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		final int btnW = 60, btnH = 18;
		rotation = 0;
		
		this.addButton(new GuiButtonExt( cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.previous").getFormattedText()) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				if (choices.previous() != null) {
					choices = (SkinChangingHandler.EnumChoices) choices.previous();
				} else {
					choices = SkinChangingHandler.EnumChoices.EITHER;
				}
				PlayerUtil.updateModel(choices);
			}
		});
		
		this.addButton(new GuiButtonExt(4, cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.next").getFormattedText()) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				if (choices.next() != null) {
					choices = (SkinChangingHandler.EnumChoices) choices.next();
				} else {
					choices = SkinChangingHandler.EnumChoices.ALEX;
				}
				PlayerUtil.updateModel(choices);
			}
		});
		
		this.addButton(new GuiButtonExt(98, cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.back").getFormattedText()) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				Minecraft.getInstance().displayGuiScreen(new GuiCustomizer());
			}
		});
		
		this.addButton(new GuiButtonExt(99, cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.open_folder").getFormattedText()) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				Util.getOSType().openFile(SkinChangingHandler.SKIN_DIRECTORY);
			}
		});
		
		CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).ifPresent((cap) -> choices = cap.getPreferredModel());
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.renderBackground();
		Minecraft.getInstance().getTextureManager().bindTexture(background);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		GlStateManager.pushMatrix();
		playerModelAlex.isChild = false;
		playerModelSteve.isChild = false;
		switch (choices) {
			case ALEX:
				Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_ALEX);
				drawModelToGui(playerModelAlex, width / 2, height / 2 - 40, 1.0f, rotation);
				break;
			case STEVE:
				Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_STEVE);
				drawModelToGui(playerModelSteve, width / 2, height / 2 - 40, 1.0f, rotation);
				break;
			case EITHER:
				Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_ALEX);
				drawModelToGui(playerModelAlex, width / 2 - 40, height / 2 - 40, 1.0f, rotation);
				Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_STEVE);
				drawModelToGui(playerModelSteve, width / 2 + 40, height / 2 - 40, 1.0f, rotation);
				break;
		}
		GlStateManager.popMatrix();
		drawCenteredString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent("regeneration.gui.preference_model", choices.name()).getUnformattedComponentText(), width / 2, height / 2 + 15, Color.WHITE.getRGB());
	}
	
	@Override
	public void tick() {
		super.tick();
		rotation++;
		if (rotation > 360) {
			rotation = 0;
		}
	}
}
