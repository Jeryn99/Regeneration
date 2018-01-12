package com.lcm.regeneration.superpower;

import com.lcm.regeneration.RegenerationMod;
import com.lcm.regeneration.traits.negative.*;
import com.lcm.regeneration.traits.positive.*;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class TimelordSuperpower extends Superpower {

	public static final TimelordSuperpower INSTANCE = new TimelordSuperpower();

	private TimelordRenderHandler timelordRenderhandler;

	public TimelordSuperpower() {
		super("Timelord");
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
}
