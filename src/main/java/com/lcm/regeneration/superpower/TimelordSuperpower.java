package com.lcm.regeneration.superpower;

import java.util.List;
import java.util.UUID;

import com.lcm.regeneration.RegenerationMod;
import com.lcm.regeneration.client.gui.GuiRegenCustomizer;
import com.lcm.regeneration.client.gui.GuiTimelordPowerAbilities;
import com.lcm.regeneration.traits.negative.TraitClumsy;
import com.lcm.regeneration.traits.negative.TraitDumb;
import com.lcm.regeneration.traits.negative.TraitFlimsy;
import com.lcm.regeneration.traits.negative.TraitFrail;
import com.lcm.regeneration.traits.negative.TraitObvious;
import com.lcm.regeneration.traits.negative.TraitRigid;
import com.lcm.regeneration.traits.negative.TraitSlow;
import com.lcm.regeneration.traits.negative.TraitUnhealthy;
import com.lcm.regeneration.traits.negative.TraitUnlucky;
import com.lcm.regeneration.traits.negative.TraitWeak;
import com.lcm.regeneration.traits.positive.TraitBouncy;
import com.lcm.regeneration.traits.positive.TraitLucky;
import com.lcm.regeneration.traits.positive.TraitQuick;
import com.lcm.regeneration.traits.positive.TraitSmart;
import com.lcm.regeneration.traits.positive.TraitSneaky;
import com.lcm.regeneration.traits.positive.TraitSpry;
import com.lcm.regeneration.traits.positive.TraitStrong;
import com.lcm.regeneration.traits.positive.TraitSturdy;
import com.lcm.regeneration.traits.positive.TraitThickSkinned;
import com.lcm.regeneration.traits.positive.TraitTough;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import lucraft.mods.lucraftcore.superpowers.gui.GuiCustomizer;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderer;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Created by AFlyingGrayson on 8/7/17 */
public class TimelordSuperpower extends Superpower {
	
	public static final TimelordSuperpower INSTANCE = new TimelordSuperpower();
	private TimelordRenderHandler timelordRenderhandler;
	
	public TimelordSuperpower() {
		super(StringHelper.translateToLocal("superpower.Timelord.name"));
		setRegistryName(RegenerationMod.MODID, "timelord");
	}
	
	@Override
	public SuperpowerPlayerHandler getNewSuperpowerHandler(ISuperpowerCapability iSuperpowerCapability) {
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(iSuperpowerCapability.getPlayer(), TimelordSuperpowerHandler.class);
		return (handler == null) ? new TimelordSuperpowerHandler(iSuperpowerCapability, this) : handler;
	}
	
	@Override
	protected List<Ability> addDefaultAbilities(EntityPlayer player, List<Ability> list) {
		UUID uuid = UUID.fromString("fe163548-51b9-4bb5-89d2-9283c3283f6b");
		
		// Positive
		list.add(new TraitLucky(player, uuid, 5.0f, 0));
		list.add(new TraitQuick(player, uuid, 0.075f, 0));
		list.add(new TraitStrong(player, uuid, 4.0f, 0));
		list.add(new TraitBouncy(player, uuid, 3.0f, 0));
		list.add(new TraitSpry(player, uuid, 1.0f, 0));
		list.add(new TraitSturdy(player, uuid, 3.0f, 0));
		list.add(new TraitThickSkinned(player, uuid, 4.0f, 0));
		list.add(new TraitTough(player, uuid, 6.0f, 0));
		list.add(new TraitSneaky(player));
		list.add(new TraitSmart(player));
		
		// Negative
		list.add(new TraitUnlucky(player, uuid, -5.0f, 0));
		list.add(new TraitSlow(player, uuid, -0.035f, 0));
		list.add(new TraitWeak(player, uuid, -0.25f, 0));
		list.add(new TraitRigid(player, uuid, -1.0f, 0));
		list.add(new TraitClumsy(player, uuid, -0.5f, 0));
		list.add(new TraitFlimsy(player, uuid, -3.0f, 0));
		list.add(new TraitFrail(player, uuid, -4.0f, 0));
		list.add(new TraitUnhealthy(player, uuid, -6.0f, 0));
		list.add(new TraitObvious(player));
		list.add(new TraitDumb(player));
		
		return list;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public SuperpowerRenderer.ISuperpowerRenderer getPlayerRenderer() {
		if (timelordRenderhandler == null) timelordRenderhandler = new TimelordRenderHandler();
		return timelordRenderhandler;
	}
	
	@Override
	public boolean canCustomize() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiCustomizer getCustomizerGui(EntityPlayer player) {
		return new GuiRegenCustomizer();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getAbilityGui(EntityPlayer player) {
		return new GuiTimelordPowerAbilities(player);
	}
	
	@Override
	public void renderIcon(Minecraft mc, Gui gui, int x, int y) {
		float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
		Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(2, 2, 1);
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(RegenerationMod.RegenerationItems.chameleonArch), 0, 0);
		GlStateManager.popMatrix();
		Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
	}
	
	@Override
	public NBTTagCompound getDefaultStyleTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", 1.0f);
		nbt.setFloat("PrimaryGreen", 0.78f);
		nbt.setFloat("PrimaryBlue", 0.0f);
		nbt.setFloat("SecondaryRed", 1.0f);
		nbt.setFloat("SecondaryGreen", 0.47f);
		nbt.setFloat("SecondaryBlue", 0.0f);
		return nbt;
	}
	
}
