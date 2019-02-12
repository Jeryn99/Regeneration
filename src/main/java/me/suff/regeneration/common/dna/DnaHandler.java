package me.suff.regeneration.common.dna;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.dna.negative.DnaHydrophobic;
import me.suff.regeneration.common.dna.positive.DnaAthlete;
import me.suff.regeneration.common.dna.positive.DnaFireResistant;
import me.suff.regeneration.common.dna.positive.DnaLucky;
import me.suff.regeneration.common.dna.positive.DnaSimple;
import me.suff.regeneration.common.dna.positive.DnaSwimmer;
import me.suff.regeneration.common.dna.positive.DnaTough;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
@Mod.EventBusSubscriber
public class DnaHandler {
	
	public static IDna DNA_ATHLETE = new DnaAthlete();
	public static IDna DNA_BORING = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "boring"));
	public static IDna DNA_DUMB = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "dumb"));
	public static IDna DNA_TOUGH = new DnaTough();
	public static IDna DNA_LUCKY = new DnaLucky();
	public static IDna DNA_SWIMMER = new DnaSwimmer();
	public static IDna DNA_SCARED_OF_WATER = new DnaHydrophobic();
	public static IDna DNA_FIRE_RESISTANT = new DnaFireResistant();
	private static HashMap<ResourceLocation, IDna> DNA_ENTRIES = new HashMap<>();
	private static ArrayList<IDna> DNA_LIST = new ArrayList<>();
	
	public static void init() {
		register(DNA_ATHLETE);
		register(DNA_BORING);
		register(DNA_DUMB);
		register(DNA_TOUGH);
		register(DNA_LUCKY);
		register(DNA_SWIMMER);
		register(DNA_SCARED_OF_WATER);
		register(DNA_FIRE_RESISTANT);
	}
	
	public static void register(IDna dna) {
		DNA_ENTRIES.put(dna.getRegistryName(), dna);
		DNA_LIST.add(dna);
	}
	
	public static IDna getDnaEntry(ResourceLocation resourceLocation) {
		if (DNA_ENTRIES.containsKey(resourceLocation)) {
			return DNA_ENTRIES.get(resourceLocation);
		}
		return DNA_BORING;
	}
	
	public static IDna getRandomDna(Random random) {
		return DNA_LIST.get(random.nextInt(DNA_LIST.size()));
	}
	
	@SubscribeEvent
	public static void onXpPickup(PlayerPickupXpEvent e) {
		IRegeneration data = CapabilityRegeneration.getForPlayer(e.getEntityPlayer());
		IDna dna = DnaHandler.getDnaEntry(data.getDnaType());
		if (dna.getRegistryName().equals(DnaHandler.DNA_DUMB.getRegistryName()) && data.isDnaActive()) {
			e.getOrb().xpValue *= 0.5;
		}
	}
	
	@SubscribeEvent
	public static void onJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IRegeneration data = CapabilityRegeneration.getForPlayer(player);
			if (player.world.isRemote) return;
			if (data.isDnaActive() && data.getDnaType().equals(DNA_ATHLETE.getRegistryName())) {
				player.motionY += 0.1D;
				player.velocityChanged = true;
			}
		}
	}
	
	
	public interface IDna {
		void onUpdate(IRegeneration cap);
		
		void onAdded(IRegeneration cap);
		
		void onRemoved(IRegeneration cap);
		
		String getLangKey();
		
		ResourceLocation getRegistryName();
	}
	
	
}
