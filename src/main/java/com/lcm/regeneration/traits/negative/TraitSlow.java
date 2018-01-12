package com.lcm.regeneration.traits.negative;

import com.lcm.regeneration.RegenerationMod;
import com.lcm.regeneration.traits.positive.TraitQuick;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAttributeModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class TraitSlow extends AbilityAttributeModifier implements INegativeTrait {
	
	public TraitSlow(EntityPlayer player, UUID uuid, float factor, int operation) {
		super(player, uuid, factor, operation);
	}
	
	@Override
	public IAttribute getAttribute() {
		return SharedMonsterAttributes.MOVEMENT_SPEED;
	}
	
	@Override
	public Class<? extends Ability> getPositiveTrait() {
		return TraitQuick.class;
	}

	@SideOnly(Side.CLIENT)
	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		mc.renderEngine.bindTexture(RegenerationMod.ICONS);
		gui.drawTexturedModalRect(x, y, 16, 0, 16, 16);
	}
}
