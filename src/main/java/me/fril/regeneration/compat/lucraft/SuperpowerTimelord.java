package me.fril.regeneration.compat.lucraft;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static me.fril.regeneration.compat.lucraft.LCCoreBarEntry.ICON_TEX;

public class SuperpowerTimelord extends Superpower {
	
	public SuperpowerTimelord(String name) {
		super(name);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getCapsuleColor() {
		return 16745472;
	}
	
	@Override
	public void renderIcon(Minecraft mc, Gui gui, int x, int y) {
		GlStateManager.pushMatrix();
		mc.renderEngine.bindTexture(ICON_TEX);
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(2, 2, 0);
		gui.drawTexturedModalRect(0, 0, 9 * 16, 16, 16, 16);
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean shouldAppearInHeroGuideBook() {
		return false;
	}
	
	@Override
	public NBTTagCompound getData() {
		return super.getData();
	}
	
	
	@Override
	public SuperpowerPlayerHandler getNewSuperpowerHandler(ISuperpowerCapability cap) {
		return new SuperpowerPlayerHandler(cap, this) {
			
			
			@Override
			public void onApplyPower() {
				super.onApplyPower();
				IRegeneration data = CapabilityRegeneration.getForPlayer(getPlayer());
				if (!getPlayer().world.isRemote) {
					if (data.getReserve() <= 0) {
						CapabilityRegeneration.getForPlayer(getPlayer()).receiveRegenerations(RegenConfig.regenCapacity);
					} else {
						CapabilityRegeneration.getForPlayer(getPlayer()).receiveRegenerations(data.getReserve());
						data.setReserve(0);
					}
				}
			}
			
			// When superpower gets removed, remove all left regenerations
			@Override
			public void onRemove() {
				IRegeneration data = CapabilityRegeneration.getForPlayer(getPlayer());
				if (!getPlayer().world.isRemote) {
					data.setReserve(data.getRegenerationsLeft());
					data.extractRegeneration(CapabilityRegeneration.getForPlayer(getPlayer()).getRegenerationsLeft());
				}
			}
		};
	}
}
