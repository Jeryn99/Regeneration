package com.lcm.regeneration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import com.lcm.regeneration.init.RegenItems;
import com.lcm.regeneration.superpower.TimelordSuperpower;
import com.lcm.regeneration.traits.negative.*;
import com.lcm.regeneration.traits.positive.*;
import com.lcm.regeneration.util.CmdRegenDebug;

import lucraft.mods.lucraftcore.LCConfig;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Created by AFlyingGrayson on 8/7/17 */
@Mod(modid = Regeneration.MODID, name = "Regeneration", version = Regeneration.VERSION, dependencies = "required:forge@[14.23.1.2574,); required-after:lucraftcore@[1.12-2.0.4,)", acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2")
@EventBusSubscriber
public class Regeneration {
	public static final String MODID = "lcm-regen", VERSION = "2.0";
	public static final ResourceLocation ICONS = new ResourceLocation(MODID, "textures/gui/ability_icons.png");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		RegenConfig.init(new Configuration(e.getSuggestedConfigurationFile()), e.getSide());
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CmdRegenDebug());
	}
	
	@SubscribeEvent
	public static void onRegisterSuperpower(RegistryEvent.Register<Superpower> e) {
		e.getRegistry().register(TimelordSuperpower.INSTANCE);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) throws Exception {
		for (Field f : RegenItems.class.getDeclaredFields()) {
			Item item = (Item) f.get(null);
			ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		}
	}
	
	@SubscribeEvent
	public static void registerLoot(LootTableLoadEvent e) { //TODO can this loot table actually be overriden in resource packs?
		if (!e.getName().toString().toLowerCase().matches(RegenConfig.lootRegex) || RegenConfig.disableArch) return;
		
		LootCondition[] condAlways = new LootCondition[] { new RandomChance(1F) };
		LootEntry entry = new LootEntryTable(new ResourceLocation(MODID + ":inject/arch_loot"), 1, 1, condAlways, "lcm-regen:arch-entry");
		LootPool lootPool = new LootPool(new LootEntry[] { entry }, condAlways, new RandomValueRange(1), new RandomValueRange(1), "lcm-regen:arch-pool");
		e.getTable().addPool(lootPool);
	}
	
	@SubscribeEvent
	public static void onRegisterAbility(RegistryEvent.Register<Ability.AbilityEntry> e) {
		ArrayList<String> disabler = new ArrayList<>();
		Collections.addAll(disabler, LCConfig.superpowers.disabledAbilities);
		
		//Positive
		registerAbility(e, TraitBouncy.class, "bouncy", disabler);
		registerAbility(e, TraitLucky.class, "lucky", disabler);
		registerAbility(e, TraitQuick.class, "quick", disabler);
		registerAbility(e, TraitSpry.class, "spry", disabler);
		registerAbility(e, TraitStrong.class, "strong", disabler);
		registerAbility(e, TraitSturdy.class, "sturdy", disabler);
		registerAbility(e, TraitThickSkinned.class, "thickSkinned", disabler);
		registerAbility(e, TraitTough.class, "tough", disabler);
		registerAbility(e, TraitSmart.class, "smart", disabler);
		registerAbility(e, TraitSneaky.class, "sneaky", disabler);
		
		//Negative
		registerAbility(e, TraitClumsy.class, "clumsy", disabler);
		registerAbility(e, TraitFlimsy.class, "flimsy", disabler);
		registerAbility(e, TraitFrail.class, "frail", disabler);
		registerAbility(e, TraitRigid.class, "rigid", disabler);
		registerAbility(e, TraitSlow.class, "slow", disabler);
		registerAbility(e, TraitUnhealthy.class, "unhealthy", disabler);
		registerAbility(e, TraitUnlucky.class, "unlucky", disabler);
		registerAbility(e, TraitWeak.class, "weak", disabler);
		registerAbility(e, TraitDumb.class, "dumb", disabler);
		registerAbility(e, TraitObvious.class, "obvious", disabler);
		
		if (RegenConfig.disableTraits) LCConfig.superpowers.disabledAbilities = disabler.toArray(new String[0]);
	}
	
	private static void registerAbility(RegistryEvent.Register<Ability.AbilityEntry> event, Class<? extends Ability> ability, String name, ArrayList<String> disabler) {
		event.getRegistry().register(new Ability.AbilityEntry(ability, new ResourceLocation(Regeneration.MODID, name)));
		disabler.add(Regeneration.MODID + ":" + name);
	}
}
