package me.fril.regeneration.common.dna;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
@Mod.EventBusSubscriber
public class DnaHandler {
	
	private static HashMap<ResourceLocation, IDna> DNA_ENTRIES = new HashMap<>();
	private static ArrayList<IDna> DNA_LIST = new ArrayList<>();
	
	public static IDna DNA_ATHLETE = new DnaAthlete();
	public static IDna DNA_BORING = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "boring"));
	public static IDna DNA_DUMB = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "dumb"));
	public static IDna DNA_SNEAK = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "sneak"));
	
	public static void init(){
		register(DNA_ATHLETE);
		register(DNA_BORING);
		register(DNA_DUMB);
		register(DNA_SNEAK);
	}
	
	public static void register(IDna dna){
		DNA_ENTRIES.put(dna.getRegistryName(), dna);
		DNA_LIST.add(dna);
	}
	
	public static IDna getDnaEntry(ResourceLocation resourceLocation){
		if(DNA_ENTRIES.containsKey(resourceLocation)){
			return DNA_ENTRIES.get(resourceLocation);
		}
		return DNA_BORING;
	}
	
	public static IDna getRandomDna(Random random){
		return DNA_LIST.get(random.nextInt(DNA_LIST.size()));
	}
	
	public interface IDna {
		void onUpdate(IRegeneration cap);
		void onAdded(IRegeneration cap);
		void onRemoved(IRegeneration cap);
		String getLangKey();
		ResourceLocation getRegistryName();
	}
	
	@SubscribeEvent
	public static void onXpPickup(PlayerPickupXpEvent e){
		IRegeneration data = CapabilityRegeneration.getForPlayer(e.getEntityPlayer());
		IDna dna = DnaHandler.getDnaEntry(data.getDnaType());
		if(dna.getRegistryName().equals(DnaHandler.DNA_DUMB.getRegistryName()) && data.dnaAlive()) {
			e.getOrb().xpValue *= 0.5;
		}
	}
	
	@SubscribeEvent
	public static void onVisibilityCalc(PlayerEvent.Visibility e) {
		IRegeneration data = CapabilityRegeneration.getForPlayer(e.getEntityPlayer());
		IDna dna = DnaHandler.getDnaEntry(data.getDnaType());
		if(dna.getRegistryName().equals(DnaHandler.DNA_SNEAK.getRegistryName()) && data.dnaAlive()) {
			e.modifyVisibility(0.5);
		}
	}
	
	
}
