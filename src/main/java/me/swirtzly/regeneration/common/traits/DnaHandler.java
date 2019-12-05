package me.swirtzly.regeneration.common.traits;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.negative.DnaHunger;
import me.swirtzly.regeneration.common.traits.negative.DnaHydrophobic;
import me.swirtzly.regeneration.common.traits.positive.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
    public static IDna DNA_REPEL_ARROW = new DnaSimple("repel_arrow");
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
        register(DNA_REPEL_ARROW);
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

    @SubscribeEvent
    public static void onArrow(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        Entity attacked = event.getEntity();
        if (source != null && attacked != null && source.getImmediateSource() != null) {
            if (attacked instanceof EntityPlayer && source.getImmediateSource() instanceof EntityArrow) {
                if (!attacked.world.isRemote) {
                    EntityPlayer player = (EntityPlayer) attacked;
                    boolean flag = CapabilityRegeneration.getForPlayer(player).getDnaType().toString().equals(DNA_REPEL_ARROW.getRegistryName().toString());
                    event.setCanceled(flag);
                }
            }
        }
    }

    public static abstract class IDna {

        public ResourceLocation resourceLocation;
        public String localName;
        private String localDesc;

        public IDna(String name) {
            localName = "traits." + name + ".name";
            localDesc = "traits." + name + ".desc";
            resourceLocation = new ResourceLocation(RegenerationMod.MODID, name);
        }


        public abstract void onUpdate(IRegeneration cap);

        public abstract void onAdded(IRegeneration cap);

        public abstract void onRemoved(IRegeneration cap);

        public String getLangKey() {
            return localName;
        }

        public String getLocalDesc() {
            return localDesc;
        }

        public ResourceLocation getRegistryName() {
            return resourceLocation;
        }
    }


}
