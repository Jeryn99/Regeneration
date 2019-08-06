package me.swirtzly.regeneration.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.ContainerBlank;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;

import static me.swirtzly.regeneration.util.RenderUtil.drawModelToGui;

public class SkinTypeScreen extends ContainerScreen {
	
	public static final PlayerModel playerModelSteve = new PlayerModel(0.1F, false);
	public static final PlayerModel playerModelAlex = new PlayerModel(0.1F, true);
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
	private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");
	private static SkinManipulation.EnumChoices choices = null;
	private float rotation = 0;

	public SkinTypeScreen() {
        super(new ContainerBlank(), null, new TranslationTextComponent("Regeneration Skin Screen"));
		xSize = 176;
		ySize = 186;
	}
	
	@Override
    public void init() {
        super.init();

		RegenCap.get(minecraft.player).ifPresent((data) -> choices = data.getPreferredModel());

		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		final int btnW = 60, btnH = 18;
		rotation = 0;

        this.addButton(new GuiButtonExt(cx + 25, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.previous").getFormattedText(), button -> {
            if (choices.previous() != null) {
				choices = (SkinManipulation.EnumChoices) choices.previous();
            } else {
				choices = SkinManipulation.EnumChoices.EITHER;
            }
            PlayerUtil.updateModel(choices);
        }));

        this.addButton(new GuiButtonExt(cx + 90, cy + 125, btnW, btnH, new TranslationTextComponent("regeneration.gui.next").getFormattedText(), button -> {
            if (choices.next() != null) {
				choices = (SkinManipulation.EnumChoices) choices.next();
            } else {
				choices = SkinManipulation.EnumChoices.ALEX;
            }
            PlayerUtil.updateModel(choices);
        }));

		this.addButton(new GuiButtonExt(cx + 25, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.back").getFormattedText(), button -> Minecraft.getInstance().displayGuiScreen(new ColorScreen())));


		this.addButton(new GuiButtonExt(cx + 90, cy + 145, btnW, btnH, new TranslationTextComponent("regeneration.gui.skin_choice").getFormattedText(), new Button.IPressable() {
			@Override
            public void onPress(Button button) {
				Minecraft.getInstance().displayGuiScreen(new SkinChoiceScreen());
			}
        }));

        RegenCap.get(Minecraft.getInstance().player).ifPresent((cap) -> choices = cap.getPreferredModel());
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

	@Override
	public String getNarrationMessage() {
		return "Regeneration Skin Screen";
	}
}
