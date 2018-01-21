package com.lcm.regeneration;

import com.lcm.regeneration.items.ItemChameleonArch;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegenerationItems { //not sure 'Items' is the correct term
	public static final ItemChameleonArch chameleonArch = new ItemChameleonArch();
	
	@SubscribeEvent public static void registerBlocks(RegistryEvent.Register<Block> e) {}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().register(RegenerationItems.chameleonArch);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e) throws ReflectiveOperationException {
		ModelLoader.setCustomModelResourceLocation(RegenerationItems.chameleonArch, 0, new ModelResourceLocation(RegenerationItems.chameleonArch.getRegistryName(), "inventory"));
	}
}