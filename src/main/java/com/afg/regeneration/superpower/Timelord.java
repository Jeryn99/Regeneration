package com.afg.regeneration.superpower;

import java.util.List;
import java.util.UUID;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.traits.negative.Clumsy;
import com.afg.regeneration.traits.negative.Dumb;
import com.afg.regeneration.traits.negative.Flimsy;
import com.afg.regeneration.traits.negative.Frail;
import com.afg.regeneration.traits.negative.Obvious;
import com.afg.regeneration.traits.negative.Rigid;
import com.afg.regeneration.traits.negative.Slow;
import com.afg.regeneration.traits.negative.Unhealthy;
import com.afg.regeneration.traits.negative.Unlucky;
import com.afg.regeneration.traits.negative.Weak;
import com.afg.regeneration.traits.positive.Bouncy;
import com.afg.regeneration.traits.positive.Lucky;
import com.afg.regeneration.traits.positive.Quick;
import com.afg.regeneration.traits.positive.Smart;
import com.afg.regeneration.traits.positive.Sneaky;
import com.afg.regeneration.traits.positive.Spry;
import com.afg.regeneration.traits.positive.Strong;
import com.afg.regeneration.traits.positive.Sturdy;
import com.afg.regeneration.traits.positive.ThickSkinned;
import com.afg.regeneration.traits.positive.Tough;

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
public class Timelord extends Superpower {
	private TimelordRenderhandler timelordRenderhandler;
	
	public Timelord() {
		super("Timelord");
		setRegistryName(Regeneration.MODID, "timelord");
	}
	
	@Override
	public SuperpowerPlayerHandler getNewSuperpowerHandler(ISuperpowerCapability iSuperpowerCapability) {
		return new TimelordHandler(iSuperpowerCapability, this);
	}
	
	@Override
	protected List<Ability> addDefaultAbilities(EntityPlayer player, List<Ability> list) {
		UUID uuid = UUID.fromString("fe163548-51b9-4bb5-89d2-9283c3283f6b");
		
		list.add(new Lucky(player, uuid, 5.0f, 0));
		list.add(new Quick(player, uuid, 0.075f, 0));
		list.add(new Strong(player, uuid, 4.0f, 0));
		list.add(new Bouncy(player, uuid, 3.0f, 0));
		list.add(new Spry(player, uuid, 1.0f, 0));
		list.add(new Sturdy(player, uuid, 3.0f, 0));
		list.add(new ThickSkinned(player, uuid, 4.0f, 0));
		list.add(new Tough(player, uuid, 6.0f, 0));
		list.add(new Sneaky(player));
		list.add(new Smart(player));
		
		list.add(new Unlucky(player, uuid, -5.0f, 0));
		list.add(new Slow(player, uuid, -0.035f, 0));
		list.add(new Weak(player, uuid, -0.25f, 0));
		list.add(new Rigid(player, uuid, -1.0f, 0));
		list.add(new Clumsy(player, uuid, -0.5f, 0));
		list.add(new Flimsy(player, uuid, -3.0f, 0));
		list.add(new Frail(player, uuid, -4.0f, 0));
		list.add(new Unhealthy(player, uuid, -6.0f, 0));
		list.add(new Obvious(player));
		list.add(new Dumb(player));
		
		return list;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public SuperpowerRenderer.ISuperpowerRenderer getPlayerRenderer() {
		if (timelordRenderhandler == null) timelordRenderhandler = new TimelordRenderhandler();
		return timelordRenderhandler;
	}
}
