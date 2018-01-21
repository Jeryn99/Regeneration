package com.lcm.regeneration.client.gui;

import com.lcm.regeneration.RegenerationMod;
import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.gui.GuiSuperpowerAbilities;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class GuiTimelordPowerTab extends GuiSuperpowerAbilities {
	
	public GuiTimelordPowerTab(EntityPlayer player) {
		super(player);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.removeIf(button -> button.displayString.equals("?"));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		if (RegenerationMod.getConfig().disableTraits) {
			String txtDisabled = StringHelper.translateToLocal("lcm-regen.messages.traitsDisabled");
			int xDisabled = this.xSize / 2 - mc.fontRenderer.getStringWidth(txtDisabled) / 2;
			mc.fontRenderer.drawString(TextFormatting.BOLD.toString() + TextFormatting.RED + txtDisabled, i + xDisabled, j + ySize/2, 0x373737);
		}
		
		String txtCount = StringHelper.translateToLocal("lcm-regen.messages.regenLeft", ((TimelordSuperpowerHandler)SuperpowerHandler.getSuperpowerPlayerHandler(player)).regenerationsLeft);
		int xCount = (this.width - i) - mc.fontRenderer.getStringWidth(txtCount) - 20;
		mc.fontRenderer.drawString(TextFormatting.DARK_GRAY + txtCount, xCount, j + 165 + 5, 0x373737);
	}
	
}
