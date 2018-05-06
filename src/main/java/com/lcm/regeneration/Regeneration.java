package com.lcm.regeneration;

import java.lang.reflect.Field;

import com.lcm.regeneration.common.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capability.IRegeneration;
import com.lcm.regeneration.events.TimelordEventHandler;
import com.lcm.regeneration.init.RegenItems;
import com.lcm.regeneration.network.MessageChangeState;
import com.lcm.regeneration.network.MessageChangeStyle;
import com.lcm.regeneration.network.MessageSyncData;
import com.lcm.regeneration.util.CmdRegenDebug;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/** Created by AFlyingGrayson on 8/7/17 */
@Mod(modid = Regeneration.MODID, name = "Regeneration", version = Regeneration.VERSION, dependencies = "required:forge@[14.23.1.2574,)", acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2")
@EventBusSubscriber
public class Regeneration {
	public static final String MODID = "lcm-regen", VERSION = "3.0";
	public static final ResourceLocation ICONS = new ResourceLocation(MODID, "textures/gui/ability_icons.png");


    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        RegenConfig.init(new Configuration(e.getSuggestedConfigurationFile()), e.getSide());
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {

		CapabilityManager.INSTANCE.register(IRegeneration.class, new CapabilityRegeneration.Storage(), CapabilityRegeneration.class);
		MinecraftForge.EVENT_BUS.register(new TimelordEventHandler());

		INSTANCE.registerMessage(MessageChangeState.Handler.class, MessageChangeState.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncData.Handler.class, MessageSyncData.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(MessageChangeStyle.Handler.class, MessageChangeStyle.class, 3, Side.SERVER);
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CmdRegenDebug());
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
}
