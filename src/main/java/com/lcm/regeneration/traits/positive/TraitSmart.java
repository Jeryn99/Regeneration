package com.lcm.regeneration.traits.positive;

import com.lcm.regeneration.RegenerationMod;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by AFlyingGrayson on 9/3/17
 * @formatter:off
 */
@Mod.EventBusSubscriber
public class TraitSmart extends AbilityConstant {
	
	public TraitSmart(EntityPlayer player) {
		super(player);
	}
	
	@SubscribeEvent
	public static void onExperienceGain(PlayerPickupXpEvent event) {
		if (SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()) == null) return;
		List<Ability> abilityList = SuperpowerHandler.getSuperpowerPlayerHandler(event.getEntityPlayer()).getAbilities();
		if (abilityList == null) return;
		
		for (Ability ability : abilityList) if (ability instanceof TraitSmart && ability.isUnlocked()) {
			event.getOrb().xpValue *= 1.5;
		}
	}
	
	@Override
	public boolean showInAbilityBar() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
		mc.renderEngine.bindTexture(RegenerationMod.ICONS);
		gui.drawTexturedModalRect(x, y, 0, 0, 16, 16);
	}
	
	@Override public void updateTick() {}
}
