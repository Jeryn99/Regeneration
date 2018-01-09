package com.afg.regeneration.superpower;

import java.util.List;
import java.util.UUID;

import com.afg.regeneration.RegenerationMod;
import com.afg.regeneration.traits.negative.TraitClumsy;
import com.afg.regeneration.traits.negative.TraitDumb;
import com.afg.regeneration.traits.negative.TraitFlimsy;
import com.afg.regeneration.traits.negative.TraitFrail;
import com.afg.regeneration.traits.negative.TraitObvious;
import com.afg.regeneration.traits.negative.TraitRigid;
import com.afg.regeneration.traits.negative.TraitSlow;
import com.afg.regeneration.traits.negative.TraitUnhealthy;
import com.afg.regeneration.traits.negative.TraitUnlucky;
import com.afg.regeneration.traits.negative.TraitWeak;
import com.afg.regeneration.traits.positive.TraitBouncy;
import com.afg.regeneration.traits.positive.TraitLucky;
import com.afg.regeneration.traits.positive.TraitQuick;
import com.afg.regeneration.traits.positive.TraitSmart;
import com.afg.regeneration.traits.positive.TraitSneaky;
import com.afg.regeneration.traits.positive.TraitSpry;
import com.afg.regeneration.traits.positive.TraitStrong;
import com.afg.regeneration.traits.positive.TraitSturdy;
import com.afg.regeneration.traits.positive.TraitThickSkinned;
import com.afg.regeneration.traits.positive.TraitTough;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class TimelordSuperpower extends Superpower {
	private TimelordRenderHandler timelordRenderhandler;
	
	public TimelordSuperpower() {
		super("Timelord");
		setRegistryName(RegenerationMod.MODID, "timelord");
	}
	
	@Override
	public SuperpowerPlayerHandler getNewSuperpowerHandler(ISuperpowerCapability iSuperpowerCapability) {
		return new TimelordSuperpowerHandler(iSuperpowerCapability, this);
	}
	
	@Override
	protected List<Ability> addDefaultAbilities(EntityPlayer player, List<Ability> list) {
		UUID uuid = UUID.fromString("fe163548-51b9-4bb5-89d2-9283c3283f6b");
		
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
