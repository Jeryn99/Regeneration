package com.afg.regeneration;

import com.afg.regeneration.superpower.Timelord;
import com.afg.regeneration.traits.*;
import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.superpower.Superpower;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by AFlyingGrayson on 8/7/17
 */

@Mod( modid = Regeneration.MODID, name = "Regeneration", version = Regeneration.VERSION)
@Mod.EventBusSubscriber
public class Regeneration
{
	public static final String MODID = "g-regen", VERSION = "0.1";

	public static final Timelord timelord = new Timelord();

	@SubscribeEvent
	public static void onRegisterSuperpower(RegistryEvent.Register<Superpower> e)
	{
		e.getRegistry().register(timelord);
	}

	@SubscribeEvent
	public static void onRegisterAbility(RegistryEvent.Register<Ability.AbilityEntry> e)
	{
		e.getRegistry().register(new Ability.AbilityEntry(Bouncy.class, new ResourceLocation(MODID, "bouncy")));
		e.getRegistry().register(new Ability.AbilityEntry(Lucky.class, new ResourceLocation(MODID, "lucky")));
		e.getRegistry().register(new Ability.AbilityEntry(Quick.class, new ResourceLocation(MODID, "quick")));
		e.getRegistry().register(new Ability.AbilityEntry(Spry.class, new ResourceLocation(MODID, "spry")));
		e.getRegistry().register(new Ability.AbilityEntry(Strong.class, new ResourceLocation(MODID, "strong")));
		e.getRegistry().register(new Ability.AbilityEntry(Sturdy.class, new ResourceLocation(MODID, "sturdy")));
		e.getRegistry().register(new Ability.AbilityEntry(ThickSkinned.class, new ResourceLocation(MODID, "thickSkinned")));
		e.getRegistry().register(new Ability.AbilityEntry(Tough.class, new ResourceLocation(MODID, "tough")));
	}
}
