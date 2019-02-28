package me.suff.regeneration.client.gui;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.util.RegenState;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class ToastRegeneration implements IToast {
	
	private final String title, subtitle;
	private RegenState visibility = RegenState.REGENERATING;
	private ItemStack itemStack;
	
	public ToastRegeneration(String titleComponent, @Nullable String subtitleComponent, RegenState regenState) {
		this.title = new TextComponentTranslation(titleComponent).getFormattedText();
		this.subtitle = subtitleComponent == null ? null : new TextComponentTranslation(subtitleComponent).getFormattedText();
		itemStack = new ItemStack(RegenObjects.Items.FOB_WATCH);
		visibility = regenState;
	}
	
	public ToastRegeneration(ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent, RegenState regenState) {
		this.title = titleComponent.getFormattedText();
		this.subtitle = subtitleComponent == null ? null : subtitleComponent.getFormattedText();
		itemStack = new ItemStack(RegenObjects.Items.FOB_WATCH);
		visibility = regenState;
	}
	
	@Override
	public IToast.Visibility draw(GuiToast toastGui, long delta) {
		toastGui.getInstance().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F);
		toastGui.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
		
		if (this.subtitle == null) {
			toastGui.getInstance().fontRenderer.drawString(this.title, 30, 12, -11534256);
		} else {
			toastGui.getInstance().fontRenderer.drawString(this.title, 30, 7, -11534256);
			toastGui.getInstance().fontRenderer.drawString(this.subtitle, 30, 18, -16777216);
		}
		
		boolean visible = CapabilityRegeneration.getForPlayer(toastGui.getInstance().player).getState() == visibility;
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.scale(1, 1, 1);
		toastGui.getInstance().getRenderItem().renderItemAndEffectIntoGUI(null, itemStack, 8, 8);
		return visible ? Visibility.SHOW : Visibility.HIDE;
	}
	
	@Override
	public Object getType() {
		return null;
	}
}
