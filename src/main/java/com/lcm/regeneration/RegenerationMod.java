package com.lcm.regeneration;

import java.lang.reflect.Field;

import com.lcm.regeneration.client.animation.PlayerRenderHandler;
import com.lcm.regeneration.items.ItemChameleonArch;
import com.lcm.regeneration.superpower.TimelordSuperpower;
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

@Mod(modid = RegenerationMod.MODID, name = "Regeneration", version = RegenerationMod.VERSION, dependencies = "required-after:lucraftcore@[1.12-2.0.3,)", acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2")
@Mod.EventBusSubscriber
public class RegenerationMod {
	public static final String MODID = "lcm-regen", VERSION = "1.3";
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide().equals(Side.CLIENT)) MinecraftForge.EVENT_BUS.register(new PlayerRenderHandler());
	}
	
	@GameRegistry.ObjectHolder(RegenerationMod.MODID)
	public static class RegenerationItems {
		public static final Item chameleonArch = null;
	}
	
	@SubscribeEvent
	public static void onRegisterSuperpower(RegistryEvent.Register<Superpower> e) {
		e.getRegistry().register(TimelordSuperpower.instance);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) throws Exception {
		event.getRegistry().register(new ItemChameleonArch());
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) throws Exception {
		for (Field f : RegenerationItems.class.getDeclaredFields()) {
			Item item = (Item) f.get(null);
			ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		}
	}
	
	@SubscribeEvent
	public static void loot(LootTableLoadEvent e) {
		if (!e.getName().toString().toLowerCase().contains("minecraft:chests/")) return;
		
		LootPool pool = e.getTable().getPool("main");
		LootCondition[] chance = { new RandomChance(0.5F) };
		LootFunction[] count = { new SetCount(chance, new RandomValueRange(1.0F, 1.0F)) };
		LootEntryItem item = new LootEntryItem(RegenerationItems.chameleonArch, 10, 1, count, chance, "symbol_" + RegenerationItems.chameleonArch.getUnlocalizedName());
		pool.addEntry(item);
	}
	
	@SubscribeEvent
	public static void onRegisterAbility(RegistryEvent.Register<Ability.AbilityEntry> e) {
		// Positive
		registerAbility(e, TraitBouncy.class, "bouncy");
		registerAbility(e, TraitLucky.class, "lucky");
		registerAbility(e, TraitQuick.class, "quick");
		registerAbility(e, TraitSpry.class, "spry");
		registerAbility(e, TraitStrong.class, "strong");
		registerAbility(e, TraitSturdy.class, "sturdy");
		registerAbility(e, TraitThickSkinned.class, "thickSkinned");
		registerAbility(e, TraitTough.class, "tough");
		registerAbility(e, TraitSmart.class, "smart");
		registerAbility(e, TraitSneaky.class, "sneaky");
		
		// Negative
		registerAbility(e, TraitClumsy.class, "clumsy");
		registerAbility(e, TraitFlimsy.class, "flimsy");
		registerAbility(e, TraitFrail.class, "frail");
		registerAbility(e, TraitRigid.class, "rigid");
		registerAbility(e, TraitSlow.class, "slow");
		registerAbility(e, TraitUnhealthy.class, "unhealthy");
		registerAbility(e, TraitUnlucky.class, "unlucky");
		registerAbility(e, TraitWeak.class, "weak");
		registerAbility(e, TraitDumb.class, "dumb");
		registerAbility(e, TraitObvious.class, "obvious");
	}
	
	private static void registerAbility(RegistryEvent.Register<Ability.AbilityEntry> event, Class<? extends Ability> ability, String name) {
		event.getRegistry().register(new Ability.AbilityEntry(ability, new ResourceLocation(RegenerationMod.MODID, name)));
	}
}
