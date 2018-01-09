package com.afg.regeneration;

import java.lang.reflect.Field;

import com.afg.regeneration.client.animation.PlayerRenderHandler;
import com.afg.regeneration.items.ItemChameleonArch;
import com.afg.regeneration.superpower.TimelordSuperpower;
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

@Mod(modid = RegenerationMod.MODID, name = "Regeneration", version = RegenerationMod.VERSION, dependencies = "required-after:lucraftcore@[1.12-1.2.0,)", acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2")
@Mod.EventBusSubscriber
public class RegenerationMod {
	public static final String MODID = "g-regen", VERSION = "1.0";
	
	public static final TimelordSuperpower timelord = new TimelordSuperpower();
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide().equals(Side.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new PlayerRenderHandler());
		}
	}
	
	@GameRegistry.ObjectHolder(RegenerationMod.MODID)
	public static class Items {
		public static final Item chameleonArch = null;
	}
	
	@SubscribeEvent
	public static void onRegisterSuperpower(RegistryEvent.Register<Superpower> e) {
		e.getRegistry().register(RegenerationMod.timelord);
	}
	
	@SubscribeEvent
	public static void onRegisterAbility(RegistryEvent.Register<Ability.AbilityEntry> e) {
		// Positive
		
		e.getRegistry().register(new Ability.AbilityEntry(TraitBouncy.class, new ResourceLocation(RegenerationMod.MODID, "bouncy")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitLucky.class, new ResourceLocation(RegenerationMod.MODID, "lucky")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitQuick.class, new ResourceLocation(RegenerationMod.MODID, "quick")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitSpry.class, new ResourceLocation(RegenerationMod.MODID, "spry")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitStrong.class, new ResourceLocation(RegenerationMod.MODID, "strong")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitSturdy.class, new ResourceLocation(RegenerationMod.MODID, "sturdy")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitThickSkinned.class, new ResourceLocation(RegenerationMod.MODID, "thickSkinned")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitTough.class, new ResourceLocation(RegenerationMod.MODID, "tough")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitSmart.class, new ResourceLocation(RegenerationMod.MODID, "smart")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitSneaky.class, new ResourceLocation(RegenerationMod.MODID, "sneaky")));
		
		// Negative
		
		e.getRegistry().register(new Ability.AbilityEntry(TraitClumsy.class, new ResourceLocation(RegenerationMod.MODID, "clumsy")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitFlimsy.class, new ResourceLocation(RegenerationMod.MODID, "flimsy")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitFrail.class, new ResourceLocation(RegenerationMod.MODID, "frail")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitRigid.class, new ResourceLocation(RegenerationMod.MODID, "rigid")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitSlow.class, new ResourceLocation(RegenerationMod.MODID, "slow")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitUnhealthy.class, new ResourceLocation(RegenerationMod.MODID, "unhealthy")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitUnlucky.class, new ResourceLocation(RegenerationMod.MODID, "unlucky")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitWeak.class, new ResourceLocation(RegenerationMod.MODID, "weak")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitDumb.class, new ResourceLocation(RegenerationMod.MODID, "dumb")));
		e.getRegistry().register(new Ability.AbilityEntry(TraitObvious.class, new ResourceLocation(RegenerationMod.MODID, "obvious")));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) throws Exception {
		event.getRegistry().register(new ItemChameleonArch());
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
