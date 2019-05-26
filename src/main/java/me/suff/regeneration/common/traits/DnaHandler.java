package me.suff.regeneration.common.traits;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.traits.negative.DnaHunger;
import me.suff.regeneration.common.traits.negative.DnaHydrophobic;
import me.suff.regeneration.common.traits.positive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    public static IDna DNA_BORING = new DnaSimple("boring");
    public static IDna DNA_DUMB = new DnaSimple("dumb");
	public static IDna DNA_TOUGH = new DnaTough();
	public static IDna DNA_LUCKY = new DnaLucky();
	public static IDna DNA_SWIMMER = new DnaSwimmer();
	public static IDna DNA_SCARED_OF_WATER = new DnaHydrophobic();
	public static IDna DNA_FIRE_RESISTANT = new DnaFireResistant();
	public static IDna DNA_HUNGER = new DnaHunger();
	public static IDna DNA_NIGHTVISION = new DnaNightvision();
    public static IDna DNA_WALLCLIMB = new DnaWallClimbing();
	public static HashMap<ResourceLocation, IDna> DNA_ENTRIES = new HashMap<>();
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
		register(DNA_HUNGER);
		register(DNA_NIGHTVISION);
        register(DNA_WALLCLIMB);
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


    public static abstract class IDna {

        public ResourceLocation resourceLocation;
        public String localName;

        public IDna(String name) {
            localName = "traits." + name + ".name";
            resourceLocation = new ResourceLocation(RegenerationMod.MODID, name);
        }


        public abstract void onUpdate(IRegeneration cap);

        public abstract void onAdded(IRegeneration cap);

        public abstract void onRemoved(IRegeneration cap);

        public String getLangKey() {
            return localName;
        }

        public ResourceLocation getRegistryName() {
            return resourceLocation;
        }
	}
	
	
}
