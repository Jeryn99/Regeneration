package me.fril.regeneration.common.dna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.dna.negative.DnaHydrophobic;
import me.fril.regeneration.common.dna.negative.DnaVampire;
import me.fril.regeneration.common.dna.positive.DnaAthlete;
import me.fril.regeneration.common.dna.positive.DnaFireResistant;
import me.fril.regeneration.common.dna.positive.DnaLucky;
import me.fril.regeneration.common.dna.positive.DnaSimple;
import me.fril.regeneration.common.dna.positive.DnaSneak;
import me.fril.regeneration.common.dna.positive.DnaSwimmer;
import me.fril.regeneration.common.dna.positive.DnaTough;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
@Mod.EventBusSubscriber
public class DnaHandler {
	
	public static IDna DNA_ATHLETE = new DnaAthlete();
	public static IDna DNA_BORING = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "boring"));
	public static IDna DNA_DUMB = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "dumb"));
	public static IDna DNA_SNEAK = new DnaSneak();
	public static IDna DNA_VAMPIRE = new DnaVampire();
	public static IDna DNA_TOUGH = new DnaTough();
	public static IDna DNA_LUCKY = new DnaLucky();
	public static IDna DNA_SWIMMER = new DnaSwimmer();
	public static IDna DNA_SCARED_OF_WATER = new DnaHydrophobic();
	public static IDna DNA_FIRE_RESISTANT = new DnaFireResistant();
	private static HashMap<ResourceLocation, IDna> DNA_ENTRIES = new HashMap<>();
	private static ArrayList<IDna> DNA_LIST = new ArrayList<>();
	//public static IDna DNA_ALCHOHOLISM = new DnaAlcoholism();
	//public static IDna DNA_WORKER = new DnaSimple(new ResourceLocation(RegenerationMod.MODID, "worker"));
	
	public static void init() {
		register(DNA_ATHLETE);
		register(DNA_BORING);
		register(DNA_DUMB);
		register(DNA_SNEAK);
		register(DNA_VAMPIRE);
		register(DNA_TOUGH);
		register(DNA_LUCKY);
		register(DNA_SWIMMER);
		register(DNA_SCARED_OF_WATER);
		register(DNA_FIRE_RESISTANT);
		//register(DNA_ALCHOHOLISM);
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
		if (dna.getRegistryName().equals(DnaHandler.DNA_DUMB.getRegistryName()) && data.dnaAlive()) {
			e.getOrb().xpValue *= 0.5;
		}
	}
	
	@SubscribeEvent
	public static void onJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IRegeneration data = CapabilityRegeneration.getForPlayer(player);
			if (data.dnaAlive() && data.getDnaType().equals(DNA_ATHLETE.getRegistryName())) {
				player.motionY += 0.1D;
				player.velocityChanged = true;
			}
		}
	}
	
	@SubscribeEvent
	public static void onVisibilityCalc(PlayerEvent.Visibility e) {
		IRegeneration data = CapabilityRegeneration.getForPlayer(e.getEntityPlayer());
		IDna dna = DnaHandler.getDnaEntry(data.getDnaType());
		if (dna.getRegistryName().equals(DnaHandler.DNA_SNEAK.getRegistryName()) && data.dnaAlive()) {
			e.modifyVisibility(0.5);
		}
	}
	
	@SubscribeEvent
	public static void onTrample(BlockEvent.FarmlandTrampleEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			IRegeneration data = CapabilityRegeneration.getForPlayer((EntityPlayer) e.getEntity());
			IDna dna = DnaHandler.getDnaEntry(data.getDnaType());
			e.setCanceled(dna.getRegistryName().equals(DNA_SNEAK.getRegistryName()));
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
