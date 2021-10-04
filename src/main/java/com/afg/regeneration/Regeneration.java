package com.afg.regeneration;

import com.afg.regeneration.capability.ITimelordCapability;
import com.afg.regeneration.capability.TimelordCapability;
import com.afg.regeneration.client.animation.PlayerRenderHandler;
import com.afg.regeneration.items.ChameleonArch;
import com.afg.regeneration.network.MessageSyncTimelordCap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;

/**
 * Created by AFlyingGrayson on 8/7/17
 */

@Mod(modid = Regeneration.MODID, name = "Regeneration", version = Regeneration.VERSION, acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2")
@Mod.EventBusSubscriber
public class Regeneration {
    public static final String MODID = "regen-standalone", VERSION = "1.1";

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Regeneration.MODID);

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
            LootCondition[] chance = {new RandomChance(0.5F)};
            LootFunction[] count = {new SetCount(chance, new RandomValueRange(1.0F, 1.0F))};
            LootEntryItem item = new LootEntryItem(Items.chameleonArch, 10, 1, count, chance, "symbol_" + Items.chameleonArch.getUnlocalizedName());
            pool.addEntry(item);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide().equals(Side.CLIENT)) {
            MinecraftForge.EVENT_BUS.register(new PlayerRenderHandler());
        }
        MinecraftForge.EVENT_BUS.register(new TimelordCapability.EventHandler());
        NETWORK_WRAPPER.registerMessage(MessageSyncTimelordCap.Handler.class, MessageSyncTimelordCap.class, 0, Side.CLIENT);
        CapabilityManager.INSTANCE.register(ITimelordCapability.class, new TimelordCapability.Storage(), TimelordCapability.class);
    }

    @GameRegistry.ObjectHolder(MODID)
    public static class Items {
        public static final Item chameleonArch = null;
    }
}
