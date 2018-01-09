package com.afg.regeneration;

import java.lang.reflect.Field;

import com.afg.regeneration.client.animation.PlayerRenderHandler;
import com.afg.regeneration.items.ChameleonArch;
import com.afg.regeneration.superpower.Timelord;
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
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by AFlyingGrayson on 8/7/17
 */

@Mod(modid = Regeneration.MODID, name = "Regeneration", version = Regeneration.VERSION, dependencies = "required-after:lucraftcore@[1.12-1.2.0,)", acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2")
@Mod.EventBusSubscriber
public class Regeneration {
	public static final String MODID = "g-regen", VERSION = "1.0";
	
	public static final Timelord timelord = new Timelord();
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide().equals(Side.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new PlayerRenderHandler());
		}
	}
	
	@GameRegistry.ObjectHolder(Regeneration.MODID)
	public static class Items {
		public static final Item chameleonArch = null;
	}
	
	@SubscribeEvent
	public static void onRegisterSuperpower(RegistryEvent.Register<Superpower> e) {
		e.getRegistry().register(Regeneration.timelord);
	}
	
	@SubscribeEvent
	public static void onRegisterAbility(RegistryEvent.Register<Ability.AbilityEntry> e) {
		// Positive
		
		e.getRegistry().register(new Ability.AbilityEntry(Bouncy.class, new ResourceLocation(Regeneration.MODID, "bouncy")));
		e.getRegistry().register(new Ability.AbilityEntry(Lucky.class, new ResourceLocation(Regeneration.MODID, "lucky")));
		e.getRegistry().register(new Ability.AbilityEntry(Quick.class, new ResourceLocation(Regeneration.MODID, "quick")));
		e.getRegistry().register(new Ability.AbilityEntry(Spry.class, new ResourceLocation(Regeneration.MODID, "spry")));
		e.getRegistry().register(new Ability.AbilityEntry(Strong.class, new ResourceLocation(Regeneration.MODID, "strong")));
		e.getRegistry().register(new Ability.AbilityEntry(Sturdy.class, new ResourceLocation(Regeneration.MODID, "sturdy")));
		e.getRegistry().register(new Ability.AbilityEntry(ThickSkinned.class, new ResourceLocation(Regeneration.MODID, "thickSkinned")));
		e.getRegistry().register(new Ability.AbilityEntry(Tough.class, new ResourceLocation(Regeneration.MODID, "tough")));
		e.getRegistry().register(new Ability.AbilityEntry(Smart.class, new ResourceLocation(Regeneration.MODID, "smart")));
		e.getRegistry().register(new Ability.AbilityEntry(Sneaky.class, new ResourceLocation(Regeneration.MODID, "sneaky")));
		
		// Negative
		
		e.getRegistry().register(new Ability.AbilityEntry(Clumsy.class, new ResourceLocation(Regeneration.MODID, "clumsy")));
		e.getRegistry().register(new Ability.AbilityEntry(Flimsy.class, new ResourceLocation(Regeneration.MODID, "flimsy")));
		e.getRegistry().register(new Ability.AbilityEntry(Frail.class, new ResourceLocation(Regeneration.MODID, "frail")));
		e.getRegistry().register(new Ability.AbilityEntry(Rigid.class, new ResourceLocation(Regeneration.MODID, "rigid")));
		e.getRegistry().register(new Ability.AbilityEntry(Slow.class, new ResourceLocation(Regeneration.MODID, "slow")));
		e.getRegistry().register(new Ability.AbilityEntry(Unhealthy.class, new ResourceLocation(Regeneration.MODID, "unhealthy")));
		e.getRegistry().register(new Ability.AbilityEntry(Unlucky.class, new ResourceLocation(Regeneration.MODID, "unlucky")));
		e.getRegistry().register(new Ability.AbilityEntry(Weak.class, new ResourceLocation(Regeneration.MODID, "weak")));
		e.getRegistry().register(new Ability.AbilityEntry(Dumb.class, new ResourceLocation(Regeneration.MODID, "dumb")));
		e.getRegistry().register(new Ability.AbilityEntry(Obvious.class, new ResourceLocation(Regeneration.MODID, "obvious")));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) throws Exception {
		event.getRegistry().register(new ChameleonArch());
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) throws Exception {
		for (Field f : Items.class.getDeclaredFields()) {
			Item item = (Item) f.get(null);
			ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		}
	}
	
	@SubscribeEvent
	public static void loot(LootTableLoadEvent e) {
		if (e.getName().toString().toLowerCase().contains("minecraft:chests/")) {
			LootPool pool = e.getTable().getPool("main");
			LootCondition[] chance = { new RandomChance(0.5F) };
			LootFunction[] count = { new SetCount(chance, new RandomValueRange(1.0F, 1.0F)) };
			LootEntryItem item = new LootEntryItem(Items.chameleonArch, 10, 1, count, chance, "symbol_" + Items.chameleonArch.getUnlocalizedName());
			pool.addEntry(item);
		}
	}
}
